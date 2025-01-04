package com.test.questioncut

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.test.lib.cut_model.ModelInstance
import com.test.lib.cut_model.PitayaAlgTask
import com.lightning.edu.ei.edgealgorithm.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class CutQuestionActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2

    private val selectImageButton: Button by lazy { findViewById(R.id.selectImageButton) }
    private val imageView: MaskImageView by lazy { findViewById(R.id.imageView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cut_question)

        lifecycleScope.launch(Dispatchers.IO) {
            ModelInstance.f28435a.a(baseContext)
        }

        selectImageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
                )
            } else {
                openImagePicker()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(this, "权限被拒绝，无法选择图片", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                imageView.setImageBitmap(bitmap)

                Log.d("CutQuestionActivity", "onActivityResult: ${bitmap.width}x${bitmap.height}")
                val res: Result? =
                    ModelInstance.f28435a.a(baseContext).runPredict(baseContext, 1, bitmap)
                Log.d(
                    "CutQuestionActivity",
                    "onActivityResult: ${res?.toJSON()}"
                )

                val qResult = PitayaAlgTask()
                    .start(applicationContext, bitmap)
                Log.d(
                    "CutQuestionActivity",
                    "onActivityResult qResult=: ${qResult.toJSON()}"
                )

                imageView.clearFrames()
                qResult.quadrilateralBoxes.forEachIndexed { index, box ->
                    imageView.addFrame(index + 1, box, 0)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
} 
package com.test.questioncut

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Xfermode
import android.util.ArrayMap
import android.util.AttributeSet
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class MaskImageView : ImageView {

    companion object {
        private const val P_LT: Int = 0
        private const val P_RT: Int = 1
        private const val P_RB: Int = 2
        private const val P_LB: Int = 3
    }

    private val mMaskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    // 显示的图片与实际图片缩放比
    private var mScaleX = 0f
    private var mScaleY = 0f

    //实际显示图片的位置
    private var mActWidth: Int = 0
    private var mActHeight: Int = 0
    private val imageRect = Rect()
    private val imageActRect = Rect()
    private var mActLeft: Int = 0
    private var mActTop: Int = 0
    private var mDensity = 0f
    private var mTransX = 0F
    private var mTransY = 0F

    private val mMatrixValue = FloatArray(9)
    private val mMaskXfermode: Xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private val mPointLinePath = Path()

    // 框选区域, 0->LeftTop, 1->RightTop， 2->RightBottom, 3->LeftBottom
    private var selectedPoints: Array<Point> = Array(4) { Point() }
    private var mMaskAlpha: Int = 77 //0 - 255, 蒙版透明度
    private var normalFrameColor: Int = 0x1A000000
    private var normalFrameBorderColor: Int = Color.WHITE
    private var normalFrameBorderWidth: Float = 1F
    private var normalFrameBorderRadius: Float = 5F

    private var frameRectList: ArrayMap<Int, Array<Point>> = ArrayMap()
    private var correctList: ArrayMap<Int, Int> = ArrayMap()
    private var selectedFrameIndex = 0

    private val normalFramePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val normalFrameRect = RectF()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context, attrs, defStyleAttr, 0
    )

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context ?: return, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val scaleType = scaleType
        if (scaleType == ScaleType.FIT_END || scaleType == ScaleType.FIT_START || scaleType == ScaleType.MATRIX) {
            throw RuntimeException("Image in MaskImageView must be in center")
        }
        mDensity = resources.displayMetrics.density
    }

    fun addFrame(index: Int, position: FloatArray, correct: Int) {
        if (position.size != 8) {
            return
        }
        val points = floatArrayToPoints(position)
        frameRectList[index] = points
        correctList[index] = correct
        invalidate()
    }

    fun clearFrames() {
        frameRectList.clear()
    }

    private fun floatArrayToPoints(position: FloatArray): Array<Point> {
        return arrayOf(
            // left-top
            Point(position[0].toInt(), position[1].toInt()),
            // right-top
            Point(position[2].toInt(), position[3].toInt()),
            // right-bottom
            Point(position[4].toInt(), position[5].toInt()),
            // left-bottom
            Point(position[6].toInt(), position[7].toInt()),
        )
    }

    private fun checkPoints(points: Array<Point>?): Boolean {
        return points != null && points.size == 4
                && points[P_LT].x != points[P_RT].x && points[P_LT].y != points[P_RB].y
    }

    override fun onDraw(canvas: Canvas?) {
        try {
            super.onDraw(canvas)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        getDrawablePosition()
        drawHighlightFrame(canvas ?: return)
    }

    private fun drawHighlightFrame(canvas: Canvas) {
        drawMask(canvas, selectedPoints)
        frameRectList.forEach { (index, rect) ->
            if (index != selectedFrameIndex) {
                drawNormalFrame(canvas, rect)
            }
        }
    }

    private fun drawNormalFrame(canvas: Canvas, points: Array<Point>) {
        if (!checkPoints(points)) {
            return
        }
        normalFrameRect.left = getViewPointX(points[P_LT])
        normalFrameRect.top = getViewPointY(points[P_LT])
        normalFrameRect.right = getViewPointX(points[P_RT])
        normalFrameRect.bottom = getViewPointY(points[P_RB])

        normalFramePaint.color = normalFrameColor
        normalFramePaint.style = Paint.Style.FILL
        canvas.drawRoundRect(
            normalFrameRect, normalFrameBorderRadius, normalFrameBorderRadius, normalFramePaint
        )
        normalFramePaint.color = normalFrameBorderColor
        normalFramePaint.style = Paint.Style.STROKE
        normalFramePaint.strokeWidth = normalFrameBorderWidth
        canvas.drawRoundRect(
            normalFrameRect, normalFrameBorderRadius, normalFrameBorderRadius, normalFramePaint
        )
    }

    private fun drawMask(canvas: Canvas, points: Array<Point>) {
        if (mMaskAlpha <= 0) {
            return
        }
        val path: Path? = resetPointPath(points)
        if (path != null) {
            val sc = canvas.saveLayer(
                mActLeft.toFloat(),
                mActTop.toFloat(),
                (mActLeft + mActWidth).toFloat(),
                (mActTop + mActHeight).toFloat(),
                mMaskPaint,
                Canvas.ALL_SAVE_FLAG
            )
            mMaskPaint.alpha = mMaskAlpha
            canvas.drawRect(
                mActLeft.toFloat(),
                mActTop.toFloat(),
                (mActLeft + mActWidth).toFloat(),
                (mActTop + mActHeight).toFloat(),
                mMaskPaint
            )
            mMaskPaint.setXfermode(mMaskXfermode)
            mMaskPaint.alpha = 255
            canvas.drawPath(path, mMaskPaint)
            mMaskPaint.setXfermode(null)
            canvas.restoreToCount(sc)
        }
    }

    private fun resetPointPath(points: Array<Point>): Path? {
        if (!checkPoints(points)) {
            return null
        }
        mPointLinePath.reset()
        val lt = points[0]
        val rt = points[1]
        val rb = points[2]
        val lb = points[3]
        mPointLinePath.moveTo(getViewPointX(lt), getViewPointY(lt))
        mPointLinePath.lineTo(getViewPointX(rt), getViewPointY(rt))
        mPointLinePath.lineTo(getViewPointX(rb), getViewPointY(rb))
        mPointLinePath.lineTo(getViewPointX(lb), getViewPointY(lb))
        mPointLinePath.close()
        return mPointLinePath
    }

    private fun getDrawablePosition() {
        val drawable = drawable ?: return
        imageMatrix.getValues(mMatrixValue)
        mScaleX = mMatrixValue[Matrix.MSCALE_X]
        mScaleY = mMatrixValue[Matrix.MSCALE_Y]
        val origW = drawable.intrinsicWidth
        val origH = drawable.intrinsicHeight
        mActWidth = Math.round(origW * mScaleX)
        mActHeight = Math.round(origH * mScaleY)
        mActLeft = (width - mActWidth) / 2
        mActTop = (height - mActHeight) / 2
        mTransX = mMatrixValue[Matrix.MTRANS_X]
        mTransY = mMatrixValue[Matrix.MTRANS_Y]
        imageRect.left = 0
        imageRect.right = origW
        imageRect.top = 0
        imageRect.bottom = origH
        imageActRect.left = mActLeft
        imageActRect.right = imageActRect.left + mActWidth
        imageActRect.top = mActTop
        imageActRect.bottom = imageActRect.top + mActHeight
    }

    private fun getViewPointX(point: Point): Float {
        return getViewPointX(point.x.toFloat())
    }

    private fun getViewPointX(x: Float): Float {
        return x * mScaleX + mActLeft
    }

    private fun getViewPointY(point: Point): Float {
        return getViewPointY(point.y.toFloat())
    }

    private fun getViewPointY(y: Float): Float {
        return y * mScaleY + mActTop
    }

}

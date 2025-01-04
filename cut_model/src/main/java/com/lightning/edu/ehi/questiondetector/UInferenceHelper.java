package com.lightning.edu.ehi.questiondetector;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class UInferenceHelper {
    private static String TAG = "UInferenceHelper";
    private long initCost;
    public long mInference;
    public CountDownLatch mLatch;

    private UInferenceHelper() {
    }

    public static String getVersion() {
        return UInferenceNative.GetVersion();
    }

    public static UInferenceHelper initModel(AssetManager assetManager, int i, int i2, String str) {
        long currentTimeMillis = System.currentTimeMillis();
        long InitByAssetManger = UInferenceNative.InitByAssetManger(assetManager, i, i2, str);
        if (-1 == InitByAssetManger) {
            Log.w(TAG, "Create Net Failed from AssetManager");
            return null;
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        UInferenceHelper uInferenceHelper = new UInferenceHelper(InitByAssetManger);
        uInferenceHelper.initCost = currentTimeMillis2;
        return uInferenceHelper;
    }

    public QResult runPredict(Bitmap bitmap) {
        QResult qResult = new QResult();
        if (0 == this.mInference) {
            qResult.pass = false;
            qResult.status = 1;
            qResult.msg = "Error! mInference is 0.";
            Log.w(TAG, "Error! mInference is 0.");
            return qResult;
        }
        if (bitmap.getWidth() < 5 || bitmap.getHeight() < 5) {
            qResult.pass = false;
            qResult.status = 7;
            qResult.msg = "Error! input image is too small.";
            Log.w(TAG, "Error! input image is too small.");
            return qResult;
        }
        this.mLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        QResult PredictBitmap = UInferenceNative.PredictBitmap(this.mInference, bitmap);
        PredictBitmap.predictCost = System.currentTimeMillis() - currentTimeMillis;
        PredictBitmap.initCost = this.initCost;
        this.mLatch.countDown();
        return PredictBitmap;
    }

    public QResult runPredict(int[] iArr, int i, int i2) {
        QResult qResult = new QResult();
        if (0 == this.mInference) {
            qResult.pass = false;
            qResult.status = 1;
            qResult.msg = "Error! mInference is 0.";
            Log.w(TAG, qResult.msg);
            return qResult;
        }
        if (i < 5 || i2 < 5) {
            qResult.pass = false;
            qResult.status = 7;
            qResult.msg = "Error! input image is too small.";
            Log.w(TAG, qResult.msg);
            return qResult;
        }
        this.mLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        QResult PredictIntArray = UInferenceNative.PredictIntArray(this.mInference, iArr, i, i2);
        PredictIntArray.predictCost = System.currentTimeMillis() - currentTimeMillis;
        PredictIntArray.initCost = this.initCost;
        this.mLatch.countDown();
        return PredictIntArray;
    }

    public String version() {
        return UInferenceNative.Version(this.mInference);
    }

    public void release() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (UInferenceHelper.this.mLatch != null) {
                    try {
                        UInferenceHelper.this.mLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                UInferenceNative.Release(UInferenceHelper.this.mInference);
                UInferenceHelper.this.mInference = 0L;
            }
        });
    }

    private UInferenceHelper(long j) {
        this.mInference = j;
    }
}

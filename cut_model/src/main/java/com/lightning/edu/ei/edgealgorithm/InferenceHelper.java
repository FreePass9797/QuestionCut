package com.lightning.edu.ei.edgealgorithm;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class InferenceHelper {
    private static String TAG = "InferenceHelper";
    private long initCost;
    public long mInference;
    public CountDownLatch mLatch;

    private InferenceHelper() {
    }

    public static String getVersion() {
        return InferenceNative.GetVersion();
    }

    public static InferenceHelper initModel(AssetManager assetManager, int i, int i2, String str, Params params) {
        long currentTimeMillis = System.currentTimeMillis();
        String valueOf = String.valueOf(params.blur_url);
        int i3 = params.blur_status;
        String valueOf2 = String.valueOf(params.scene_url);
        int i4 = params.scene_status;
        String valueOf3 = String.valueOf(params.dark_url);
        int i5 = params.dark_status;
        String valueOf4 = String.valueOf(params.orientation_url);
        long InitByAssetManger = InferenceNative.InitByAssetManger(assetManager, i, i2, i3, i4, i5, params.orientation_status, params.questions_status, str, valueOf, valueOf2, valueOf3, valueOf4, String.valueOf(params.questions_url));
        if (-1 == InitByAssetManger) {
            Log.w(TAG, "Create Net Failed from AssetManager");
            return null;
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        InferenceHelper inferenceHelper = new InferenceHelper(InitByAssetManger);
        inferenceHelper.initCost = currentTimeMillis2;
        return inferenceHelper;
    }

    public Result runPredict(Bitmap bitmap, Params params) {
        Result result = new Result();
        if (0 == this.mInference) {
            result.pass = false;
            result.status = 1;
            result.msg = "Error! mInference is 0.";
            Log.w(TAG, "Error! mInference is 0.");
            return result;
        }
        if (bitmap.getWidth() < 5 || bitmap.getHeight() < 5) {
            result.pass = false;
            result.status = 2;
            result.msg = "Error! input image is too small.";
            Log.w(TAG, "Error! input image is too small.");
            return result;
        }
        this.mLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        Result PredictBitmap = InferenceNative.PredictBitmap(this.mInference, bitmap, 0, params);
        PredictBitmap.predictCost = System.currentTimeMillis() - currentTimeMillis;
        PredictBitmap.initCost = this.initCost;
        this.mLatch.countDown();
        return PredictBitmap;
    }

    public Result runPredict(Bitmap bitmap, int i, Params params) {
        Result result = new Result();
        if (0 == this.mInference) {
            result.pass = false;
            result.status = 1;
            result.msg = "Error! mInference is 0.";
            Log.w(TAG, "Error! mInference is 0.");
            return result;
        }
        if (bitmap.getWidth() < 5 || bitmap.getHeight() < 5) {
            result.pass = false;
            result.status = 2;
            result.msg = "Error! input image is too small.";
            Log.w(TAG, "Error! input image is too small.");
            return result;
        }
        this.mLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        Result PredictBitmap = InferenceNative.PredictBitmap(this.mInference, bitmap, i, params);
        PredictBitmap.predictCost = System.currentTimeMillis() - currentTimeMillis;
        PredictBitmap.initCost = this.initCost;
        this.mLatch.countDown();
        return PredictBitmap;
    }

    public Result runPredict(int[] iArr, int i, int i2, Params params) {
        Result result = new Result();
        if (0 == this.mInference) {
            result.pass = false;
            result.status = 1;
            result.msg = "Error! mInference is 0.";
            Log.w(TAG, result.msg);
            return result;
        }
        if (i < 5 || i2 < 5) {
            result.pass = false;
            result.status = 2;
            result.msg = "Error! input image is too small.";
            Log.w(TAG, result.msg);
            return result;
        }
        this.mLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        Result PredictIntArray = InferenceNative.PredictIntArray(this.mInference, iArr, i, i2, 0, params);
        PredictIntArray.predictCost = System.currentTimeMillis() - currentTimeMillis;
        PredictIntArray.initCost = this.initCost;
        this.mLatch.countDown();
        return PredictIntArray;
    }

    public Result runPredict(int[] iArr, int i, int i2, int i3, Params params) {
        Result result = new Result();
        if (0 == this.mInference) {
            result.pass = false;
            result.status = 1;
            result.msg = "Error! mInference is 0.";
            Log.w(TAG, result.msg);
            return result;
        }
        if (i < 5 || i2 < 5) {
            result.pass = false;
            result.status = 2;
            result.msg = "Error! input image is too small.";
            Log.w(TAG, result.msg);
            return result;
        }
        this.mLatch = new CountDownLatch(1);
        long currentTimeMillis = System.currentTimeMillis();
        Result PredictIntArray = InferenceNative.PredictIntArray(this.mInference, iArr, i, i2, i3, params);
        PredictIntArray.predictCost = System.currentTimeMillis() - currentTimeMillis;
        PredictIntArray.initCost = this.initCost;
        this.mLatch.countDown();
        return PredictIntArray;
    }

    public String version() {
        return InferenceNative.Version(this.mInference);
    }

    public void release() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (InferenceHelper.this.mLatch != null) {
                    try {
                        InferenceHelper.this.mLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                InferenceNative.Release(InferenceHelper.this.mInference);
                InferenceHelper.this.mInference = 0L;
            }
        });
    }

    private InferenceHelper(long j) {
        this.mInference = j;
    }

    public static Params getVaildParams() {
        Params params = new Params();
        params.blur_status = 1;
        params.scene_status = 1;
        params.dark_status = 1;
        params.orientation_status = 1;
        params.questions_status = 1;
        return params;
    }
}

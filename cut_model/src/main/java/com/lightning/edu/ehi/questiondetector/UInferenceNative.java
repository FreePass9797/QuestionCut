package com.lightning.edu.ehi.questiondetector;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

public class UInferenceNative {
    public static String TAG = "UInferenceNative";

    public static native String GetVersion();

    public static native long InitByAssetManger(AssetManager assetManager, int i, int i2, String str);

    public static native QResult PredictBitmap(long j, Bitmap bitmap);

    public static native QResult PredictIntArray(long j, int[] iArr, int i, int i2);

    public static native void Release(long j);

    public static native String Version(long j);

    static void loadByteNN(String str) {
        try {
            System.loadLibrary(str);
        } catch (Throwable th) {
            Log.w(TAG, "Load " + str + " Library Error! Exception: %s", th);
        }
    }

    static {
        System.loadLibrary("question_detection");
        loadByteNN("bytenn");
    }
}

package com.lightning.edu.ei.edgealgorithm;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

public class InferenceNative {
    public static String TAG = "InferenceNative";

    public static native String GetVersion();

    public static native long InitByAssetManger(AssetManager assetManager, int i, int i2, int i3, int i4, int i5, int i6, int i7, String str, String str2, String str3, String str4, String str5, String str6);

    public static native Result PredictBitmap(long j, Bitmap bitmap, int i, Params params);

    public static native Result PredictIntArray(long j, int[] iArr, int i, int i2, int i3, Params params);

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
        System.loadLibrary("edge-algorithm");
        loadByteNN("bytenn");
    }
}

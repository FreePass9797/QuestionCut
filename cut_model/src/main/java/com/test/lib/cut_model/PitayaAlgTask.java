package com.test.lib.cut_model;

import android.content.Context;
import android.graphics.Bitmap;

import com.lightning.edu.ehi.questiondetector.QResult;
import com.lightning.edu.ehi.questiondetector.UInferenceHelper;

public class PitayaAlgTask {
    public QResult start(Context context, Bitmap bitmap) {
        UInferenceHelper initModel = UInferenceHelper.initModel(context.getAssets(), 1, 0, String.valueOf(context.getExternalCacheDir()));
        QResult runPredict = initModel != null ? initModel.runPredict(bitmap) : null;
        if (initModel != null) {
            initModel.release();
        }
        return runPredict;
    }
}

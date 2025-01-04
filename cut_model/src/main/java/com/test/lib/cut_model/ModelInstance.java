package com.test.lib.cut_model;

import android.content.Context;
import android.graphics.Bitmap;

import com.lightning.edu.ei.edgealgorithm.InferenceHelper;
import com.lightning.edu.ei.edgealgorithm.Params;
import com.lightning.edu.ei.edgealgorithm.Result;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

public final class ModelInstance {

    public static final C0944a f28435a;

    public static WeakReference<ModelInstance> instanceRef;

    public Params params;
    private InferenceHelper helper;

    static {
        f28435a = new C0944a();
    }

    public static final class C0944a {

        public C0944a() {
        }

        public synchronized ModelInstance a(Context context) {
            ModelInstance aVar;
            WeakReference<ModelInstance> weakReference = ModelInstance.instanceRef;
            if ((weakReference != null ? weakReference.get() : null) == null) {
                ModelInstance.instanceRef = new WeakReference<>(new ModelInstance(context));
                LinkedHashMap<Object, Object> a2 = ModelInstanceHolder.holder.a();
                WeakReference<ModelInstance> weakReference2 = ModelInstance.instanceRef;
                ModelInstance aVar2 = weakReference2.get();
                a2.put(aVar2, null);
            }
            WeakReference<ModelInstance> weakReference3 = ModelInstance.instanceRef;
            aVar = weakReference3.get();
            return aVar;
        }
    }

    public ModelInstance(Context context) {
        String valueOf = String.valueOf(context.getExternalCacheDir());
        this.params = InferenceHelper.getVaildParams();
        this.helper = InferenceHelper.initModel(context.getAssets(), 1, 0, valueOf, this.params);
    }

    private InferenceHelper a(Context context) {
        Params vaildParams = InferenceHelper.getVaildParams();
        boolean z = !(this.params != null && vaildParams.blur_status == this.params.blur_status);
        boolean z2 = !(this.params != null && vaildParams.dark_status == this.params.dark_status);
        boolean z3 = !(this.params != null && vaildParams.orientation_status == this.params.orientation_status);
        boolean z4 = !(this.params != null && vaildParams.scene_status == this.params.scene_status);
        boolean z5 = z || z2 || z3 || ((this.params != null && vaildParams.questions_status == this.params.questions_status) ^ true) || z4;
        this.params = vaildParams;
        if (this.helper == null) {
            this.helper = InferenceHelper.initModel(context.getAssets(), 1, 0, String.valueOf(context.getExternalCacheDir()), this.params);
        } else if (z5) {
            this.helper = null;
            this.helper = InferenceHelper.initModel(context.getAssets(), 1, 0, String.valueOf(context.getExternalCacheDir()), this.params);
        }
        InferenceHelper inferenceHelper = this.helper;
        return inferenceHelper;
    }

    public Result runPredict(Context context, Integer num, Bitmap bitmap) {
        InferenceHelper a2 = a(context);
        Params params = new Params();
        Params params2 = this.params;
        params.blur_status = params2 != null ? params2.blur_status : 1;
        Params params3 = this.params;
        params.dark_status = params3 != null ? params3.dark_status : 1;
        Params params4 = this.params;
        params.orientation_status = params4 != null ? params4.orientation_status : 1;
        Params params5 = this.params;
        params.scene_status = params5 != null ? params5.scene_status : 1;
        Params params6 = this.params;
        params.questions_status = params6 != null ? params6.questions_status : 1;
        Result result = null;
        if (num != null) {
            if (a2 != null) {
                result = a2.runPredict(bitmap, num, params);
            }
        } else if (a2 != null) {
            result = a2.runPredict(bitmap, params);
        }
        return result;
    }
}

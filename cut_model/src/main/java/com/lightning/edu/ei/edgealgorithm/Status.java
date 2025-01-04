package com.lightning.edu.ei.edgealgorithm;


public enum Status {
    STATUS_SUCCESS(0, "Success"),
    STATUS_INFERENCE_IS_NULL(1, "Inference is null (InferenceNative)"),
    STATUS_IMAGE_SMALL(2, "Image too small"),
    STATUS_INFERENCE_IS_NULL_C(3, "Inference is nullptr (JNI)"),
    STATUS_BITMAP_IS_RECYCLED(4, "Bitmap is recycled"),
    STATUS_BITMAP_GET_INFO(5, "Bitmap get info error"),
    STATUS_BITMAP_NOT_RGBA8888(6, "Bitmap is not RGBA8888 format"),
    STATUS_BITMAP_LOCK_PIXELS(7, "Bitmap lock pixels error"),
    STATUS_CHECK_BLUR_FAILED(8, "Check Blur Failed"),
    STATUS_CHECK_SCENE_FAILED(9, "Check Scene Failed"),
    STATUS_CHECK_DARK_FAILED(10, "Check Dark Failed, Too Dark"),
    STATUS_NEED_RETAKE(11, "Need retake photo!"),
    STATUS_NEED_REFUSE(12, "Need refuse photo!"),
    STATUS_DETECTION_INFERENCE_FAILED(13, "Detection Inference run error"),
    STATUS_DETECTION_INFERENCE_IS_NULL(14, "Detection Inference is null (InferenceNative)");

    private int statusCode;
    private String statusMsg;

    public static Status[] valuesCustom() {
        return values().clone();
    }

    Status(int i, String str) {
        this.statusCode = i;
        this.statusMsg = str;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMsg() {
        return this.statusMsg;
    }
}

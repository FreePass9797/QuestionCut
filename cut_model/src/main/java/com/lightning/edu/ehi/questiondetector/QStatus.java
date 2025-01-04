package com.lightning.edu.ehi.questiondetector;

public enum QStatus {
    STATUS_SUCCESS(0, "Success"),
    STATUS_INFERENCE_FAILED(-2, "Inference run error"),
    STATUS_INFERENCE_IS_NULL(-1, "Inference is null (InferenceNative)"),
    STATUS_BITMAP_IS_RECYCLED(4, "Bitmap is recycled"),
    STATUS_BITMAP_GET_INFO_FAILED(5, "Bitmap get info error"),
    STATUS_BITMAP_NOT_RGBA8888(6, "Bitmap is not RGBA8888 format"),
    STATUS_BITMAP_LOCK_PIXELS_FAILED(7, "Bitmap lock pixels error");

    private int statusCode;
    private String statusMsg;

    public static QStatus[] valuesCustom() {
        return values().clone();
    }

    QStatus(int i, String str) {
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

package com.lightning.edu.ehi.questiondetector;

import android.graphics.Bitmap;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QResult {
    public String deviceInfo;
    public float[][] horizontalBoxes;
    public int inHeight;
    public int inWidth;
    public long initCost;
    public int midBoxIndex;
    public String msg;
    public String os;
    public boolean pass;
    public long predictCost;
    public float[][] quadrilateralBoxes;
    public ArrayList<Bitmap> questionImages;
    public float[][] rotatedBoxes;
    public int status;
    public String version;

    public QResult() {
        this.pass = true;
        this.msg = "Success";
        this.version = "";
        this.horizontalBoxes = new float[][]{new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f}};
        this.rotatedBoxes = new float[][]{new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f}};
        this.quadrilateralBoxes = new float[][]{new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f}};
    }

    public String toString() {
        getOsDeviceInfo();
        return " 是否通过=" + this.pass
                + ",\n 推理引擎初始化耗时="
                + this.initCost
                + "ms,\n 输入图像宽度=" + this.inWidth
                + ", 高度=" + this.inHeight
                + ",\n 算法总耗时=" + this.predictCost
                + "ms,\n 状态=" + readableStatus(this.status)
                + ",\n 调试信息='" + this.msg
                + "',\n 版本&业务=" + this.version
                + ",\n midBoxIndex=" + this.midBoxIndex
                + ",\n 系统&设备=" + this.os + "_" + this.deviceInfo;
    }

    public String toJSON() {
        getOsDeviceInfo();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("pass", this.pass);
            JSONArray jSONArray = new JSONArray();
            for (float[] horizontalBox : this.horizontalBoxes) {
                JSONArray jSONArray2 = new JSONArray();
                for (float box : horizontalBox) {
                    jSONArray2.put(box);
                }
                jSONArray.put(jSONArray2);
            }
            jSONObject.put("horizontalBoxes", jSONArray);
            JSONArray jSONArray3 = new JSONArray();
            for (float[] rotatedBox : this.rotatedBoxes) {
                JSONArray jSONArray4 = new JSONArray();
                for (float box : rotatedBox) {
                    jSONArray4.put(box);
                }
                jSONArray3.put(jSONArray4);
            }
            jSONObject.put("rotatedBoxes", jSONArray3);
            JSONArray jSONArray5 = new JSONArray();
            for (float[] quadrilateralBox : this.quadrilateralBoxes) {
                JSONArray jSONArray6 = new JSONArray();
                for (float box : quadrilateralBox) {
                    jSONArray6.put(box);
                }
                jSONArray5.put(jSONArray6);
            }
            jSONObject.put("quadrilateralBoxes", jSONArray5);
            JSONArray jSONArray7 = new JSONArray();
            for (Bitmap questionImage : this.questionImages) {
                jSONArray7.put(questionImage);
            }
            jSONObject.put("questionImages", jSONArray7);
            jSONObject.put("midBoxIndex", this.midBoxIndex);
            jSONObject.put("inWidth", this.inWidth);
            jSONObject.put("inHeight", this.inHeight);
            jSONObject.put("initCost", this.initCost + "ms");
            jSONObject.put("predictCost", this.predictCost + "ms");
            jSONObject.put("version", this.version);
            jSONObject.put("status", this.status);
            jSONObject.put("msg", this.msg);
            jSONObject.put("os", this.os);
            jSONObject.put("deviceInfo", this.deviceInfo);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public QStatus getStatus() {
        switch (this.status) {
            case 0:
                return QStatus.STATUS_SUCCESS;
            case 1:
                return QStatus.STATUS_INFERENCE_FAILED;
            case 2:
                return QStatus.STATUS_INFERENCE_IS_NULL;
            case 3:
                return QStatus.STATUS_BITMAP_IS_RECYCLED;
            case 4:
                return QStatus.STATUS_BITMAP_GET_INFO_FAILED;
            case 5:
                return QStatus.STATUS_BITMAP_NOT_RGBA8888;
            case 6:
                return QStatus.STATUS_BITMAP_LOCK_PIXELS_FAILED;
            default:
                return QStatus.STATUS_SUCCESS;
        }
    }

    public void getOsDeviceInfo() {
        this.os = "Android " + Build.VERSION.RELEASE;
        this.deviceInfo = Build.BRAND + "_" + Build.HARDWARE + "_" + Build.CPU_ABI;
    }

    public static String readableStatus(int i) {
        if (i == 0) {
            return "成功";
        }
        return "失败(" + i + ")";
    }
}

package com.lightning.edu.ei.edgealgorithm;

import android.os.Build;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Result {
    // 模糊置信度
    public float[] blurConf;
    // 模糊检测耗时 ms
    public long blurCost;
    // 模糊打分
    public float blurScore;
    // 模糊阈值
    public float blurThreshold;
    public float[][] boundingBoxes;
    // ORB判断
    public boolean checkOrbFeat;
    // 过暗判断耗时 ms
    public long darkCheckCost;
    // 过暗判断
    public int darkLabel;
    // 置信度
    public float[] darkLabelConf;
    // 切题算法总耗时 ms
    public long detectionCost;
    // 切题调试信息
    public String detectionMsg;
    // 切题状态
    public int detectionStatus;
    // 设备
    public String deviceInfo;
    // ORB特征耗时 ms
    public long featureCheckCost;
    // 输入尺寸，切题输入图像高度
    public int inHeight;
    // 输入Exif方向
    public int inOrientation;
    // 输入尺寸，切题输入图像宽度
    public int inWidth;
    // 模型初始化耗时 ms
    public long initCost;
    // 切题最近中心检测结果
    public float[] midboundingBox;
    // 调试信息
    public String msg;
    // 旋转角度
    public int orientation;
    // 置信度
    public float[] orientationConf;
    // 方向检测耗时 ms
    public long orientationCost;
    // 系统
    public String os;
    // 是否通过
    public boolean pass;
    // 预处理耗时 ms
    public long preProcessCost;
    // 算法总耗时 ms
    public long predictCost;
    // 场景判断耗时 ms
    public long sceneCost;
    // 场景分类
    public int sceneLabel;
    // 置信度
    public float[] sceneLabelConf;
    public long sceneOrientationCost;
    // 状态机
    public String stateMachineInfo;
    // 预测前状态机计数
    public int stateMachinePrePC;
    // 状态
    public int status;
    // Refuse信息
    public String threeClassMsg;
    // Refuse状态
    public int threeClassStatus;
    // 版本
    public String version;

    public static String readableDark(int i) {
        return i == 0 ? "过暗" : i == 1 ? "正常" : "未知亮度";
    }

    public static String readableOrientation(int i) {
        return i == 0 ? "0度" : i == 1 ? "左转90度" : i == 2 ? "旋转180度" : i == 3 ? "右转90度" : "未知角度";
    }

    public static String readableScene(int i) {
        return i == 0 ? "题目场景" : i == 1 ? "非题目场景" : "未知场景";
    }

    public Result() {
        this.pass = true;
        this.blurScore = -1.0f;
        this.orientation = -1;
        this.sceneLabel = -1;
        this.darkLabel = -1;
        this.blurConf = new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
        this.orientationConf = new float[]{-1.0f, -1.0f, -1.0f, -1.0f};
        this.sceneLabelConf = new float[]{-1.0f, -1.0f};
        this.darkLabelConf = new float[]{-1.0f, -1.0f};
        this.msg = "Success";
        this.threeClassMsg = "Success";
        this.stateMachineInfo = "";
        this.stateMachinePrePC = -1;
        this.version = "";
        this.checkOrbFeat = true;
        this.detectionMsg = "Success";
        this.boundingBoxes = new float[][]{new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f}};
        this.midboundingBox = new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
    }

    public String toString() {
        getOsDeviceInfo();
        return " 是否通过=" + this.pass
                + ",\n 模糊打分=" + this.blurScore
                + " [模糊阈值: " + this.blurThreshold
                + "],\n 模糊置信度=" + Arrays.toString(this.blurConf)
                + ",\n 输入Exif方向=" + this.inOrientation
                + ", 输入尺寸=w:" + this.inWidth + ", h:" + this.inHeight
                + ",\n 旋转角度=" + readableOrientation(this.orientation)
                + " 置信度: " + Arrays.toString(this.orientationConf)
                + ",\n 场景分类=" + readableScene(this.sceneLabel)
                + " 置信度: " + Arrays.toString(this.sceneLabelConf)
                + ",\n 过暗判断=" + readableDark(this.darkLabel)
                + " 置信度: " + Arrays.toString(this.darkLabelConf)
                + ",\n 模型初始化耗时=" + this.initCost
                + "ms,\n 预处理耗时=" + this.preProcessCost
                + "ms,\n 过暗判断耗时=" + this.darkCheckCost
                + "ms,\n 模糊检测耗时=" + this.blurCost
                + "ms,\n ORB特征耗时=" + this.featureCheckCost
                + "ms,\n 场景判断耗时=" + this.sceneCost
                + "ms,\n 方向检测耗时=" + this.orientationCost
                + "ms,\n 算法总耗时=" + this.predictCost
                + "ms,\n ORB判断=" + this.checkOrbFeat
                + ",\n 状态=" + readableStatus(this.status)
                + ",\n 调试信息='" + this.msg
                + "',\n Refuse状态=" + readableStatus(this.threeClassStatus)
                + ",\n Refuse信息='" + this.threeClassMsg
                + "',\n 预测前状态机计数='" + this.stateMachinePrePC
                + "',\n 状态机='" + this.stateMachineInfo
                + "',\n 切题输入图像宽度=" + this.inWidth
                + ", 高度=" + this.inHeight
                + ",\n 切题最近中心检测结果=" + Arrays.toString(this.midboundingBox)
                + ",\n 切题算法总耗时=" + this.detectionCost
                + "ms,\n 切题状态=" + readableDetectionStatus(this.detectionStatus)
                + ",\n 切题调试信息='" + this.detectionMsg
                + "',\n 版本=" + this.version
                + ",\n 系统&设备=" + this.os + "_" + this.deviceInfo;
    }

    public String toJSON() {
        getOsDeviceInfo();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("pass", this.pass);
            jSONObject.put("blurScore", this.blurScore);
            jSONObject.put("blurThreshold", this.blurThreshold);
            JSONArray jSONArray = new JSONArray();
            for (float v : this.blurConf) {
                jSONArray.put(v);
            }
            jSONObject.put("blurConf", jSONArray);
            jSONObject.put("sceneLabel", this.sceneLabel);
            JSONArray jSONArray2 = new JSONArray();
            for (float v : this.sceneLabelConf) {
                jSONArray2.put(v);
            }
            jSONObject.put("sceneLabelConf", jSONArray2);
            jSONObject.put("darkLabel", this.darkLabel);
            JSONArray jSONArray3 = new JSONArray();
            for (float v : this.darkLabelConf) {
                jSONArray3.put(v);
            }
            jSONObject.put("darkLabelConf", jSONArray3);
            jSONObject.put("inOrientation", this.inOrientation);
            jSONObject.put("inWidth", this.inWidth);
            jSONObject.put("inHeight", this.inHeight);
            jSONObject.put("orientation", this.orientation);
            JSONArray jSONArray4 = new JSONArray();
            for (float v : this.orientationConf) {
                jSONArray4.put(v);
            }
            jSONObject.put("orientationConf", jSONArray4);
            jSONObject.put("initCost", this.initCost + "ms");
            jSONObject.put("preProcessCost", this.preProcessCost + "ms");
            jSONObject.put("darkCheckCost", this.darkCheckCost + "ms");
            jSONObject.put("blurCost", this.blurCost + "ms");
            jSONObject.put("featureCheckCost", this.featureCheckCost + "ms");
            jSONObject.put("sceneCost", this.sceneCost + "ms");
            jSONObject.put("orientationCost", this.orientationCost + "ms");
            jSONObject.put("predictCost", this.predictCost + "ms");
            jSONObject.put("version", this.version);
            jSONObject.put("status", this.status);
            jSONObject.put("threeClassStatus", this.threeClassStatus);
            jSONObject.put("stateMachineInfo", this.stateMachineInfo);
            jSONObject.put("stateMachinePrePC", this.stateMachinePrePC);
            jSONObject.put("msg", this.msg);
            jSONObject.put("threeClassMsg", this.threeClassMsg);
            jSONObject.put("os", this.os);
            jSONObject.put("deviceInfo", this.deviceInfo);
            JSONArray jSONArray5 = new JSONArray();
            for (float box : this.midboundingBox) {
                jSONArray5.put(box);
            }
            JSONArray jSONArray6 = new JSONArray();
            jSONObject.put("midboundingBox", jSONArray5);
            for (float[] boundingBox : this.boundingBoxes) {
                JSONArray jSONArray7 = new JSONArray();
                for (float box : boundingBox) {
                    jSONArray7.put(box);
                }
                jSONArray6.put(jSONArray7);
            }
            jSONObject.put("boundingBoxes", jSONArray6);
            jSONObject.put("detectionCost", this.detectionCost + "ms");
            jSONObject.put("detectionStatus", this.detectionStatus);
            jSONObject.put("detectionMsg", this.detectionMsg);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getOsDeviceInfo() {
        this.os = "Android " + Build.VERSION.RELEASE;
        this.deviceInfo = Build.BRAND + "_" + Build.HARDWARE + "_" + Build.CPU_ABI;
    }

    public Status getStatus() {
        switch (this.status) {
            case 0:
                return Status.STATUS_SUCCESS;
            case 1:
                return Status.STATUS_INFERENCE_IS_NULL;
            case 2:
                return Status.STATUS_IMAGE_SMALL;
            case 3:
                return Status.STATUS_INFERENCE_IS_NULL_C;
            case 4:
                return Status.STATUS_BITMAP_IS_RECYCLED;
            case 5:
                return Status.STATUS_BITMAP_GET_INFO;
            case 6:
                return Status.STATUS_BITMAP_NOT_RGBA8888;
            case 7:
                return Status.STATUS_BITMAP_LOCK_PIXELS;
            case 8:
                return Status.STATUS_CHECK_BLUR_FAILED;
            case 9:
                return Status.STATUS_CHECK_SCENE_FAILED;
            case 10:
                return Status.STATUS_CHECK_DARK_FAILED;
            case 11:
                return Status.STATUS_NEED_RETAKE;
            case 12:
                return Status.STATUS_NEED_REFUSE;
            case 13:
                return Status.STATUS_DETECTION_INFERENCE_FAILED;
            case 14:
                return Status.STATUS_DETECTION_INFERENCE_IS_NULL;
            default:
                return Status.STATUS_SUCCESS;
        }
    }

    public static String readableStatus(int i) {
        if (i == 0) {
            return "成功";
        }
        return "失败(" + i + ")";
    }

    public static String readableDetectionStatus(int i) {
        if (i == -1) {
            return "被拒识";
        }
        if (i == 0) {
            return "成功";
        }
        return "失败(" + i + ")";
    }
}

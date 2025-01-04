package com.bytedance.ies.common;

import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class IESAppLogger {
    private static volatile IESAppLogger instance;
    private static final Object object;
    private String appId;
    private a appLoggerCallback;
    private Boolean initFlag;
    private Boolean isAbroad;

    /* loaded from: classes2.dex */
    public interface a {
        void a(String str, JSONObject jSONObject, String str2);
    }

    static {
        object = new Object();
        instance = null;
    }

    private IESAppLogger() {
        this.initFlag = false;
        this.appId = null;
        this.isAbroad = false;
    }

    public static IESAppLogger sharedInstance() {
        IESAppLogger iESAppLogger;
        if (instance != null) {
            IESAppLogger iESAppLogger2 = instance;
            return iESAppLogger2;
        }
        synchronized (object) {
            try {
                if (instance == null) {
                    instance = new IESAppLogger();
                }
                iESAppLogger = instance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return iESAppLogger;
    }

    public void setAppLogCallback(String str, a aVar, boolean z) {
        if (this.initFlag.booleanValue()) {
            return;
        }
        synchronized (object) {
            try {
                if (!this.initFlag.booleanValue()) {
                    this.appId = str;
                    this.appLoggerCallback = aVar;
                    this.initFlag = true;
                    this.isAbroad = Boolean.valueOf(z);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void appLogOnEvent(String str, String str2, String str3) {
        appLogOnEvent(str, str2, str3, false);
    }

    public void appLogOnEvent(String str, String str2, String str3, boolean z) {
        JSONObject jSONObject;
        if (this.appLoggerCallback == null) {
            return;
        }
        try {
            jSONObject = new JSONObject(str2);
        } catch (JSONException e) {
            e.printStackTrace();
            jSONObject = new JSONObject();
        }
        appLogOnEvent(str, jSONObject, str3, z);
    }

    public void appLogOnEvent(String str, JSONObject jSONObject, String str2) {
        appLogOnEvent(str, jSONObject, str2, false);
    }

    public void appLogOnEvent(String str, JSONObject jSONObject, String str2, boolean z) {
        if (this.appLoggerCallback == null) {
            return;
        }
        if (z) {
            this.appLoggerCallback.a(str, copyJson(jSONObject), str2);
        }
        JSONObject copyJson = copyJson(jSONObject);
        try {
            if (this.isAbroad.booleanValue()) {
                copyJson.put("second_appid", "2780");
                copyJson.put("second_appname", "vesdk_abroad");
            } else {
                copyJson.put("second_appid", "1357");
                copyJson.put("second_appname", "video_editor_sdk");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.appLoggerCallback.a(str, copyJson, str2);
    }

    private static JSONObject copyJson(JSONObject jSONObject) {
        if (jSONObject == null) {
            JSONObject jSONObject2 = new JSONObject();
            return jSONObject2;
        }
        LinkedList linkedList = new LinkedList();
        Iterator<String> keys = jSONObject.keys();
        if (keys == null) {
            JSONObject jSONObject3 = new JSONObject();
            return jSONObject3;
        }
        while (keys.hasNext()) {
            linkedList.add(keys.next());
        }
        try {
            JSONObject jSONObject4 = new JSONObject(jSONObject, (String[]) linkedList.toArray(new String[0]));
            return jSONObject4;
        } catch (Exception e) {
            e.printStackTrace();
            return jSONObject;
        }
    }
}

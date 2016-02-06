package com.sannniu.ncore.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/7/2.
 */
public class LogUtils {
    private static String TAG = "";
    private static int LOG_LEVEL = 6;
    public static int ERROR = 1;
    public static int WARN = 2;
    public static int INFO = 3;
    public static int DEBUG = 4;
    public static int VERBOS = 5;

    public static void setDebug(boolean isDebug, String TAG) {
        LogUtils.TAG = TAG;
        if (!isDebug) {
            LogUtils.LOG_LEVEL = ERROR;
        }

    }

    public static void setLogLevel(int logLevel) {
        LOG_LEVEL = logLevel;
    }

    public static void setTAG(String tag) {
        TAG = tag;
    }

    static String className;
    static String methodName;
    static int lineNumber;

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(className);
        buffer.append("[");
        buffer.append(methodName + ":" + lineNumber);
        buffer.append("]");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        if (sElements.length >= 1) {
            className = sElements[1].getFileName();
            methodName = sElements[1].getMethodName();
            lineNumber = sElements[1].getLineNumber();
        }
    }

    public static void e(String msg) {
        if (LOG_LEVEL > ERROR && msg != null) {
            getMethodNames(new Exception().getStackTrace());
            Log.e(TAG, createLog(msg));
        }
    }

    public static void w(String msg) {
        if (LOG_LEVEL > WARN && msg != null) {
            getMethodNames(new Exception().getStackTrace());
            Log.w(TAG, createLog(msg));
        }
    }

    public static void i(String msg) {
        if (LOG_LEVEL > INFO && msg != null) {
            getMethodNames(new Exception().getStackTrace());
            Log.i(TAG, createLog(msg));
        }
    }

    public static void d(String msg) {
        if (LOG_LEVEL > DEBUG && msg != null) {
            getMethodNames(new Exception().getStackTrace());
            Log.d(TAG, createLog(msg));
        }
    }

    public static void v(String msg) {
        if (LOG_LEVEL > VERBOS && msg != null) {
            getMethodNames(new Exception().getStackTrace());
            Log.v(TAG, createLog(msg));
        }
    }

    public static void error(String msg) {
        Log.e(TAG, msg);
    }
}

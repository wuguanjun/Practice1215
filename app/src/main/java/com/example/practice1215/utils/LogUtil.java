package com.example.practice1215.utils;

import android.util.Log;

import com.example.practice1215.BuildConfig;
import com.example.practice1215.constants.Constants;

/**
 * Created by guanjun on 2016/5/12.
 */
public class LogUtil {
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG && Constants.isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG && Constants.isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG && Constants.isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG && Constants.isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG && Constants.isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (BuildConfig.DEBUG && Constants.isDebug) {
            Log.wtf(tag, msg);
        }
    }
}

package com.jacky.mycloudmusic.util;

import android.util.Log;

import com.jacky.mycloudmusic.BuildConfig;

/**
 * 日志工具类
 * 对Android日志API做一层简单的封装
 */
public class LogUtil {

    /**
     * 是否是调试模式
     */
    public static boolean isDebug = BuildConfig.DEBUG;

    /**
     * 调试级别日志
     */
    public static void d(String tag, String value) {
        if (isDebug) {
            Log.d(tag,value);
        }
    }

    /**
     * 警告级别日志
     */
    public static void w(String tag, String value) {
        if (isDebug) {
            Log.w(tag,value);
        }
    }
}

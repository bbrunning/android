package com.zwt.superandroid.util;

import android.util.Log;

/**
 * log接口类
 *
 * @author guozhiqing
 */
public final class LogUtil {
    public static boolean mDebug = true;


    public static void d(String tag, String str) {
        if (mDebug) {
            try {
                Log.d(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void w(String tag, String str) {
        if (mDebug) {
            try {
                Log.w(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void e(String tag, String str) {
        if (mDebug) {
            try {
                Log.e(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void i(String tag, String str) {
        if (mDebug) {
            try {
                Log.i(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void v(String tag, String str) {
        if (mDebug) {
            try {
                Log.v(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}

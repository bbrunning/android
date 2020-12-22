package com.zwt.superandroid.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.zwt.superandroid.base.BaseApplication;

import java.lang.reflect.Field;

/**
 * 显示Toast
 */
public class ToastUtil {

    private static final String TAG = ToastUtil.class.getSimpleName();

    public static final int TOAST_DURATION = Toast.LENGTH_SHORT;

    private static long sLastToastTimestamp;

    private static Toast mToast;

    /**
     * 显示Toast
     *
     * @param context : 上下文
     * @param msg     ：提示信息
     */
    public static void show(Context context, String msg) {
        show(context, msg, TOAST_DURATION);
    }

    public static void show(String msg) {
        show(BaseApplication.getContext(), msg);
    }

    /**
     * 显示Toast
     *
     * @param msg      : 提示信息
     * @param duration ：显示时长
     */
    public synchronized static void show(String msg, int duration) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(BaseApplication.getContext(), msg, duration);
            } else {
                mToast.cancel();
                mToast.setText(msg);
                mToast.setDuration(duration);
            }
            hookToast(mToast);
            mToast.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static void show(Context context, String msg, int duration) {
        try {
            Toast toast = Toast.makeText(context, msg, duration);
            hookToast(toast);
            toast.show();
//            Toast.makeText(context, msg, duration).show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示Toast
     *
     * @param context  : 上下文
     * @param msgId    : 提示信息id
     * @param duration ：显示时长
     */
    public static void show(Context context, int msgId, int duration) {
        try {
            show(context, context.getString(msgId), duration);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }



    //refer: https://blog.csdn.net/okg0111/article/details/83957680
    private static Field sField_TN;
    private static Field sField_TN_Handler;
    private static boolean sIsHookFieldInit = false;
    private static final String FIELD_NAME_TN = "mTN";
    private static final String FIELD_NAME_HANDLER = "mHandler";

    private static void hookToast(Toast toast) {
        try {
            if (!sIsHookFieldInit) {
                sField_TN = Toast.class.getDeclaredField(FIELD_NAME_TN);
                sField_TN.setAccessible(true);
                sField_TN_Handler = sField_TN.getType().getDeclaredField(FIELD_NAME_HANDLER);
                sField_TN_Handler.setAccessible(true);
                sIsHookFieldInit = true;
            }
            Object tn = sField_TN.get(toast);
            Handler originHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(originHandler));
        } catch (Throwable e) {
            Log.e(TAG, "Hook toast exception=" + e);
        }
    }

    /**
     * Safe outside Handler class which just warps the system origin handler object in the Toast.class
     */
    private static class SafelyHandlerWrapper extends Handler {
        private Handler originHandler;

        public SafelyHandlerWrapper(Handler originHandler) {
            this.originHandler = originHandler;
        }

        @Override
        public void dispatchMessage(Message msg) {
            // The outside handler SafelyHandlerWrapper object just catches the Exception while dispatch the message
            // if the the inside system origin handler object throw the BadTokenException，the outside safe SafelyHandlerWrapper object
            // just catches the exception here to avoid the app crashing
            try {
                super.dispatchMessage(msg);
            } catch (Throwable e) {
                Log.e(TAG, "Catch system toast exception:" + e);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            //just pass the Message to the origin handler object to handle
            if (originHandler != null) {
                originHandler.handleMessage(msg);
            }
        }
    }
}

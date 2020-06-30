package com.zwt.superandroid.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by john on 2016/11/21.
 */

public class WeakHandler extends Handler {
    private WeakReference<Context> mContextReference;
    private MessageListener mListener;
    private boolean mHaveContext = true;

    public WeakHandler(Context context, MessageListener listener) {
        mHaveContext = context != null;
        mContextReference = new WeakReference<>(context);
        mListener = listener;
    }

    public void setListener(MessageListener listener) {
        this.mListener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mContextReference != null) {
            Context context = mContextReference.get();
            if ((context != null || !mHaveContext) && mListener != null) {
                mListener.handleMessages(msg);
            }
        }
    }

    public interface MessageListener {
        void handleMessages(Message msg);
    }
}

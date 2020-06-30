package com.zwt.superandroid.base;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/9/4
 * Time: 14:55
 */
public class BaseApplication extends MultiDexApplication {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}

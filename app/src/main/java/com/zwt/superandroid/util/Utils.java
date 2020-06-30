package com.zwt.superandroid.util;

import android.content.Context;

import java.util.List;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/6/14
 * Time: 18:02
 */
public class Utils {

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return px
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static int getCount(List<?> list) {
        return list == null ? 0 : list.size();
    }
}

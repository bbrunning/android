package com.zwt.superandroid.base.widget;

import android.view.View;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/6/17
 * Time: 14:11
 */
public interface BaseInitListener {
    void onLeftButtonClick(View view);

    void onRightButtonClick(View view);

    int onGetTitleColor();

    Object getTitleString();

    int getContentView();

}

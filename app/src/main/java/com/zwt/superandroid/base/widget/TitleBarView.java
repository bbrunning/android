package com.zwt.superandroid.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.zwt.superandroid.R;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/6/17
 * Time: 15:23
 */
public class TitleBarView extends Toolbar {
    private TextView mTbLeftView;
    private TextView mTbTitleView;
    private TextView mTbRightView;

    public TitleBarView(Context context) {
        super(context);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTbLeftView = (TextView) findViewById(R.id.tb_left_tv);
        mTbTitleView = (TextView) findViewById(R.id.tb_title_tv);
        mTbRightView = (TextView) findViewById(R.id.tb_right_tv);
    }

    public TextView getLeftView() {
        return mTbLeftView;
    }

    public void setTitleView(Object title) {
        if (title instanceof Integer) {
            mTbTitleView.setText((Integer) title);
        } else if (title instanceof String) {
            mTbTitleView.setText((String) title);
        }
    }

    public TextView getRightView() {
        return mTbRightView;
    }
}

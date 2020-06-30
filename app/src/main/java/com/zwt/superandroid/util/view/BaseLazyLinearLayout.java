package com.zwt.superandroid.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseLazyLinearLayout<E extends ViewDataBinding> extends LinearLayout {

    public Context mContext;
    private View mRootView;
    protected E dataBinding;

    public BaseLazyLinearLayout(Context context) {
        super(context,null);

    }

    public BaseLazyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView();
    }

    public BaseLazyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        mContext = getContext();
        if ((mRootView = isUseDataBinding() ? initDataBindingView() : initLayoutView()) != null) {
            initChildView(mRootView);
        }
    }

    private View initDataBindingView() {
        View dataBindingView = null;
        try {
            dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), getRootViewIds(), this, false);
            if (dataBinding != null) {
                addView(dataBindingView = dataBinding.getRoot());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return dataBindingView;
    }

    private View initLayoutView() {
        return LayoutInflater.from(getContext()).inflate(getRootViewIds(), this, true);
    }

    /**
     * 是否使用dataBinding
     * @return
     */
    public boolean isUseDataBinding() {
        return false;
    }

    public abstract int getRootViewIds();

    public abstract void initChildView(View view);

}

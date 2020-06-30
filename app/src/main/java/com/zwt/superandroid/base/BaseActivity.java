package com.zwt.superandroid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.zwt.superandroid.R;
import com.zwt.superandroid.base.widget.BaseActivityHelper;
import com.zwt.superandroid.base.widget.BaseInitListener;
import com.zwt.superandroid.base.widget.TitleBarView;
import com.zwt.superandroid.util.StatusBarUtil;

import java.util.HashMap;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/6/14
 * Time: 15:02
 */
public abstract class BaseActivity<E extends ViewDataBinding> extends FragmentActivity implements BaseInitListener {

    public Context mContext;
    private TitleBarView mToolbar;
    private RelativeLayout mContentLayout;
    private ViewGroup mBaseFragment;
    private boolean isShowTitle;
    protected E mDataBinding;
    private View mChildContentView;
    /**
     * 辅助帮助类
     */
    private BaseActivityHelper mBaseActivityHelper;

    /**
     * 可以直接add Fragment
     */
    protected FragmentManager mFragmentManager;
    private View mToolBarTierView;
    private HashMap<String, Fragment> mFragmentMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mFragmentManager = getSupportFragmentManager();
        mBaseActivityHelper = new BaseActivityHelper((Activity) mContext);
        mBaseFragment = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        mToolbar = (TitleBarView) mBaseFragment.findViewById(R.id.base_tool_bar_view);
        mToolBarTierView = mBaseFragment.findViewById(R.id.tier_view);
        mContentLayout = (RelativeLayout) mBaseFragment.findViewById(R.id.body);
        if (getTitleString() != null && !getTitleString().equals(0)) {
            mToolbar.setTitleView(getTitleString());
            StatusBarUtil.showToolBar(mContext, mToolbar, mToolBarTierView, isShowTitle = true, onGetTitleColor());
            mToolbar.getLeftView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftButtonClick(v);
                }
            });
            mToolbar.getRightView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightButtonClick(v);
                }
            });
        } else {
            StatusBarUtil.showToolBar(mContext, mToolbar, mToolBarTierView, isShowTitle = false, onGetTitleColor());
        }
        if (getContentView() > 0) {
            mContentLayout.removeAllViews();
            mChildContentView = LayoutInflater.from(this).inflate(getContentView(), null);
            mContentLayout.addView(mChildContentView, mContentLayout.getLayoutParams());
        }
        setContentView(mBaseFragment);
        if (mChildContentView != null) {
            try {
                mDataBinding = DataBindingUtil.bind(mChildContentView);
            } catch (Throwable r) {
//                LogUtil.i(TAG, "data binding exception");
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mBaseActivityHelper.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaseActivityHelper.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaseActivityHelper.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaseActivityHelper.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseActivityHelper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaseActivityHelper.onPause();
    }

    @Override
    public void onLeftButtonClick(View view) {

    }

    @Override
    public void onRightButtonClick(View view) {

    }

    @Override
    public int onGetTitleColor() {
        return R.color.colorPrimary;
    }

    /**
     * 此方法可以设置Tier color 6.0以下手机可用
     *
     * @param color
     */
    public void setTierViewColor(int color) {
        if (mToolBarTierView != null) {
            mToolBarTierView.setBackgroundColor(mContext.getResources().getColor(color));
        }
    }

    /**
     * 是否使用深色调
     *
     * @param isUseDart
     */
    public void setStatusTextColor(boolean isUseDart) {
        StatusBarUtil.setStatusTextColor((Activity) mContext, isUseDart);
    }

    /**
     * 是否展示title
     *
     * @param isShow
     */
    public void isShowTitleBarView(boolean isShow) {
        isShowTitleBarView(isShow, null);
    }

    /**
     * 是否展示title
     *
     * @param isShow
     * @param title
     */
    public void isShowTitleBarView(boolean isShow, String title) {
        if (mToolbar != null) {
            if (isShow) {
                if (!TextUtils.isEmpty(title)) {
                    mToolbar.setTitleView(title);
                }
                mToolbar.setVisibility(View.VISIBLE);
            } else {
                mToolbar.setVisibility(View.GONE);
            }
        }
    }


    public void setFragmentMap() {
        if (mFragmentMap == null) {
            this.mFragmentMap = new HashMap<>();
        }
    }

    public HashMap<String, Fragment> getFragmentMap() {
        return mFragmentMap;
    }

    public void addFragmentMapElement(String key, Fragment fragment) {
        if (mFragmentMap != null) {
            mFragmentMap.put(key, fragment);
        }
    }

    public boolean isNeedAddFragment(String name) {
        return mFragmentMap != null && mFragmentMap.get(name) == null;
    }

    /**
     * 加到默认根节点, 使用此方法依赖于{@link R.id#body}, 确保调用的{@link BaseActivity#getContentView()}返回0
     *
     * @param fragment
     */
    public void add(Fragment fragment) {
        add(R.id.body, fragment);
    }

    /**
     * @param resid
     * @param fragment
     */
    public void add(int resid, Fragment fragment) {
        mFragmentManager.beginTransaction().add(resid, fragment).commitAllowingStateLoss();
        if (mFragmentMap != null) {
            mFragmentMap.put(fragment.getClass().getSimpleName(), fragment);
        }
    }

    /**
     * 替换
     *
     * @param resid
     * @param fragment
     */
    public void replace(int resid, Fragment fragment) {
        mFragmentManager.beginTransaction().replace(resid, fragment).commitAllowingStateLoss();
    }

    /**
     * 显示fragment
     *
     * @param fragment
     */
    public void show(Fragment fragment) {
        if (fragment != null) {
            mFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    public void hide(Fragment fragment) {
        if (fragment != null) {
            mFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    public void remove(Fragment fragment) {
        try {
            mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}

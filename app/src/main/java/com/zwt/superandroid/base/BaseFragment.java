package com.zwt.superandroid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.zwt.superandroid.util.StatusBarUtil;
import com.zwt.superandroid.util.UIUtil;
import com.zwt.superandroid.util.Utils;


/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/8/5
 * Time: 19:49
 */
public abstract class BaseFragment extends Fragment {
    public Context mContext;
    private FragmentManager mFragmentManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentView() != 0 && mContext != null) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            View statusBarColorView = new View(mContext);
            frameLayout.addView(statusBarColorView);
            View contentView = inflater.inflate(getContentView(), container, false);
            frameLayout.addView(contentView);
            if (isShowStatusBar()) {
                if (getStatusBarColor() != 0) {
                    statusBarColorView.setBackgroundColor(mContext.getResources().getColor(getStatusBarColor()));
                } else {
                    statusBarColorView.setBackground(contentView.getBackground());
                }
                UIUtil.setFrameLayoutParams(statusBarColorView, ViewGroup.LayoutParams.MATCH_PARENT, Utils.getStatusHeight(mContext));
                UIUtil.setFrameLayoutMargin(contentView, 0, Utils.getStatusHeight(mContext), 0, 0);
            }
            return frameLayout;
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarUtil.setStatusTextColor((Activity) mContext, isUseDart());
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusTextColor((Activity) mContext, isUseDart());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            StatusBarUtil.setStatusTextColor((Activity) mContext, isUseDart());
        }
    }

    public void replace(int resid, Fragment fragment) {
        mFragmentManager.beginTransaction().replace(resid, fragment);
    }

    public void add(int resid, Fragment fragment, String tag) {
        mFragmentManager.beginTransaction().add(resid, fragment, tag).commitAllowingStateLoss();
    }

    public void show(Fragment fragment) {
        mFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
    }

    public void hide(Fragment fragment) {
        mFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }


    public abstract int getContentView();

    /**
     * 是否展示状态栏
     * @return
     */
    public abstract boolean isShowStatusBar();

    /**
     * 是否设置深色
     *
     * @return
     */
    public boolean isUseDart() {
        return false;
    }

    /**
     * 设置状态栏背景颜色
     *
     * @return
     */
    public int getStatusBarColor() {
        return 0;
    }

}

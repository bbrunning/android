package com.zwt.superandroid.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zwt.superandroid.R;
import com.zwt.superandroid.base.BaseFragment;


public class MineFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    public boolean isUseDart() {
        return false;
    }

    @Override
    public int getStatusBarColor() {
        return R.color.colorAccent;
    }

    @Override
    public boolean isShowStatusBar() {
        return true;
    }

}

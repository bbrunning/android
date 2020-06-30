package com.zwt.superandroid.home.fragment;

import com.zwt.superandroid.R;
import com.zwt.superandroid.base.BaseFragment;


public class FindFragment extends BaseFragment {

    @Override
    public int getContentView() {
        return R.layout.fragment_find;
    }

    @Override
    public boolean isUseDart() {
        return false;
    }

    @Override
    public int getStatusBarColor() {
        return R.color.home_text_select;
    }

    @Override
    public boolean isShowStatusBar() {
        return true;
    }


}

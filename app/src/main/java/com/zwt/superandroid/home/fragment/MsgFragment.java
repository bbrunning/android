package com.zwt.superandroid.home.fragment;

import com.zwt.superandroid.R;
import com.zwt.superandroid.base.BaseFragment;


public class MsgFragment extends BaseFragment {

    @Override
    public int getContentView() {
        return R.layout.fragment_msg;
    }

    @Override
    public boolean isUseDart() {
        return false;
    }

    @Override
    public int getStatusBarColor() {
        return 0;
    }

    @Override
    public boolean isShowStatusBar() {
        return true;
    }


}

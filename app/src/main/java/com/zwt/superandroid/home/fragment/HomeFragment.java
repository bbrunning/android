package com.zwt.superandroid.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zwt.superandroid.R;
import com.zwt.superandroid.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView mTestWeb = (WebView)view.findViewById(R.id.testWeb);
        WebSettings sSet = mTestWeb.getSettings();
        sSet.setJavaScriptEnabled(true);
        mTestWeb.loadUrl("file:///android_asset/test.html");
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public boolean isUseDart() {
        return true;
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

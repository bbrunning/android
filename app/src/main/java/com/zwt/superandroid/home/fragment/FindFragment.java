package com.zwt.superandroid.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zwt.superandroid.R;
import com.zwt.superandroid.base.BaseFragment;


public class FindFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.test_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                 intent.setData(Uri.parse("zwt://data:8888/android/study?p1=zhangsan&p2=你好"));
                startActivity(intent);
            }
        });
    }

    


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

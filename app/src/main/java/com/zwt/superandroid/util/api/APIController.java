package com.zwt.superandroid.util.api;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zwt.superandroid.R;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/16
 * Time: 18:50
 */
public class APIController extends Dialog {

    public APIController(@NonNull Context context) {
        super(context);
    }

    public APIController(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected APIController(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.api_controller_view, null);
        setContentView(view);

    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        window.getDecorView().setBackgroundColor(0x00000000); // 设置A通道为0即完全透明
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

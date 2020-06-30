package com.zwt.superandroid.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.Toolbar;

import com.zwt.superandroid.R;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/6/14
 * Time: 18:05
 */
public class StatusBarUtil {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static void showToolBar(Context context, Toolbar mToolbar, View tierView, boolean showTitle, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(context);
        }
        toolbarTitleInvade(context, mToolbar, tierView, showTitle, color);
    }

    /**
     * 显示Toolbar
     *
     * @param show true:显示,false:隐藏
     */
    private static void toolbarTitleInvade(Context context, Toolbar mToolbar, View tierView, boolean show, int color) {
        if (mToolbar != null) {
            int paddingTop = mToolbar.getPaddingTop();
            int paddingBottom = mToolbar.getPaddingBottom();
            int paddingLeft = mToolbar.getPaddingLeft();
            int paddingRight = mToolbar.getPaddingRight();
            int statusHeight = Utils.getStatusHeight(context);
            ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
            int height = params.height;
            /**
             * 利用状态栏的高度，4.4及以上版本给Toolbar设置一个paddingTop值为status_bar的高度，
             * Toolbar延伸到status_bar顶部
             **/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (show) {
                    paddingTop += statusHeight;
                    height += statusHeight;
                } else {
                    paddingTop -= statusHeight;
                    height -= statusHeight;
                }
            }
            params.height = height;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                tierView.setVisibility(View.VISIBLE);
                UIUtil.setRelativeLayoutParams(tierView, ScreenUtil.getScreenWidth(context), Utils.getStatusHeight(context));
            } else {
                tierView.setVisibility(View.GONE);
            }
            mToolbar.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            mToolbar.setVisibility(show ? View.VISIBLE : View.GONE);
            mToolbar.setBackgroundColor(context.getResources().getColor(color));
        }
    }

    /**
     * 设置透明状态栏
     * 对4.4及以上版本有效
     * 参考 https://www.jianshu.com/p/afbeb24deb7f
     */
    public static void setTranslucentStatus(Context context) {
        Window window = ((Activity) context).getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(context.getResources().getColor(R.color.android_statusBar_color));
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private static void processFlyMe(boolean isLightStatusBar, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        try {
            Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
            int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
            Field field = instance.getDeclaredField("meizuFlags");
            field.setAccessible(true);
            int origin = field.getInt(lp);
            if (isLightStatusBar) {
                field.set(lp, origin | value);
            } else {
                field.set(lp, (~value) & origin);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上  lightStatusBar为真时表示黑色字体
     */
    private static void processMIUI(boolean lightStatusBar, Activity activity) {
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (lightStatusBar) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (lightStatusBar) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    /**
     * 判断手机是否是小米
     *
     * @return
     */
    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 判断手机是否是魅族
     *
     * @return
     */
    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 设置状态栏文字色值为深色调
     *
     * @param useDart  是否使用深色调
     * @param activity
     * 参考https://www.jianshu.com/p/3042bf7d72e3
     */
    public static void setStatusTextColor(Activity activity, boolean useDart) {
        try {
            if (isFlyme()) {
                processFlyMe(useDart, activity);
            } else if (isMIUI()) {
                processMIUI(useDart, activity);
            } else {
                if (useDart) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                } else {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

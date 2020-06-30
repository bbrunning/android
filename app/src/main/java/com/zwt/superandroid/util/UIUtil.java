package com.zwt.superandroid.util;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import androidx.core.view.ViewCompat;

public class UIUtil {

    /**
     * 设置RelativeLayout中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public static void setRelativeLayoutParams(View view, int width, int height) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.width = width;
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }

    public static void setRelativeLayoutParams(View view, int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.width = width;
            lp.height = height;
            lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            view.setLayoutParams(lp);
        }
    }

    public static void setRelativeLayoutMargin(View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            view.setLayoutParams(lp);
        }
    }

    public static void setLinearLayoutMargin(View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            view.setLayoutParams(lp);
        }
    }

    public static int getRelativeLayoutMarginLeft(View view) {
        if (null != view) {
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            if (null != lp) {
                return lp.leftMargin;
            }
        }
        return 0;
    }

    public static int getRelativeLayoutMarginTop(View view) {
        if (null != view) {
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            if (null != lp) {
                return lp.topMargin;
            }
        }
        return 0;
    }

    public static int getRelativeLayoutMarginRight(View view) {
        if (null != view) {
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            if (null != lp) {
                return lp.rightMargin;
            }
        }
        return 0;
    }

    public static int getRelativeLayoutMarginBottom(View view) {
        if (null != view) {
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            if (null != lp) {
                return lp.bottomMargin;
            }
        }
        return 0;
    }

    /**
     * 设置FrameLayout中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public static void setFrameLayoutParams(View view, int width, int height) {
        if (null != view) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            if (null != lp) {
                lp.width = width;
                lp.height = height;
                view.setLayoutParams(lp);
            }
        }
    }

    public static void setFrameLayoutMargin(View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        if (null != view) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            if (null != lp) {
                lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
                view.setLayoutParams(lp);
            }
        }
    }

    public static void setFrameLayoutParams(View view, int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.width = width;
            lp.height = height;
            lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            view.setLayoutParams(lp);
        }
    }


    /**
     * 设置AbsListView中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public static void setAbsListViewParams(View view, int width, int height) {
        AbsListView.LayoutParams lp = (AbsListView.LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.width = width;
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 设置LinearLayout中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public static void setLinearLayoutParams(View view, int width, int height) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.width = width;
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }

    public static void setLinearLayoutParams(View view, int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (null != lp) {
            lp.width = width;
            lp.height = height;
            lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            view.setLayoutParams(lp);
        }
    }

    public static void setLinearLayoutParams(View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        view.setLayoutParams(lp);
    }

    /**
     * 创建ImageView（心情）(width=150dip;height=150dip)
     *
     * @param context
     * @return
     */
    public static ImageView createMoodContent(Context context) {
        int width = ScreenUtil.dip2px(context, 180);
        int height = ScreenUtil.dip2px(context, 180);
        ImageView imageView = createContentImageView(context);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.gravity = Gravity.LEFT;
        params.width = width;
        params.height = height;
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setLayoutParams(params);
        return imageView;
    }

    /**
     * 创建ImageView（帖子）
     *
     * @param context
     * @return
     */
    public static ImageView createContentImageView(Context context) {
        int imagePxMarginTopBottom = ScreenUtil.dip2px(context, 5);
        int imagePxMarginLeftRight = ScreenUtil.dip2px(context, 6);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.leftMargin = imagePxMarginLeftRight;
        params.rightMargin = imagePxMarginLeftRight;
        params.topMargin = imagePxMarginTopBottom;
        params.bottomMargin = imagePxMarginTopBottom;
        ImageView iv = new ImageView(context);
        iv.setLayoutParams(params);
        return iv;
    }

    public static int getVisibleHeight(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.bottom - rect.top;
    }

    public static void setBackground(View view, Drawable drawable) {
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();
        ViewCompat.setBackground(view, drawable);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public static void getVisibleRectOnScreen(View view, Rect rect, boolean ignoreOffset, int[] screenLocation) {
        if (ignoreOffset) {
            view.getGlobalVisibleRect(rect);
        } else {
            if (screenLocation == null || screenLocation.length != 2) {
                screenLocation = new int[2];
            }

            view.getLocationOnScreen(screenLocation);
            rect.set(0, 0, view.getWidth(), view.getHeight());
            rect.offset(screenLocation[0], screenLocation[1]);
        }

    }

    public static void getVisibleRectOnScreen(View view, Rect rect, boolean ignoreOffset) {
        getVisibleRectOnScreen(view, rect, ignoreOffset, (int[])null);
    }
}

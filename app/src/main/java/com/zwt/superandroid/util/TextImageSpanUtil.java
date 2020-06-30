package com.zwt.superandroid.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DCH <a href="mailto:chuanhao.dai@drcuiyutao.com">Contract me.</a>
 * @since 2019/4/17
 */
public class TextImageSpanUtil {

    public static void setTextImage(Context context, TextView textView, String uri, String source, int maxLine, int maxWidth) {
      //  setTextImage(context, textView, uri, source, maxLine, maxWidth, null);
    }

    public static void setTextImageDrawable(Context context, TextView textView, String source, int sourceId, int maxLine, int maxWidth) {
        if (context != null && textView != null) {
            final Drawable drawable = context.getResources().getDrawable(sourceId);// 获取图片
            setTextViewAddImageTag(textView, source, drawable, maxLine, maxWidth);
        }
    }

    /**
     * 这里要根据工程网络图片下载机制自己实现
     *
     * @param textView
     * @param content
     * @param drawable
     * @param maxLines
     * @param maxWidth
     */
/*    public static void setTextImage(Context context, TextView textView, String uri, String source, int maxLine, int maxWidth, ImageUtil.ImageLoadingListener listener) {
        if (source != null) {
            ImageUtil.loadImage(uri, new ImageUtil.ImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (context != null && textView != null) {
                        setTextViewAddImageTag(textView, source, new BitmapDrawable(loadedImage), maxLine, maxWidth);
                    }

                    if (listener != null) {
                        listener.onLoadingComplete(imageUri, view, loadedImage);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, ImageUtil.LoadingFailType failType) {
                    if (textView != null) {
                        textView.setText(source);
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    if (textView != null) {
                        textView.setText(source);
                    }
                }
            });
        }
    }*/
    public static void setTextViewAddImageTag(TextView textView, String content, Drawable drawable, int maxLines, int maxWidth) {
        try {
            textView.setMaxLines(maxLines);
            maxLines++;
            TextPaint paint = textView.getPaint();
            Paint.FontMetrics fm = paint.getFontMetrics();
            int textFontHeight = (int) Math.ceil(fm.descent - fm.top);
            int imageWidth = drawable.getIntrinsicWidth() * textFontHeight
                    / drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, (int) (imageWidth / 1.5),
                    (int) (imageWidth / 1.5));
            String textTemp = content;
            int endIndex = 0;
            int index = 0;
            String towLineText = "";
            List<String> strDataList = new ArrayList<>();
            while (!TextUtils.isEmpty(textTemp)) {
                TextParams textParams = getLineMaxNumber(textTemp, textView.getPaint(), maxWidth);
                endIndex = textParams.getIndex();
                index++;
                if (index < maxLines) {
                    strDataList.add(textParams.getText());
                }
                if (content.endsWith(textParams.getText())) {
                    if (((maxWidth - textParams.getWidth() >= drawable.getIntrinsicWidth())) && index < maxLines) {
                        addImageTagView(textView, drawable, content);
                    } else if (Utils.getCount(strDataList) > 0) {
                        for (String text : strDataList) {
                            towLineText = towLineText + text;
                        }
                        String endAddStr = "...";
                        content = towLineText.substring(0, towLineText.length() - endAddStr.length()) + endAddStr;
                        addImageTagView(textView, drawable, content);
                    } else {
                        textView.setText(content);
                    }
                }
                textTemp = textTemp.substring(endIndex);
            }
        } catch (Throwable e) {
            textView.setText(content);
        }
    }

    private static void addImageTagView(TextView textView, Drawable drawable, String content) {
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        content += "*";
        SpannableString spanString = new SpannableString(content);
        spanString.setSpan(span, content.length() - 1, content.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    private static TextParams getLineMaxNumber(String text, TextPaint paint, int maxWidth) {
        TextParams textParam = null;
        if (null == text || "".equals(text)) {
            return textParam;
        }
        StaticLayout staticLayout = new StaticLayout(text, paint, maxWidth, Layout.Alignment.ALIGN_NORMAL
                , 0, 0, false);
        textParam = new TextParams();
        //获取第一行最后显示的字符下标
        textParam.setIndex(staticLayout.getLineEnd(0));
        textParam.setWidth(staticLayout.getLineWidth(0));
        String textTemp = text.substring(0, textParam.getIndex());
        textParam.setText(textTemp);
        return textParam;
    }

    public static class TextParams {
        private String mText;
        private int mIndex;
        private float mWidth;

        public int getIndex() {
            return mIndex;
        }

        public void setIndex(int index) {
            this.mIndex = index;
        }

        public float getWidth() {
            return mWidth;
        }

        public void setWidth(float width) {
            this.mWidth = width;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            this.mText = text;
        }
    }
}

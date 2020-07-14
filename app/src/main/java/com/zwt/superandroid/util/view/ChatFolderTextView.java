package com.zwt.superandroid.util.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.zwt.superandroid.R;
import com.zwt.superandroid.util.LogUtil;


/**
 * 参考网址
 *
 *尾部带查看更多的TextView
 * https://blog.csdn.net/lmo28021080/article/details/52181766
 *
 * ListView里TextView使用ClickableSpan,ListView的Item无法点击的解决办法
 * 参考https://blog.csdn.net/xiaokanghello/article/details/55520587
 */
public class ChatFolderTextView extends AppCompatTextView {

    // TAG
    private static final String TAG = "FolderTextView";

    // 默认打点文字
    private static final String DEFAULT_ELLIPSIZE = "...";
    // 默认收起文字
    private static final String DEFAULT_FOLD_TEXT = "收起";
    // 默认展开文字
    private static final String DEFAULT_UNFOLD_TEXT = "点击展开";
    // 默认固定行数
    private static final int DEFAULT_FOLD_LINE = 4;
    // 默认收起和展开文字颜色
    private static final int DEFAULT_TAIL_TEXT_COLOR = Color.BLUE;
    // 默认是否可以再次收起
    private static final boolean DEFAULT_CAN_FOLD_AGAIN = false;

    // 收起文字
    private String mFoldText;
    // 展开文字
    private String mUnFoldText;
    // 固定行数
    private int mFoldLine;
    // 尾部文字颜色
    private int mTailColor;
    // 是否可以再次收起
    private boolean mCanFoldAgain = false;

    // 收缩状态
    private boolean mIsFold = false;
    // 绘制，防止重复进行绘制
    private boolean mHasDrawn = false;
    // 内部绘制
    private boolean mIsInner = false;

    // 全文本
    private String mFullText;
    // 行间距倍数
    private float mLineSpacingMultiplier = 1.0f;
    // 行间距额外像素
    private float mLineSpacingExtra = 0.0f;

    // 统计使用二分法裁剪源文本的次数
    private int mCountBinary = 0;
    // 统计使用备用方法裁剪源文本的次数
    private int mCountBackUp = 0;
    // 统计onDraw调用的次数
    private int mCountOnDraw = 0;

    // 点击处理
    private ClickableSpan clickSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            mIsFold = !mIsFold;
            mHasDrawn = false;
            invalidate();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mTailColor);
        }
    };
    private String mHeaderText;
    private boolean linkHit;
    private boolean noConsumeNonUrlClicks = true;

    /**
     * 构造
     *
     * @param context 上下文
     */
    public ChatFolderTextView(Context context) {
        this(context, null);
    }

    /**
     * 构造
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public ChatFolderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造
     *
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 样式
     */
    public ChatFolderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChatFolderTextView);
        mFoldText = a.getString(R.styleable.ChatFolderTextView_foldText);
        if (null == mFoldText) {
            mFoldText = DEFAULT_FOLD_TEXT;
        }
        mUnFoldText = a.getString(R.styleable.ChatFolderTextView_unFoldText);
        if (null == mUnFoldText) {
            mUnFoldText = DEFAULT_UNFOLD_TEXT;
        }
        mFoldLine = a.getInt(R.styleable.ChatFolderTextView_foldLine, DEFAULT_FOLD_LINE);
        if (mFoldLine < 1) {
            throw new RuntimeException("foldLine must not less than 1");
        }
        mTailColor = a.getColor(R.styleable.ChatFolderTextView_tailTextColor, DEFAULT_TAIL_TEXT_COLOR);
        mCanFoldAgain = a.getBoolean(R.styleable.ChatFolderTextView_canFoldAgain, DEFAULT_CAN_FOLD_AGAIN);

        a.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(mFullText) || !mIsInner) {
            mHasDrawn = false;
            mFullText = String.valueOf(text);
        }
        super.setText(text, type);
    }

    @Override
    public void setLineSpacing(float extra, float multiplier) {
        mLineSpacingExtra = extra;
        mLineSpacingMultiplier = multiplier;
        super.setLineSpacing(extra, multiplier);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 必须解释下：由于为了得到实际一行的宽度（makeTextLayout()中需要使用），必须要先把源文本设置上，然后再裁剪至指定行数；
        // 这就导致了该TextView会先布局一次高度很高（源文本行数高度）的布局，裁剪后再次布局成指定行数高度，因而下方View会抖动；
        // 这里的处理是，super.onMeasure()已经计算出了源文本的实际宽高了，取出指定行数的文本再次测量一下其高度，
        // 然后把这个高度设置成最终的高度就行了！
        if (!mIsFold) {
            Layout layout = getLayout();
            int line = getFoldLine();
            if (line < layout.getLineCount()) {
                int index = layout.getLineEnd(line - 1);
                if (index > 0) {
                    // 得到一个字符串，该字符串恰好占据mFoldLine行数的高度
                    String strWhichHasExactlyFoldLine = getText().subSequence(0, index).toString();
                    LogUtil.d(TAG, "strWhichHasExactlyFoldLine-->" + strWhichHasExactlyFoldLine);
                    layout = makeTextLayout(strWhichHasExactlyFoldLine);
                    // 把这个高度设置成最终的高度，这样下方View就不会抖动了
                    setMeasuredDimension(getMeasuredWidth(), layout.getHeight() + getPaddingTop() + getPaddingBottom());
                }
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.d(TAG, "onDraw() " + mCountOnDraw++ + ", getMeasuredHeight() " + getMeasuredHeight());
        if (!mHasDrawn) {
            resetText();
        }
        super.onDraw(canvas);
        mHasDrawn = true;
        mIsInner = false;
    }

    /**
     * 获取折叠文字
     *
     * @return 折叠文字
     */
    public String getFoldText() {
        return mFoldText;
    }

    /**
     * 设置折叠文字
     *
     * @param foldText 折叠文字
     */
    public void setFoldText(String foldText) {
        mFoldText = foldText;
        invalidate();
    }

    /**
     * 获取展开文字
     *
     * @return 展开文字
     */
    public String getUnFoldText() {
        return mUnFoldText;
    }

    /**
     * 设置展开文字
     *
     * @param unFoldText 展开文字
     */
    public void setUnFoldText(String unFoldText) {
        mUnFoldText = unFoldText;
        invalidate();
    }

    /**
     * 获取折叠行数
     *
     * @return 折叠行数
     */
    public int getFoldLine() {
        return mFoldLine;
    }

    /**
     * 设置折叠行数
     *
     * @param foldLine 折叠行数
     */
    public void setFoldLine(int foldLine) {
        mFoldLine = foldLine;
        invalidate();
    }

    /**
     * 获取尾部文字颜色
     *
     * @return 尾部文字颜色
     */
    public int getTailColor() {
        return mTailColor;
    }

    /**
     * 设置尾部文字颜色
     *
     * @param tailColor 尾部文字颜色
     */
    public void setTailColor(int tailColor) {
        mTailColor = tailColor;
        invalidate();
    }

    /**
     * 获取是否可以再次折叠
     *
     * @return 是否可以再次折叠
     */
    public boolean isCanFoldAgain() {
        return mCanFoldAgain;
    }

    /**
     * 获取全文本
     *
     * @return 全文本
     */
    public String getFullText() {
        return mFullText;
    }

    public void setHeaderText(String headerText) {
        mHeaderText = headerText;
    }

    /**
     * 设置是否可以再次折叠
     *
     * @param canFoldAgain 是否可以再次折叠
     */
    public void setCanFoldAgain(boolean canFoldAgain) {
        mCanFoldAgain = canFoldAgain;
        invalidate();
    }

    public void setFold(boolean fold) {
        mIsFold = fold;
    }


    /**
     * 获取TextView的Layout，注意这里使用getWidth()得到宽度
     *
     * @param text 源文本
     * @return Layout
     */
    private Layout makeTextLayout(String text) {
        return new StaticLayout(text, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(), Layout.Alignment
                .ALIGN_NORMAL, mLineSpacingMultiplier, mLineSpacingExtra, true);
    }

    /**
     * 重置文字
     */
    private void resetText() {
        try {
            // 文字本身就小于固定行数的话，不添加尾部的收起/展开文字
            Layout layout = makeTextLayout(mFullText);
            if (layout.getLineCount() <= getFoldLine()) {
                if (!TextUtils.isEmpty(mHeaderText)) {
                    setText(getTextStr(mFullText, mHeaderText.length()));
                }else {
                    setText(mFullText);
                }
                return;
            }

            SpannableStringBuilder spanStr = null;
            if (mIsFold) {
                // 收缩状态
                if (mCanFoldAgain) {
                    spanStr = createUnFoldSpan(mFullText);
                } else {
                    spanStr = createNotFoldAgainFoldSpan(mFullText);
                }
            } else {
                // 展开状态
                spanStr = createFoldSpan(mFullText);
            }

            updateText(spanStr);
            setMovementMethod(LocalLinkMovementMethod.getInstance());
        } catch (Throwable e) {
            e.printStackTrace();
            setText(mFullText);
        }

    }

    /**
     * 不更新全文本下，进行展开和收缩操作
     *
     * @param text 源文本
     */
    private void updateText(CharSequence text) {
        mIsInner = true;
        setText(text);
    }

    private SpannableStringBuilder getTextStr(String content, int end) {
        SpannableStringBuilder builder = null;
        try {
            builder = new SpannableStringBuilder(content);
            builder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.android_statusBar_color)), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return builder;
    }

    /**
     * 创建展开状态下的Span
     *
     * @param text 源文本
     * @return 展开状态下的Span
     */
    private SpannableStringBuilder createUnFoldSpan(String text) {
        String destStr = text + mFoldText;
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(getHeaderSpannableString(mHeaderText));
        sb.append(getContentSpannableString(destStr, mFoldText.length()));
        return sb;
    }

    /**
     * 创建展开状态下的Span
     *
     * @param text 源文本
     * @return 展开状态下的Span
     */
    private SpannableStringBuilder createNotFoldAgainFoldSpan(String text) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(getHeaderSpannableString(text));
        return sb;
    }

    private SpannableString getHeaderSpannableString(String text) {
        SpannableString spanStr = new SpannableString(text);
        if (!TextUtils.isEmpty(mHeaderText)) {
            spanStr.setSpan(new ForegroundColorSpan(mTailColor), 0, mHeaderText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanStr;
    }

    private SpannableString getContentSpannableString(String destStr, int extraNumber) {
        if (!TextUtils.isEmpty(mHeaderText)) {
            destStr = destStr.substring(mHeaderText.length());
        }
        int start = destStr.length() - extraNumber;
        int end = destStr.length();
        SpannableString spanStr = new SpannableString(destStr);
        spanStr.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /**
     * 创建收缩状态下的Span
     *
     * @param text
     * @return 收缩状态下的Span
     */
    private SpannableStringBuilder createFoldSpan(String text) {
        long startTime = System.currentTimeMillis();
        String destStr = tailorText(text);
        LogUtil.d(TAG, (System.currentTimeMillis() - startTime) + "ms");
        SpannableStringBuilder sb = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(mHeaderText)) {
            sb.append(getHeaderSpannableString(mHeaderText));
        }
        sb.append(getContentSpannableString(destStr, mUnFoldText.length()));
        return sb;
    }

    /**
     * 裁剪文本至固定行数（备用方法）
     *
     * @param text 源文本
     * @return 裁剪后的文本
     */
    private String tailorTextBackUp(String text) {
        LogUtil.d(TAG, "使用备用方法: tailorTextBackUp() " + mCountBackUp++);

        String destStr = text + DEFAULT_ELLIPSIZE + mUnFoldText;
        Layout layout = makeTextLayout(destStr);

        // 如果行数大于固定行数
        if (layout.getLineCount() > getFoldLine()) {
            int index = layout.getLineEnd(getFoldLine() - 1);
            if (text.length() < index) {
                index = text.length();
            }
            // 从最后一位逐渐试错至固定行数（可以考虑用二分法改进）
            if (index <= 1) {
                return DEFAULT_ELLIPSIZE + mUnFoldText;
            }
            String subText = text.substring(0, index - 1);
            return tailorText(subText);
        } else {
            return destStr;
        }
    }

    /**
     * 裁剪文本至固定行数（二分法）。经试验，在文字长度不是很长时，效率比备用方法高不少；当文字长度过长时，备用方法则优势明显。
     *
     * @param text 源文本
     * @return 裁剪后的文本
     */
    private String tailorText(String text) {
        // return tailorTextBackUp(text);

        int start = 0;
        int end = text.length() - 1;
        int mid = (start + end) / 2;
        int find = finPos(text, mid);
        while (find != 0 && end > start) {
            LogUtil.d(TAG, "使用二分法: tailorText() " + mCountBinary++);
            if (find > 0) {
                end = mid - 1;
            } else if (find < 0) {
                start = mid + 1;
            }
            mid = (start + end) / 2;
            find = finPos(text, mid);
        }
        LogUtil.d(TAG, "mid is: " + mid);

        String ret;
        if (find == 0) {
            ret = text.substring(0, mid) + DEFAULT_ELLIPSIZE + mUnFoldText;
        } else {
            ret = tailorTextBackUp(text);
        }
        return ret;
    }

    /**
     * 查找一个位置P，到P时为mFoldLine这么多行，加上一个字符‘A’后则刚好为mFoldLine+1这么多行
     *
     * @param text 源文本
     * @param pos  位置
     * @return 查找结果
     */
    private int finPos(String text, int pos) {
        String destStr = text.substring(0, pos) + DEFAULT_ELLIPSIZE + mUnFoldText;
        Layout layout = makeTextLayout(destStr);
        Layout layoutMore = makeTextLayout(destStr + "A");

        int lineCount = layout.getLineCount();
        int lineCountMore = layoutMore.getLineCount();

        if (lineCount == getFoldLine() && (lineCountMore == getFoldLine() + 1)) {
            // 行数刚好到折叠行数
            return 0;
        } else if (lineCount > getFoldLine()) {
            // 行数比折叠行数多
            return 1;
        } else {
            // 行数比折叠行数少
            return -1;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        boolean res = super.onTouchEvent(event);

        if (noConsumeNonUrlClicks)
            return linkHit;
        return res;

    }

    @Override
    public boolean hasFocusable() {
        return false;
    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod{
        static LocalLinkMovementMethod sInstance;


        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LocalLinkMovementMethod();

            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget,
                                    Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(
                        off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }

                    if (widget instanceof ChatFolderTextView){
                        ((ChatFolderTextView) widget).linkHit = true;
                    }
                    return true;
                } else {
                    Selection.removeSelection(buffer);
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }
            return Touch.onTouchEvent(widget, buffer, event);
        }
    }
}


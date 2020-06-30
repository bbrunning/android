package com.zwt.superandroid.util.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import com.zwt.superandroid.R;
import com.zwt.superandroid.util.LogUtil;
import com.zwt.superandroid.util.view.listener.SimpleAnimatorListener;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2019/10/17
 * Time: 17:21
 */
public class LoadPathTextView extends View {
    private static final String TAG = LoadPathTextView.class.getSimpleName();

    public static final int Default = 11;//默认
    public static final int Bounce = 12;//反弹
    public static final int Oblique = 13;//倾斜飞出

    private int Mode = Oblique;
    private static String TEST = "正在努力加载哦!";
    private float mDensity = 0;
    private Context mContext;
    private Bitmap mCurrentBitmap;
    private float mRadioCenterY;
    private float mRadioCenterX;
    private Path mPath;
    private Paint mTextPaint;
    private float mTextWidth;
    private Paint mBitmapPaint;
    private ObjectAnimator mOffsetAnimator;//偏移动画
    private ObjectAnimator mDistanceDownAnimator;//图片下降的动画
    private ObjectAnimator mDistanceUpAnimator;//图片上升的动画
    private float mCurrentOffset = -1;//文字偏移量
    private float mFraction;
    private boolean isUp;//是否为向上
    private float dXXX;//x方向的偏移量
    private int mCurrentIndex;
    private float mDefaultY = 0;//文字的默认y
    private float mAmplitude = 100.0f;//振幅
    private float mTextHeight;
    private boolean isLeft;
    private int mCurrentHeight;


    public LoadPathTextView(Context context) {
        this(context, null);
    }

    public LoadPathTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadPathTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(getResources().getColor(R.color.transparent));
        mContext = getContext();
        mDensity = mContext.getResources().getDisplayMetrics().density;
        final ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit1));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit2));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit3));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit4));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit5));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit6));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit7));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.fruit8));
        mCurrentBitmap = bitmaps.get(0);
        mRadioCenterY = mCurrentBitmap.getHeight() / 2.0f;
        mPath = new Path();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//创建画笔并消除锯齿
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(sp2px(mContext, 10));
        mTextPaint.setColor(Color.WHITE);
        mTextWidth = mTextPaint.measureText(TEST);

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setStrokeWidth(5);
        mBitmapPaint.setColor(Color.GREEN);

        mOffsetAnimator = ObjectAnimator.ofFloat(this, mOffsetProperty, 0);
        mOffsetAnimator.setDuration(200);
        mOffsetAnimator.setInterpolator(new BounceInterpolator());

        mDistanceDownAnimator = ObjectAnimator.ofFloat(this, mDistanceProperty, 0);
        mDistanceDownAnimator.setInterpolator(new LinearInterpolator());
        mDistanceDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFraction = animation.getAnimatedFraction();
            }
        });
        mDistanceUpAnimator = ObjectAnimator.ofFloat(this, mDistanceProperty, 0);
        mDistanceUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFraction = animation.getAnimatedFraction();
                float f = (Float) animation.getAnimatedValue();
                if (Mode == Bounce && (int) (mDefaultY - mTextHeight + mDensity) == (int) f && !mOffsetAnimator.isRunning()) {
                    dXXX = (isLeft ? mRadioCenterX * mFraction : mRadioCenterX * mFraction * -1.0f);
                    mOffsetAnimator.cancel();
                    mOffsetAnimator.setDuration(100);
                    mOffsetAnimator.setFloatValues(mDefaultY, mDefaultY + 50, mDefaultY);
                    mOffsetAnimator.start();
                }
            }
        });
        mDistanceUpAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isUp = true;
                isLeft = !isLeft;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDistanceDownAnimator.start();
            }
        });
        mDistanceDownAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isUp = false;
                dXXX = 0;
                ++mCurrentIndex;
                if (mCurrentIndex >= bitmaps.size()) {
                    mCurrentIndex = 0;
                }
                mCurrentBitmap = bitmaps.get(mCurrentIndex);
                mRadioCenterY = mCurrentBitmap.getHeight() / 2.0f;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mOffsetAnimator.cancel();
                mOffsetAnimator.setDuration(200);
                mOffsetAnimator.setFloatValues(mDefaultY, mDefaultY + mAmplitude, mDefaultY);
                mOffsetAnimator.start();
                mDistanceUpAnimator.start();
            }
        });
        mDistanceDownAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtil.i(TAG, "onSizeChanged: size改变了！！！！");
        mCurrentHeight = h;
        initAnim();
    }

    private void initAnim() {
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top;
        }
        mDefaultY = mCurrentHeight - mTextHeight;

        mOffsetAnimator.setFloatValues(mDefaultY, mDefaultY + mAmplitude, mDefaultY);
        mRadioCenterY = mCurrentBitmap.getHeight() / 2.0f;//初始化默认高度
        mDistanceDownAnimator.setFloatValues(mRadioCenterY, mDefaultY - mTextHeight);//到文字的顶部就好
        Log.i(TAG, "initAnim: radioCenterY:" + mRadioCenterY + ";;TO:" + (mDefaultY));

        switch (Mode) {
            case Default:
                mDistanceDownAnimator.setDuration(600);
                mDistanceUpAnimator.setDuration(600);
                mDistanceUpAnimator.setInterpolator(new DecelerateInterpolator());
                mDistanceUpAnimator.setFloatValues(mDefaultY - mTextHeight, mRadioCenterY);
                break;
            case Oblique:
                mDistanceDownAnimator.setDuration(300);
                mDistanceUpAnimator.setDuration(600);
                mDistanceUpAnimator.setInterpolator(new DecelerateInterpolator());
                mDistanceUpAnimator.setFloatValues(mDefaultY - mTextHeight, mRadioCenterY + mCurrentBitmap.getHeight());//到达不了最高处
                break;
            case Bounce:
                mDistanceDownAnimator.setDuration(600);
                mDistanceUpAnimator.setDuration(1000);
                mDistanceUpAnimator.setInterpolator(new DecelerateInterpolator());
                mDistanceUpAnimator.setFloatValues(mDefaultY - mTextHeight, mDefaultY - 4 * mTextHeight, (int) (mDefaultY - mTextHeight + mDensity * 2f), mDefaultY - 2 * mTextHeight);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTextHeight = mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) mTextPaint.measureText(TEST), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float dX = (isLeft ? mRadioCenterX * mFraction : mRadioCenterX * mFraction * -1.0f);
        mPath.reset();
        float defaultX = 0;
        mPath.moveTo(defaultX, mDefaultY);
        mRadioCenterX = (defaultX + mTextWidth) / 2.0f;
        if (mCurrentOffset != -1) {
            mPath.quadTo(dXXX == 0 ? mRadioCenterX : mRadioCenterX + dXXX, mCurrentOffset, mTextWidth, mDefaultY);
        } else {
            mPath.lineTo(mTextWidth, mDefaultY);
        }
        canvas.drawTextOnPath(TEST, mPath, 0, 0, mTextPaint);

        if (mCurrentBitmap != null) {
            if (!isUp) {
                canvas.rotate(360 * mFraction, mRadioCenterX, mRadioCenterY);
                canvas.drawBitmap(mCurrentBitmap, mRadioCenterX - mCurrentBitmap.getWidth() / 2.0f, mRadioCenterY - mCurrentBitmap.getHeight() / 2.0f, null);
                return;
            }
            switch (Mode) {
                case Default:
                    canvas.rotate(360 * mFraction, mRadioCenterX, mRadioCenterY);
                    canvas.drawBitmap(mCurrentBitmap, mRadioCenterX - mCurrentBitmap.getWidth() / 2.0f, mRadioCenterY - mCurrentBitmap.getHeight() / 2.0f, null);
                    break;
                case Bounce:
                case Oblique:
                    canvas.rotate(360 * mFraction, mRadioCenterX + dX, mRadioCenterY);
                    canvas.translate(dX, 0);
                    int i1 = blendColors(Color.WHITE, Color.TRANSPARENT, mFraction);
                    mBitmapPaint.setColor(i1);
                    canvas.drawBitmap(mCurrentBitmap, mRadioCenterX - mCurrentBitmap.getWidth() / 2.0f, mRadioCenterY - mCurrentBitmap.getHeight() / 2.0f, mBitmapPaint);
                    break;

            }

        }
    }

    public Float getCurrentOffset() {
        return mCurrentOffset;
    }

    public static
    @ColorInt
    int blendColors(@ColorInt int color1,
                    @ColorInt int color2,
                    @FloatRange(from = 0f, to = 1f) float ratio) {
        final float inverseRatio = 1f - ratio;
        float a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio);
        float r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio);
        float g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio);
        float b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }


    public void setCurrentOffset(Float currentOffset) {
        this.mCurrentOffset = currentOffset;
        invalidate();
    }

    public void setCurrentDistance(Float currentDistance) {
        this.mRadioCenterY = currentDistance;
        invalidate();
    }

    public Float getCurrentDistance() {
        return mRadioCenterY;
    }


    private Property<LoadPathTextView, Float> mOffsetProperty = new Property<LoadPathTextView, Float>(Float.class, "offset") {
        @Override
        public Float get(LoadPathTextView object) {
            return object.getCurrentOffset();
        }

        @Override
        public void set(LoadPathTextView object, Float value) {

            object.setCurrentOffset(value);
        }
    };

    private Property<LoadPathTextView, Float> mDistanceProperty = new Property<LoadPathTextView, Float>(Float.class, "distance") {
        @Override
        public Float get(LoadPathTextView object) {
            return object.getCurrentDistance();
        }

        @Override
        public void set(LoadPathTextView object, Float value) {
            object.setCurrentDistance(value);

        }
    };

    public static float sp2px(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

}

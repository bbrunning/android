package com.zwt.superandroid.util.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.zwt.superandroid.R;
import com.zwt.superandroid.databinding.FlipAnimatorViewBinding;

/**
 * Created by Android Studio.
 * User: ZWT
 * Date: 2020/7/24
 * Time: 15:47
 */
class FlipAnimatorView extends BaseLazyLinearLayout<FlipAnimatorViewBinding> {
    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;
    private static final int DELAY_TIME = 1800;
    boolean mIsShowBack;
    final Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mRightOutSet == null || mLeftInSet == null) {
                    return;
                }

                if (!mIsShowBack) {
                    mRightOutSet.setTarget(dataBinding.signView);
                    mLeftInSet.setTarget(dataBinding.signQianView);
                } else {
                    mRightOutSet.setTarget(dataBinding.signQianView);
                    mLeftInSet.setTarget(dataBinding.signView);
                }
                mRightOutSet.start();
                mLeftInSet.start();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    public FlipAnimatorView(Context context) {
        super(context);
    }

    public FlipAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isUseDataBinding() {
        return true;
    }

    @Override
    public int getRootViewIds() {
        return R.layout.flip_animator_view;
    }

    @Override
    public void initChildView(View view) {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.rotate_out_anim);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.rotate_in_anim);
        mLeftInSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null && mRunnable != null) {
                    mIsShowBack = !mIsShowBack;
                    mHandler.postDelayed(mRunnable, DELAY_TIME);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        dataBinding.signQianView.setVisibility(View.VISIBLE);
        dataBinding.signView.setBackgroundResource(R.drawable.home_sign_sun);
        dataBinding.signQianView.setBackgroundResource(R.drawable.home_title_qian_icon);
    }

    private void setCameraDistance(View v1, View v2) {
        mIsShowBack = false;
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        v1.setCameraDistance(scale);
        v2.setCameraDistance(scale);
    }

    private void flip(View v1, final View v2) {
        setCameraDistance(v1, v2);
        if (mHandler != null) {
            mHandler.postDelayed(mRunnable, DELAY_TIME);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        flip(dataBinding.signView, dataBinding.signQianView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (mLeftInSet != null) {
            mLeftInSet.removeAllListeners();
            mLeftInSet.end();
        }
        if (mRightOutSet != null) {
            mRightOutSet.end();
        }
    }
}

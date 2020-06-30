package com.zwt.superandroid.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zwt.superandroid.R;




public abstract class BottomSheetDialogBase extends BaseLazyLinearLayout {

    public Context mContext;
    private View mRootView;
    private BottomSheetDialog mBottomSheetDialog;

    public BottomSheetDialogBase(Context context) {
        super(context);
        initView();
    }

    public BottomSheetDialogBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BottomSheetDialogBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        mContext = getContext();
        mRootView = LayoutInflater.from(getContext()).inflate(getRootViewIds(), this, true);
        if (mRootView != null) {
            initChildView(mRootView);
        }
        try {
            mBottomSheetDialog = new BottomSheetDialog(mContext, R.style.bottom_sheet_dialog);
            mBottomSheetDialog.setContentView(this);
            ViewGroup parent = (ViewGroup) this.getParent();
            parent.setBackgroundColor(getResources().getColor(R.color.transparent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.show();
        }
    }

    public void dismiss() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
    }

    public BottomSheetDialog getBottomDialog() {
        return mBottomSheetDialog;
    }

    public boolean isShowing() {
        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public abstract int getRootViewIds();

    public abstract void initChildView(View view);
}

package com.zwt.superandroid.home;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zwt.superandroid.R;
import com.zwt.superandroid.base.BaseActivity;
import com.zwt.superandroid.home.fragment.FindFragment;
import com.zwt.superandroid.home.fragment.HomeFragment;
import com.zwt.superandroid.home.fragment.MineFragment;
import com.zwt.superandroid.home.fragment.MsgFragment;

import java.util.List;

public class MainActivity extends BaseActivity {

    private String[] mFragmentNames = {
            HomeFragment.class.getSimpleName(),
            FindFragment.class.getSimpleName(),
            MsgFragment.class.getSimpleName(),
            MineFragment.class.getSimpleName(),
    };
    private HomeFragment mHomeFragment;
    private MineFragment mMineFragment;
    private LinearLayout mBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentMap();
        if (savedInstanceState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null) {
                        addFragmentMapElement(fragment.getClass().getSimpleName(), fragment);
                    }
                }
            }
        }
        mBottom = (LinearLayout) findViewById(R.id.bottom_layout);

    }

    public void onTabClick(View view){
        int index = Integer.parseInt((String) view.getTag());
        updateState(mBottom, index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateState(mBottom, 0);
    }

    private void updateState(LinearLayout bottom, int index) {
        if (bottom != null && bottom.getChildCount() > 0) {
            for (int i = 0; i < bottom.getChildCount(); ++i) {
                View child = bottom.getChildAt(i);
                if (child instanceof RelativeLayout) {
                    if (child.getTag() == null) {
                        continue;
                    }
                    View subChild = ((RelativeLayout) child).getChildAt(0);
                    TextView text = (TextView) ((RelativeLayout) child).getChildAt(1);
                    if (subChild instanceof RadioButton) {
                        ((RadioButton) subChild).setChecked(index == i || index == Integer.parseInt((String) child.getTag()));
                        text.setSelected(((RadioButton) subChild).isChecked());
                    }
                }
            }
        }
        boolean isNeedAddFragment = isNeedAddFragment(mFragmentNames[index]);
        if (isNeedAddFragment) {
            switch (index) {
                case 0:
                    mHomeFragment = new HomeFragment();
                    add(R.id.main_body, mHomeFragment);
                    break;
                case 1:
                    add(R.id.main_body,new FindFragment());
                    break;
                case 2:
                    add(R.id.main_body, new MsgFragment());
                    break;
                case 3:
                    add(R.id.main_body, mMineFragment = new MineFragment());
                    break;

                default:
                    break;
            }
        }
        for (int i = 0; i < mFragmentNames.length; ++i) {
            if (i != index) {
                hide((Fragment) getFragmentMap().get(mFragmentNames[i]));
            }
        }
        show((Fragment) getFragmentMap().get(mFragmentNames[index]));
    }

    @Override
    public Object getTitleString() {
        return 0;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }
}

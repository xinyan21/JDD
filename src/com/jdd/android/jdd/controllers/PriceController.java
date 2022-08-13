package com.jdd.android.jdd.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.PriceActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-24
 * @since 1.0
 */
@Deprecated
public class PriceController extends ListenerControllerAdapter implements RadioGroup.OnCheckedChangeListener {

    private PriceActivity mActivity;

    public PriceController(PriceActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CHECK:
                if (view instanceof RadioGroup) {
                    ((RadioGroup) view).setOnCheckedChangeListener(this);
                }

                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = mActivity.mFragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (i) {
            case R.id.rb_drop_list:
                transaction.replace(R.id.ll_container, mActivity.mDropListFragment);

                break;

            case R.id.rb_index:
                transaction.replace(R.id.ll_container, mActivity.mIndexFragment);

                break;
            case R.id.rb_rise_list:
                transaction.replace(R.id.ll_container, mActivity.mRiseListFragment);

                break;
            case R.id.rb_user_list:
                transaction.replace(R.id.ll_container, mActivity.mSelfSelectionStocksFragment);


                break;
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}

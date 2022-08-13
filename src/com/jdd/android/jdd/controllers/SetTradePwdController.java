package com.jdd.android.jdd.controllers;

import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.SendCoinToFriendActivity;
import com.jdd.android.jdd.ui.SetTradePwdActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：设置交易密码控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class SetTradePwdController extends ListenerControllerAdapter implements View.OnClickListener {
    private SetTradePwdActivity mActivity;

    public SetTradePwdController(SetTradePwdActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                mActivity.finish();

                break;
        }
    }
}

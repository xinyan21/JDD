package com.jdd.android.jdd.controllers;

import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.ResetLoginPwdActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：重置登录密码控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class ResetLoginPwdController extends ListenerControllerAdapter implements View.OnClickListener {
    private ResetLoginPwdActivity mActivity;

    public ResetLoginPwdController(ResetLoginPwdActivity activity) {
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
            case R.id.btn_submit:


                break;
        }
    }
}

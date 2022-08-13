package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.ResetLoginPwdActivity;
import com.jdd.android.jdd.ui.ResetTradePwdActivity;
import com.jdd.android.jdd.utils.Utility;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：重置交易密码控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class ResetTradePwdController extends ListenerControllerAdapter implements View.OnClickListener {
    private ResetTradePwdActivity mActivity;

    public ResetTradePwdController(ResetTradePwdActivity activity) {
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
                mActivity.submit();

                break;
            case R.id.btn_get_verification_code:
                mActivity.sendVerifyCode();

                break;
            case R.id.btn_login_immediately:
                mActivity.dialogResetPwdSuccess.dismiss();

                break;
        }
    }
}

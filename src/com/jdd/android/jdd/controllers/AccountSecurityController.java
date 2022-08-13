package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.*;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：账户安全控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-03
 * @last_edit 2016-01-03
 * @since 1.0
 */
public class AccountSecurityController extends ListenerControllerAdapter implements View.OnClickListener {
    private AccountSecurityActivity mActivity;

    public AccountSecurityController(AccountSecurityActivity activity) {
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
            case R.id.rl_edit_login_pwd:
                startActivity(FindPwdBackActivity.class);

                break;
            case R.id.rl_set_security_email:
                startActivity(SetEmailActivity.class);

                break;
            case R.id.rl_set_trade_pwd:
                startActivity(SetTradePwdActivity.class);

                break;
        }
    }

    private void startActivity(Class cls) {
        mActivity.startActivity(new Intent(mActivity, cls));
    }
}

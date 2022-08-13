package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.RegisterActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：注册第一步控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class SignInStep1Controller extends ListenerControllerAdapter implements View.OnClickListener {
    private RegisterActivity mActivity;

    public SignInStep1Controller(RegisterActivity activity) {
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
            case R.id.btn_send_verification_code:
                if (!mActivity.checkPhoneNo(mActivity.etPhoneNum.getText().toString())) {
                    Toast.makeText(mActivity, "您输入的手机号格式有误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                mActivity.sendVerifyCode();

                break;
            case R.id.btn_next_step:
                mActivity.register();

                break;
            case R.id.btn_login_immediately:
                mActivity.registerSuccess();

                break;
        }
    }
}

package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.LoginFunction;
import com.jdd.android.jdd.requests.LoginRequest;
import com.jdd.android.jdd.ui.FindPwdBackActivity;
import com.jdd.android.jdd.ui.LoginActivity;
import com.jdd.android.jdd.ui.RegisterActivity;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.utils.StringUtils;

/**
 * 描述：登录控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class LoginController extends ListenerControllerAdapter
        implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private LoginActivity mActivity;

    public LoginController(LoginActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_CHECK:
                ((CheckBox)view).setOnCheckedChangeListener(this);
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
            case R.id.btn_login:
                if (!verifyPhoneNo(mActivity.etPhoneNum.getText().toString())) {
                    Toast.makeText(mActivity,"手机号格式错误，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!verifyPWD(mActivity.etPassword.getText().toString())) {
                    Toast.makeText(mActivity,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Parameter param = new Parameter();
                param.addParameter(TaskId.TASK_ID, String.valueOf(TaskId.TASK_ID_FIRST));
                param.addParameter(
                        LoginFunction.PHONE_NO, mActivity.etPhoneNum.getText().toString());
                param.addParameter(
                        LoginFunction.PASSWORD, mActivity.etPassword.getText().toString());
                mActivity.startTask(new LoginRequest(param, new QueryAction(mActivity)));
                mActivity.btnLogin.setText("登录中...");
                mActivity.btnLogin.setEnabled(false);

                break;
            case R.id.btn_sign_in:
                mActivity.startActivity(new Intent(mActivity, RegisterActivity.class));

                break;
            case R.id.btn_forget_password:
                mActivity.startActivity(new Intent(mActivity, FindPwdBackActivity.class));

                break;
        }
    }

    public static boolean verifyPhoneNo(String data) {
        if (StringUtils.isEmpty(data) || data.length() < 11) {
            return false;
        }

        return true;
    }

    public static boolean verifyPWD(String data) {
        if (StringUtils.isEmpty(data)) {
            return false;
        }

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}

package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.RegisterFunction;
import com.jdd.android.jdd.controllers.FindPwdBackController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.SendVerifyCodeRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.jdd.android.jdd.utils.Utility;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：找回密码
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class FindPwdBackActivity extends TKActivity implements IQueryServer {
    public EditText etVerifyCode, etNewPwd, etConfirmPwd, etPhoneNo;
    private Button mBtnSubmit;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnLoginImmediately;
    private Dialog mDialogLoading, mDialogResetPwdSuccess;
    private Button mBtnSendVerifyCode;

    private FindPwdBackController mController;
    private Timer mTimer = new Timer();
    private int mTimeRemains = RegisterActivity.DEFAULT_TIME_WAITING;
    private UserEntity mUserEntity;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd_back);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        etConfirmPwd = (EditText) findViewById(R.id.et_confirmed_pwd);
        etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
        etVerifyCode = (EditText) findViewById(R.id.et_verification_code);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mBtnSendVerifyCode = (Button) findViewById(R.id.btn_get_verification_code);
        etPhoneNo = (EditText) findViewById(R.id.et_phone_num);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(this, "处理中，请稍候...", true, true);
        mDialogResetPwdSuccess = new Dialog(this);
        mDialogResetPwdSuccess.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mDialogResetPwdSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogResetPwdSuccess.setContentView(R.layout.dialog_register_success);
        mDialogResetPwdSuccess.setCanceledOnTouchOutside(false);
        mBtnLoginImmediately = (Button)
                mDialogResetPwdSuccess.findViewById(R.id.btn_login_immediately);
        TextView text = (TextView) mDialogResetPwdSuccess.findViewById(R.id.tv_dialog_text);
        text.setText("您的密码已经修改成功，立刻登录吧！");
    }

    @Override
    protected void setListeners() {
        mController = new FindPwdBackController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmit, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSendVerifyCode, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnLoginImmediately, mController);
    }

    @Override
    protected void initData() {
        mUserEntity = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("找回密码");
//        if (null != userEntity) {
//            etPhoneNo.setEnabled(false);
//            etPhoneNo.setText(String.valueOf(userEntity.getPhoneNo()));
//        }
    }

    public void sendVerifyCode() {
        String phoneNo = etPhoneNo.getText().toString();
        Parameter param = new Parameter();
        param.addParameter(RegisterFunction.IN_PHONE_NO, phoneNo);
        startTask(new SendVerifyCodeRequest(
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        ));
    }

    public void resetPwd() {
        String phoneNo = etPhoneNo.getText().toString();
        String pwd = etNewPwd.getText().toString();
        String confirmedPwd = etConfirmPwd.getText().toString();
        String verificationCode = etVerifyCode.getText().toString();
        if (!Utility.checkPhoneNo(phoneNo)) {
            toast("您输入的手机号格式有误，请重新输入");
            return;
        }
        if (!Utility.checkPwd(confirmedPwd)) {
            toast("您输入的密码格式有误，请重新输入");
            return;
        }
        if (!pwd.equals(confirmedPwd)) {
            toast("两次密码输入不一致，请重新输入");
            return;
        }

        Parameter param = new Parameter();
        param.addParameter(RegisterFunction.IN_PHONE_NO, phoneNo);
        param.addParameter(RegisterFunction.IN_PWD, pwd);
        param.addParameter(RegisterFunction.IN_VERIFY_CODE, verificationCode);
        HashMap<String, String> returnKeys = new HashMap<>();
        returnKeys.put(RegisterFunction.ERR_CONTENT, null);
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_RESET_PWD"),
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        );
        request.setReturnKeys(returnKeys);
        startTask(request);
        mDialogLoading.show();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void dismissDialog() {
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
    }

    public void forwardToLogin() {
        if (mDialogResetPwdSuccess.isShowing()) {
            mDialogResetPwdSuccess.dismiss();
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                toast("语音验证码已经发送，请注意接听您的电话");
                mBtnSendVerifyCode.setEnabled(false);
                mBtnSendVerifyCode.setTextColor(getResources().getColor(R.color.text_gray));
                mBtnSendVerifyCode.setText("60秒后重新获取");
                mTimeRemains = RegisterActivity.DEFAULT_TIME_WAITING;
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTimeRemains -= 1000;
                                if (mTimeRemains <= 0) {
                                    cancel();
                                    mBtnSendVerifyCode.setEnabled(true);
                                    mBtnSendVerifyCode.setText("获取验证码");
                                    mBtnSendVerifyCode.setTextColor(
                                            getResources().getColor(R.color.menu_text_orange)
                                    );
                                    return;
                                }
                                mBtnSendVerifyCode.setText(
                                        String.valueOf(mTimeRemains / 1000) + "秒后重新发送"
                                );
                            }
                        });
                    }
                }, 0, 1000);

                break;
            case TaskId.TASK_ID_SECOND:
                if (!mDialogResetPwdSuccess.isShowing()) {
                    mDialogResetPwdSuccess.show();
                }

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
            case TaskId.TASK_ID_SECOND:
                HashMap returnData = (HashMap) bundle.getSerializable(String.valueOf(taskId));
                if (returnData.containsKey(RegisterFunction.ERR_CONTENT)) {
                    toast(returnData.get(RegisterFunction.ERR_CONTENT).toString());
                }

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
    }
}

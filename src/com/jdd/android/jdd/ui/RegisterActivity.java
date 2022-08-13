package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.LoginFunction;
import com.jdd.android.jdd.constants.function.RegisterFunction;
import com.jdd.android.jdd.controllers.SignInStep1Controller;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.LoginRequest;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.SendVerifyCodeRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：注册
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-05
 * @last_edit 2016-01-05
 * @since 1.0
 */
public class RegisterActivity extends TKActivity implements IQueryServer {
    public static final int DEFAULT_TIME_WAITING = 60000;
    public EditText etPhoneNum;
    private EditText mETLoginPwd, mETConfirmedPwd, mETVerificationCode, mETRefereeId;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnNextStep, mBtnSendVerifyCode;
    private Dialog mDialog, mDialogRegisterSuccess;
    private Button mBtnLoginImmediately;

    private SignInStep1Controller mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Timer mTimer = new Timer();
    private int mTimeRemains = DEFAULT_TIME_WAITING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mETConfirmedPwd = (EditText) findViewById(R.id.et_confirmed_pwd);
        mETLoginPwd = (EditText) findViewById(R.id.et_login_pwd);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        mETRefereeId = (EditText) findViewById(R.id.et_referee_id);
        mETVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        mBtnNextStep = (Button) findViewById(R.id.btn_next_step);
        mBtnSendVerifyCode = (Button) findViewById(R.id.btn_send_verification_code);
        mDialog = PopUpComponentUtil.createLoadingProgressDialog(this, "注册中，请稍候...", true, true);
        mDialogRegisterSuccess = new Dialog(this);
        mDialogRegisterSuccess.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mDialogRegisterSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogRegisterSuccess.setContentView(R.layout.dialog_register_success);
        mDialogRegisterSuccess.setCanceledOnTouchOutside(false);
        mBtnLoginImmediately = (Button)
                mDialogRegisterSuccess.findViewById(R.id.btn_login_immediately);
    }

    @Override
    protected void setListeners() {
        mController = new SignInStep1Controller(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnNextStep, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSendVerifyCode, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnLoginImmediately, mController);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTVTitle.setText("免费注册");
    }

    public void sendVerifyCode() {
        String phoneNo = etPhoneNum.getText().toString();
        Parameter param = new Parameter();
        param.addParameter(RegisterFunction.IN_PHONE_NO, phoneNo);
        startTask(new SendVerifyCodeRequest(
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        ));
    }

    public void register() {
        String phoneNo = etPhoneNum.getText().toString();
        String pwd = mETLoginPwd.getText().toString();
        String confirmedPwd = mETConfirmedPwd.getText().toString();
        String verificationCode = mETVerificationCode.getText().toString();
        if (!checkPhoneNo(phoneNo)) {
            toast("您输入的手机号格式有误，请重新输入");
            return;
        }
        if (!checkPwd(confirmedPwd)) {
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
        param.addParameter(RegisterFunction.IN_RECOMMEND_CODE, mETRefereeId.getText().toString());
        HashMap<String, String> returnKeys = new HashMap<>();
        returnKeys.put(RegisterFunction.ERR_CONTENT, null);
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_REGISTER"),
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        );
        request.setReturnKeys(returnKeys);
        startTask(request);
        mDialog.show();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void dismissDialog() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void registerSuccess() {
        if (mDialogRegisterSuccess.isShowing()) {
            mDialogRegisterSuccess.dismiss();
        }

        Parameter param = new Parameter();
        param.addParameter(TaskId.TASK_ID, String.valueOf(TaskId.TASK_ID_THIRD));
        param.addParameter(
                LoginFunction.PHONE_NO, etPhoneNum.getText().toString());
        param.addParameter(
                LoginFunction.PASSWORD, mETLoginPwd.getText().toString());
        startTask(new LoginRequest(param, new QueryAction(this)));
        mBtnNextStep.setText("登录中...");
        mBtnNextStep.setEnabled(false);
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                mDialogRegisterSuccess.show();

                break;
            case TaskId.TASK_ID_SECOND:
                toast("语音验证码已经发送，请注意接听您的电话");
                mBtnSendVerifyCode.setEnabled(false);
                mBtnSendVerifyCode.setTextColor(getResources().getColor(R.color.text_gray));
                mBtnSendVerifyCode.setText("60秒后重新发送");
                mTimeRemains = DEFAULT_TIME_WAITING;
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
                                    mBtnSendVerifyCode.setText("发送验证码");
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
            case TaskId.TASK_ID_THIRD:
                mBtnNextStep.setText("提交");
                mBtnNextStep.setEnabled(true);
                startActivity(new Intent(this, RealNameAuthActivity.class));

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        mBtnNextStep.setText("提交");
        mBtnNextStep.setEnabled(true);
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
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
        toast("网络错误");
        mBtnNextStep.setText("提交");
        mBtnNextStep.setEnabled(true);
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
        mBtnNextStep.setText("提交");
        mBtnNextStep.setEnabled(true);
    }

    public static boolean checkPhoneNo(String phoneNo) {
        if (StringUtils.isEmpty(phoneNo) || phoneNo.length() != 11 || !phoneNo.startsWith("1")) {
            return false;
        }

        return true;
    }

    public static boolean checkPwd(String pwd) {
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
            return false;
        }
        return true;
    }
}

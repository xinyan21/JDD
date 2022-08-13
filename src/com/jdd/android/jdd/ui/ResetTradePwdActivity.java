package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.RegisterFunction;
import com.jdd.android.jdd.constants.function.ResetTradePwdFunction;
import com.jdd.android.jdd.controllers.ResetTradePwdController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
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
 * 描述：重置交易密码
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class ResetTradePwdActivity extends TKActivity implements IQueryServer {
    public EditText etNewPwd, etConfirmedPwd, etVerificationCode, etOldPwd;
    public Dialog dialogResetPwdSuccess;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnSubmit;
    private Button mBtnConfirmAndDismiss;
    private Button mBtnSendVerifyCode;

    private Timer mTimer = new Timer();
    private int mTimeRemains = RegisterActivity.DEFAULT_TIME_WAITING;
    private UserEntity mUserEntity;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private ResetTradePwdController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_trade_pwd);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etConfirmedPwd = (EditText) findViewById(R.id.et_confirmed_pwd);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        etOldPwd = (EditText) findViewById(R.id.et_old_pwd);
        etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnSendVerifyCode = (Button) findViewById(R.id.btn_get_verification_code);
        dialogResetPwdSuccess = new Dialog(this);
        dialogResetPwdSuccess.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogResetPwdSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogResetPwdSuccess.setContentView(R.layout.dialog_register_success);
        dialogResetPwdSuccess.setCanceledOnTouchOutside(true);
        mBtnConfirmAndDismiss = (Button)
                dialogResetPwdSuccess.findViewById(R.id.btn_login_immediately);
        TextView text = (TextView) dialogResetPwdSuccess.findViewById(R.id.tv_dialog_text);
        text.setText("您的交易密码已经修改成功！");
    }

    @Override
    protected void setListeners() {
        mController = new ResetTradePwdController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmit, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSendVerifyCode, mController);
    }

    @Override
    protected void initData() {
        mUserEntity = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("修改登录密码");
        mBtnConfirmAndDismiss.setText("确定");
    }

    public void sendVerifyCode() {
        if (null == mUserEntity) {
            PopUpComponentUtil.showShortToast(this, "请登录后再修改");
            return;
        }
        String phoneNo = String.valueOf(mUserEntity.getPhoneNo());
        Parameter param = new Parameter();
        param.addParameter(RegisterFunction.IN_PHONE_NO, phoneNo);
        startTask(new SendVerifyCodeRequest(
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        ));
    }

    public void submit() {
        String oldPwd = etOldPwd.getText().toString();
        String newPwd = etNewPwd.getText().toString();
        String confirmPwd = etConfirmedPwd.getText().toString();
        String verifyCode = etVerificationCode.getText().toString();
        if (StringUtils.isEmpty(oldPwd) || oldPwd.length() < 6) {
            PopUpComponentUtil.showShortToast(this, "原交易密码格式错误，请重新输入");
            return;
        }
        if (!RegisterActivity.checkPwd(newPwd)) {
            PopUpComponentUtil.showShortToast(this, "新密码格式错误，请重新输入");
            return;
        }
        if (!RegisterActivity.checkPwd(confirmPwd)) {
            PopUpComponentUtil.showShortToast(this, "确认密码格式错误，请重新输入");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            PopUpComponentUtil.showShortToast(this, "两次密码输入不一致，请重新输入");
            return;
        }
        if (!RegisterActivity.checkPwd(verifyCode)) {
            PopUpComponentUtil.showShortToast(this, "验证码不能为空");
            return;
        }
        Parameter param = new Parameter();
        param.addParameter(ResetTradePwdFunction.IN_NEW_PWD, newPwd);
        param.addParameter(ResetTradePwdFunction.IN_OLD_PWD, oldPwd);
        param.addParameter(ResetTradePwdFunction.IN_VERIFY_CODE, verifyCode);
        HashMap<String, String> returnKey = new HashMap<>();
        returnKey.put(ResetTradePwdFunction.ERR_CONTENT, "");
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_RESET_TRADE_PWD"),
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        );
        request.setReturnKeys(returnKey);
        startTask(request);
        mBtnSubmit.setText("处理中...");
        mBtnSubmit.setEnabled(false);
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                PopUpComponentUtil.showShortToast(this, "语音验证码已经发送，请注意接听您的电话");
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
                enableSubmitButton();
                if (!dialogResetPwdSuccess.isShowing()) {
                    dialogResetPwdSuccess.show();
                }

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        enableSubmitButton();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                PopUpComponentUtil.showShortToast(this, "验证码发送失败，请稍后重试");

                break;
            case TaskId.TASK_ID_SECOND:
                HashMap errorMsg = (HashMap) bundle.getSerializable(String.valueOf(taskId));
                try {
                    PopUpComponentUtil.showShortToast(this, errorMsg.get(RegisterFunction.ERR_CONTENT).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        enableSubmitButton();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        enableSubmitButton();
    }

    private void enableSubmitButton() {
        mBtnSubmit.setEnabled(true);
        mBtnSubmit.setText("提交");
    }
}

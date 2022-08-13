package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.VerifyEmailFunction;
import com.jdd.android.jdd.controllers.SetEmailController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：设置邮箱
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class SetEmailActivity extends TKActivity implements IQueryServer {
    public EditText etEmailAddr, etTradePwd;
    private Button mBtnSubmit;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;

    private SetEmailController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private UserEntity mUserEntity;
    private boolean mIsFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_email);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etEmailAddr = (EditText) findViewById(R.id.et_email_addr);
        etTradePwd = (EditText) findViewById(R.id.et_trade_pwd);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void setListeners() {
        mController = new SetEmailController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmit, mController);
    }

    @Override
    protected void initData() {
        mUserEntity = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("设置安全邮箱");
    }

    public void submit() {
        String email = etEmailAddr.getText().toString();
        if (StringUtils.isEmpty(email)) {
            PopUpComponentUtil.showShortToast(this, "邮箱地址不能为空，请输入您的邮箱地址");
            return;
        }
        if (!checkEmail(email)) {
            PopUpComponentUtil.showShortToast(this, "邮箱地址格式错误，请重新输入");
            return;
        }
        Parameter param = new Parameter();
        param.addParameter(VerifyEmailFunction.IN_EMAIL, etEmailAddr);
        if (!StringUtils.isEmpty(mUserEntity.getEmail())) {
            param.addParameter(VerifyEmailFunction.IN_OLD_EMAIL, mUserEntity.getEmail());
        }
        if (!mIsFirstTime) {
            param.addParameter(VerifyEmailFunction.IN_TYPE, VerifyEmailFunction.TYPE_RESEND);
        }
        startTask(new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_VERIFY_EMAIL"),
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        ));
        mBtnSubmit.setText("提交中...");
        mBtnSubmit.setEnabled(false);
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        mIsFirstTime = false;
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        onError();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        onError();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        onError();
    }

    private void onError() {
        mBtnSubmit.setEnabled(true);
        mBtnSubmit.setText("提交");
    }
}

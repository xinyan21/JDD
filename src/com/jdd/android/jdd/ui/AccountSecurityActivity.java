package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.controllers.AccountSecurityController;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：账户安全
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-03
 * @last_edit 2016-01-03
 * @since 1.0
 */
public class AccountSecurityActivity extends TKActivity {
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private RelativeLayout mRLEditLoginPwd;
    private RelativeLayout mRLSetSecurityEmail;
    private RelativeLayout mRLSetTradePwd;

    private AccountSecurityController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mRLEditLoginPwd = (RelativeLayout) findViewById(R.id.rl_edit_login_pwd);
        mRLSetSecurityEmail = (RelativeLayout) findViewById(R.id.rl_set_security_email);
        mRLSetTradePwd = (RelativeLayout) findViewById(R.id.rl_set_trade_pwd);
    }

    @Override
    protected void setListeners() {
        mController = new AccountSecurityController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLEditLoginPwd, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLSetSecurityEmail, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLSetTradePwd, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTVTitle.setText("账号安全");
    }
}

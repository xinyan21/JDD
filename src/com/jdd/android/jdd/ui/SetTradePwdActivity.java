package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.controllers.SetTradePwdController;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：设置交易密码
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class SetTradePwdActivity extends TKActivity implements IQueryServer {
    public EditText etTradePwd, etConfirmedPwd;
    public EditText etIDNum, etVerificationCode;
    private Button mBtnSubmit;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;

    private SetTradePwdController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_trade_pwd);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etIDNum = (EditText) findViewById(R.id.et_id_num);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        etTradePwd = (EditText) findViewById(R.id.et_trade_pwd);
        etConfirmedPwd = (EditText) findViewById(R.id.et_confirmed_pwd);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void setListeners() {
        mController = new SetTradePwdController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK,mIBtnBack,mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmit,mController);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTVTitle.setText("设置交易密码");
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {

    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {

    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {

    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {

    }
}

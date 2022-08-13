package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.controllers.ResetLoginPwdController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：重置登录密码
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class ResetLoginPwdActivity extends TKActivity implements IQueryServer {
    public EditText etNewPwd, etConfirmedPwd, etIDNum;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnSubmit;

    private ResetLoginPwdController mController;
    private UserEntity mUserEntity;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_login_pwd);

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
        etIDNum = (EditText) findViewById(R.id.et_id_num);
        etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void setListeners() {
        mController = new ResetLoginPwdController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmit, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
    }

    @Override
    protected void initData() {
        mUserEntity = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("修改登录密码");
        if (null != mUserEntity) {
            etIDNum.setEnabled(false);
            etIDNum.setText(String.valueOf(mUserEntity.getPhoneNo()));
        }
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

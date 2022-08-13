package com.jdd.android.jdd.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.LoginFunction;
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.controllers.LoginController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryRegisterInfoRequest;
import com.jdd.android.jdd.requests.QueryUserInfoRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.AppEngine;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.PreferencesUtils;
import com.thinkive.android.app_engine.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 描述：登录
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-03
 * @last_edit 2016-01-03
 * @since 1.0
 */
public class LoginActivity extends TKActivity implements IQueryServer {
    private static final String KEY_CACHE_ACCOUNT = "ACCOUNT";
    private static final String KEY_CACHE_PWD = "PWD";
    public EditText etPassword;
    public EditText etPhoneNum;
    public Button btnLogin;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private ImageView mIVPortrait;
    private TextView mTVVersionName;
    private Button mBtnSignIn, mBtnForgetPwd;
    private CheckBox mCBRememberPwd;

    private LoginController mController;
    private Handler mHandler;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setListeners();
        initData();
        initViews();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                finish();
                startActivity(new Intent(LoginActivity.this, AppEngine.class));
            }
        };

        //图片加载组件初始化
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(10 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        mIVPortrait = (ImageView) findViewById(R.id.iv_user_portrait);
        mBtnForgetPwd = (Button) findViewById(R.id.btn_forget_password);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnLogin = (Button) findViewById(R.id.btn_login);
        mTVVersionName = (TextView) findViewById(R.id.tv_version_name);
        mCBRememberPwd = (CheckBox) findViewById(R.id.cb_remember_pwd);
    }

    @Override
    protected void setListeners() {
        mController = new LoginController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnForgetPwd, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSignIn, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnLogin, mController);
        registerListener(ListenerControllerAdapter.ON_CHECK, mCBRememberPwd, mController);
    }

    @Override
    protected void initData() {
        mCache.clearCache();
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("登录");
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            mTVVersionName.setText("版本号：" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String cacheAccount = PreferencesUtils.getString(this, KEY_CACHE_ACCOUNT, "");
        String cachePwd = PreferencesUtils.getString(this, cacheAccount, "");
        if (!StringUtils.isEmpty(cacheAccount)) {
            etPhoneNum.setText(cacheAccount);
            etPassword.requestFocus();
        }
        if (!StringUtils.isEmpty(cachePwd)) {
            etPassword.setText(cachePwd);
        }
    }

    private void enableLogin() {
        btnLogin.setEnabled(true);
        btnLogin.setText("登录");
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        UserEntity user = (UserEntity)
                mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                Parameter param = new Parameter();
                param.addParameter(QueryUserInfoFunction.IN_USER_ID, "");
                startTask(new QueryUserInfoRequest(
                        TaskId.TASK_ID_SECOND, CacheKey.KEY_CURRENT_LOGIN_USER_INFO,
                        param, new QueryAction(this))
                );

                break;
            case TaskId.TASK_ID_SECOND:
                Parameter queryRegisterParam = new Parameter();
                queryRegisterParam.addParameter(
                        QueryUserInfoFunction.IN_USER_ID, String.valueOf(user.getUserId())
                );
                startTask(new QueryRegisterInfoRequest(
                        TaskId.TASK_ID_THIRD, queryRegisterParam, user, new QueryAction(this)
                ));

                break;
            case TaskId.TASK_ID_THIRD:
                String realNameVerifyStatus = user.getRealNameVerifyStatus();
                PreferencesUtils.putString(
                        this, KEY_CACHE_ACCOUNT, etPhoneNum.getText().toString()
                );
                if (mCBRememberPwd.isChecked()) {
                    PreferencesUtils.putString(
                            this, etPhoneNum.getText().toString(), etPassword.getText().toString()
                    );
                } else {
                    PreferencesUtils.putString(this, etPhoneNum.getText().toString(), " ");
                }
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                //通过延迟finish的方式，防止引发程序崩溃的bug
                mHandler.sendEmptyMessageDelayed(0, 1000);
//                if ("C".equals(realNameVerifyStatus) || "N".equals(realNameVerifyStatus)) {
//                    PreferencesUtils.putString(
//                            this, KEY_CACHE_ACCOUNT, etPhoneNum.getText().toString()
//                    );
//                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    //通过延迟finish的方式，防止引发程序崩溃的bug
//                    mHandler.sendEmptyMessageDelayed(0, 1000);
//                }else if ("B".equals(realNameVerifyStatus)) {
//                    PopUpComponentUtil.showShortToast(this, "您的资料审核不合格，请重新进行实名认证");
//                    startActivity(new Intent(this, RealNameAuthActivity.class));
//                } else {
//                    PopUpComponentUtil.showShortToast(this, "您还未进行实名认证，请进行实名认证");
//                    startActivity(new Intent(this, RealNameAuthActivity.class));
//                }
                enableLogin();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        enableLogin();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                if (bundle.containsKey(BaseServerFunction.ERR_MSG)) {
                    Toast.makeText(LoginActivity.this,
                            bundle.getString(BaseServerFunction.ERR_MSG), Toast.LENGTH_SHORT).show();
                } else if (bundle.containsKey(LoginFunction.CONTENT)) {
                    Toast.makeText(LoginActivity.this,
                            bundle.getString(LoginFunction.CONTENT), Toast.LENGTH_SHORT).show();
                }

                break;
            case TaskId.TASK_ID_SECOND:
                Toast.makeText(this, "获取用户信息失败", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        enableLogin();
        Toast.makeText(LoginActivity.this, R.string.error_net, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        enableLogin();
        Toast.makeText(LoginActivity.this, R.string.error_internal, Toast.LENGTH_SHORT).show();
    }
}

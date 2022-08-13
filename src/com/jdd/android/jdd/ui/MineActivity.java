package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.PayFunction;
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.constants.function.RegisterFunction;
import com.jdd.android.jdd.controllers.MineController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.QueryUserInfoRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinkive.adf.core.CoreApplication;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.constants.msg.EngineMsgList;
import com.thinkive.android.app_engine.constants.msg.engine.SetModuleEnableMsg;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IAppControl;
import com.thinkive.android.app_engine.interfaces.IID;
import com.thinkive.android.app_engine.interfaces.IModule;
import com.thinkive.android.app_engine.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 描述：我的
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class MineActivity extends TKActivity implements IModule, IQueryServer {
    public UserEntity userEntity = new UserEntity();

    private ImageButton mIBtnBack;
    private ImageView mIVPortrait, mIVGender;
    private TextView mTVUserName, mTVRankText, mTVRestCoin;
    private ImageView mIVIsVerified, mIVRankIcon;
    private Button mBtnRecharge, mBtnAccSecurity;
    private TextView mTVFansNum, mTVIntelsNum, mTVExperiencesNum;
    private LinearLayout mLLMYFans, mLLMyIntels, mLLMyExperiences;
    private RelativeLayout mRLEditProfile, mRLSendCoinToFriend;
    private RelativeLayout mRLMyFollows, mRLFavorites;
    private RelativeLayout mRLBoughtCourses, mRLMyIntels;
    private RelativeLayout mRLMyExperiences, mRLFeedBack;
    private Button mBtnLogout;
    private PopupWindow mPPWOutOfDate;
    private TextView mTVOutOfDateHint;
    private TextView mTVLastLoginTime;
    private Button mBtnBuyThreeMonths, mBtnBuySixMonths, mBtnBuyTwelveMonths;
    private Dialog mDialogLoading;

    private MineController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private boolean mIsExpired = false;     //试用期或者会员是否过期
    private IAppControl mAppControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //每次进来都要刷新数据
        queryUserInfo();
        if (mIsExpired) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showOrHideBuyVIPWindow();
                }
            }, 2000);
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void findViews() {
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mIVGender = (ImageView) findViewById(R.id.iv_gender);
        mIVIsVerified = (ImageView) findViewById(R.id.iv_is_verified);
        mIVPortrait = (ImageView) findViewById(R.id.iv_user_portrait);
        mIVRankIcon = (ImageView) findViewById(R.id.iv_rank_icon);
        mTVRestCoin = (TextView) findViewById(R.id.tv_rest_coin);
        mTVExperiencesNum = (TextView) findViewById(R.id.tv_experience_num);
        mTVFansNum = (TextView) findViewById(R.id.tv_fans_num);
        mTVIntelsNum = (TextView) findViewById(R.id.tv_intels_num);
        mTVRankText = (TextView) findViewById(R.id.tv_rank_text);
        mTVUserName = (TextView) findViewById(R.id.tv_user_name);
        mLLMyExperiences = (LinearLayout) findViewById(R.id.ll_my_experiences);
        mLLMYFans = (LinearLayout) findViewById(R.id.ll_my_fans);
        mLLMyIntels = (LinearLayout) findViewById(R.id.ll_my_intels);
        mRLBoughtCourses = (RelativeLayout) findViewById(R.id.rl_bought_courses);
        mRLEditProfile = (RelativeLayout) findViewById(R.id.rl_edit_profile);
        mRLFavorites = (RelativeLayout) findViewById(R.id.rl_favorites);
        mRLFeedBack = (RelativeLayout) findViewById(R.id.rl_feed_back);
        mRLSendCoinToFriend = (RelativeLayout) findViewById(R.id.rl_send_coin_to_friend);
        mRLMyIntels = (RelativeLayout) findViewById(R.id.rl_my_intels);
        mRLMyFollows = (RelativeLayout) findViewById(R.id.rl_my_follows);
        mRLMyExperiences = (RelativeLayout) findViewById(R.id.rl_my_experiences);
        mBtnAccSecurity = (Button) findViewById(R.id.btn_acc_security);
        mBtnRecharge = (Button) findViewById(R.id.btn_recharge);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);
        mTVOutOfDateHint = (TextView) findViewById(R.id.tv_out_of_date_hint);
        View outOfDate = LayoutInflater.from(this).inflate(R.layout.ppw_vip_out_of_date, null);
        mPPWOutOfDate = new PopupWindow(
                outOfDate, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true
        );
        mBtnBuyThreeMonths = (Button) outOfDate.findViewById(R.id.btn_buy_three_month);
        mBtnBuySixMonths = (Button) outOfDate.findViewById(R.id.btn_buy_six_month);
        mBtnBuyTwelveMonths = (Button) outOfDate.findViewById(R.id.btn_buy_twelve_month);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(
                this, "处理中，请稍候...", true, true
        );
        mTVLastLoginTime = (TextView) findViewById(R.id.tv_last_login_time);
    }

    @Override
    protected void setListeners() {
        mController = new MineController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLMyExperiences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLMYFans, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLMyIntels, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLBoughtCourses, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLEditProfile, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLFavorites, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLFeedBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLSendCoinToFriend, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLMyIntels, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLMyFollows, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mRLMyExperiences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnAccSecurity, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnRecharge, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnLogout, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnBuyThreeMonths, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnBuySixMonths, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnBuyTwelveMonths, mController);
//        registerListener(ListenerControllerAdapter.ON_CLICK, mTVOutOfDateHint, mController);
    }

    @Override
    protected void initData() {
        userEntity = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
    }

    @Override
    protected void initViews() {
        if (null == userEntity) {
            return;
        }
        if (!StringUtils.isEmpty(userEntity.getNickName())) {
            mTVUserName.setText(userEntity.getNickName());
        } else if (!StringUtils.isEmpty(userEntity.getName())) {
            mTVUserName.setText(userEntity.getName());
        }
        if (!StringUtils.isEmpty(userEntity.getGender())) {
            if ("wom".equals(userEntity.getGender())) {
                mIVGender.setImageResource(R.drawable.female_b);
            } else if ("man".equals(userEntity.getGender())) {
                mIVGender.setImageResource(R.drawable.male_b);
            }
        } else {
            mIVGender.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(userEntity.getAvatarUrl())) {
            ImageLoader.getInstance().displayImage(
                    ProjectUtil.getFullUrl(userEntity.getAvatarUrl()), mIVPortrait
            );
        }
        mTVRestCoin.setText(String.valueOf(userEntity.getCoins()));
        mTVFansNum.setText(String.valueOf(userEntity.getFansCount()));
        mTVIntelsNum.setText(String.valueOf(userEntity.getIntelligenceCount()));
        mTVExperiencesNum.setText(String.valueOf(userEntity.getExperienceCount()));
        mPPWOutOfDate.setOutsideTouchable(true);
        mPPWOutOfDate.setFocusable(true);
        mPPWOutOfDate.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.transparent))
        );
        StringBuffer sbHint = new StringBuffer();
        String lastLoginTime = DateFormat.format(
                "yyyy-MM-dd", userEntity.getLastLoginTime()).toString();
        sbHint.append("上次登录时间：").append(lastLoginTime);
        int gray = getResources().getColor(R.color.text_gray);
        mTVLastLoginTime.setText(sbHint);
        mTVOutOfDateHint.setText(sbHint);
        if (userEntity.getTrialExpireTime() >= userEntity.getServerTime()
                && userEntity.getTrialExpireTime() >= userEntity.getVipExpireTime()
                && userEntity.getTrialExpireTime() > 0) {
            String text = "会员有效期截止：（试用期至" +
                    DateFormat.format(
                            "yyyy-MM-dd", userEntity.getTrialExpireTime()).toString() + ")";
            mTVOutOfDateHint.setText(text);
            mTVOutOfDateHint.setTextColor(gray);
        } else if (userEntity.getVipExpireTime() >= userEntity.getServerTime()
                && userEntity.getVipExpireTime() > 0) {
            String text = "会员有效期截止：" +
                    DateFormat.format(
                            "yyyy-MM-dd", userEntity.getVipExpireTime()).toString();
            mTVOutOfDateHint.setText(text);
            mTVOutOfDateHint.setTextColor(gray);
        } else if (userEntity.getTrialExpireTime() == 0 ||
                (userEntity.getTrialExpireTime() < userEntity.getServerTime()
                        && userEntity.getTrialExpireTime() >= userEntity.getVipExpireTime())) {
            String text = "试用期已到期，请购买会员后再使用。会员购买后无法退回。";
            mTVOutOfDateHint.setText(text);
            mTVOutOfDateHint.setTextColor(Color.RED);
            mTVLastLoginTime.setVisibility(View.GONE);
            mIsExpired = true;

        } else if (userEntity.getVipExpireTime() == 0 ||
                userEntity.getVipExpireTime() < userEntity.getServerTime()) {
            String text = "会员已到期，请续费后再使用。会员购买后无法退回。";
            mTVOutOfDateHint.setText(text);
            mTVOutOfDateHint.setTextColor(Color.RED);
            mTVLastLoginTime.setVisibility(View.GONE);
            mIsExpired = true;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsExpired) {
                    enableOtherModules(false);
                } else {
                    enableOtherModules(true);
                }
            }
        }, 2000);
    }

    private void enableOtherModules(boolean enable) {
        String modules = "index:price:intelligence:think_tank";
        JSONObject params = new JSONObject();
        try {
            params.put(SetModuleEnableMsg.KEY_ENABLE, enable);
            params.put(SetModuleEnableMsg.KEY_MODULES, modules);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppMsg msg = new AppMsg(String.valueOf(EngineMsgList.SET_MODULE_ENABLE), params);
        mAppControl.sendMessage(msg);
        if (!enable) {
            mAppControl.sendMessage(
                    new AppMsg("mine", String.valueOf(EngineMsgList.OPEN_MODULE), null)
            );
        }
    }

    public void showOrHideBuyVIPWindow() {
        if (mPPWOutOfDate.isShowing()) {
            mPPWOutOfDate.dismiss();
        } else {
            mPPWOutOfDate.setWidth(mTVOutOfDateHint.getWidth());
            mPPWOutOfDate.showAsDropDown(mTVOutOfDateHint);
        }
    }

    private void queryUserInfo() {
        Parameter param = new Parameter();
        param.addParameter(QueryUserInfoFunction.IN_USER_ID, "");
        startTask(new QueryUserInfoRequest(
                TaskId.TASK_ID_FIRST, CacheKey.KEY_CURRENT_LOGIN_USER_INFO,
                param, new QueryAction(this)
        ));
    }

    public void buyVIP(String money) {
        Parameter param = new Parameter();
        param.addParameter(PayFunction.MONEY, money);
        HashMap<String, String> returnKeys = new HashMap<>();
        returnKeys.put(BaseServerFunction.ERR_MSG, null);
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_BUY_VIP"),
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        );
        request.setReturnKeys(returnKeys);
        startTask(request);
        mDialogLoading.show();
    }

    public void logout() {
        ((CoreApplication) getApplication()).exit();
    }

    @Override
    public boolean init(IAppContext iAppContext) {
        if (null != iAppContext) {
            mAppControl = (IAppControl) iAppContext.queryInterface(this, IID.IID_IAppControl);
        }
        return true;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onMessage(AppMsg appMsg) {

    }

    @Override
    public String onCallMessage(AppMsg appMsg) {
        return null;
    }

    @Override
    public void onUnload() {

    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                userEntity = (UserEntity)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                initViews();

                break;
            case TaskId.TASK_ID_SECOND:
                PopUpComponentUtil.showShortToast(this, "您已成功购买会员");
                queryUserInfo();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
            case TaskId.TASK_ID_SECOND:
            case TaskId.TASK_ID_THIRD:
                HashMap returnData = (HashMap) bundle.getSerializable(String.valueOf(taskId));
                if (returnData.containsKey(RegisterFunction.ERR_CONTENT)) {
                    PopUpComponentUtil.showShortToast(
                            this, returnData.get(RegisterFunction.ERR_CONTENT).toString()
                    );
                }
                if (returnData.containsKey(BaseServerFunction.ERR_MSG)) {
                    PopUpComponentUtil.showShortToast(
                            this, returnData.get(BaseServerFunction.ERR_MSG).toString()
                    );
                }

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
        Toast.makeText(this, R.string.error_net, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
        Toast.makeText(this, R.string.error_internal, Toast.LENGTH_SHORT).show();
    }

    private void dismissDialog() {
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
    }
}

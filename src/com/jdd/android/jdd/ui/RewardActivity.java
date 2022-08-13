package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.constants.function.RewardFunction;
import com.jdd.android.jdd.controllers.RewardController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.QueryUserInfoRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.adf.tools.Utilities;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.HashMap;

/**
 * 描述：打赏
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class RewardActivity extends TKActivity implements IQueryServer {
    public static final String KEY_USER_ID = "USER_ID";
    public boolean isCustomMoney = false;
    public Dialog dialogCustomMoney;
    public Dialog dialogConfirmToReward;
    public TextView tvConfirmText;
    public EditText etCustomMoney;

    public Button btnCoinNum1, btnCoinNum2, btnCoinNum3, btnCoinNum4, btnCoinNum5, btnCoinNum6;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private ImageView mIVPortrait, mIVGender;
    private TextView mTVUserName;
    private Button mBtnOpenCustomDialog;
    private Button mBtnRewardTA, mBtnRewardTAOfDialog;
    private Dialog mDialogProcessing;
    private Button mBtnCancelReward, mBtnConfirmReward;
    private Button mBtnCancelCustomMoney;

    private RewardController mController;
    private int mRewardCoins = 0;
    private UserEntity mUserEntity;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mIVGender = (ImageView) findViewById(R.id.iv_gender);
        mIVPortrait = (ImageView) findViewById(R.id.iv_user_portrait);
        mTVUserName = (TextView) findViewById(R.id.tv_user_name);
        btnCoinNum1 = (Button) findViewById(R.id.btn_coin_num1);
        btnCoinNum2 = (Button) findViewById(R.id.btn_coin_num2);
        btnCoinNum3 = (Button) findViewById(R.id.btn_coin_num3);
        btnCoinNum4 = (Button) findViewById(R.id.btn_coin_num4);
        btnCoinNum5 = (Button) findViewById(R.id.btn_coin_num5);
        btnCoinNum6 = (Button) findViewById(R.id.btn_coin_num6);
        mBtnOpenCustomDialog = (Button) findViewById(R.id.btn_custom_money);
        mBtnRewardTA = (Button) findViewById(R.id.btn_reward_ta);
        dialogCustomMoney = new Dialog(this);
        dialogCustomMoney.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogCustomMoney.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomMoney.setContentView(R.layout.dialog_custom_reward_money);
        dialogCustomMoney.setCanceledOnTouchOutside(true);
        etCustomMoney
                = (EditText) dialogCustomMoney.findViewById(R.id.et_amount_of_money);
        mBtnRewardTAOfDialog
                = (Button) dialogCustomMoney.findViewById(R.id.btn_reward_custom_money);
        mBtnCancelCustomMoney
                = (Button) dialogCustomMoney.findViewById(R.id.btn_cancel_custom_money);
        mDialogProcessing = PopUpComponentUtil.createLoadingProgressDialog(this, "处理中，请稍候...", true, true);
        dialogConfirmToReward = new Dialog(this);
        dialogConfirmToReward.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogConfirmToReward.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirmToReward.setContentView(R.layout.dialog_confirm);
        dialogConfirmToReward.setCanceledOnTouchOutside(true);
        mBtnCancelReward = (Button) dialogConfirmToReward.findViewById(R.id.btn_cancel);
        mBtnConfirmReward = (Button) dialogConfirmToReward.findViewById(R.id.btn_confirm);
        tvConfirmText = (TextView) dialogConfirmToReward.findViewById(R.id.tv_dialog_text);
    }

    @Override
    protected void setListeners() {
        mController = new RewardController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum1, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum2, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum3, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum4, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum5, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum6, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnOpenCustomDialog, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnRewardTA, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnRewardTAOfDialog, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnCancelReward, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnConfirmReward, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnCancelCustomMoney, mController);
    }

    @Override
    protected void initData() {
        mRewardCoins = Integer.valueOf(btnCoinNum1.getTag().toString());
        mUserEntity = new UserEntity();
        mUserEntity.setUserId(getIntent().getLongExtra(KEY_USER_ID, 0));
        queryUserInfo();
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("打赏");
        if (!StringUtils.isEmpty(mUserEntity.getAvatarUrl())) {
            ImageLoader.getInstance().displayImage(
                    ProjectUtil.getFullUrl(mUserEntity.getAvatarUrl()), mIVPortrait
            );
        }
        if (!StringUtils.isEmpty(mUserEntity.getGender())) {
            if ("wom".equals(mUserEntity.getGender())) {
                mIVGender.setImageResource(R.drawable.female_b);
            } else if ("man".equals(mUserEntity.getGender())) {
                mIVGender.setImageResource(R.drawable.male_b);
            }
        } else {
            mIVGender.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(mUserEntity.getNickName())) {
            mTVUserName.setText(mUserEntity.getNickName());
        }
    }

    /**
     * 设置选中的打赏金额样式
     *
     * @param numChecked 选中的打赏金额
     */
    public void setCoinNumSelected(int numChecked) {
        isCustomMoney = false;
        Button button;
        for (int i = 1; i <= 6; i++) {
            button = ((Button) findViewById(Utilities.getResourceID(this, "id", "btn_coin_num" + i)));
            if (numChecked == i) {
                mRewardCoins = Integer.valueOf(button.getTag().toString());
                button.setTextColor(getResources().getColor(R.color.white));
                button.setBackgroundResource(R.drawable.shape_btn_rounded_orange);
            } else {
                button.setTextColor(getResources().getColor(R.color.text_gray));
                button.setBackgroundResource(R.drawable.shape_input_rounded_corner);
            }
        }
    }

    private void queryUserInfo() {
        Parameter param = new Parameter();
        param.addParameter(
                QueryUserInfoFunction.IN_USER_ID, String.valueOf(mUserEntity.getUserId())
        );
        startTask(new QueryUserInfoRequest(
                TaskId.TASK_ID_FIRST, CacheKey.KEY_USER_INFO, param, new QueryAction(this)
        ));
    }

    public void reward() {
        Parameter param = new Parameter();
        param.addParameter(RewardFunction.IN_COINS, String.valueOf(mRewardCoins));
        param.addParameter(RewardFunction.IN_USER_ID, String.valueOf(mUserEntity.getUserId()));
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_REWARD"),
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        );
        HashMap<String, String> returnKey = new HashMap<>();
        returnKey.put("message", "");
        request.setReturnKeys(returnKey);
        startTask(request);
        mDialogProcessing.show();
    }

    private void dismissDialog() {
        if (mDialogProcessing.isShowing()) {
            mDialogProcessing.dismiss();
        }
    }

    public void showRewardConfirm() {
        if (isCustomMoney) {
            mRewardCoins = Integer.valueOf(etCustomMoney.getText().toString());
        }
        tvConfirmText.setText("您确认要打赏" + mRewardCoins + "金币吗？");
        dialogConfirmToReward.show();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                mUserEntity = (UserEntity) mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                initViews();
                break;
            case TaskId.TASK_ID_SECOND:
                Toast.makeText(this, "恭喜您已经成功打赏" + mRewardCoins + "金币", Toast.LENGTH_LONG).show();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                mUserEntity = (UserEntity) mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                initViews();
                break;
            case TaskId.TASK_ID_SECOND:
                HashMap<String, String> returnMsg =
                        (HashMap<String, String>) bundle.getSerializable(String.valueOf(taskId));
                PopUpComponentUtil.showShortToast(this, returnMsg.get("message"));

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
        Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
        Toast.makeText(this, "程序异常", Toast.LENGTH_SHORT).show();
    }
}

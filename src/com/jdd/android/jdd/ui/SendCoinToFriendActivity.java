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
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.constants.function.TransferCoinFunction;
import com.jdd.android.jdd.controllers.SendCoinToFriendController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.QueryUserInfoRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.HashMap;

/**
 * 描述：发送金币给好友
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class SendCoinToFriendActivity extends TKActivity implements IQueryServer {
    public EditText etPayeeAccount, etPayeeName;
    public EditText etAmountOfCoin, etPassword;
    public Dialog dialogConfirmToReward;
    public TextView tvConfirmText;
    private Button mBtnSubmitTransfer;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnQueryUserName;
    private Button mBtnCancelTransfer, mBtnConfirmTransfer;
    private Dialog mDialogProcessing;

    private SendCoinToFriendController mController;
    private UserEntity mUserEntity;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coin_to_friend);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etAmountOfCoin = (EditText) findViewById(R.id.et_amount_of_coin);
        etPassword = (EditText) findViewById(R.id.et_trade_pwd);
        etPayeeAccount = (EditText) findViewById(R.id.et_payee_account);
        etPayeeName = (EditText) findViewById(R.id.et_payee_name);
        mBtnSubmitTransfer = (Button) findViewById(R.id.btn_confirm_transfer);
        mBtnQueryUserName = (Button) findViewById(R.id.btn_query_user_name);
        dialogConfirmToReward = new Dialog(this);
        dialogConfirmToReward.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogConfirmToReward.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirmToReward.setContentView(R.layout.dialog_confirm);
        dialogConfirmToReward.setCanceledOnTouchOutside(true);
        mBtnCancelTransfer = (Button) dialogConfirmToReward.findViewById(R.id.btn_cancel);
        mBtnConfirmTransfer = (Button) dialogConfirmToReward.findViewById(R.id.btn_confirm);
        tvConfirmText = (TextView) dialogConfirmToReward.findViewById(R.id.tv_dialog_text);
        mDialogProcessing = PopUpComponentUtil.createLoadingProgressDialog(this, "处理中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new SendCoinToFriendController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmitTransfer, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnQueryUserName, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnCancelTransfer, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnConfirmTransfer, mController);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTVTitle.setText("赠送金币给好友");
    }

    public void queryUserInfo() {
        String phoneNo = etPayeeAccount.getText().toString();
        if (!RegisterActivity.checkPhoneNo(phoneNo)) {
            PopUpComponentUtil.showShortToast(this, "手机号格式错误，请重新输入");
            return;
        }
        Parameter param = new Parameter();
        param.addParameter(
                QueryUserInfoFunction.IN_USER_ID, phoneNo
        );
        startTask(new QueryUserInfoRequest(
                TaskId.TASK_ID_FIRST, CacheKey.KEY_USER_INFO, param, new QueryAction(this)
        ));
    }

    public void submit() {
        String pwd = etPassword.getText().toString();
        String phoneNo = etPayeeAccount.getText().toString();
        if (!RegisterActivity.checkPwd(pwd)) {
            PopUpComponentUtil.showShortToast(this, "密码格式错误，请重新输入");
            return;
        }
//        if (null == mUserEntity) {
//            PopUpComponentUtil.showShortToast(this, "很抱歉，未查询到该用户！");
//            return;
//        }
        Parameter param = new Parameter();
        param.addParameter(TransferCoinFunction.IN_NAME, etPayeeName.getText().toString());
        param.addParameter(TransferCoinFunction.IN_ACCOUNT, phoneNo);
        param.addParameter(TransferCoinFunction.IN_COUNT, etAmountOfCoin.getText().toString());
        param.addParameter(TransferCoinFunction.IN_TRADE_PWD, pwd);
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_TRANSFER_COIN"),
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        );
        HashMap<String, String> returnKey = new HashMap<>();
        returnKey.put(TransferCoinFunction.ERR_CONTENT, "");
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
        tvConfirmText.setText("您确认要赠送给" +
                etPayeeName.getText().toString() + etAmountOfCoin.getText().toString() + "金币吗？");
        dialogConfirmToReward.show();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                mUserEntity = (UserEntity) mCache.getCacheItem(String.valueOf(taskId));
                if (null != mUserEntity) {
                    etPayeeName.setText(mUserEntity.getNickName());
                } else {
//                    PopUpComponentUtil.showShortToast(this, "很抱歉，未查询到该用户！");
                }

                break;
            case TaskId.TASK_ID_SECOND:
                PopUpComponentUtil.showShortToast(this, "恭喜您成功转账给" +
                        etPayeeName.getText().toString() + etAmountOfCoin.getText().toString() + "  金币！");

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                PopUpComponentUtil.showShortToast(this, "很抱歉，未查询到该用户！");

                break;
            case TaskId.TASK_ID_SECOND:
                HashMap<String, String> returnMsg =
                        (HashMap<String, String>) bundle.getSerializable(String.valueOf(taskId));
                PopUpComponentUtil.showShortToast(
                        this, returnMsg.get(TransferCoinFunction.ERR_CONTENT)
                );

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
        PopUpComponentUtil.showShortToast(this, "网络错误！");
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
        PopUpComponentUtil.showShortToast(this, "程序异常！");
    }
}

package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.PayFunction;
import com.jdd.android.jdd.constants.function.RegisterFunction;
import com.jdd.android.jdd.controllers.RechargeController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.QueryWXOrderInfoRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.adf.tools.Utilities;
import com.thinkive.android.app_engine.engine.TKActivity;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 描述：充值界面
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-04-27
 * @since 1.0
 */
public class RechargeActivity extends TKActivity implements IQueryServer {
    public static final String KEY_USER_ENTITY = "USER_ENTITY";
    public UserEntity userEntity = new UserEntity();
    public boolean isConfirmPayDialog = false;
    public Button btnCoinNum1, btnCoinNum2, btnCoinNum3, btnCoinNum4, btnCoinNum5, btnCoinNum6;
    public Button btnCoinNum7, btnCoinNum8, btnCoinNum9;
    public Dialog dialogConfirm;
    public TextView tvConfirmText;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Dialog mDialogLoading;
    private Button mBtnPay;
    private Button mBtnCancelRecharge, mBtnConfirmRecharge;

    private RechargeController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private int mRechargeMoney = 20;//默认是选中第一个
    private IWXAPI mIWXAPI;
    private int mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mOrderId > 0) {
            confirmPaySuccess();
        }
    }

    @Override
    protected void findViews() {
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        btnCoinNum1 = (Button) findViewById(R.id.btn_coin_num1);
        btnCoinNum2 = (Button) findViewById(R.id.btn_coin_num2);
        btnCoinNum3 = (Button) findViewById(R.id.btn_coin_num3);
        btnCoinNum4 = (Button) findViewById(R.id.btn_coin_num4);
        btnCoinNum5 = (Button) findViewById(R.id.btn_coin_num5);
        btnCoinNum6 = (Button) findViewById(R.id.btn_coin_num6);
        btnCoinNum7 = (Button) findViewById(R.id.btn_coin_num7);
        btnCoinNum8 = (Button) findViewById(R.id.btn_coin_num8);
        btnCoinNum9 = (Button) findViewById(R.id.btn_coin_num9);
        mBtnPay = (Button) findViewById(R.id.btn_pay);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(
                this, "处理中，请稍候...", true, true
        );
        dialogConfirm = new Dialog(this);
        dialogConfirm.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirm.setContentView(R.layout.dialog_confirm);
        dialogConfirm.setCanceledOnTouchOutside(true);
        mBtnCancelRecharge = (Button) dialogConfirm.findViewById(R.id.btn_cancel);
        mBtnConfirmRecharge = (Button) dialogConfirm.findViewById(R.id.btn_confirm);
        tvConfirmText = (TextView) dialogConfirm.findViewById(R.id.tv_dialog_text);
    }

    @Override
    protected void setListeners() {
        mController = new RechargeController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum1, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum2, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum3, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum4, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum5, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum6, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum7, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum8, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCoinNum9, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnCancelRecharge, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnConfirmRecharge, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnPay, mController);
    }

    @Override
    protected void initData() {
        userEntity = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("购买今币");
    }

    public void confirmPay() {
        if (dialogConfirm.isShowing()) {
            return;
        }
        tvConfirmText.setText("确认充值" + mRechargeMoney + "元吗？");
        dialogConfirm.show();
        isConfirmPayDialog = true;
    }

    public void confirmPaySuccess() {
        isConfirmPayDialog = false;
        tvConfirmText.setText("支付是否成功？");
        dialogConfirm.show();
    }

    public void createOrder() {
        Parameter param = new Parameter();
        param.addParameter(PayFunction.USER_ID, String.valueOf(userEntity.getUserId()));
        param.addParameter(PayFunction.MONEY, String.valueOf(mRechargeMoney));
        HashMap<String, String> returnKeys = new HashMap<>();
        returnKeys.put(BaseServerFunction.ERR_MSG, null);
        returnKeys.put(PayFunction.ORDER_ID, null);
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_CREATE_RECHARGE_ORDER"),
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        );
        request.setReturnKeys(returnKeys);
        startTask(request);
        mDialogLoading.show();
    }

    public void queryPayInfo(long orderId) {
        Parameter param = new Parameter();
        param.addParameter(PayFunction.USER_ID, String.valueOf(userEntity.getUserId()));
        param.addParameter(PayFunction.ORDER_ID, String.valueOf(orderId));
        startTask(new QueryWXOrderInfoRequest(
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        ));
    }

    public void queryPay() {
        Parameter param = new Parameter();
        param.addParameter(PayFunction.ORDER_ID, String.valueOf(mOrderId));
        HashMap<String, String> returnKeys = new HashMap<>();
        returnKeys.put(BaseServerFunction.ERR_MSG, null);
        returnKeys.put(PayFunction.ORDER_ID, null);
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_QUERY_RECHARGE_RESULT"),
                TaskId.TASK_ID_THIRD, param, new QueryAction(this)
        );
        request.setReturnKeys(returnKeys);
        startTask(request);
        mDialogLoading.show();
    }

    /**
     * 设置选中的充值金额样式
     *
     * @param numChecked 选中的充值金额
     */
    public void setCoinNumSelected(int numChecked) {
        Button button;
        for (int i = 1; i <= 6; i++) {
            button = ((Button) findViewById(Utilities.getResourceID(this, "id", "btn_coin_num" + i)));
            if (numChecked == i) {
                mRechargeMoney = Integer.valueOf(button.getTag().toString());
                button.setTextColor(getResources().getColor(R.color.white));
                button.setBackgroundResource(R.drawable.shape_btn_rounded_orange);
            } else {
                button.setTextColor(getResources().getColor(R.color.text_gray));
                button.setBackgroundResource(R.drawable.shape_input_rounded_corner);
            }
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                HashMap<String, String> returnData = (HashMap<String, String>)
                        bundle.getSerializable(String.valueOf(taskId));
                //查询支付信息
                try {
                    mOrderId = Integer.valueOf(returnData.get(PayFunction.ORDER_ID));
                    queryPayInfo(mOrderId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case TaskId.TASK_ID_SECOND:
                JSONObject json = (JSONObject)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                json = json.optJSONObject("OrderInfo");
                PayReq req = new PayReq();
                req.appId = json.optString("appid");
                req.partnerId = json.optString("partnerid");
                req.prepayId = json.optString("prepayid");
                req.nonceStr = json.optString("noncestr");
                req.timeStamp = json.optString("timestamp");
                req.packageValue = json.optString("package");
                req.sign = json.optString("sign");
                //发起微信支付
                mIWXAPI = WXAPIFactory.createWXAPI(this, req.appId);
                mIWXAPI.sendReq(req);
                break;
            case TaskId.TASK_ID_THIRD:
                Toast.makeText(this, "您已成功充值" + mRechargeMoney + "元", Toast.LENGTH_LONG).show();
                break;
            default:
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

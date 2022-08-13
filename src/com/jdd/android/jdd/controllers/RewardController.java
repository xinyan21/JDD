package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.RewardActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：打赏控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class RewardController extends ListenerControllerAdapter implements View.OnClickListener {
    private RewardActivity mActivity;

    public RewardController(RewardActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                if (mActivity.dialogCustomMoney.isShowing()) {
                    mActivity.dialogCustomMoney.dismiss();
                }
                mActivity.finish();

                break;
            case R.id.btn_custom_money:
                if (!mActivity.dialogCustomMoney.isShowing()) {
                    mActivity.dialogCustomMoney.show();
                }
                mActivity.setCoinNumSelected(-1);

                break;
            case R.id.btn_coin_num1:
                mActivity.setCoinNumSelected(1);

                break;
            case R.id.btn_coin_num2:
                mActivity.setCoinNumSelected(2);

                break;
            case R.id.btn_coin_num3:
                mActivity.setCoinNumSelected(3);

                break;
            case R.id.btn_coin_num4:
                mActivity.setCoinNumSelected(4);

                break;
            case R.id.btn_coin_num5:
                mActivity.setCoinNumSelected(5);

                break;
            case R.id.btn_coin_num6:
                mActivity.setCoinNumSelected(6);

                break;
            case R.id.btn_reward_ta:
                dismissCustomMoneyDialog();
                mActivity.showRewardConfirm();
                break;
            case R.id.btn_reward_custom_money:
                double coins = 0;
                try {
                    coins = Double.valueOf(mActivity.etCustomMoney.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(mActivity, "您输入的金额格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (coins <= 0) {
                    Toast.makeText(mActivity, "打赏金额必须大于0，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                mActivity.isCustomMoney = true;
                dismissCustomMoneyDialog();
                mActivity.showRewardConfirm();

                break;
            case R.id.btn_cancel_custom_money:
                dismissCustomMoneyDialog();

                break;
            case R.id.btn_cancel:
                dismissConfirmDialog();

                break;
            case R.id.btn_confirm:
                dismissConfirmDialog();
                mActivity.reward();

                break;
        }
    }

    private void dismissCustomMoneyDialog() {
        if (mActivity.dialogCustomMoney.isShowing()) {
            mActivity.dialogCustomMoney.dismiss();
        }
    }

    private void dismissConfirmDialog() {
        if (mActivity.dialogConfirmToReward.isShowing()) {
            mActivity.dialogConfirmToReward.dismiss();
        }
    }
}

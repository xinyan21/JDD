package com.jdd.android.jdd.controllers;

import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.SendCoinToFriendActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：发送金币给好友控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class SendCoinToFriendController extends ListenerControllerAdapter implements View.OnClickListener {
    private SendCoinToFriendActivity mActivity;

    public SendCoinToFriendController(SendCoinToFriendActivity activity) {
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
                mActivity.finish();

                break;
            case R.id.btn_cancel:
                dismissConfirmDialog();

                break;
            case R.id.btn_confirm:
                dismissConfirmDialog();
                mActivity.submit();

                break;
            case R.id.btn_confirm_transfer:
                mActivity.showRewardConfirm();

                break;
            case R.id.btn_query_user_name:
                mActivity.queryUserInfo();

                break;
        }
    }

    private void dismissConfirmDialog() {
        if (mActivity.dialogConfirmToReward.isShowing()) {
            mActivity.dialogConfirmToReward.dismiss();
        }
    }
}

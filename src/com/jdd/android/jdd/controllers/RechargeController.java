package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.RechargeActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：充值控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-04-27
 * @since 1.0
 */
public class RechargeController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private RechargeActivity mActivity;

    public RechargeController(RechargeActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof ListView) {
                    ((ListView) view).setOnItemClickListener(this);
                }

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
            case R.id.btn_coin_num7:
                mActivity.setCoinNumSelected(7);

                break;
            case R.id.btn_coin_num8:
                mActivity.setCoinNumSelected(8);

                break;
            case R.id.btn_coin_num9:
                mActivity.setCoinNumSelected(9);

                break;
            case R.id.btn_pay:
                mActivity.confirmPay();

                break;
            case R.id.btn_cancel:
                if (mActivity.dialogConfirm.isShowing()) {
                    mActivity.dialogConfirm.dismiss();
                }

                break;
            case R.id.btn_confirm:
                if (mActivity.isConfirmPayDialog) {
                    mActivity.createOrder();
                    mActivity.dialogConfirm.dismiss();
                } else {
                    mActivity.queryPay();
                    mActivity.dialogConfirm.dismiss();
                }

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}

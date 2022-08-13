package com.jdd.android.jdd.controllers;

import android.view.View;
import com.jdd.android.jdd.R;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：涨幅榜监听器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-17
 * @since 1.0
 */
@Deprecated
public class RiseListController extends ListenerControllerAdapter implements View.OnClickListener {
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
            case R.id.tv_header_handover:

                break;
            case R.id.tv_header_negotiable_market_capitalization:


                break;
            case R.id.tv_header_now:


                break;
            case R.id.tv_header_rise_speed:


                break;
            case R.id.tv_header_quantity_relative:


                break;
            default:
                break;
        }
    }
}

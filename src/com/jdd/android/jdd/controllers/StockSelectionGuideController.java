package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.ui.IntelligenceDetailActivity;
import com.jdd.android.jdd.ui.StockSelectionGuideActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;


/**
 * 描述：选股界面监听器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-17
 * @since 1.0
 */
@Deprecated
public class StockSelectionGuideController extends ListenerControllerAdapter implements AdapterView.OnItemClickListener {
    private StockSelectionGuideActivity mActivity;

    public StockSelectionGuideController(StockSelectionGuideActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        if (view instanceof ListView) {
            ((ListView) view).setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mActivity.startActivity(new Intent(mActivity, IntelligenceDetailActivity.class));
    }
}

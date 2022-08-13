package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.IntelligencesIndexAdapter;
import com.jdd.android.jdd.controllers.StockSelectionGuideController;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选股指南，雇佣情报的首页
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-05
 * @since 1.0
 */
public class StockSelectionGuideActivity extends TKActivity implements IModule {
    private TextView mTitle;
    private ImageButton mBack;
    private ListView mStockSelectionIntelligences;
    private IntelligencesIndexAdapter mIntelligencesIndexAdapter;
    private List<IntelligenceEntity> mIntelligenceEntities;
    private StockSelectionGuideController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_selection_guide);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mStockSelectionIntelligences = (ListView) findViewById(R.id.lv_intelligences);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = (ImageButton) findViewById(R.id.ibtn_back);
    }

    @Override
    protected void setListeners() {
        mController = new StockSelectionGuideController(this);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mStockSelectionIntelligences, mController);
    }

    @Override
    protected void initData() {
        mIntelligenceEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mIntelligenceEntities.add(new IntelligenceEntity());
        }
        mIntelligencesIndexAdapter = new IntelligencesIndexAdapter(this, mIntelligenceEntities);
    }

    @Override
    protected void initViews() {
        mStockSelectionIntelligences.setAdapter(mIntelligencesIndexAdapter);
        mTitle.setText("雇佣情报");
        mBack.setVisibility(View.GONE);
    }

    @Override
    public boolean init(IAppContext iAppContext) {
        return false;
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
}

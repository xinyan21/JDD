package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.ui.IntelligenceDetailActivity;
import com.jdd.android.jdd.ui.MyIntelligencesActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：我的情报控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class MyIntelligencesController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    public static final int ON_PULL_REFRESH = 88888;
    private MyIntelligencesActivity mActivity;

    public MyIntelligencesController(MyIntelligencesActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof PullToRefreshListView) {
                    ((PullToRefreshListView) view).setOnItemClickListener(this);
                }

                break;
            case ON_PULL_REFRESH:
                if (view instanceof PullToRefreshListView) {
                    ((PullToRefreshListView) view).setOnRefreshListener(this);
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
            case R.id.ibtn_search:
                mActivity.searchKey = mActivity.etSearchKey.getText().toString();
                mActivity.currentSearchPage = 1;
                mActivity.switchAdapter();
                mActivity.queryMyIntels(false);

                break;
            case R.id.btn_my_published_intels:
                mActivity.btnIMyBoughtIntels.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineMyBoughtIntels.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnMyPublishedIntels.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineMyPublishedIntels.setBackgroundResource(R.color.transparent);
                mActivity.currentIntelType = MyIntelligencesActivity.INTEL_ALL;
                mActivity.switchAdapter();
                mActivity.queryMyIntels(false);

                break;
            case R.id.btn_my_bought_intels:
                mActivity.btnIMyBoughtIntels.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineMyBoughtIntels.setBackgroundResource(R.color.transparent);
                mActivity.btnMyPublishedIntels.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineMyPublishedIntels.setBackgroundResource(R.color.menu_text_orange);
                mActivity.currentIntelType = MyIntelligencesActivity.INTEL_MY_BOUGHT;
                mActivity.switchAdapter();
                mActivity.queryMyIntels(false);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IntelligenceEntity intel = (IntelligenceEntity)
                mActivity.allIntelEntities.get(position - 1);
        Intent intelIntent = new Intent(mActivity, IntelligenceDetailActivity.class);
        intelIntent.putExtra(IntelligenceDetailActivity.KEY_INTENT, intel);
        mActivity.startActivity(intelIntent);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mActivity.queryMyIntels(true);
    }
}

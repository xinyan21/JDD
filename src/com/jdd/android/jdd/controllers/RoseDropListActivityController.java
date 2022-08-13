package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.RoseDropRankAdapter;
import com.jdd.android.jdd.constants.function.RankFunction;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.RoseDropListActivity;
import com.jdd.android.jdd.ui.StockDetailsActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：涨跌幅榜activity控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-04-02
 * @last_edit 2016-04-02
 * @since 1.0
 */
public class RoseDropListActivityController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    private RoseDropListActivity mActivity;

    public RoseDropListActivityController(RoseDropListActivity activity) {
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
            case ThinkTankController.ON_PULL_REFRESH:
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
            case R.id.tv_change_percent:
                if (RankFunction.RANK_DIRECTION_DOWN.equals(mActivity.rankDirection)) {
                    mActivity.rankDirection = RankFunction.RANK_DIRECTION_UP;
                    mActivity.tvChangePercent.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_orange, 0
                    );
                } else {
                    mActivity.rankDirection = RankFunction.RANK_DIRECTION_DOWN;
                    mActivity.tvChangePercent.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_orange, 0
                    );
                }
                mActivity.stocks.clear();
                mActivity.currentPage = 1;
                mActivity.queryStocks();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StockEntity entity = (StockEntity) parent.getAdapter().getItem(position);
        Intent intent = new Intent(mActivity, StockDetailsActivity.class);
        intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, entity);
        mActivity.startActivity(intent);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mActivity.currentPage += 1;
        mActivity.queryStocks();
    }
}

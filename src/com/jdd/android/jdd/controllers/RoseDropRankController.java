package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.RoseDropRankAdapter;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.RoseDropListActivity;
import com.jdd.android.jdd.ui.RoseDropRankListFragment;
import com.jdd.android.jdd.ui.StockDetailsActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：涨跌排名模板控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-10
 * @last_edit 2016-01-10
 * @since 1.0
 */
public class RoseDropRankController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private RoseDropRankListFragment mFragment;

    public RoseDropRankController(RoseDropRankListFragment fragment) {
        mFragment = fragment;
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
            case R.id.btn_more_rose:
                Intent moreRoseIntent = new Intent(mFragment.getActivity(), RoseDropListActivity.class);
                moreRoseIntent.putExtra(RoseDropListActivity.KEY_DATA_KEY, mFragment.roseDataKey);
                moreRoseIntent.putExtra(
                        RoseDropListActivity.KEY_LIST_TYPE, RoseDropListActivity.LIST_TYPE_ROSE
                );
                mFragment.getActivity().startActivity(moreRoseIntent);

                break;
            case R.id.btn_more_drop:
                Intent moreDropIntent = new Intent(mFragment.getActivity(), RoseDropListActivity.class);
                moreDropIntent.putExtra(RoseDropListActivity.KEY_DATA_KEY, mFragment.dropDataKey);
                moreDropIntent.putExtra(
                        RoseDropListActivity.KEY_LIST_TYPE, RoseDropListActivity.LIST_TYPE_DROP
                );
                mFragment.getActivity().startActivity(moreDropIntent);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RoseDropRankAdapter adapter = (RoseDropRankAdapter) parent.getAdapter();
        StockEntity entity = (StockEntity) adapter.getItem(position);
        Intent intent = new Intent(mFragment.getActivity(), StockDetailsActivity.class);
        intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, entity);
        mFragment.startActivity(intent);
    }
}

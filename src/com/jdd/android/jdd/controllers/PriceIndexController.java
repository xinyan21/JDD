package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.RoseDropRankAdapter;
import com.jdd.android.jdd.adapters.SelfSelectionStocksAdapter;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.EditSelfSelectionsActivity;
import com.jdd.android.jdd.ui.PriceIndexActivity;
import com.jdd.android.jdd.ui.SearchActivity;
import com.jdd.android.jdd.ui.StockDetailsActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：新版行情首页控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-10-01
 * @since 1.0
 */
public class PriceIndexController extends ListenerControllerAdapter
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PriceIndexActivity mActivity;

    public PriceIndexController(PriceIndexActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemClickListener(this);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_search:
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));

                break;
            case R.id.btn_self_selection_stocks:
                if (mActivity.currentFragment == R.id.btn_self_selection_stocks) {
                    return;
                }
                mActivity.btnEditSelfSelection.setVisibility(View.VISIBLE);
                mActivity.viewFlipper.showPrevious();
                mActivity.btnRoseDropList.setTextColor(mActivity.getResources().getColor(R.color.text_black));
                mActivity.btnSelfSelectionStocks.setTextColor(mActivity.getResources().getColor(R.color.text_red));
                mActivity.selfSelectionStocksBoldLine.setVisibility(View.VISIBLE);
                mActivity.roseDropListBoldLine.setVisibility(View.GONE);
                mActivity.selfSelectionFragment.onResume();
                mActivity.currentFragment = R.id.btn_self_selection_stocks;

                break;
            case R.id.btn_rose_drop_list:
                if (mActivity.currentFragment == R.id.btn_rose_drop_list) {
                    return;
                }
                mActivity.btnEditSelfSelection.setVisibility(View.INVISIBLE);
                mActivity.viewFlipper.showNext();
                mActivity.btnRoseDropList.setTextColor(mActivity.getResources().getColor(R.color.text_red));
                mActivity.btnSelfSelectionStocks.setTextColor(mActivity.getResources().getColor(R.color.text_black));
                mActivity.selfSelectionStocksBoldLine.setVisibility(View.GONE);
                mActivity.roseDropListBoldLine.setVisibility(View.VISIBLE);
                mActivity.roseDropRankFragment.onResume();
                mActivity.currentFragment = R.id.btn_rose_drop_list;

                break;
            case R.id.btn_edit_self_selection:
                mActivity.startActivity(new Intent(mActivity, EditSelfSelectionsActivity.class));

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StockEntity entity;
        Intent intent;
        switch (parent.getId()) {
            case R.id.lv_self_selection_stocks:
                entity = (StockEntity) parent.getAdapter().getItem(position);
                intent = new Intent(mActivity, StockDetailsActivity.class);
                intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, entity);
                mActivity.startActivity(intent);

                break;
            case R.id.gv_index:
                entity = (StockEntity) parent.getAdapter().getItem(position);
                intent = new Intent(mActivity, StockDetailsActivity.class);
                intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, entity);
                mActivity.startActivity(intent);

                break;
        }
    }
}

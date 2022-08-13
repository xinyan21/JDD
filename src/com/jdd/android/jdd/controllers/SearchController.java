package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.ExperienceDetailActivity;
import com.jdd.android.jdd.ui.IntelligenceDetailActivity;
import com.jdd.android.jdd.ui.SearchActivity;
import com.jdd.android.jdd.ui.StockDetailsActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：搜索控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class SearchController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    public static final int ON_TEXT_CHANGE = 8888;
    private SearchActivity mActivity;

    public SearchController(SearchActivity activity) {
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
            case R.id.ibtn_search:
                mActivity.doSearch();

                break;
            case R.id.btn_sort_by_price:
                mActivity.btnPrice.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLinePrice.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnIntelligences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineIntels.setBackgroundResource(R.color.transparent);
                mActivity.btnExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineExperiences.setBackgroundResource(R.color.transparent);
                mActivity.btnCourses.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineCourses.setBackgroundResource(R.color.transparent);

                mActivity.setSelectedState(SearchActivity.CURRENT_SEARCH_TYPE_PRICE);

                break;
            case R.id.btn_intelligences:
                mActivity.btnPrice.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLinePrice.setBackgroundResource(R.color.transparent);
                mActivity.btnIntelligences.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineIntels.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineExperiences.setBackgroundResource(R.color.transparent);
                mActivity.btnCourses.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineCourses.setBackgroundResource(R.color.transparent);

                mActivity.setSelectedState(SearchActivity.CURRENT_SEARCH_TYPE_INTEL);

                break;
            case R.id.btn_experiences_study:
                mActivity.btnPrice.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLinePrice.setBackgroundResource(R.color.transparent);
                mActivity.btnIntelligences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineIntels.setBackgroundResource(R.color.transparent);
                mActivity.btnExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineExperiences.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnCourses.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineCourses.setBackgroundResource(R.color.transparent);

                mActivity.setSelectedState(SearchActivity.CURRENT_SEARCH_TYPE_EXPERIENCE);

                break;
            case R.id.btn_courses:
                mActivity.btnPrice.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLinePrice.setBackgroundResource(R.color.transparent);
                mActivity.btnIntelligences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineIntels.setBackgroundResource(R.color.transparent);
                mActivity.btnExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineExperiences.setBackgroundResource(R.color.transparent);
                mActivity.btnCourses.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineCourses.setBackgroundResource(R.color.menu_text_orange);

                mActivity.setSelectedState(SearchActivity.CURRENT_SEARCH_TYPE_COURSE);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= 1;
        switch (mActivity.currentSearchType) {
            case SearchActivity.CURRENT_SEARCH_TYPE_COURSE:


                break;
            case SearchActivity.CURRENT_SEARCH_TYPE_EXPERIENCE:
                Intent expIntent = new Intent(mActivity, ExperienceDetailActivity.class);
                expIntent.putExtra(
                        ExperienceDetailActivity.KEY_INTENT,
                        mActivity.experienceEntities.get(position)
                );
                mActivity.startActivity(expIntent);

                break;
            case SearchActivity.CURRENT_SEARCH_TYPE_INTEL:
                Intent intelIntent = new Intent(mActivity, IntelligenceDetailActivity.class);
                intelIntent.putExtra(
                        IntelligenceDetailActivity.KEY_INTENT,
                        mActivity.intelligenceEntities.get(position)
                );
                mActivity.startActivity(intelIntent);

                break;
            case SearchActivity.CURRENT_SEARCH_TYPE_PRICE:
                StockEntity entity = mActivity.searchStockEntities.get(position);
                Intent intent = new Intent(mActivity, StockDetailsActivity.class);
                intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, entity);
                mActivity.startActivity(intent);

                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        switch (mActivity.currentSearchType) {
            case SearchActivity.CURRENT_SEARCH_TYPE_COURSE:

                break;
            case SearchActivity.CURRENT_SEARCH_TYPE_EXPERIENCE:
                mActivity.currentExpPage += 1;
                mActivity.doSearch();

                break;
            case SearchActivity.CURRENT_SEARCH_TYPE_INTEL:
                mActivity.currentIntelPage += 1;
                mActivity.doSearch();

                break;
        }
    }
}

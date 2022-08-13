package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.FavoritesActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：收藏界面控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class FavoritesController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener,PullToRefreshBase.OnRefreshListener {
    public static final int ON_PULL_REFRESH = 88888;
    private FavoritesActivity mActivity;

    public FavoritesController(FavoritesActivity activity) {
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
            case R.id.btn_courses:
                mActivity.btnCourse.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.vBoldLineCourse.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnExperience.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.vBoldLineExperience.setBackgroundResource(R.color.transparent);
                mActivity.btnIntelligence.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.vBoldLineIntelligence.setBackgroundResource(R.color.transparent);

                mActivity.setSelectedState(FavoritesActivity.FAVORITE_COURSE);

                break;
            case R.id.btn_experiences_study:
                mActivity.btnCourse.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.vBoldLineCourse.setBackgroundResource(R.color.transparent);
                mActivity.btnExperience.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.vBoldLineExperience.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnIntelligence.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.vBoldLineIntelligence.setBackgroundResource(R.color.transparent);

                mActivity.setSelectedState(FavoritesActivity.FAVORITE_EXP);

                break;
            case R.id.btn_intelligences:
                mActivity.btnCourse.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.vBoldLineCourse.setBackgroundResource(R.color.transparent);
                mActivity.btnExperience.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.vBoldLineExperience.setBackgroundResource(R.color.transparent);
                mActivity.btnIntelligence.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.vBoldLineIntelligence.setBackgroundResource(R.color.menu_text_orange);

                mActivity.setSelectedState(FavoritesActivity.FAVORITE_INTEL);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mActivity.onItemClick(position);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mActivity.loadMore();
    }
}

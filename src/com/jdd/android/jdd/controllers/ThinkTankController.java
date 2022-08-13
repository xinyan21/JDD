package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.ui.ExperienceDetailActivity;
import com.jdd.android.jdd.ui.ExperienceFilterActivity;
import com.jdd.android.jdd.ui.SearchActivity;
import com.jdd.android.jdd.ui.ThinkTankIndexActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：智文首页控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class ThinkTankController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    public static final int ON_PULL_REFRESH = 88888;
    private ThinkTankIndexActivity mActivity;

    public ThinkTankController(ThinkTankIndexActivity activity) {
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
            case R.id.btn_latest_published:
                mActivity.btnLatestPublished.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_orange));
                mActivity.btnLatestSelected.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.setStrOrderName(QueryExperiencesFunction.ORDER_NAME_TIME);
                mActivity.queryArticles();

                break;
            case R.id.btn_latest_selected:
                mActivity.btnLatestPublished.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnLatestSelected.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_orange));
                mActivity.setStrOrderName(QueryExperiencesFunction.ORDER_NAME_RECOMMEND);
                mActivity.queryArticles();

                break;
            case R.id.ibtn_search:
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra(
                        SearchActivity.KEY_SEARCH_TYPE,
                        SearchActivity.CURRENT_SEARCH_TYPE_EXPERIENCE
                );
                intent.putExtra(
                        SearchActivity.KEY_SEARCH_KEY,
                        mActivity.etExperienceSearchKey.getText().toString()
                );
                mActivity.startActivity(intent);

                break;
            case R.id.btn_filter:
                mActivity.startActivityForResult(new Intent(mActivity, ExperienceFilterActivity.class), 1);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mActivity, ExperienceDetailActivity.class);
        intent.putExtra(
                ExperienceDetailActivity.KEY_INTENT, mActivity.articleEntities.get(position - 1));
        mActivity.startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        // Update the LastUpdatedLabel
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        mActivity.loadingType = ThinkTankIndexActivity.LOADING_TYPE_REFRESH;
        mActivity.currentPage = 1;
        mActivity.queryArticles();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mActivity.loadingType = ThinkTankIndexActivity.LOADING_TYPE_MORE;
        mActivity.currentPage += 1;
        mActivity.queryArticles();
    }
}

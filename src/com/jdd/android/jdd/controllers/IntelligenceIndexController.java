package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.ui.*;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：情报首页控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-05
 * @last_edit 2016-01-05
 * @since 1.0
 */
public class IntelligenceIndexController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    public static final int ON_PULL_REFRESH = 88888;
    private static final short KEY_DIRECTION = 1;
    private static final short DIRECTION_UP = 2;
    private static final short DIRECTION_DOWN = 3;

    private IntelligenceIndexActivity mActivity;
    short mLastGoodCmtDirection = DIRECTION_DOWN;
    short mLastTimeDirection = DIRECTION_DOWN;

    public IntelligenceIndexController(IntelligenceIndexActivity activity) {
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
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra(
                        SearchActivity.KEY_SEARCH_TYPE,
                        SearchActivity.CURRENT_SEARCH_TYPE_INTEL
                );
                intent.putExtra(
                        SearchActivity.KEY_SEARCH_KEY,
                        mActivity.etSearchKey.getText().toString()
                );
                mActivity.startActivity(intent);

                break;
            case R.id.btn_sort_by_good_comment:
                mActivity.setStrOrderName(QueryIntelligencesFunction.ORDER_NAME_TOP);
                if (DIRECTION_DOWN == mLastGoodCmtDirection) {
                    mActivity.btnGoodComments.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_orange, 0);
                    mLastGoodCmtDirection = DIRECTION_UP;
                    mActivity.setStrOrderValue(QueryIntelligencesFunction.ORDER_VALUE_UP);
                } else if (DIRECTION_UP == mLastGoodCmtDirection) {
                    mActivity.btnGoodComments.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_orange, 0);
                    mLastGoodCmtDirection = DIRECTION_DOWN;
                    mActivity.setStrOrderValue(QueryIntelligencesFunction.ORDER_VALUE_DOWN);
                }
                if (DIRECTION_UP == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_gray, 0);
                } else if (DIRECTION_DOWN == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_gray, 0);
                }
                mActivity.btnGoodComments.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_orange));
                mActivity.btnTime.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.queryArticles();

                break;
            case R.id.btn_sort_by_time:
                mActivity.setStrOrderName(QueryIntelligencesFunction.ORDER_NAME_TIME);
                if (DIRECTION_DOWN == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_orange, 0);
                    mLastTimeDirection = DIRECTION_UP;
                    mActivity.setStrOrderValue(QueryIntelligencesFunction.ORDER_VALUE_UP);
                } else if (DIRECTION_UP == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_orange, 0);
                    mLastTimeDirection = DIRECTION_DOWN;
                    mActivity.setStrOrderValue(QueryIntelligencesFunction.ORDER_VALUE_DOWN);
                }
                if (DIRECTION_UP == mLastGoodCmtDirection) {
                    mActivity.btnGoodComments.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_gray, 0);
                } else if (DIRECTION_DOWN == mLastGoodCmtDirection) {
                    mActivity.btnGoodComments.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_gray, 0);
                }
                mActivity.btnGoodComments.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnTime.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_orange));
                mActivity.queryArticles();

                break;
            case R.id.btn_filter:
                mActivity.startActivityForResult(new Intent(mActivity, IntelligenceFilterActivity.class), 1);

                break;
            case R.id.tv_only_charges:
                if (mActivity.cbOnlyCharges.isChecked()) {
                    mActivity.cbOnlyCharges.setChecked(false);
                } else {
                    mActivity.cbOnlyCharges.setChecked(true);
                }

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IntelligenceEntity intel = (IntelligenceEntity) mActivity.intelligenceEntities.get(position - 1);
        Intent intelIntent = new Intent(mActivity, IntelligenceDetailActivity.class);
        intelIntent.putExtra(IntelligenceDetailActivity.KEY_INTENT, intel);
        mActivity.startActivity(intelIntent);
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

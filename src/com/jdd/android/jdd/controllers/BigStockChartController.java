package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.BigStockChartActivity;
import com.jdd.android.jdd.ui.SearchActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：股票大图控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-15
 * @last_edit 2016-01-15
 * @since 1.0
 */
public class BigStockChartController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private BigStockChartActivity mActivity;

    public BigStockChartController(BigStockChartActivity activity) {
        mActivity = activity;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                mActivity.finish();

                break;
            case R.id.ibtn_search:
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));

                break;
            case R.id.ibtn_switch_chart:
                if (BigStockChartActivity.CURRENT_CHART_MINUTE_PRICE == mActivity.currentChart) {
                    mActivity.containerFlipper.showNext();
                    mActivity.currentChart = BigStockChartActivity.CURRENT_CHART_K_LINE;
                    mActivity.ibtnSwitchChart.setImageResource(R.drawable.ibtn_switch_to_minute_price);
                    mActivity.btnSwitchPeriod.setVisibility(View.VISIBLE);
                } else if (BigStockChartActivity.CURRENT_CHART_K_LINE == mActivity.currentChart) {
                    mActivity.containerFlipper.showPrevious();
                    mActivity.currentChart = BigStockChartActivity.CURRENT_CHART_MINUTE_PRICE;
                    mActivity.ibtnSwitchChart.setImageResource(R.drawable.ibtn_switch_to_kline);
                    mActivity.btnSwitchPeriod.setVisibility(View.GONE);
                }

                break;
            case R.id.btn_switch_period:
                if (View.VISIBLE == mActivity.periodSwitcher.getVisibility()) {
                    mActivity.periodSwitcher.setVisibility(View.GONE);
                } else {
                    mActivity.periodSwitcher.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_five_minute:
                mActivity.boldLineFiveMinute.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineFifteenMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineThirtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineSixtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineDayLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineWeekLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineMonthLine.setBackgroundResource(R.color.transparent);

                mActivity.onSwitchPeriodData(view.getId());

                break;
            case R.id.btn_fifteen_minute:
                mActivity.boldLineFiveMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineFifteenMinute.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineThirtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineSixtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineDayLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineWeekLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineMonthLine.setBackgroundResource(R.color.transparent);

                mActivity.onSwitchPeriodData(view.getId());

                break;
            case R.id.btn_thirty_minute:
                mActivity.boldLineFiveMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineFifteenMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineThirtyMinute.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineSixtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineDayLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineWeekLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineMonthLine.setBackgroundResource(R.color.transparent);

                mActivity.onSwitchPeriodData(view.getId());

                break;
            case R.id.btn_sixty_minute:
                mActivity.boldLineFiveMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineFifteenMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineThirtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineSixtyMinute.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineDayLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineWeekLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineMonthLine.setBackgroundResource(R.color.transparent);

                mActivity.onSwitchPeriodData(view.getId());

                break;
            case R.id.btn_day_line:
                mActivity.boldLineFiveMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineFifteenMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineThirtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineSixtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineDayLine.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineWeekLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineMonthLine.setBackgroundResource(R.color.transparent);

                mActivity.onSwitchPeriodData(view.getId());

                break;
            case R.id.btn_week_line:
                mActivity.boldLineFiveMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineFifteenMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineThirtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineSixtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineDayLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineWeekLine.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineMonthLine.setBackgroundResource(R.color.transparent);

                mActivity.onSwitchPeriodData(view.getId());

                break;
            case R.id.btn_month_line:
                mActivity.boldLineFiveMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineFifteenMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineThirtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineSixtyMinute.setBackgroundResource(R.color.transparent);
                mActivity.boldLineDayLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineWeekLine.setBackgroundResource(R.color.transparent);
                mActivity.boldLineMonthLine.setBackgroundResource(R.color.stock_red);

                mActivity.onSwitchPeriodData(view.getId());

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}

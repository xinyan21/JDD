package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.ui.BigStockChartActivity;
import com.jdd.android.jdd.ui.SearchActivity;
import com.jdd.android.jdd.ui.SelfSelectionStocksFragment;
import com.jdd.android.jdd.ui.StockDetailsActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

import java.util.Date;


/**
 * 描述：股票详情监听器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-17
 * @since 1.0
 */
public class StockDetailsController extends ListenerControllerAdapter implements
        View.OnClickListener, View.OnTouchListener {
    private StockDetailsActivity mActivity;
    //监测行情图双击事件变量定义
    private long mLastClickTime = 0;
    private boolean mIsDoubleClickTriggered = false;

    public StockDetailsController(StockDetailsActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_TOUCH:
                view.setOnTouchListener(this);
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
            case R.id.ibtn_show_more_stock_details:
                if (!mActivity.dialogMoreDetails.isShowing()) {
                    mActivity.dialogMoreDetails.show();
                }

                break;
            case R.id.ibtn_dialog_close:
                if (mActivity.dialogMoreDetails.isShowing()) {
                    mActivity.dialogMoreDetails.dismiss();
                }

                break;
            case R.id.btn_minute_price:
                if (mActivity.currentChartId == R.id.smp_minute_price) {
                    return;
                }
                mActivity.boldLineMinutePrice.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineLongShortTapeReading.setBackgroundResource(R.color.transparent);
                mActivity.boldLineExplanation.setBackgroundResource(R.color.transparent);
                if (mActivity.currentChartId != R.id.smp_minute_price) {
                    mActivity.containerFlipper.showPrevious();
                }
                if (mActivity.pwLongShortExplanation.isShowing()) {
                    mActivity.pwLongShortExplanation.dismiss();
                }
//                if (View.VISIBLE == mActivity.tvLongShortExplanation.getVisibility()) {
//                    mActivity.tvLongShortExplanation.setVisibility(View.GONE);
//                }
                mActivity.currentChartId = R.id.smp_minute_price;

                break;
            case R.id.btn_long_short_tape_reading:
                if (mActivity.currentChartId == R.id.klc_kline_small) {
                    return;
                }
                mActivity.boldLineMinutePrice.setBackgroundResource(R.color.transparent);
                mActivity.boldLineLongShortTapeReading.setBackgroundResource(R.color.stock_red);
                mActivity.boldLineExplanation.setBackgroundResource(R.color.transparent);
                if (mActivity.currentChartId == R.id.smp_minute_price) {
                    mActivity.containerFlipper.showNext();
                }
                if (mActivity.pwLongShortExplanation.isShowing()) {
                    mActivity.pwLongShortExplanation.dismiss();
                }
//                if (View.VISIBLE == mActivity.tvLongShortExplanation.getVisibility()) {
//                    mActivity.tvLongShortExplanation.setVisibility(View.GONE);
//                }
                mActivity.currentChartId = R.id.klc_kline_small;

                break;
            case R.id.btn_long_short_explanation:
                if (mActivity.currentChartId == R.id.btn_long_short_explanation) {
                    if (mActivity.pwLongShortExplanation.isShowing()) {
                        mActivity.pwLongShortExplanation.dismiss();
                    } else {
                        mActivity.showLongShortExplanation();
                    }
//                    if (View.VISIBLE == mActivity.tvLongShortExplanation.getVisibility()) {
//                        mActivity.tvLongShortExplanation.setVisibility(View.GONE);
//                    } else {
//                        mActivity.tvLongShortExplanation.setVisibility(View.VISIBLE);
//                    }
                    return;
                }
                mActivity.boldLineMinutePrice.setBackgroundResource(R.color.transparent);
                mActivity.boldLineLongShortTapeReading.setBackgroundResource(R.color.transparent);
                mActivity.boldLineExplanation.setBackgroundResource(R.color.stock_red);
                if (mActivity.currentChartId == R.id.smp_minute_price) {
                    mActivity.containerFlipper.showNext();
                }
                mActivity.currentChartId = R.id.btn_long_short_explanation;
                mActivity.showLongShortExplanation();
                mActivity.tvLongShortExplanation.setVisibility(View.VISIBLE);

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
            case R.id.ibtn_add_self_selection:
                //已存在自选，那么就是删除，否则就是添加
                if (null != mActivity.selfSelectionManager.query(mActivity.stockEntity)) {
                    mActivity.selfSelectionManager.delete(mActivity.stockEntity);
                    mActivity.ibtnAddToSelfSelections.setImageResource(R.drawable.ibtn_add_to_self_selection);
                    Toast.makeText(mActivity, "已删除自选", Toast.LENGTH_SHORT).show();
                } else {
                    //设置用户id后再保存
                    UserEntity loginUser = (UserEntity) mActivity.cache.getCacheItem(
                            CacheKey.KEY_CURRENT_LOGIN_USER_INFO
                    );
                    if (null != loginUser) {
                        mActivity.stockEntity.setUserId(loginUser.getUserId());
                    } else {
                        mActivity.stockEntity.setUserId(SelfSelectionStocksFragment.sDefaultUserID);
                    }
                    mActivity.selfSelectionManager.insert(mActivity.stockEntity);
                    mActivity.ibtnAddToSelfSelections.setImageResource(R.drawable.ibtn_delete_self_selection);
                    Toast.makeText(mActivity, "已添加自选", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            Intent intent = new Intent(mActivity, BigStockChartActivity.class);
            intent.putExtra(BigStockChartActivity.KEY_STOCK_ENTITY, mActivity.stockEntity);
            intent.putExtra(
                    BigStockChartActivity.KEY_MINUTE_PRICE_DATA, mActivity.minutePriceDataKey
            );
            intent.putExtra(BigStockChartActivity.KEY_CURRENT_PERIOD, mActivity.currentPeriod);
            if (R.id.smp_minute_price == mActivity.currentChartId) {
                intent.putExtra(
                        BigStockChartActivity.KEY_CURRENT_CHART,
                        BigStockChartActivity.CURRENT_CHART_MINUTE_PRICE);
            } else {
                intent.putExtra(
                        BigStockChartActivity.KEY_CURRENT_CHART,
                        BigStockChartActivity.CURRENT_CHART_K_LINE);
            }
            mActivity.startActivity(intent);
        }

        return true;
    }
}

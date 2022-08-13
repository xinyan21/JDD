package com.jdd.android.jdd.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

/**
 * 描述：股票详情里小股票图专用的ViewFlipper，主要是拦截触摸事件，用来处理双击
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-18
 * @last_edit 2016-01-18
 * @since 1.0
 */
public class StockChartViewFlipper extends ViewFlipper {
    public StockChartViewFlipper(Context context) {
        super(context);
    }

    public StockChartViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}

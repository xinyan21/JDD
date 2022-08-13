package com.jdd.android.jdd.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

/**
 * 描述：用来解决横屏K线点击事件问题，K线截取了事件处理，导致无法截取单击事件用来跳转到大图
 *
 * @author  徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-10-13
 * @since 1.0
 */
public class StockDetailsViewFlipper extends ViewFlipper {
    public StockDetailsViewFlipper(Context context) {
        super(context);
    }

    public StockDetailsViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;
    }
}

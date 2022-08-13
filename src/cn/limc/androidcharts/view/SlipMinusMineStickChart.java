/*
 * SlipMinusStickChart.java
 * Android-Charts
 *
 * Created by limc on 2014.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.limc.androidcharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import cn.limc.androidcharts.entity.IMeasurable;
import cn.limc.androidcharts.entity.IStickEntity;

/**
 * <p>
 * 描述：股票资金-最近5日主力增减仓
 * </p>
 * 
 * @author 叶青
 * @version 1.0
 * @corporation
 * @date 14-6-9
 */
public class SlipMinusMineStickChart extends SlipStickChart {
    /**
     * <p>
     * 日期字体大小
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_FONT_SIZE = 19;
    // 日期字体大小
    private int mDateFontSize = DEFAULT_LONGITUDE_FONT_SIZE;
    // 价格字体大小
    private int mPriceFontSize = DEFAULT_LONGITUDE_FONT_SIZE;

    private float lowYMarginTop;
    private float lowYMarginBottom;

    /*
     * (non-Javadoc)
     * @param context
     * @see cn.limc.cn.limc.androidcharts.view.GridChart#GridChart(Context)
     */
    public SlipMinusMineStickChart(Context context) {
        super(context);
    }

    /*
     * (non-Javadoc)
     * @param context
     * @param attrs
     * @param defStyle
     * @see cn.limc.cn.limc.androidcharts.view.GridChart#GridChart(Context,
     * AttributeSet, int)
     */
    public SlipMinusMineStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * (non-Javadoc)
     * @param context
     * @param attrs
     * @see cn.limc.cn.limc.androidcharts.view.GridChart#GridChart(Context,
     * AttributeSet)
     */
    public SlipMinusMineStickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void calcDataValueRange() {

        double maxValue = Integer.MIN_VALUE;
        double minValue = Integer.MAX_VALUE;

        IMeasurable first = this.stickData.get(0);
        // 第一个stick为停盘的情况
        if (first.getHigh() == 0 && first.getLow() == 0) {

        } else {
            maxValue = first.getHigh();
            minValue = first.getLow();
        }

        // 判断显示为方柱或显示为线条
        for (int i = 0; i < this.stickData.size(); i++) {
            IMeasurable stick = this.stickData.get(i);
            if (stick.getLow() < minValue) {
                minValue = stick.getLow();
            }

            if (stick.getHigh() > maxValue) {
                maxValue = stick.getHigh();
            }

        }

        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    @Override
    protected void calcValueRangePaddingZero() {
    }

    @Override
    protected void calcValueRangeFormatForAxis() {
    }

    /*
     * (non-Javadoc)
     * @param canvas
     * @see cn.limc.cn.limc.androidcharts.view.StickChart#drawSticks(Canvas)
     */
    @Override
    protected void drawSticks(Canvas canvas) {

        float stickWidth = getDataQuadrantPaddingWidth() / 5
                - stickSpacing;
        float stickX = getDataQuadrantPaddingStartX();

        Paint mPaintFill = new Paint();
        mPaintFill.setStyle(Style.FILL);
        mPaintFill.setColor(super.getStickFillColor());

        Paint mPaintBorder = new Paint();
        mPaintBorder.setStyle(Style.STROKE);
        // mPaintBorder.setStrokeWidth(1);
        mPaintBorder.setColor(super.getStickBorderColor());
        // 文字
        Paint mPaintPriceText = new Paint();
        mPaintPriceText.setAntiAlias(true);
        mPaintPriceText.setTextSize(mPriceFontSize);

        // 文字日期
        Paint mPaintDate = new Paint();
        mPaintDate.setColor(0Xff000000);
        mPaintDate.setTextSize(mDateFontSize);
        mPaintDate.setAntiAlias(true);
        // Line描画
        Paint bottomLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder.setStrokeWidth(2);
        bottomLinePaint.setColor(0xff2a2e33);
        canvas.drawLine(0,
                (float) ((maxValue / (maxValue - minValue) * getDataQuadrantPaddingHeight())
                + getDataQuadrantPaddingStartY()), getWidth(),
                (float) ((maxValue / (maxValue - minValue) * getDataQuadrantPaddingHeight())
                + getDataQuadrantPaddingStartY()), bottomLinePaint);

        if (null != stickData) {
            // display as stick or line
            for (int i = super.getDisplayFrom(); i < super.getDisplayFrom()
                    + super.getDisplayNumber(); i++) {
                IMeasurable entity = stickData.get(i);
                IStickEntity mIStickEntity = stickData.get(i);
                float highY = (float) ((1f - (entity.getHigh() - minValue)
                        / (maxValue - minValue))
                        * (getDataQuadrantPaddingHeight()) + getDataQuadrantPaddingStartY());
                float lowY = (float) ((1f - (entity.getLow() - minValue)
                        / (maxValue - minValue))
                        * (getDataQuadrantPaddingHeight()) + getDataQuadrantPaddingStartY());
                if (entity.getHigh() > 0) {
                    mPaintFill.setColor(0xffff4433);
                    mPaintPriceText.setColor(0xffff4433);
                    // 绘制柱状图
                    canvas.drawRect(stickX, highY, stickX + stickWidth, lowY - 0.8f,
                            mPaintFill);
                    // 绘制 价格——
                    canvas.drawText(String.valueOf(mIStickEntity.getHigh()), stickX, lowY
                            + lowYMarginTop,
                            mPaintPriceText);

                } else {
                    mPaintFill.setColor(0xff32a632);
                    mPaintPriceText.setColor(0xff32a632);
                    // 绘制柱状图
                    canvas.drawRect(stickX, highY + 1.0f, stickX + stickWidth, lowY,
                            mPaintFill);

                    // 绘制 价格——
                    canvas.drawText(String.valueOf(mIStickEntity.getLow()),
                            stickX, (float) ((maxValue
                                    / (maxValue - minValue) * getDataQuadrantPaddingHeight())
                                    + getDataQuadrantPaddingStartY() - lowYMarginBottom),
                            mPaintPriceText);
                }
                // 绘制边框
                canvas.drawRect(stickX, highY, stickX + stickWidth, lowY,
                        mPaintBorder);
                // 绘制日期
                canvas.drawText(String.valueOf(mIStickEntity.getDate()).substring(4, 6) + "-"
                        + String.valueOf(mIStickEntity.getDate()).substring(6, 8),
                        stickX,
                        getHeight(), mPaintDate);
                // next x
                stickX = stickX + stickSpacing + stickWidth;
            }
        }
    }

    public float getLowYMarginBottom() {
        return lowYMarginBottom;
    }

    public void setLowYMarginBottom(float lowYMarginBottom) {
        this.lowYMarginBottom = lowYMarginBottom;
    }

    public float getLowYMarginTop() {
        return lowYMarginTop;
    }

    public void setLowYMarginTop(float lowYMarginTop) {
        this.lowYMarginTop = lowYMarginTop;
    }

    public int getmDateFontSize() {
        return mDateFontSize;
    }

    public void setmDateFontSize(int mDateFontSize) {
        this.mDateFontSize = mDateFontSize;
    }

    public int getmPriceFontSize() {
        return mPriceFontSize;
    }

    public void setmPriceFontSize(int mPriceFontSize) {
        this.mPriceFontSize = mPriceFontSize;
    }
}

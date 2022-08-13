/*
 * GridChart.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
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

package com.jdd.android.jdd.views;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import cn.limc.androidcharts.view.BaseChart;
import com.jdd.android.jdd.interfaces.ITouchEventNotifyX;
import com.jdd.android.jdd.interfaces.ITouchEventResponseX;
import com.thinkive.android.app_engine.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * TwoXYGridChart是K线图的基类，它实现了基本的网格图表功能，这些功能将被它的继承类使用
 * </p>
 *
 * @author -徐鑫炎
 * @version v1.0
 */
public class TwoXYGridChartView extends BaseChart implements
        ITouchEventNotifyX, ITouchEventResponseX {

    public static final int AXIS_X_POSITION_BOTTOM = 1 << 0;
    @Deprecated
    public static final int AXIS_X_POSITION_TOP = 1 << 1; // Y轴在上面
    public static final int AXIS_Y_POSITION_LEFT = 1 << 2; // Y轴在左边
    public static final int AXIS_Y_POSITION_RIGHT = 1 << 3; // Y轴在右边
    /**
     * <p>
     * 默认背景色
     * </p>
     */
    public static final int DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    /**
     * <p>
     * 默认坐标轴X的显示颜色
     * </p>
     */
    public static final int DEFAULT_AXIS_X_COLOR = 0xff9ca49c;
    /**
     * <p>
     * 默认坐标轴Y的显示颜色
     * </p>
     */
    public static final int DEFAULT_AXIS_Y_COLOR = 0xff9ca49c;
    public static final float DEFAULT_AXIS_WIDTH = 1;
    public static final int DEFAULT_AXIS_X_POSITION = AXIS_X_POSITION_BOTTOM;
    public static final int DEFAULT_AXIS_Y_POSITION = AXIS_Y_POSITION_LEFT;
    /**
     * <p>
     * 默认网格经线的显示颜色
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_COLOR = 0xff9ca49c;
    /**
     * <p>
     * 默认网格纬线的显示颜色
     * </p>
     */
    public static final int DEFAULT_LAITUDE_COLOR = 0xff9ca49c;
    /**
     * <p>
     * 默认轴线左边距
     * </p>
     */
    @Deprecated
    public static final float DEFAULT_AXIS_MARGIN_LEFT = 42f;
    public static final float DEFAULT_AXIS_Y_TITLE_QUADRANT_WIDTH = 16f;
    /**
     * <p>
     * 默认轴线下边距
     * </p>
     */
    @Deprecated
    public static final float DEFAULT_AXIS_MARGIN_BOTTOM = 16f;
    public static final float DEFAULT_AXIS_X_TITLE_QUADRANT_HEIGHT = 16f;
    /**
     * <p>
     * 默认轴线上边距
     * </p>
     */
    @Deprecated
    public static final float DEFAULT_AXIS_MARGIN_TOP = 5f;
    public static final float DEFAULT_DATA_QUADRANT_PADDING_TOP = 8f;
    public static final float DEFAULT_DATA_QUADRANT_PADDING_BOTTOM = 8f;
    /**
     * <p>
     * 轴线右边距
     * </p>
     */
    @Deprecated
    public static final float DEFAULT_AXIS_MARGIN_RIGHT = 5f;
    public static final float DEFAULT_DATA_QUADRANT_PADDING_LEFT = -1f;
    public static final float DEFAULT_DATA_QUADRANT_PADDING_RIGHT = -20f;
    /**
     * <p>
     * 网格纬线的数量
     * </p>
     */
    public static final int DEFAULT_LATITUDE_NUM = 2;
    /**
     * <p>
     * 网格经线的数量
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_NUM = 3;
    /**
     * <p>
     * 默认经线是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_LONGITUDE = Boolean.TRUE;
    /**
     * <p>
     * 默认经线是否显示为虚线
     * </p>
     */
    public static final boolean DEFAULT_DASH_LONGITUDE = Boolean.TRUE;
    /**
     * <p>
     * 纬线是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_LATITUDE = Boolean.TRUE;
    /**
     * <p>
     * 纬线是否显示为虚线
     * </p>
     */
    public static final boolean DEFAULT_DASH_LATITUDE = Boolean.TRUE;
    /**
     * <p>
     * X轴上的标题是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_LONGITUDE_TITLE = Boolean.TRUE;
    /**
     * <p>
     * 默认Y轴上的标题是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_LATITUDE_TITLE = Boolean.TRUE;
    /**
     * <p>
     * 默认控件是否显示边框
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_BORDER = Boolean.TRUE;
    /**
     * <p>
     * 默认经线刻度字体颜色
     * </p>
     */
    public static final int DEFAULT_BORDER_COLOR = 0xff9ca49c;
    public static final float DEFAULT_BORDER_WIDTH = 0.8f;
    /**
     * <p>
     * 经线刻度字体颜色
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_FONT_COLOR = Color.WHITE;
    /**
     * <p>
     * 经线刻度字体大小--X轴
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_FONT_SIZE = 22;
    /**
     * <p>
     * 纬线刻度字体颜色
     * </p>
     */
    public static final int DEFAULT_LATITUDE_FONT_COLOR = Color.RED;
    /**
     * <p>
     * 默认纬线刻度字体大小--Y轴
     * </p>
     */
    public static final int DEFAULT_LATITUDE_FONT_SIZE = 22;

    public static final int DEFAULT_CROSS_LINES_FONT_COLOR = Color.CYAN;
    /**
     * <p>
     * 默认Y轴标题最大文字长度
     * </p>
     */
    public static final int DEFAULT_LATITUDE_MAX_TITLE_LENGTH = 5;
    /**
     * <p>
     * 默认虚线效果
     * </p>
     */
    public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
            new float[]{3, 3, 3, 3}, 1);
    /**
     * <p>
     * 默认在控件被点击时，显示十字竖线线
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_CROSS_X_ON_TOUCH = true;
    /**
     * <p>
     * 默认在控件被点击时，显示十字横线线
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH = true;
    protected int axisXPosition = DEFAULT_AXIS_X_POSITION;
    protected int axisYPosition = DEFAULT_AXIS_Y_POSITION;
    /**
     * <p>
     * 轴线左边距
     * </p>
     */
    protected float axisYTitleQuadrantWidth = DEFAULT_AXIS_Y_TITLE_QUADRANT_WIDTH;
    /**
     * <p>
     * 轴线下边距
     * </p>
     */
    protected float axisXTitleQuadrantHeight = DEFAULT_AXIS_X_TITLE_QUADRANT_HEIGHT;
    /**
     * <p>
     * 轴线上边距
     * </p>
     */
    protected float dataQuadrantPaddingTop = DEFAULT_DATA_QUADRANT_PADDING_TOP;
    /**
     * <p>
     * 轴线右边距
     * </p>
     */
    protected float dataQuadrantPaddingLeft = DEFAULT_DATA_QUADRANT_PADDING_LEFT;
    protected float dataQuadrantPaddingBottom = DEFAULT_DATA_QUADRANT_PADDING_BOTTOM;
    /**
     * <p>
     * 轴线右边距
     * </p>
     */
    protected float dataQuadrantPaddingRight = DEFAULT_DATA_QUADRANT_PADDING_RIGHT;
    /**
     * <p>
     * 网格纬线的数量
     * </p>
     */
    protected int latitudeNum = DEFAULT_LATITUDE_NUM;
    /**
     * <p>
     * 网格经线的数量
     * </p>
     */
    protected int longitudeNum = DEFAULT_LONGITUDE_NUM;
    protected float borderWidth = DEFAULT_BORDER_WIDTH;
    /**
     * <p>
     * 单点触控的选中点
     * </p>
     */
    protected PointF touchPoint;
    /**
     * <p>
     * 坐标轴X的显示颜色
     * </p>
     */
    private int axisXColor = DEFAULT_AXIS_X_COLOR;
    /**
     * <p>
     * 坐标轴Y的显示颜色
     * </p>
     */
    private int axisYColor = DEFAULT_AXIS_Y_COLOR;
    private float axisWidth = DEFAULT_AXIS_WIDTH;
    /**
     * <p>
     * 网格经线的显示颜色
     * </p>
     */
    private int longitudeColor = DEFAULT_LONGITUDE_COLOR;
    /**
     * <p>
     * 网格纬线的显示颜色
     * </p>
     */
    private int latitudeColor = DEFAULT_LAITUDE_COLOR;
    /**
     * <p>
     * X轴上的标题是否显示
     * </p>
     */
    private boolean displayLongitudeTitle = DEFAULT_DISPLAY_LONGITUDE_TITLE;
    /**
     * <p>
     * Y轴上的标题是否显示
     * </p>
     */
    private boolean displayLatitudeTitle = DEFAULT_DISPLAY_LATITUDE_TITLE;
    /**
     * <p>
     * 经线是否显示
     * </p>
     */
    private boolean displayLongitude = DEFAULT_DISPLAY_LONGITUDE;
    /**
     * <p>
     * 经线是否显示为虚线
     * </p>
     */
    private boolean dashLongitude = DEFAULT_DASH_LONGITUDE;
    /**
     * <p>
     * 纬线是否显示
     * </p>
     */
    private boolean displayLatitude = DEFAULT_DISPLAY_LATITUDE;
    /**
     * <p>
     * 纬线是否显示为虚线
     * </p>
     */
    private boolean dashLatitude = DEFAULT_DASH_LATITUDE;
    /**
     * <p>
     * 虚线效果
     * </p>
     */
    private PathEffect dashEffect = DEFAULT_DASH_EFFECT;
    /**
     * <p>
     * 控件是否显示边框
     * </p>
     */
    private boolean displayBorder = DEFAULT_DISPLAY_BORDER;
    /**
     * -是否响应触摸事件
     */
    private boolean isResponseTouchEvent = false;
    /**
     * <p>
     * 图边框的颜色
     * </p>
     */
    private int borderColor = DEFAULT_BORDER_COLOR;
    /**
     * <p>
     * 经线刻度字体颜色
     * </p>
     */
    private int longitudeFontColor = DEFAULT_LONGITUDE_FONT_COLOR;
    /**
     * <p>
     * 经线刻度字体大小
     * </p>
     */
    private int longitudeFontSize = DEFAULT_LONGITUDE_FONT_SIZE;
    /**
     * <p>
     * 纬线刻度字体颜色
     * </p>
     */
    private int latitudeFontColor = DEFAULT_LATITUDE_FONT_COLOR;
    /**
     * <p>
     * 纬线刻度字体大小
     * </p>
     */
    private int latitudeFontSize = DEFAULT_LATITUDE_FONT_SIZE;
    /**
     * <p>
     * 十字交叉线颜色
     * </p>
     */
    private int crossLinesColor = DEFAULT_CROSS_LINES_COLOR;
    /**
     * <p>
     * 十字交叉线坐标轴字体颜色
     * </p>
     */
    private int crossLinesFontColor = DEFAULT_CROSS_LINES_FONT_COLOR;
    /**
     * <p>
     * -成交量X轴标题数组
     * </p>
     */
    private List<String> volLongitudeTitles;
    /**
     * <p>
     * -成交量Y轴标题数组
     * </p>
     */
    private List<String> volLatitudeTitles;
    /**
     * <p>
     * -最底部多空士气X轴标题数组
     * </p>
     */
    private List<String> battleMoraleLongitudeTitles;
    /**
     * <p>
     * -最底部多空士气Y轴标题数组
     * </p>
     */
    private List<String> battleMoraleLatitudeTitles;
    /**
     * <p>
     * -烛蜡图X轴标题数组
     * </p>
     */
    private List<String> candleLongitudeTitles;
    /**
     * <p>
     * -烛蜡图Y轴标题数组
     * </p>
     */
    private List<String> candleLatitudeTitles;
    /**
     * <p>
     * Y轴标题最大文字长度
     * </p>
     */
    private int latitudeMaxTitleLength = DEFAULT_LATITUDE_MAX_TITLE_LENGTH;
    /**
     * <p>
     * 在控件被点击时，显示十字竖线线
     * </p>
     */
    private boolean displayCrossXOnTouch = DEFAULT_DISPLAY_CROSS_X_ON_TOUCH;
    /**
     * <p>
     * 在控件被点击时，显示十字横线线
     * </p>
     */
    private boolean displayCrossYOnTouch = DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH;
    /**
     * <p>
     * 单点触控的选中点的X
     * </p>
     */
    private float clickPostX;
    /**
     * <p>
     * 单点触控的选中点的Y
     * </p>
     */
    private float clickPostY;
    /**
     * <p>
     * 事件通知对象列表
     * </p>
     */
    private List<ITouchEventResponseX> notifyList;

    /**
     * 默认柱线的条数
     */
    private int defaultNumber = 100;
    /**
     * 实际柱线的条数
     */
    private int displayNumber = 100;

    /**
     * <p>
     * 十字交叉线颜色
     * </p>
     */
    public static final int DEFAULT_CROSS_LINES_COLOR = Color.BLACK;

    /*
     * (non-Javadoc)
     *
     * @param context
     *
     * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context)
     */
    public TwoXYGridChartView(Context context) {
        super(context);
    }

    /*
     * (non-Javadoc)
     *
     * @param context
     *
     * @param attrs
     *
     * @param defStyle
     *
     * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
     * AttributeSet, int)
     */
    public TwoXYGridChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * (non-Javadoc)
     *
     * @param context
     *
     * @param attrs
     *
     * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
     * AttributeSet)
     */
    public TwoXYGridChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * (non-Javadoc) <p>Called when is going to draw this chart<p>
     * <p>チャートを書く前、メソッドを呼ぶ<p> <p>绘制图表时调用<p>
     *
     * @param canvas
     *
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setAxisYTitleQuadrantWidth();
        //  绘制下半部表格
        drawTopXAxis(canvas);
        drawYAxis(canvas);

        drawTopLongitudeLine(canvas);
        drawTopLongitudeTitle(canvas);
        drawTopLatitudeLine(canvas);
        drawTopLatitudeTitle(canvas);

        // 绘制上半部表格
        drawVolXAxis(canvas);
        drawBottomYAxis(canvas);

        // 绘制底部的纬度标题
        drawVolLatitudeTitle(canvas);
        drawBattleMoraleLatitudeTitle(canvas);

        // 触摸时是否显示竖直线条以标识选中的K线值
        if (isDisplayCrossXOnTouch()) {
            drawCrossLine(canvas);
        }
    }

    /*
     * (non-Javadoc) <p>Called when chart is touched<p>
     * <p>チャートをタッチしたら、メソッドを呼ぶ<p> <p>图表点击时调用<p>
     *
     * @param event
     *
     * @see android.view.View#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isResponseTouchEvent) {
            return false;
        }

        if (event.getX() < getDataQuadrantPaddingStartX()
                || event.getX() > getDataQuadrantPaddingEndX()) {
            return false;
        }

        if (event.getY() < getDataQuadrantPaddingStartY()
                || event.getY() > getDataQuadrantPaddingEndY()) {
            return false;
        }
        refreshVerticalLine(event);

        return true;
    }

    /**
     * -刷新用户选中的竖线
     *
     * @param event
     */
    protected void refreshVerticalLine(MotionEvent event) {
        clickPostX = event.getX();
        clickPostY = event.getY();

        PointF point = new PointF(clickPostX, clickPostY);
        touchPoint = point;

        // redraw
        super.invalidate();

        // do notify
        // notifyEventAll(this);
    }

    /**
     * <p>
     * draw some text with border
     * </p>
     * <p>
     * 文字を書く、枠あり
     * </p>
     * <p>
     * 绘制一段文本，并增加外框
     * </p>
     *
     * @param ptStart  <p>
     *                 start point
     *                 </p>
     *                 <p>
     *                 開始ポイント
     *                 </p>
     *                 <p>
     *                 开始点
     *                 </p>
     * @param ptEnd    <p>
     *                 end point
     *                 </p>
     *                 <p>
     *                 結束ポイント
     *                 </p>
     *                 <p>
     *                 结束点
     *                 </p>
     * @param content  <p>
     *                 text content
     *                 </p>
     *                 <p>
     *                 文字内容
     *                 </p>
     *                 <p>
     *                 文字内容
     *                 </p>
     * @param fontSize <p>
     *                 font size
     *                 </p>
     *                 <p>
     *                 文字フォントサイズ
     *                 </p>
     *                 <p>
     *                 字体大小
     *                 </p>
     * @param canvas
     */
    private void drawAlphaTextBox(PointF ptStart, PointF ptEnd, String content,
                                  int fontSize, Canvas canvas) {

        Paint mPaintBox = new Paint();
        mPaintBox.setColor(Color.WHITE);
        mPaintBox.setAlpha(80);
        mPaintBox.setStyle(Style.FILL);

        Paint mPaintBoxLine = new Paint();
        mPaintBoxLine.setColor(crossLinesColor);
        mPaintBoxLine.setAntiAlias(true);
        mPaintBoxLine.setTextSize(fontSize);

        // draw a rectangle
        canvas.drawRect(ptStart.x, ptStart.y, ptEnd.x, ptEnd.y, mPaintBox);

        // draw a rectangle' border
        canvas.drawLine(ptStart.x, ptStart.y, ptStart.x, ptEnd.y, mPaintBoxLine);
        canvas.drawLine(ptStart.x, ptEnd.y, ptEnd.x, ptEnd.y, mPaintBoxLine);
        canvas.drawLine(ptEnd.x, ptEnd.y, ptEnd.x, ptStart.y, mPaintBoxLine);
        canvas.drawLine(ptEnd.x, ptStart.y, ptStart.x, ptStart.y, mPaintBoxLine);

        mPaintBoxLine.setColor(crossLinesFontColor);
        // draw text
        canvas.drawText(content, ptStart.x, ptStart.y + fontSize, mPaintBoxLine);
    }

    protected float getDataQuadrantWidth() {
        return super.getWidth() - axisYTitleQuadrantWidth - axisWidth * 2;
    }

    protected float getDataQuadrantHeight() {
        return super.getHeight() - axisXTitleQuadrantHeight - 2 * borderWidth
                - axisWidth;
    }

    protected float getCandleDataQuadrantHeight() {
        return super.getHeight() * 1 / 2 - axisXTitleQuadrantHeight - 2
                * borderWidth - axisWidth;
    }

    protected float getStickDataQuadrantHeight() {
        return super.getHeight() * 1 / 4 - axisXTitleQuadrantHeight - 2
                * borderWidth - axisWidth;
    }

    protected float getDataQuadrantStartX() {
        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            return borderWidth + axisYTitleQuadrantWidth + axisWidth;
        } else {
            return borderWidth;
        }
    }

    /**
     * -获取柱状图数据X轴的起始位置
     *
     * @return
     */
    protected float getStickDataQuadrantStartX() {
        return axisYTitleQuadrantWidth + axisWidth;
    }

    public float getDataQuadrantPaddingStartX() {
        return getDataQuadrantStartX();
    }

    protected float getStickDataQuadrantPaddingStartX() {
        setAxisYTitleQuadrantWidth();
        return getDataQuadrantStartX() + axisWidth;
    }

    protected float getDataQuadrantEndX() {
        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            return super.getWidth() - borderWidth;
        } else {
            return super.getWidth() - borderWidth - axisYTitleQuadrantWidth
                    - axisWidth;
        }
    }

    protected float getStickDataQuadrantEndX() {
        return super.getWidth() - borderWidth;
    }

    protected float getDataQuadrantPaddingEndX() {
        return getDataQuadrantEndX() - dataQuadrantPaddingRight;
    }

    protected float getStickDataQuadrantPaddingEndX() {
        return getStickDataQuadrantEndX() - axisWidth;
    }

    protected float getDataQuadrantStartY() {
        return borderWidth;
    }

    protected float getCandleDataQuadrantStartY() {
        return super.getHeight() * 2 / 4 - axisWidth;
    }

    protected float getStickDataQuadrantStartY() {
        return super.getHeight() * 1 / 2 - axisWidth;
    }

    protected float getDataQuadrantPaddingStartY() {
        return getDataQuadrantStartY() + dataQuadrantPaddingTop;
    }

    protected float getCandleDataQuadrantPaddingStartY() {
        return getDataQuadrantStartY() + dataQuadrantPaddingTop;
    }

    protected float getStickDataQuadrantPaddingStartY() {
        return getStickDataQuadrantStartY() + borderWidth * 2;
    }

    protected float getDataQuadrantEndY() {
        return super.getHeight() - borderWidth - axisXTitleQuadrantHeight
                - axisWidth;
    }

    protected float getStickDataQuadrantEndY() {
        return super.getHeight() - axisXTitleQuadrantHeight - axisWidth;
    }

    protected float getDataQuadrantPaddingEndY() {
        return getDataQuadrantEndY() - dataQuadrantPaddingBottom;
    }

    protected float getStickDataQuadrantPaddingEndY() {
        return getStickDataQuadrantEndY() - dataQuadrantPaddingBottom;
    }

    protected float getDataQuadrantPaddingWidth() {
        return getDataQuadrantWidth() - ScreenUtils.dpToPx(getContext(), 8);
    }

    protected float getStickDataQuadrantPaddingWidth() {
        return getDataQuadrantWidth() - axisWidth;
    }

    protected float getDataQuadrantPaddingHeight() {
        return getDataQuadrantHeight() - dataQuadrantPaddingTop
                - dataQuadrantPaddingBottom;
    }

    protected float getCandleDataQuadrantPaddingHeight() {
        return getCandleDataQuadrantHeight() - dataQuadrantPaddingTop
                - dataQuadrantPaddingBottom;
    }

    protected float getStickDataQuadrantPaddingHeight() {
        return getStickDataQuadrantHeight() - borderWidth * 2;
    }

    protected float getBattleMoraleQuadrantPaddingHeight() {
        return getBattleMoraleQuadrantHeight() - dataQuadrantPaddingBottom - borderWidth * 2;
    }

    protected float getBattleMoraleQuadrantHeight() {
        return getHeight() / 4;
    }

    protected float getBattleMoraleQuadrantStartY() {
        return getHeight() * 3 / 4;
    }

    /**
     * <p>
     * calculate degree title on X axis
     * </p>
     * <p>
     * X軸の目盛を計算する
     * </p>
     * <p>
     * 计算X轴上显示的坐标值
     * </p>
     *
     * @param value <p>
     *              value for calculate
     *              </p>
     *              <p>
     *              計算有用データ
     *              </p>
     *              <p>
     *              计算用数据
     *              </p>
     * @return String
     * <p>
     * degree
     * </p>
     * <p>
     * 目盛
     * </p>
     * <p>
     * 坐标值
     * </p>
     */
    public String getAxisXGraduate(Object value) {
        float valueLength = ((Float) value).floatValue() - getDataQuadrantPaddingStartX();
        return String.valueOf(valueLength / getDataQuadrantPaddingWidth());
    }

    public String getAxisXGraduate(Object value, Object width) {
        float valueLength = ((Float) value).floatValue();
        float widthLength = ((Float) width).floatValue();
        return String.valueOf(valueLength / widthLength);

    }

    /**
     * <p>
     * calculate degree title on Y axis
     * </p>
     * <p>
     * Y軸の目盛を計算する
     * </p>
     * <p>
     * 计算Y轴上显示的坐标值
     * </p>
     *
     * @param value <p>
     *              value for calculate
     *              </p>
     *              <p>
     *              計算有用データ
     *              </p>
     *              <p>
     *              计算用数据
     *              </p>
     * @return String
     * <p>
     * degree
     * </p>
     * <p>
     * 目盛
     * </p>
     * <p>
     * 坐标值
     * </p>
     */
    public String getAxisYGraduate(Object value) {
        float valueLength = ((Float) value).floatValue()
                - getDataQuadrantPaddingStartY();
        return String
                .valueOf(valueLength / this.getDataQuadrantPaddingHeight());
    }

    /**
     * <p>
     * draw cross line ,called when graph is touched
     * </p>
     * <p>
     * 十字線を書く、グラプをタッチたら、メソードを呼び
     * </p>
     * <p>
     * 在图表被点击后绘制十字线
     * </p>
     *
     * @param canvas
     */
    protected void drawBottomVerticalLine(Canvas canvas) {

        if (!displayLongitudeTitle) {
            return;
        }
        if (!displayCrossXOnTouch) {
            return;
        }
        if (clickPostX <= 0) {
            return;
        }

        Paint mPaint = new Paint();
        mPaint.setColor(crossLinesColor);

        float lineVLength = getDataQuadrantHeight() + axisWidth;

        PointF boxVS = new PointF(clickPostX - longitudeFontSize * 5f / 2f,
                borderWidth + lineVLength);
        PointF boxVE = new PointF(clickPostX + longitudeFontSize * 5f / 2f,
                borderWidth + lineVLength + axisXTitleQuadrantHeight);

        // draw text
        drawAlphaTextBox(boxVS, boxVE, getAxisXGraduate(clickPostX),
                longitudeFontSize, canvas);

        canvas.drawLine(clickPostX, borderWidth, clickPostX, lineVLength,
                mPaint);
    }

    protected void drawBottomHorizontalLine(Canvas canvas) {

        if (!displayLatitudeTitle) {
            return;
        }
        if (!displayCrossYOnTouch) {
            return;
        }
        if (clickPostY <= 0) {
            return;
        }

        Paint mPaint = new Paint();
        mPaint.setColor(crossLinesColor);

        float lineHLength = getDataQuadrantWidth() + axisWidth;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            PointF boxHS = new PointF(borderWidth, clickPostY
                    - latitudeFontSize / 2f - 2);
            PointF boxHE = new PointF(borderWidth + axisYTitleQuadrantWidth,
                    clickPostY + latitudeFontSize / 2f + 2);

            // draw text
            drawAlphaTextBox(boxHS, boxHE, getAxisYGraduate(clickPostY),
                    latitudeFontSize, canvas);

            canvas.drawLine(borderWidth + axisYTitleQuadrantWidth, clickPostY,
                    borderWidth + axisYTitleQuadrantWidth + lineHLength,
                    clickPostY, mPaint);
        } else {
            PointF boxHS = new PointF(super.getWidth() - borderWidth
                    - axisYTitleQuadrantWidth, clickPostY - latitudeFontSize
                    / 2f - 2);
            PointF boxHE = new PointF(super.getWidth() - borderWidth,
                    clickPostY + latitudeFontSize / 2f + 2);

            // draw text
            drawAlphaTextBox(boxHS, boxHE, getAxisYGraduate(clickPostY),
                    latitudeFontSize, canvas);

            canvas.drawLine(borderWidth, clickPostY, borderWidth + lineHLength,
                    clickPostY, mPaint);
        }

    }

    /**
     * <p>
     * draw border
     * </p>
     * <p>
     * グラプのボーダーを書く
     * </p>
     * <p>
     * 绘制边框
     * </p>
     *
     * @param canvas
     */
    protected void drawBottomBorder(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setStyle(Style.STROKE);
        // draw a rectangle
        canvas.drawRect(borderWidth / 2, borderWidth / 2, super.getWidth()
                        - borderWidth / 2, super.getHeight() / 4 - borderWidth / 2,
                mPaint);
    }

    /**
     * <p>
     * draw X Axis
     * </p>
     * <p>
     * X軸を書く
     * </p>
     * <p>
     * 绘制X轴
     * </p>
     *
     * @param canvas
     */
    protected void drawVolXAxis(Canvas canvas) {
        float length = super.getWidth();
        float postY;
        float postX;
        float postYTop;    //顶部横线位置
        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            postX = borderWidth + axisYTitleQuadrantWidth + axisWidth / 2;
        } else {
            postX = super.getWidth() - borderWidth - axisYTitleQuadrantWidth
                    - axisWidth / 2;
        }
        if (axisXPosition == AXIS_X_POSITION_BOTTOM) {
            postY = super.getHeight() * 3 / 4 - axisXTitleQuadrantHeight - borderWidth - axisWidth / 2;
        } else {
            postY = super.getHeight() - borderWidth - axisWidth / 2;
        }
        postYTop = super.getHeight() * 1 / 2 - ScreenUtils.dpToPx(getContext(), 1);

        Paint mPaint = new Paint();
        mPaint.setColor(axisXColor);
        mPaint.setStrokeWidth(axisWidth);

        canvas.drawLine(postX, postY, length, postY, mPaint);
    }

    /**
     * <p>
     * draw Y Axis
     * </p>
     * <p>
     * Y軸を書く
     * </p>
     * <p>
     * 绘制Y轴
     * </p>
     *
     * @param canvas
     */
    protected void drawBottomYAxis(Canvas canvas) {

        float length = super.getHeight() * 3 / 10 - axisXTitleQuadrantHeight
                - borderWidth;
        float postX;
        float postY;
        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            postX = borderWidth + axisYTitleQuadrantWidth + axisWidth / 2;
        } else {
            postX = super.getWidth() - borderWidth - axisYTitleQuadrantWidth
                    - axisWidth / 2;
        }
        if (axisXPosition == AXIS_X_POSITION_BOTTOM) {
            postY = super.getHeight() - axisXTitleQuadrantHeight - borderWidth
                    - axisWidth / 2;
        } else {
            postY = super.getHeight() - borderWidth - axisWidth / 2;
        }

        Paint paint = new Paint();
        paint.setColor(axisXColor);
        paint.setStrokeWidth(axisWidth);

        canvas.drawLine(postX, postY, postX, postY - length, paint);

        canvas.drawLine(super.getWidth() - borderWidth, postY, super.getWidth()
                - borderWidth, postY - length, paint);
    }

    /**
     * <p>
     * draw longitude lines
     * </p>
     * <p>
     * 経線を書く
     * </p>
     * <p>
     * 绘制经线
     * </p>
     *
     * @param canvas
     */
    protected void drawBottomLongitudeLine(Canvas canvas) {
        if (null == volLongitudeTitles) {
            return;
        }
        if (!displayLongitude) {
            return;
        }
        // -----------------------------------------
        int counts = volLongitudeTitles.size();
        float length = getDataQuadrantPaddingWidth() / 4;

        Paint mPaintLine = new Paint();
        mPaintLine.setColor(longitudeColor);
        if (dashLongitude) {
            mPaintLine.setPathEffect(dashEffect);
        }
        if (counts > 1) {
            float postOffset = this.getDataQuadrantPaddingWidth()
                    / (counts - 1);

            float offset;
            if (axisYPosition == AXIS_Y_POSITION_LEFT) {
                offset = borderWidth + axisYTitleQuadrantWidth + axisWidth
                        + dataQuadrantPaddingLeft;
            } else {
                offset = borderWidth + dataQuadrantPaddingLeft;
            }

            for (int i = 0; i < counts; i++) {
                canvas.drawLine(offset + i * postOffset, borderWidth, offset
                        + i * postOffset, length, mPaintLine);
            }
        }
    }

    /**
     * <p>
     * draw longitude lines
     * </p>
     * <p>
     * 経線を書く
     * </p>
     * <p>
     * 绘制经线
     * </p>
     *
     * @param canvas
     */
    protected void drawBottomLongitudeTitle(Canvas canvas) {

        if (null == volLongitudeTitles) {
            return;
        }
        if (!displayLongitude) {
            return;
        }
        if (!displayLongitudeTitle) {
            return;
        }

        if (volLongitudeTitles.size() <= 1) {
            return;
        }

        Paint mPaintFont = new Paint();
        mPaintFont.setColor(longitudeFontColor);
        mPaintFont.setTextSize(longitudeFontSize);
        mPaintFont.setAntiAlias(true);

        float postOffset = this.getDataQuadrantPaddingWidth()
                / (volLongitudeTitles.size() - 1);

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            float offset = borderWidth + axisYTitleQuadrantWidth + axisWidth
                    + dataQuadrantPaddingLeft;
            for (int i = 0; i < volLongitudeTitles.size(); i++) {
                if (0 == i) {
                    canvas.drawText(volLongitudeTitles.get(i), offset + 2f,
                            super.getHeight() * 7 / 10
                                    - axisXTitleQuadrantHeight
                                    + longitudeFontSize, mPaintFont);
                } else {
                    canvas.drawText(volLongitudeTitles.get(i), offset + i
                                    * postOffset - (volLongitudeTitles.get(i).length())
                                    * longitudeFontSize / 2f,
                            super.getHeight() * 7 / 10
                                    - axisXTitleQuadrantHeight
                                    + longitudeFontSize, mPaintFont);
                }
            }

        } else {
            float offset = borderWidth + dataQuadrantPaddingLeft;
            for (int i = 0; i < volLongitudeTitles.size(); i++) {
                if (0 == i) {
                    canvas.drawText(volLongitudeTitles.get(i), offset + 2f,
                            super.getHeight() / 4 - axisXTitleQuadrantHeight
                                    + longitudeFontSize, mPaintFont);
                } else {
                    canvas.drawText(volLongitudeTitles.get(i), offset + i
                                    * postOffset - (volLongitudeTitles.get(i).length())
                                    * longitudeFontSize / 2f, super.getHeight() / 4
                                    - axisXTitleQuadrantHeight + longitudeFontSize,
                            mPaintFont);
                }

            }
        }
    }

    /**
     * <p>
     * draw latitude lines
     * </p>
     * <p>
     * 緯線を書く
     * </p>
     * <p>
     * 绘制纬线
     * </p>
     *
     * @param canvas
     */
    protected void drawBottomLatitudeLine(Canvas canvas) {

        if (null == volLatitudeTitles) {
            return;
        }
        if (!displayLatitude) {
            return;
        }
        if (!displayLatitudeTitle) {
            return;
        }
        if (volLatitudeTitles.size() <= 1) {
            return;
        }

        float length = getDataQuadrantWidth();

        Paint mPaintLine = new Paint();
        mPaintLine.setColor(latitudeColor);
        if (dashLatitude) {
            mPaintLine.setPathEffect(dashEffect);
        }

        Paint mPaintFont = new Paint();
        mPaintFont.setColor(latitudeFontColor);
        mPaintFont.setTextSize(latitudeFontSize);
        mPaintFont.setAntiAlias(true);

        float postOffset = this.getDataQuadrantPaddingHeight()
                / (volLatitudeTitles.size() - 1);

        float offset = super.getHeight() / 4 - borderWidth
                - axisXTitleQuadrantHeight - axisWidth
                - dataQuadrantPaddingBottom;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            float startFrom = borderWidth + axisYTitleQuadrantWidth + axisWidth;
            for (int i = 0; i < volLatitudeTitles.size(); i++) {
                canvas.drawLine(startFrom, offset - i * postOffset, startFrom
                        + length, offset - i * postOffset, mPaintLine);

            }
        } else {
            float startFrom = borderWidth;
            for (int i = 0; i < volLatitudeTitles.size(); i++) {
                canvas.drawLine(startFrom, offset - i * postOffset, startFrom
                        + length, offset - i * postOffset, mPaintLine);

            }
        }
    }

    /**
     * <p>
     * draw latitude lines
     * </p>
     * <p>
     * 緯線を書く
     * </p>
     * <p>
     * 绘制纬线
     * </p>
     *
     * @param canvas
     */
    protected void drawVolLatitudeTitle(Canvas canvas) {
        if (null == volLatitudeTitles) {
            return;
        }
        if (!displayLatitudeTitle) {
            return;
        }
        if (volLatitudeTitles.size() <= 1) {
            return;
        }
        Paint paintFont = new Paint();
        paintFont.setColor(latitudeFontColor);
        paintFont.setTextSize(latitudeFontSize);
        paintFont.setAntiAlias(true);

        float postOffset = getBattleMoraleQuadrantHeight() / (volLatitudeTitles.size() - 1);

        float offset = super.getHeight() - axisWidth
                - getBattleMoraleQuadrantHeight() - axisXTitleQuadrantHeight;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            for (int i = 0; i < volLatitudeTitles.size(); i++) {
                float startFrom = borderWidth + axisYTitleQuadrantWidth
                        - paintFont.measureText(volLatitudeTitles.get(i)) - 5;
                if (volLatitudeTitles.size() == 3) {
                    // 计算文字，可以得到宽高
                    Rect rect = new Rect();
                    paintFont.getTextBounds(volLatitudeTitles.get(0), 0,
                            volLatitudeTitles.get(0).length(), rect);
                    int w = rect.width();
                    int h = rect.height();

                    switch (i) {
                        case 0:
                            canvas.drawText(volLatitudeTitles.get(i), startFrom,
                                    super.getHeight() - getBattleMoraleQuadrantHeight()
                                            - axisXTitleQuadrantHeight
                                            - borderWidth - axisWidth - latitudeFontSize / 2,
                                    paintFont);
                            break;
                        case 1:
                            canvas.drawText(volLatitudeTitles.get(i), startFrom,
                                    offset - i * postOffset - h / 2, paintFont);
                            break;
                        case 2:
                            canvas.drawText(volLatitudeTitles.get(i), startFrom,
                                    offset - i * postOffset + latitudeFontSize, paintFont);
                            break;

                        default:
                            break;
                    }
                } else {
                    if (0 == i) {
                        canvas.drawText(volLatitudeTitles.get(i), startFrom,
                                super.getHeight() - axisXTitleQuadrantHeight
                                        - borderWidth - axisWidth - 2f,
                                paintFont);
                    } else {
                        canvas.drawText(
                                volLatitudeTitles.get(i),
                                startFrom,
                                offset - i * postOffset + latitudeFontSize / 2f,
                                paintFont);
                    }
                }
            }
        }
    }

    protected void drawBattleMoraleLatitudeTitle(Canvas canvas) {
        if (null == battleMoraleLatitudeTitles) {
            return;
        }
        if (!displayLatitudeTitle) {
            return;
        }
        if (battleMoraleLatitudeTitles.size() <= 1) {
            return;
        }
        Paint paintFont = new Paint();
        paintFont.setColor(latitudeFontColor);
        paintFont.setTextSize(latitudeFontSize);
        paintFont.setAntiAlias(true);

        float postOffset = getBattleMoraleQuadrantHeight() / (battleMoraleLatitudeTitles.size() - 1);

        float offset = super.getHeight() - axisWidth - axisXTitleQuadrantHeight;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            for (int i = 0; i < battleMoraleLatitudeTitles.size(); i++) {
                float startFrom = borderWidth + axisYTitleQuadrantWidth
                        - paintFont.measureText(battleMoraleLatitudeTitles.get(i)) - 5;
                canvas.drawText(
                        battleMoraleLatitudeTitles.get(i),
                        startFrom,
                        offset - i * postOffset + latitudeFontSize / 2f,
                        paintFont);
            }
        }
    }

    /**
     * <p>
     * draw cross line ,called when graph is touched
     * </p>
     * <p>
     * 十字線を書く、グラプをタッチたら、メソードを呼び
     * </p>
     * <p>
     * 在图表被点击后绘制竖线
     * </p>
     *
     * @param canvas
     */
    protected void drawCrossLine(Canvas canvas) {

        if (clickPostX <= 0 || null == touchPoint) {
            return;
        }

        Paint paint = new Paint();
        paint.setStrokeWidth(2f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setColor(crossLinesColor);

        float lineVLength = getDataQuadrantHeight() + axisWidth;

        canvas.drawLine(clickPostX, borderWidth * 4, clickPostX, lineVLength, paint);
        //绘制竖线
        canvas.drawLine(
                getDataQuadrantPaddingStartX(),
                clickPostY,
                getDataQuadrantEndX(),
                clickPostY + borderWidth * 4,
                paint);
    }

    protected void drawTopHorizontalLine(Canvas canvas) {

        if (!displayLatitudeTitle) {
            return;
        }
        if (!displayCrossYOnTouch) {
            return;
        }
        if (clickPostY <= 0) {
            return;
        }

        Paint mPaint = new Paint();
        mPaint.setColor(crossLinesColor);

        float lineHLength = getDataQuadrantWidth() + axisWidth;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            PointF boxHS = new PointF(borderWidth, clickPostY
                    - latitudeFontSize / 2f - 2);
            PointF boxHE = new PointF(borderWidth + axisYTitleQuadrantWidth,
                    clickPostY + latitudeFontSize / 2f + 2);

            // draw text
            drawAlphaTextBox(boxHS, boxHE, getAxisYGraduate(clickPostY),
                    latitudeFontSize, canvas);

            canvas.drawLine(borderWidth + axisYTitleQuadrantWidth, clickPostY,
                    borderWidth + axisYTitleQuadrantWidth + lineHLength,
                    clickPostY, mPaint);
        } else {
            PointF boxHS = new PointF(super.getWidth() - borderWidth
                    - axisYTitleQuadrantWidth, clickPostY - latitudeFontSize
                    / 2f - 2);
            PointF boxHE = new PointF(super.getWidth() - borderWidth,
                    clickPostY + latitudeFontSize / 2f + 2);

            // draw text
            drawAlphaTextBox(boxHS, boxHE, getAxisYGraduate(clickPostY),
                    latitudeFontSize, canvas);

            canvas.drawLine(borderWidth, clickPostY, borderWidth + lineHLength,
                    clickPostY, mPaint);
        }

    }

    /**
     * <p>
     * draw border
     * </p>
     * <p>
     * グラプのボーダーを書く
     * </p>
     * <p>
     * 绘制边框
     * </p>
     *
     * @param canvas
     */
    protected void drawTopBorder(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setStyle(Style.STROKE);
        // draw a rectangle
        canvas.drawRect(borderWidth / 2, borderWidth / 2, super.getWidth()
                        - borderWidth / 2, super.getHeight() * 1 / 2 - borderWidth / 2,
                mPaint);
    }

    /**
     * <p>
     * draw X Axis
     * </p>
     * <p>
     * X軸を書く
     * </p>
     * <p>
     * 绘制X轴
     * </p>
     *
     * @param canvas
     */
    protected void drawTopXAxis(Canvas canvas) {

        float length = super.getWidth();
        float postY;
        float postX;
        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            postX = borderWidth + axisYTitleQuadrantWidth + axisWidth / 2;
        } else {
            postX = super.getWidth() - borderWidth - axisYTitleQuadrantWidth
                    - axisWidth / 2;
        }
        if (axisXPosition == AXIS_X_POSITION_BOTTOM) {
            postY = super.getHeight() * 1 / 2 - axisXTitleQuadrantHeight
                    - borderWidth - axisWidth / 2;
        } else {
            postY = super.getHeight() * 1 / 2 - borderWidth - axisWidth / 2;
        }

        Paint paint = new Paint();
        paint.setColor(axisXColor);
        paint.setStrokeWidth(axisWidth);

        canvas.drawLine(postX, postY, length, postY, paint);
        postY = getHeight() - borderWidth * 2;
        canvas.drawLine(postX, postY, length, postY, paint);

        // 绘制顶部横线
        canvas.drawLine(postX, borderWidth, length, borderWidth, paint);
    }

    /**
     * <p>
     * draw Y Axis
     * </p>
     * <p>
     * Y軸を書く
     * </p>
     * <p>
     * 绘制Y轴
     * </p>
     *
     * @param canvas
     */
    protected void drawYAxis(Canvas canvas) {

        float length = super.getHeight();
        float postX;
        float postXRight;
        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            postX = borderWidth + axisYTitleQuadrantWidth + axisWidth / 2;
        } else {
            postX = super.getWidth() - borderWidth - axisYTitleQuadrantWidth
                    - axisWidth / 2;
        }
        postXRight = super.getWidth() - borderWidth;

        Paint paint = new Paint();
        paint.setColor(axisXColor);
        paint.setStrokeWidth(axisWidth);

        canvas.drawLine(postX, borderWidth, postX, length, paint);

        // 绘制右边轴
        canvas.drawLine(postXRight, borderWidth, postXRight, length, paint);
    }

    /**
     * <p>
     * draw longitude lines
     * </p>
     * <p>
     * 経線を書く
     * </p>
     * <p>
     * 绘制经线
     * </p>
     *
     * @param canvas
     */
    protected void drawTopLongitudeLine(Canvas canvas) {
        if (null == candleLongitudeTitles) {
            return;
        }
        if (!displayLongitude) {
            return;
        }
        int counts = candleLongitudeTitles.size();
        float length = getCandleDataQuadrantHeight();

        Paint mPaintLine = new Paint();
        mPaintLine.setColor(longitudeColor);
        if (dashLongitude) {
            mPaintLine.setPathEffect(dashEffect);
        }
        if (counts > 1) {
            float postOffset = this.getDataQuadrantPaddingWidth() / (counts - 1);
            float offset;
            if (axisYPosition == AXIS_Y_POSITION_LEFT) {
                offset = borderWidth * 2 + axisYTitleQuadrantWidth + axisWidth
                        + dataQuadrantPaddingLeft;
            } else {
                offset = borderWidth + dataQuadrantPaddingLeft;
            }

            for (int i = 0; i < counts; i++) {
                canvas.drawLine(offset + i * postOffset, borderWidth, offset
                        + i * postOffset, length, mPaintLine);
            }
        }
    }

    /**
     * <p>
     * draw longitude lines
     * </p>
     * <p>
     * 経線を書く
     * </p>
     * <p>
     * 绘制经线
     * </p>
     *
     * @param canvas
     */
    protected void drawTopLongitudeTitle(Canvas canvas) {

        if (null == candleLongitudeTitles) {
            return;
        }
        if (!displayLongitude) {
            return;
        }
        if (!displayLongitudeTitle) {
            return;
        }

        if (candleLongitudeTitles.size() <= 1) {
            return;
        }

        Paint mPaintFont = new Paint();
        mPaintFont.setColor(longitudeFontColor);
        mPaintFont.setTextSize(longitudeFontSize);
        mPaintFont.setAntiAlias(true);
        float postOffset = ((getStickDataQuadrantPaddingWidth() / defaultNumber) * displayNumber)
                / (candleLongitudeTitles.size() - 1);

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            float offset = borderWidth + axisYTitleQuadrantWidth + axisWidth
                    + dataQuadrantPaddingLeft;
            for (int i = 0; i < candleLongitudeTitles.size(); i++) {
                if (0 == i) {
                    canvas.drawText(candleLongitudeTitles.get(i), offset + 2f,
                            super.getHeight() * 1 / 2
                                    - axisXTitleQuadrantHeight
                                    + longitudeFontSize, mPaintFont);
                } else {
                    // 设置K线X轴title-----------------------------------
                    canvas.drawText(
                            candleLongitudeTitles.get(i),
                            offset + i * postOffset
                                    - (candleLongitudeTitles.get(i).length())
                                    * longitudeFontSize / 2f - 14f,
                            super.getHeight() * 1 / 2
                                    - axisXTitleQuadrantHeight
                                    + longitudeFontSize, mPaintFont);
                }
            }

        } else {
            float offset = borderWidth + dataQuadrantPaddingLeft;
            for (int i = 0; i < candleLongitudeTitles.size(); i++) {
                if (0 == i) {
                    canvas.drawText(candleLongitudeTitles.get(i), offset + 2f,
                            super.getHeight() * 3 / 4
                                    - axisXTitleQuadrantHeight
                                    + longitudeFontSize, mPaintFont);
                } else {
                    canvas.drawText(
                            candleLongitudeTitles.get(i),
                            offset + i * postOffset
                                    - (candleLongitudeTitles.get(i).length())
                                    * longitudeFontSize / 2f, super.getHeight()
                                    * 3 / 4 - axisXTitleQuadrantHeight
                                    + longitudeFontSize, mPaintFont);
                }
            }
        }
    }

    /**
     * <p>
     * draw latitude lines
     * </p>
     * <p>
     * 緯線を書く
     * </p>
     * <p>
     * 绘制纬线
     * </p>
     *
     * @param canvas
     */
    protected void drawTopLatitudeLine(Canvas canvas) {

        if (null == candleLatitudeTitles) {
            return;
        }
        if (!displayLatitude) {
            return;
        }
        if (!displayLatitudeTitle) {
            return;
        }
        if (candleLatitudeTitles.size() <= 1) {
            return;
        }

        float length = getDataQuadrantWidth();

        Paint mPaintLine = new Paint();
        mPaintLine.setColor(latitudeColor);
//        if (dashLatitude) {
//            mPaintLine.setPathEffect(dashEffect);
//        }

        Paint paintFont = new Paint();
        paintFont.setColor(latitudeFontColor);
        paintFont.setTextSize(latitudeFontSize);
        paintFont.setAntiAlias(true);

        float postOffset = (this.getCandleDataQuadrantPaddingHeight())
                / (candleLatitudeTitles.size() - 1);

        float offset = super.getHeight() * 1 / 2 - dataQuadrantPaddingBottom;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            float startFrom = borderWidth + axisYTitleQuadrantWidth + axisWidth;
            for (int i = 1; i < candleLatitudeTitles.size(); i++) {
                canvas.drawLine(startFrom, offset - i * postOffset, startFrom
                        + length, offset - i * postOffset, mPaintLine);

            }
        } else {
            float startFrom = borderWidth;
            for (int i = 0; i < candleLatitudeTitles.size(); i++) {
                canvas.drawLine(startFrom, offset - i * postOffset, startFrom
                        + length, offset - i * postOffset, mPaintLine);

            }
        }
    }

    /**
     * <p>
     * draw latitude lines
     * </p>
     * <p>
     * 緯線を書く
     * </p>
     * <p>
     * 绘制纬线
     * </p>
     *
     * @param canvas
     */
    protected void drawTopLatitudeTitle(Canvas canvas) {
        if (null == candleLatitudeTitles) {
            return;
        }
        if (!displayLatitudeTitle) {
            return;
        }
        if (candleLatitudeTitles.size() <= 1) {
            return;
        }
        Paint mPaintFont = new Paint();
        mPaintFont.setColor(latitudeFontColor);
        mPaintFont.setTextSize(latitudeFontSize);
        mPaintFont.setAntiAlias(true);

        float postOffset = this.getCandleDataQuadrantPaddingHeight()
                / (candleLatitudeTitles.size() - 1);

        float offset = super.getHeight() * 1 / 2 - borderWidth - axisWidth;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            for (int i = 0; i < candleLatitudeTitles.size(); i++) {
                float startFrom = borderWidth + axisYTitleQuadrantWidth
                        - mPaintFont.measureText(candleLatitudeTitles.get(i)) - 5;
                if (0 == i) {
                    canvas.drawText(candleLatitudeTitles.get(i), startFrom,
                            super.getHeight() * 1 / 2
                                    - this.axisXTitleQuadrantHeight
                                    - borderWidth - axisWidth - 2f, mPaintFont);
                } else {
                    canvas.drawText(candleLatitudeTitles.get(i), startFrom,
                            offset - i * postOffset + latitudeFontSize / 2f,
                            mPaintFont);
                }
            }
        } else {
            float startFrom = super.getWidth() - borderWidth
                    - axisYTitleQuadrantWidth;
            for (int i = 0; i < candleLatitudeTitles.size(); i++) {

                if (0 == i) {
                    canvas.drawText(candleLatitudeTitles.get(i), startFrom,
                            super.getHeight() * 1 / 2
                                    - this.axisXTitleQuadrantHeight
                                    - borderWidth - axisWidth - 2f, mPaintFont);
                } else {
                    canvas.drawText(candleLatitudeTitles.get(i), startFrom,
                            offset - i * postOffset + latitudeFontSize / 2f,
                            mPaintFont);
                }
            }
        }

    }

    /**
     * <p>
     * Zoom in the graph
     * </p>
     * <p>
     * 拡大表示する。
     * </p>
     * <p>
     * 放大表示
     * </p>
     */
    protected void zoomIn() {
        // DO NOTHING
    }

    /**
     * <p>
     * Zoom out the grid
     * </p>
     * <p>
     * 縮小表示する。
     * </p>
     * <p>
     * 缩小
     * </p>
     */
    protected void zoomOut() {
        // DO NOTHING
    }

    /*
     * (non-Javadoc)
     *
     * @param event
     *
     * @see
     * cn.limc.androidcharts.event.ITouchEventResponse#notifyEvent(GridChart)
     */
    public void notifyEvent(TwoXYGridChartView chart) {
        PointF point = chart.getTouchPoint();
        if (null != point) {
            clickPostX = point.x;
            clickPostY = point.y;
        }
        touchPoint = new PointF(clickPostX, clickPostY);
        super.invalidate();
    }

    /*
     * (non-Javadoc)
     *
     * @param event
     *
     * @see
     * cn.limc.androidcharts.event.ITouchEventNotify#addNotify(ITouchEventResponse
     * )
     */
    public void addNotify(ITouchEventResponseX notify) {
        if (null == notifyList) {
            notifyList = new ArrayList<ITouchEventResponseX>();
        }
        notifyList.add(notify);
    }

    /*
     * (non-Javadoc)
     *
     * @param event
     *
     * @see cn.limc.androidcharts.event.ITouchEventNotify#removeNotify(int)
     */
    public void removeNotify(int index) {
        if (null != notifyList && notifyList.size() > index) {
            notifyList.remove(index);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @param event
     *
     * @see cn.limc.androidcharts.event.ITouchEventNotify#removeNotify()
     */
    public void removeAllNotify() {
        if (null != notifyList) {
            notifyList.clear();
        }
    }

    public void notifyEventAll(TwoXYGridChartView chart) {
        if (null != notifyList) {
            for (int i = 0; i < notifyList.size(); i++) {
                ITouchEventResponseX ichart = notifyList.get(i);
                ichart.notifyEvent(chart);
            }
        }
    }

    /**
     * @return the axisXColor
     */
    public int getAxisXColor() {
        return axisXColor;
    }

    /**
     * @param axisXColor the axisXColor to set
     */
    public void setAxisXColor(int axisXColor) {
        this.axisXColor = axisXColor;
    }

    /**
     * @return the axisYColor
     */
    public int getAxisYColor() {
        return axisYColor;
    }

    /**
     * @param axisYColor the axisYColor to set
     */
    public void setAxisYColor(int axisYColor) {
        this.axisYColor = axisYColor;
    }

    /**
     * @return the axisWidth
     */
    public float getAxisWidth() {
        return axisWidth;
    }

    /**
     * @param axisWidth the axisWidth to set
     */
    public void setAxisWidth(float axisWidth) {
        this.axisWidth = axisWidth;
    }

    /**
     * @return the longitudeColor
     */
    public int getLongitudeColor() {
        return longitudeColor;
    }

    /**
     * @param longitudeColor the longitudeColor to set
     */
    public void setLongitudeColor(int longitudeColor) {
        this.longitudeColor = longitudeColor;
    }

    /**
     * @return the latitudeColor
     */
    public int getLatitudeColor() {
        return latitudeColor;
    }

    /**
     * @param latitudeColor the latitudeColor to set
     */
    public void setLatitudeColor(int latitudeColor) {
        this.latitudeColor = latitudeColor;
    }

    /**
     * @return the axisMarginLeft
     */
    @Deprecated
    public float getAxisMarginLeft() {
        return axisYTitleQuadrantWidth;
    }

    /**
     * @param axisMarginLeft the axisMarginLeft to set
     */
    @Deprecated
    public void setAxisMarginLeft(float axisMarginLeft) {
        this.axisYTitleQuadrantWidth = axisMarginLeft;
    }

    /**
     * @return the axisMarginLeft
     */
    public float getAxisYTitleQuadrantWidth() {
        return axisYTitleQuadrantWidth;
    }

    /**
     * @param axisYTitleQuadrantWidth the axisYTitleQuadrantWidth to set
     */
    public void setAxisYTitleQuadrantWidth(float axisYTitleQuadrantWidth) {
        this.axisYTitleQuadrantWidth = axisYTitleQuadrantWidth;
    }

    /**
     * @return the axisXTitleQuadrantHeight
     */
    @Deprecated
    public float getAxisMarginBottom() {
        return axisXTitleQuadrantHeight;
    }

    /**
     * @param axisXTitleQuadrantHeight the axisXTitleQuadrantHeight to set
     */
    @Deprecated
    public void setAxisMarginBottom(float axisXTitleQuadrantHeight) {
        this.axisXTitleQuadrantHeight = axisXTitleQuadrantHeight;
    }

    /**
     * @return the axisXTitleQuadrantHeight
     */
    public float getAxisXTitleQuadrantHeight() {
        return axisXTitleQuadrantHeight;
    }

    /**
     * @param axisXTitleQuadrantHeight the axisXTitleQuadrantHeight to set
     */
    public void setAxisXTitleQuadrantHeight(float axisXTitleQuadrantHeight) {
        this.axisXTitleQuadrantHeight = axisXTitleQuadrantHeight;
    }

    /**
     * @return the dataQuadrantPaddingTop
     */
    @Deprecated
    public float getAxisMarginTop() {
        return dataQuadrantPaddingTop;
    }

    /**
     * @param axisMarginTop the axisMarginTop to set
     */
    @Deprecated
    public void setAxisMarginTop(float axisMarginTop) {
        this.dataQuadrantPaddingTop = axisMarginTop;
        this.dataQuadrantPaddingBottom = axisMarginTop;
    }

    /**
     * @return the dataQuadrantPaddingRight
     */
    @Deprecated
    public float getAxisMarginRight() {
        return dataQuadrantPaddingRight;
    }

    /**
     * @param axisMarginRight the axisMarginRight to set
     */
    @Deprecated
    public void setAxisMarginRight(float axisMarginRight) {
        this.dataQuadrantPaddingRight = axisMarginRight;
        this.dataQuadrantPaddingLeft = axisMarginRight;
    }

    /**
     * @return the dataQuadrantPaddingTop
     */
    public float getDataQuadrantPaddingTop() {
        return dataQuadrantPaddingTop;
    }

    /**
     * @param dataQuadrantPaddingTop the dataQuadrantPaddingTop to set
     */
    public void setDataQuadrantPaddingTop(float dataQuadrantPaddingTop) {
        this.dataQuadrantPaddingTop = dataQuadrantPaddingTop;
    }

    /**
     * @return the dataQuadrantPaddingLeft
     */
    public float getDataQuadrantPaddingLeft() {
        return dataQuadrantPaddingLeft;
    }

    /**
     * @param dataQuadrantPaddingLeft the dataQuadrantPaddingLeft to set
     */
    public void setDataQuadrantPaddingLeft(float dataQuadrantPaddingLeft) {
        this.dataQuadrantPaddingLeft = dataQuadrantPaddingLeft;
    }

    /**
     * @return the dataQuadrantPaddingBottom
     */
    public float getDataQuadrantPaddingBottom() {
        return dataQuadrantPaddingBottom;
    }

    /**
     * @param dataQuadrantPaddingBottom the dataQuadrantPaddingBottom to set
     */
    public void setDataQuadrantPaddingBottom(float dataQuadrantPaddingBottom) {
        this.dataQuadrantPaddingBottom = dataQuadrantPaddingBottom;
    }

    /**
     * @return the dataQuadrantPaddingRight
     */
    public float getDataQuadrantPaddingRight() {
        return dataQuadrantPaddingRight;
    }

    /**
     * @param dataQuadrantPaddingRight the dataQuadrantPaddingRight to set
     */
    public void setDataQuadrantPaddingRight(float dataQuadrantPaddingRight) {
        this.dataQuadrantPaddingRight = dataQuadrantPaddingRight;
    }

    /**
     * @param padding the dataQuadrantPaddingTop dataQuadrantPaddingBottom
     *                dataQuadrantPaddingLeft dataQuadrantPaddingRight to set
     */
    public void setDataQuadrantPadding(float padding) {
        this.dataQuadrantPaddingTop = padding;
        this.dataQuadrantPaddingLeft = padding;
        this.dataQuadrantPaddingBottom = padding;
        this.dataQuadrantPaddingRight = padding;
    }

    /**
     * @param topnbottom the dataQuadrantPaddingTop dataQuadrantPaddingBottom to set
     * @param leftnright the dataQuadrantPaddingLeft dataQuadrantPaddingRight to set
     */
    public void setDataQuadrantPadding(float topnbottom, float leftnright) {
        this.dataQuadrantPaddingTop = topnbottom;
        this.dataQuadrantPaddingLeft = leftnright;
        this.dataQuadrantPaddingBottom = topnbottom;
        this.dataQuadrantPaddingRight = leftnright;
    }

    /**
     * @param top    the dataQuadrantPaddingTop to set
     * @param right  the dataQuadrantPaddingLeft to set
     * @param bottom the dataQuadrantPaddingBottom to set
     * @param left   the dataQuadrantPaddingRight to set
     */
    public void setDataQuadrantPadding(float top, float right, float bottom,
                                       float left) {
        this.dataQuadrantPaddingTop = top;
        this.dataQuadrantPaddingLeft = right;
        this.dataQuadrantPaddingBottom = bottom;
        this.dataQuadrantPaddingRight = left;
    }

    /**
     * @return the displayLongitudeTitle
     */
    public boolean isDisplayLongitudeTitle() {
        return displayLongitudeTitle;
    }

    /**
     * @param displayLongitudeTitle the displayLongitudeTitle to set
     */
    public void setDisplayLongitudeTitle(boolean displayLongitudeTitle) {
        this.displayLongitudeTitle = displayLongitudeTitle;
    }

    /**
     * @return the displayAxisYTitle
     */
    public boolean isDisplayLatitudeTitle() {
        return displayLatitudeTitle;
    }

    /**
     * @param displayLatitudeTitle the displayLatitudeTitle to set
     */
    public void setDisplayLatitudeTitle(boolean displayLatitudeTitle) {
        this.displayLatitudeTitle = displayLatitudeTitle;
    }

    /**
     * @return the latitudeNum
     */
    public int getLatitudeNum() {
        return latitudeNum;
    }

    /**
     * @param latitudeNum the latitudeNum to set
     */
    public void setLatitudeNum(int latitudeNum) {
        this.latitudeNum = latitudeNum;
    }

    /**
     * @return the longitudeNum
     */
    public int getLongitudeNum() {
        return longitudeNum;
    }

    /**
     * @param longitudeNum the longitudeNum to set
     */
    public void setLongitudeNum(int longitudeNum) {
        this.longitudeNum = longitudeNum;
    }

    /**
     * @return the displayLongitude
     */
    public boolean isDisplayLongitude() {
        return displayLongitude;
    }

    /**
     * @param displayLongitude the displayLongitude to set
     */
    public void setDisplayLongitude(boolean displayLongitude) {
        this.displayLongitude = displayLongitude;
    }

    /**
     * @return the dashLongitude
     */
    public boolean isDashLongitude() {
        return dashLongitude;
    }

    /**
     * @param dashLongitude the dashLongitude to set
     */
    public void setDashLongitude(boolean dashLongitude) {
        this.dashLongitude = dashLongitude;
    }

    /**
     * @return the displayLatitude
     */
    public boolean isDisplayLatitude() {
        return displayLatitude;
    }

    /**
     * @param displayLatitude the displayLatitude to set
     */
    public void setDisplayLatitude(boolean displayLatitude) {
        this.displayLatitude = displayLatitude;
    }

    /**
     * @return the dashLatitude
     */
    public boolean isDashLatitude() {
        return dashLatitude;
    }

    /**
     * @param dashLatitude the dashLatitude to set
     */
    public void setDashLatitude(boolean dashLatitude) {
        this.dashLatitude = dashLatitude;
    }

    /**
     * @return the dashEffect
     */
    public PathEffect getDashEffect() {
        return dashEffect;
    }

    /**
     * @param dashEffect the dashEffect to set
     */
    public void setDashEffect(PathEffect dashEffect) {
        this.dashEffect = dashEffect;
    }

    /**
     * @return the displayBorder
     */
    public boolean isDisplayBorder() {
        return displayBorder;
    }

    /**
     * @param displayBorder the displayBorder to set
     */
    public void setDisplayBorder(boolean displayBorder) {
        this.displayBorder = displayBorder;
    }

    /**
     * @return the borderColor
     */
    public int getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor the borderColor to set
     */
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the borderWidth
     */
    public float getBorderWidth() {
        return borderWidth;
    }

    /**
     * @param borderWidth the borderWidth to set
     */
    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * @return the longitudeFontColor
     */
    public int getLongitudeFontColor() {
        return longitudeFontColor;
    }

    /**
     * @param longitudeFontColor the longitudeFontColor to set
     */
    public void setLongitudeFontColor(int longitudeFontColor) {
        this.longitudeFontColor = longitudeFontColor;
    }

    /**
     * @return the longitudeFontSize
     */
    public int getLongitudeFontSize() {
        return longitudeFontSize;
    }

    /**
     * @param longitudeFontSize the longitudeFontSize to set
     */
    public void setLongitudeFontSize(int longitudeFontSize) {
        this.longitudeFontSize = longitudeFontSize;
    }

    /**
     * @return the latitudeFontColor
     */
    public int getLatitudeFontColor() {
        return latitudeFontColor;
    }

    /**
     * @param latitudeFontColor the latitudeFontColor to set
     */
    public void setLatitudeFontColor(int latitudeFontColor) {
        this.latitudeFontColor = latitudeFontColor;
    }

    /**
     * @return the latitudeFontSize
     */
    public int getLatitudeFontSize() {
        return latitudeFontSize;
    }

    /**
     * @param latitudeFontSize the latitudeFontSize to set
     */
    public void setLatitudeFontSize(int latitudeFontSize) {
        this.latitudeFontSize = latitudeFontSize;
    }

    /**
     * @return the crossLinesColor
     */
    public int getCrossLinesColor() {
        return crossLinesColor;
    }

    /**
     * @param crossLinesColor the crossLinesColor to set
     */
    public void setCrossLinesColor(int crossLinesColor) {
        this.crossLinesColor = crossLinesColor;
    }

    /**
     * @return the crossLinesFontColor
     */
    public int getCrossLinesFontColor() {
        return crossLinesFontColor;
    }

    /**
     * @param crossLinesFontColor the crossLinesFontColor to set
     */
    public void setCrossLinesFontColor(int crossLinesFontColor) {
        this.crossLinesFontColor = crossLinesFontColor;
    }

    /**
     * @return the candleLongitudeTitles
     */
    public List<String> getVolLongitudeTitles() {
        return candleLongitudeTitles;
    }

    /**
     * @param volLongitudeTitles the volLongitudeTitles to set
     */
    public void setVolLongitudeTitles(List<String> volLongitudeTitles) {
        this.volLongitudeTitles = volLongitudeTitles;
    }

    /**
     * @return the volLatitudeTitles
     */
    public List<String> getVolLatitudeTitles() {
        return volLatitudeTitles;
    }

    /**
     * @param volLatitudeTitles the volLatitudeTitles to set
     */
    public void setVolLatitudeTitles(List<String> volLatitudeTitles) {
        this.volLatitudeTitles = volLatitudeTitles;
    }

    /**
     * @return the latitudeMaxTitleLength
     */
    public int getLatitudeMaxTitleLength() {
        return latitudeMaxTitleLength;
    }

    /**
     * @param latitudeMaxTitleLength the latitudeMaxTitleLength to set
     */
    public void setLatitudeMaxTitleLength(int latitudeMaxTitleLength) {
        this.latitudeMaxTitleLength = latitudeMaxTitleLength;
    }

    /**
     * @return the displayCrossXOnTouch
     */
    public boolean isDisplayCrossXOnTouch() {
        return displayCrossXOnTouch;
    }

    /**
     * @param displayCrossXOnTouch the displayCrossXOnTouch to set
     */
    public void setDisplayCrossXOnTouch(boolean displayCrossXOnTouch) {
        this.displayCrossXOnTouch = displayCrossXOnTouch;
    }

    /**
     * @return the displayCrossYOnTouch
     */
    public boolean isDisplayCrossYOnTouch() {
        return displayCrossYOnTouch;
    }

    /**
     * @param displayCrossYOnTouch the displayCrossYOnTouch to set
     */
    public void setDisplayCrossYOnTouch(boolean displayCrossYOnTouch) {
        this.displayCrossYOnTouch = displayCrossYOnTouch;
    }

    /**
     * @return the clickPostX
     */
    public float getClickPostX() {
        return clickPostX;
    }

    /**
     * @param clickPostX the clickPostX to set
     */
    public void setClickPostX(float clickPostX) {
        this.clickPostX = clickPostX;
    }

    /**
     * @return the clickPostY
     */
    public float getClickPostY() {
        return clickPostY;
    }

    /**
     * @param clickPostY the clickPostY to set
     */
    public void setClickPostY(float clickPostY) {
        this.clickPostY = clickPostY;
    }

    /**
     * @return the notifyList
     */
    public List<ITouchEventResponseX> getNotifyList() {
        return notifyList;
    }

    /**
     * @param notifyList the notifyList to set
     */
    public void setNotifyList(List<ITouchEventResponseX> notifyList) {
        this.notifyList = notifyList;
    }

    /**
     * @return the touchPoint
     */
    public PointF getTouchPoint() {
        return touchPoint;
    }

    /**
     * @param touchPoint the touchPoint to set
     */
    public void setTouchPoint(PointF touchPoint) {
        this.touchPoint = touchPoint;
    }

    /**
     * @return the axisXPosition
     */
    public int getAxisXPosition() {
        return axisXPosition;
    }

    /**
     * @param axisXPosition the axisXPosition to set
     */
    public void setAxisXPosition(int axisXPosition) {
        this.axisXPosition = axisXPosition;
    }

    /**
     * @return the axisYPosition
     */
    public int getAxisYPosition() {
        return axisYPosition;
    }

    /**
     * @param axisYPosition the axisYPosition to set
     */
    public void setAxisYPosition(int axisYPosition) {
        this.axisYPosition = axisYPosition;
    }

    public List<String> getCandleLongitudeTitles() {
        return candleLongitudeTitles;
    }

    public void setCandleLongitudeTitles(List<String> candleLongitudeTitles) {
        this.candleLongitudeTitles = candleLongitudeTitles;
    }

    public List<String> getCandleLatitudeTitles() {
        return candleLatitudeTitles;
    }

    public void setCandleLatitudeTitles(List<String> candleLatitudeTitles) {
        this.candleLatitudeTitles = candleLatitudeTitles;
    }

    public boolean isResponseTouchEvent() {
        return isResponseTouchEvent;
    }

    public void setResponseTouchEvent(boolean isResponseTouchEvent) {
        this.isResponseTouchEvent = isResponseTouchEvent;
    }

    /**
     * 通过画图计算setAxisYTitleQuadrantWidth
     */
    public void setAxisYTitleQuadrantWidth() {
        //  计算左边Y轴title的宽度
        Paint mPaintFont = new Paint();
        mPaintFont.setColor(latitudeFontColor);
        mPaintFont.setTextSize(latitudeFontSize);
        mPaintFont.setAntiAlias(true);
        List<Integer> list = new ArrayList<Integer>();
        if (null != volLatitudeTitles) {
            for (int i = 0; i < volLatitudeTitles.size(); i++) {
                // 计算文字所在矩形，可以得到宽高
                Rect rectTitle = new Rect();
                mPaintFont.getTextBounds(String.valueOf(volLatitudeTitles.get(i)),
                        0, String.valueOf(volLatitudeTitles.get(i)).length(),
                        rectTitle
                );
                list.add(rectTitle.width());
            }
        }
        if (null != candleLatitudeTitles) {
            for (int i = 0; i < candleLatitudeTitles.size(); i++) {
                // 计算文字所在矩形，可以得到宽高
                Rect rectTitle = new Rect();
                mPaintFont.getTextBounds(
                        String.valueOf(candleLatitudeTitles.get(i)), 0, String
                                .valueOf(candleLatitudeTitles.get(i)).length(),
                        rectTitle);
                list.add(rectTitle.width());
            }
        }
        if (list != null && list.size() > 0) {
            axisYTitleQuadrantWidth = Collections.max(list) + 15f;
        }
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public int getDisplayNumber() {
        return displayNumber;
    }

    public void setDisplayNumber(int displayNumber) {
        this.displayNumber = displayNumber;
    }

    public List<String> getBattleMoraleLongitudeTitles() {
        return battleMoraleLongitudeTitles;
    }

    public void setBattleMoraleLongitudeTitles(List<String> battleMoraleLongitudeTitles) {
        this.battleMoraleLongitudeTitles = battleMoraleLongitudeTitles;
    }

    public List<String> getBattleMoraleLatitudeTitles() {
        return battleMoraleLatitudeTitles;
    }

    public void setBattleMoraleLatitudeTitles(List<String> battleMoraleLatitudeTitles) {
        this.battleMoraleLatitudeTitles = battleMoraleLatitudeTitles;
    }
}

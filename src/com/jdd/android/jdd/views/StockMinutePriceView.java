package com.jdd.android.jdd.views;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import cn.limc.androidcharts.view.BaseChart;
import com.jdd.android.jdd.entities.MinutePrice;
import com.jdd.android.jdd.entities.PointInfo;
import com.jdd.android.jdd.entities.PriceInfo;
import com.jdd.android.jdd.utils.Utility;
import com.thinkive.android.app_engine.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StockMinutePriceView extends BaseChart {

    //  2014年8月21日 14:23:32 新加颜色常量
    /**
     * 画板背景颜色
     */
    private static final int VIEW_BG_COLOR = 0xff21292c;

    /**
     * 成交量——红色
     */
    private static final int VALUME_RED = 0xffcf211d;

    /**
     * 成交量——绿色
     */
    private static final int VALUME_GREEN = 0xff177738;

    /**
     * Y轴标题——红色
     */
    private static final int Y_TITLE_RED = 0xfffd2d11;

    /**
     * Y轴标题——灰色 以及坐标默认颜色
     */
    private static final int Y_TITLE_GRAY = 0xff888888;

    /**
     * Y轴标题——绿色
     */
    private static final int Y_TITLE_GREEN = 0xff287f2a;

    /**
     * 四边颜色 所有经线纬线 2a3440
     */
    private static final int BORDER_COLOR = 0xff888888;

    /**
     * 画分时图现价填充线
     */
    private static final int FILL_COLOR = 0x773399FF;

    /**
     * 画分时图价格弧线 ——弧线颜色 0ba4c4
     */
    private static final int PRICE_LINE_COLOR = 0xffffffff;

    /**
     * 均价弧线颜色
     */
    private static final int AVERAGE_PRICE_LINE_COLOR = 0xfff4be04;

    /**
     * 均价弧线颜色
     */
    private static final int TRANSPARENT_COLOR = 0x00ffffff;

    /**
     * <p>
     * 默认虚线效果
     * </p>
     */
    public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
            new float[]{3, 3, 3, 3}, 1);
    /**
     * <p>
     * 虚线效果
     * </p>
     */
    private PathEffect dashEffect = DEFAULT_DASH_EFFECT;

    /****************************************************** 默认常量值 ******************************************************/
    /**
     * 默认左侧文字宽度*
     */
    private static final float DEFAULT_LEFT_MARGIN = 7;
    /**
     * 默认右侧文字宽度*
     */
    private static final float DEFAULT_RIGHT_MARGIN = 7;
    /**
     * 默认顶部矩形高度*
     */
    private static final float DEFAULT_TOP_REC_HEIGHT = 246;
    /**
     * 默认y轴文字左边距*
     */
    private static final float DEFAULT_YLTEXT_MARGIN = 0;
    /**
     * 默认右侧文字与矩形右边之间的边距*
     */
    private static final float DEFAULT_TEXT_RIGHT_BORDER_MARGIN = 50;
    /**
     * 默认y轴上纬线与第一条线之间的间隔*
     */
    private static final float DEFAULT_YLINE_TOP_MARGIN = 8;
    /**
     * 默认y轴文字顶边距*
     */
    private static final float DEFAULT_YTTEXT_MARGIN = 13;
    /**
     * 默认x轴文字顶边距 即文字矩形底边之间的距离*
     */
    private static final float DEFAULT_TEXT_LINE_MARGIN = 36;
    /**
     * 成交量矩形的默认高度*
     */
    private static final float DEFAULT_BREC_HEITHT = 90;
    /**
     * 默认图例高度 如果没有图例，则是矩形顶边与该View外部布局的顶部边距*
     */
    private static final float DEFAULT_LEGEND_HEIGHT = 10;
    /**
     * 默认成交量单位进率值*
     */
    private static final int DEFAULT_UNIT = 1;
    /**
     * 默认成交量每个区间柱形长短调整比例因子*
     */
    private static final float DEFAULT_SCALE = 1.5f;
    /**
     * 默认显示的最大点数*
     */
    private static final int DEFAULT_MAX_POINT_NUM = 240;
    /**
     * 默认显示蜡烛线的最大点数*
     */
    private static final int DEFAULT_MAX_STICK_DATA_NUM = 240;
    /**
     * 填充线水平线间隔*
     */
    private static final float DEFAULT_FILL_LINE_BLANK = 1;
    /**
     * 默认均价线与价格线线宽 *
     */
    private static final float DEFAULT_LINE_WIDTH = 2f;

    /**
     * ***************************************************属性********************
     * *********************************
     */
    // 对SurfaceView操作的一个助手类
    private SurfaceHolder surfaceHolder;
    // 对画布进行绘制操作的类
    private Canvas canvas;
    // 分时图数据
    private ArrayList<MinutePrice> mMinutePriceList;
    // 分时图每分钟数据 集合
    private ArrayList<PointInfo> pointInfoList;
    // 文字字号
    private int textSize = (int) ScreenUtils.dpToPx(getContext(), 8);
    // 左边界 除去左侧文字所占用的边界 左侧文字宽度
    private float leftMargin = DEFAULT_LEFT_MARGIN;
    // 右边界 除去右侧文字所占用的边界 右侧文字宽度
    private float rightMargin = DEFAULT_RIGHT_MARGIN;
    // 上面矩形的宽 = SurfaceView的宽-左边文字的宽-右边文字的宽
    private float topRecWidth;
    // 上面矩形的高 指定固定值
    private float topRecHeight = DEFAULT_TOP_REC_HEIGHT;
    // x轴上经线的间隔
    private float xLineBlank;
    // y轴上纬线的间隔
    private float yLineBlank;
    // y轴上纬线与第一条线之间的间隔
    private float yLineTopMargin = DEFAULT_YLINE_TOP_MARGIN;
    // y轴文字左边距
    private float yLTextMargin = DEFAULT_YLTEXT_MARGIN;
    // y轴文字右边距
    private float yRTextMargin;
    // y轴文字顶边距
    private float yTTextMargin = DEFAULT_YTTEXT_MARGIN;
    // x轴文字顶边距
    private float xBTextMargin;
    // x轴文字顶边距 即文字最矩形底边之间的距离
    private float textLineMargin = DEFAULT_TEXT_LINE_MARGIN;
    // 成交量矩形宽度
    private float bRecWidth;
    // 成交量矩形高度
    private float bRecHeight = DEFAULT_BREC_HEITHT;
    // 右侧文字与矩形右边之间的边距
    private float textRightBorderMargin = DEFAULT_TEXT_RIGHT_BORDER_MARGIN;
    // 图例高度 如果没有图例，则是矩形顶边与该View外部布局的顶部边距
    private float legendHight = DEFAULT_LEGEND_HEIGHT;
    // 股票价格
    private PriceInfo priceInfo;
    // 涨幅
    private double zhangfu;
    // 跌幅
    private double diefu;
    // 涨跌幅
    private double zdfu;
    // 右侧涨跌幅
    private double upp1, upp2, upp3, upp4, upp5;
    // 左侧价格
    private double p1, p2, p4, p5;
    // 显示的最大点数
    private int maxPointNum = DEFAULT_MAX_POINT_NUM;
    // 显示蜡烛线的最大点数
    private int maxStickDataNum = DEFAULT_MAX_STICK_DATA_NUM;
    // 最大成交量数值
    private double maxVolumeNum;
    // 最低成交量数值
    private double minVolumeNum;
    // 刻度线上的成交量
    private double keduVolume;
    // 成交量单位进率 万手
    private int unit = DEFAULT_UNIT;
    // 成交量每个区间柱形长短调整比例因子
    private float scale = DEFAULT_SCALE;
    // 是否停牌
    private boolean tingPaiState;
    /**
     * ****************************************画填充线部分属性*************************
     * ***********
     */
    // 填充线水平线间隔
    private float fillLineBlank = DEFAULT_FILL_LINE_BLANK;

    /*
     * (non-Javadoc)
     *
     * @param context
     *
     * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context)
     */
    public StockMinutePriceView(Context context) {
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
    public StockMinutePriceView(Context context, AttributeSet attrs, int defStyle) {
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
    public StockMinutePriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawTimeSharingPlan();
    }

    /**
     * 画股票分时图
     */
    public void drawTimeSharingPlan() {
        drawData(0);
    }

    /**
     * 初始化数据
     */
    public void initData() {

        topRecHeight = getHeight() * 0.62f; // 占视图矩形高度的62%

        topRecWidth = getWidth() - leftMargin - rightMargin;

        // 左侧文字起始点
        yLTextMargin = leftMargin;

        yRTextMargin = getWidth(); // 宽度-右侧文字宽度
        // +
        // 距最右边边距

        xLineBlank = topRecWidth / 4;

        yLineBlank = (topRecHeight - yLineTopMargin * 2) / 4;

        xBTextMargin = topRecHeight + textLineMargin;

        bRecWidth = topRecWidth;

        bRecHeight = getHeight() * 0.2f; // 占高度的20%

    }

    /**
     * 画分时图数据
     */
    private void drawData(int drawType) {
        try {

            if (tingPaiState) {
                drawTingPaiFenShi();
            } else {
                drawNomalFenShi();
            }

        } catch (Exception e) {
        }
    }

    /**
     * 画停牌分时
     */
    private void drawTingPaiFenShi() {
        // 初始化画图相关数据
        initData();
        // 画背景
        drawSurfaceViewBg();

        // 画四条边
        drawBorder();

        // 画x轴数字
        drawXData();

        // 画y轴上的纬线
        drawYDataLine();

        // 画y轴第三条线
        drawYThreeDataLine();

        // 画x轴上的经线
        drawXDataLine();

        // 画下面成交量矩型
        drawTimeShare();

        // 画停牌价格线
        drawTingPaiPrice();

        // 画停牌均线
        drawTingPaiAverage();

        // 画停牌数值线
        drawTingBaiData();
    }

    /**
     * 画正常分时图
     */
    private void drawNomalFenShi() {
        // 初始化画图相关数据
        initData();

        // 画背景
        drawSurfaceViewBg();

        // 画四条边
        drawBorder();

        // 画x轴数字
        drawXData();

        // 画y轴上的纬线
        drawYDataLine();

        // 画y轴第三条线
        drawYThreeDataLine();

        // 画x轴上的经线
        drawXDataLine();

        // 画下面成交量四边
        drawTimeShare();

        // 画下面矩形数值
        drawTurnoverData();

        // 画--价格弧线
        drawPriceLine();

        // 画填充线
//		drawFill();

        // 画分时图均价弧线
        drawAveragePriceLine();

        // 画成交量--柱状线
        drawValume();

        // 画y轴数字
        drawSingleStockYData();
    }

    /**
     * 画SurfaceView的背景色
     */
    public void drawSurfaceViewBg() {
        Paint paint = new Paint();
        paint.setColor(VIEW_BG_COLOR);
        paint.setAntiAlias(true);
        int left = 0;
        int top = 0;
        int right = getWidth();
        int bottom = getHeight();
        canvas.drawRect(left, top, right, bottom, paint); // 左、顶、右、底 画笔
    }

    /**
     * 绘制边 此处绘制
     */
    public void drawBorder() {
        Paint paint = new Paint();
        paint.setColor(BORDER_COLOR);
        paint.setAntiAlias(true);

        // 绘制四边
        canvas.drawLine(leftMargin, legendHight, getWidth() - rightMargin,
                legendHight, paint); // 绘制顶边

        canvas.drawLine(getWidth() - rightMargin, legendHight, getWidth()
                - rightMargin, topRecHeight + legendHight, paint); // 绘制右边

        canvas.drawLine(leftMargin, legendHight + topRecHeight, getWidth()
                - rightMargin, legendHight + topRecHeight, paint); // 底 x轴

        canvas.drawLine(leftMargin, legendHight, leftMargin, topRecHeight
                + legendHight, paint); // 左 y轴
    }

    /**
     * 绘制y轴上的纬线
     */
    public void drawYDataLine() {
        Paint paint = new Paint();
        paint.setColor(BORDER_COLOR);
        paint.setAntiAlias(true);
        paint.setPathEffect(dashEffect);
        canvas.drawLine(leftMargin, yLineTopMargin + legendHight, getWidth()
                - rightMargin, yLineTopMargin + legendHight, paint); // 纬线1
        canvas.drawLine(leftMargin, yLineTopMargin + legendHight + yLineBlank,
                getWidth() - rightMargin, yLineTopMargin + legendHight
                        + yLineBlank, paint); // 纬线2

        canvas.drawLine(leftMargin, yLineTopMargin + legendHight + yLineBlank
                * 3, getWidth() - rightMargin, yLineTopMargin + legendHight
                + yLineBlank * 3, paint); // 纬线4
        canvas.drawLine(leftMargin, yLineTopMargin + legendHight + yLineBlank
                * 4, getWidth() - rightMargin, yLineTopMargin + legendHight
                + yLineBlank * 4, paint); // 纬线5
    }

    /**
     * y轴第三条纬线 昨收 纬线3
     */
    public void drawYThreeDataLine() {
        Paint paint = new Paint();
        paint.setStrokeWidth(1.5f);
        // paint.setColor(0xff797e85);
        paint.setColor(BORDER_COLOR);
        paint.setPathEffect(dashEffect);
        // paint.setAntiAlias(true);
        // paint.setStyle(Paint.Style.STROKE);
        //
        // Path path = new Path();
        // path.moveTo(leftMargin, yLineTopMargin + legendHight + yLineBlank *
        // 2);
        // path.lineTo(getWidth() - rightMargin, yLineTopMargin + legendHight +
        // yLineBlank * 2);
        // PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },
        // 1);
        // paint.setPathEffect(effects);
        // canvas.drawPath(path, paint);
        canvas.drawLine(leftMargin, yLineTopMargin + legendHight + yLineBlank
                * 2, getWidth() - rightMargin, yLineTopMargin + legendHight
                + yLineBlank * 2, paint); // 纬线3
    }

    /**
     * Y轴线上数值计算
     */
    public void initYData() {
        if (null != priceInfo) {
            zhangfu = (priceInfo.getHigh() - priceInfo.getYesterday())
                    / priceInfo.getYesterday() * 100;

            diefu = (priceInfo.getLow() - priceInfo.getYesterday())
                    / priceInfo.getYesterday() * 100;

            if (Math.abs(zhangfu) > Math.abs(diefu)) {
                zdfu = zhangfu;
            } else {
                zdfu = diefu;
            }

            if (zdfu > 0) {
                upp1 = zdfu;
                upp2 = upp1 / 2;
                upp3 = 0.00;
                upp4 = -upp2;
                upp5 = -upp1;
            } else {
                upp5 = zdfu;
                upp4 = upp5 / 2;
                upp3 = 0.00;
                upp2 = Math.abs(upp4);
                upp1 = Math.abs(upp5);
            }

            /**
             * 具体刻度上左侧的价格计算
             *
             * 刻度价格 = 昨日收盘价 + (昨日收盘价 * 涨跌幅)
             */
            p1 = priceInfo.getYesterday()
                    + (priceInfo.getYesterday() * upp1 / 100);
            p2 = priceInfo.getYesterday()
                    + (priceInfo.getYesterday() * upp2 / 100);
            p4 = priceInfo.getYesterday()
                    + (priceInfo.getYesterday() * upp4 / 100);
            p5 = priceInfo.getYesterday()
                    + (priceInfo.getYesterday() * upp5 / 100);
        }
    }

    /**
     * 停牌y轴数值初始化
     */
    private void initTingPai() {
        if (null != priceInfo) {
            zdfu = 0.00;
            upp1 = zdfu;
            upp2 = zdfu;
            upp3 = zdfu;
            upp4 = zdfu;
            upp5 = zdfu;
            /**
             * 具体刻度上左侧的价格计算
             *
             * 刻度价格 = 昨日收盘价 + (昨日收盘价 * 涨跌幅)
             */
            p1 = priceInfo.getYesterday();
            p2 = priceInfo.getYesterday();
            p4 = priceInfo.getYesterday();
            p5 = priceInfo.getYesterday();
        }
    }

    /**
     * 个股画四角上的数字
     */
    public void drawSingleStockYData() {
        if (null != priceInfo) {
            initYData();
            drawYlineValue();
        }
    }

    /**
     * 画停牌数字
     */
    public void drawTingBaiData() {
        initTingPai();
        drawYlineValue();
    }

    /**
     * 画y轴数值
     */
    private void drawYlineValue() {
        if (tingPaiState) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextSize(textSize);

            // 获取文字宽度 画出文字背后矩形
            // float textLength = paint.measureText(Utility
            // .formatDouble2PointTwo(p1));
            // paint.setColor(0x00ffffff);
            // canvas.drawRect(yLTextMargin, legendHight, yLTextMargin
            // + textLength, yTTextMargin + legendHight, paint);

            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(Utility.formatDouble2PointTwo(p1), yLTextMargin,
                    yTTextMargin + legendHight, paint); // 线一左

            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(
                    Utility.formatDouble2PointTwo(priceInfo.getYesterday()),
                    yLTextMargin, yTTextMargin + legendHight + yLineBlank * 2
                            - 12, paint); // 线三左 昨收

            // 画出文字背后矩形
            // textLength =
            // paint.measureText(Utility.formatDouble2PointTwo(p5));
            // paintTRANSPARENT_COLORTRANSPARENT_COLOR);
            // canvas.drawRect(yLTextMargin, yTTextMargin + yLineBlank * 4,
            // yLTextMargin + textLength, yTTextMargin + legendHight
            // + yLineBlank * 4, paint);

            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(Utility.formatDouble2PointTwo(p5), yLTextMargin,
                    yTTextMargin + legendHight + yLineBlank * 4, paint); // 线五左

            // 画出文字背后矩形
            // textLength = paint.measureText(" "
            // + Utility.formatDouble2PointTwo(upp1) + "%");
            // paint.setColor(TRANSPARENT_COLOR);
            // canvas.drawRect(yRTextMargin, legendHight, yRTextMargin
            // + textLength, yTTextMargin + legendHight, paint);

            // 右侧百分比
            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(" " + Utility.formatDouble2PointTwo(upp1) + "%",
                    yRTextMargin, yTTextMargin + legendHight, paint); // 线一右

            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(Utility.formatDouble2PointTwo(upp3) + "%",
                    yRTextMargin + 10, yTTextMargin + legendHight + yLineBlank
                            * 2 - 12, paint); // 线三右

            // 画出文字背后矩形
            // textLength =
            // paint.measureText(Utility.formatDouble2PointTwo(upp5)
            // + "%");
            // paint.setColor(TRANSPARENT_COLOR);
            // canvas.drawRect(yRTextMargin, yTTextMargin + yLineBlank * 4,
            // yRTextMargin + textLength, yTTextMargin + legendHight
            // + yLineBlank * 4, paint);

            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(Utility.formatDouble2PointTwo(upp5) + "%",
                    yRTextMargin, yTTextMargin + legendHight + yLineBlank * 4,
                    paint); // 线五右
        } else {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextSize(textSize);

            // 获取文字宽度 画出文字背后矩形
            float textLength = paint.measureText(Utility
                    .formatDouble2PointTwo(p1));
            // paint.setColor(0xff060b0e);
            paint.setColor(TRANSPARENT_COLOR);
            canvas.drawRect(yLTextMargin, legendHight, yLTextMargin
                    + textLength, yTTextMargin + legendHight, paint);

            paint.setColor(Y_TITLE_RED);
            canvas.drawText(Utility.formatDouble2PointTwo(p1), yLTextMargin,
                    yTTextMargin + legendHight, paint); // 线一左

            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(
                    Utility.formatDouble2PointTwo(priceInfo.getYesterday()),
                    yLTextMargin, yTTextMargin + legendHight + yLineBlank * 2,
                    paint); // 线三左 昨收

            // 画出文字背后矩形
            textLength = paint.measureText(Utility.formatDouble2PointTwo(p5));
            // paint.setColor(0xff060b0e);
            paint.setColor(TRANSPARENT_COLOR);
            canvas.drawRect(yLTextMargin, yTTextMargin + yLineBlank * 4,
                    yLTextMargin + textLength, yTTextMargin + legendHight
                            + yLineBlank * 4, paint);

            paint.setColor(Y_TITLE_GREEN);
            canvas.drawText(Utility.formatDouble2PointTwo(p5), yLTextMargin,
                    yTTextMargin + legendHight + yLineBlank * 4, paint); // 线五左

            // 画出文字背后矩形
            textLength = paint.measureText(" "
                    + Utility.formatDouble2PointTwo(upp1) + "%");
            // paint.setColor(0xff060b0e);
            paint.setColor(TRANSPARENT_COLOR);
            canvas.drawRect(yRTextMargin, legendHight, yRTextMargin
                    + textLength, yTTextMargin + legendHight, paint);

            // 右侧百分比
            paint.setColor(Y_TITLE_RED);
            canvas.drawText(" " + Utility.formatDouble2PointTwo(upp1) + "%",
                    yRTextMargin - textLength, yTTextMargin + legendHight, paint); // 线一右

            paint.setColor(Y_TITLE_GRAY);
            canvas.drawText(Utility.formatDouble2PointTwo(upp3) + "%",
                    yRTextMargin - textLength, yTTextMargin + legendHight + yLineBlank
                            * 2, paint); // 线三右

            paint.setColor(Y_TITLE_GREEN);
            canvas.drawText(Utility.formatDouble2PointTwo(upp5) + "%",
                    yRTextMargin - textLength, yTTextMargin + legendHight + yLineBlank
                            * 4, paint); // 线五右
        }
    }

    /**
     * 绘制X轴上三条竖线 时间
     */
    public void drawXDataLine() {
        Paint paint = new Paint();
        paint.setColor(BORDER_COLOR);
        paint.setPathEffect(dashEffect);
        paint.setAntiAlias(true);

        canvas.drawLine(leftMargin + xLineBlank, legendHight, leftMargin
                + xLineBlank, topRecHeight + legendHight, paint);

        canvas.drawLine(leftMargin + xLineBlank * 2, legendHight, leftMargin
                + xLineBlank * 2, topRecHeight + legendHight, paint);

        canvas.drawLine(leftMargin + xLineBlank * 3, legendHight, leftMargin
                + xLineBlank * 3, topRecHeight + legendHight, paint);
    }

    /**
     * 画x轴上的时间值 经线
     */
    public void drawXData() {
        Paint paint = new Paint();
        paint.setColor(Y_TITLE_GRAY);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);

        canvas.drawText("09:30", leftMargin, xBTextMargin, paint); // 09:30
        canvas.drawText("10:30", leftMargin + xLineBlank - 20, xBTextMargin,
                paint); // 10:30
        canvas.drawText("11:30/13:00", leftMargin + xLineBlank * 2 - 44,
                xBTextMargin, paint); // 11:30/13:00
        canvas.drawText("14:00", leftMargin + xLineBlank * 3 - 20,
                xBTextMargin, paint); // 14:00
        canvas.drawText("15:00", leftMargin + xLineBlank * 4 - 60,
                xBTextMargin, paint); // 15:00
    }

    /**
     * 画成交量
     */
    public void drawTimeShare() {
        Paint paint = new Paint();
        paint.setColor(BORDER_COLOR);
        paint.setAntiAlias(true);

        // 绘制底部矩形边框
        canvas.drawLine(leftMargin, getHeight() - bRecHeight, getWidth()
                - rightMargin, getHeight() - bRecHeight, paint); // 成交量顶边
        canvas.drawLine(leftMargin, getHeight(), getWidth() - rightMargin,
                getHeight(), paint); // 成交量底边
        canvas.drawLine(leftMargin, getHeight() - bRecHeight, leftMargin,
                getHeight(), paint); // 成交量左边
        canvas.drawLine(getWidth() - rightMargin, getHeight() - bRecHeight,
                getWidth() - rightMargin, getHeight(), paint); // 成交量右边

        // 绘制底部矩形竖线
        canvas.drawLine(leftMargin + xLineBlank, getHeight() - bRecHeight,
                leftMargin + xLineBlank, getHeight(), paint);
        canvas.drawLine(leftMargin + xLineBlank * 2, getHeight() - bRecHeight,
                leftMargin + xLineBlank * 2, getHeight(), paint);
        canvas.drawLine(leftMargin + xLineBlank * 3, getHeight() - bRecHeight,
                leftMargin + xLineBlank * 3, getHeight(), paint);
    }

    /**
     * 画成交量数值
     */
    private void drawTurnoverData() {
        Paint paint = new Paint();
        paint.setColor(Y_TITLE_GRAY);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);

        getMaxVolume();

        getMinVolume();

        if (maxVolumeNum == minVolumeNum) {
            minVolumeNum = 0;
        }

        // 刻度值如果是最高的价格，那么后面画出的图形 柱状就会比较短，因此在这里将最大价格均分 以万手为单位则需 / 10000
        // 将刻度的区间缩小，柱状加长
        keduVolume = maxVolumeNum / unit / scale;
    }

    /**
     * 画停牌成交量
     */

    /**
     * 获取最大成交量值
     */
    private void getMaxVolume() {

        List<Double> doubleList = new ArrayList<Double>();
        if (null != mMinutePriceList) {
            for (MinutePrice minutePrice : mMinutePriceList) {
                doubleList.add(minutePrice.getVolume());
            }
        }

        if (doubleList.size() > 1) {
            maxVolumeNum = Collections.max(doubleList).doubleValue();
        } else if (doubleList.size() == 1) {
            maxVolumeNum = mMinutePriceList.get(0).getVolume();
        }
    }

    /**
     * 获取最低成交量
     */
    private void getMinVolume() {
        List<Double> doubleList = new ArrayList<Double>();
        if (null != mMinutePriceList) {
            for (MinutePrice minutePrice : mMinutePriceList) {
                doubleList.add(minutePrice.getVolume());
            }
        }

        if (doubleList.size() > 1) {
            // minVolumeNum = Collections.min(doubleList).doubleValue();
            minVolumeNum = 0;
        } else if (doubleList.size() == 1) {
            minVolumeNum = 0;
        }
    }

    /**
     * 画分时图价格弧线
     */
    public void drawPriceLine() {
        initYData();
        Paint paint = new Paint();
        paint.setColor(PRICE_LINE_COLOR);
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        // 点线距离
        float lineLength = (topRecWidth / maxPointNum) - 1;
        // 起始位置
        float startX = leftMargin + lineLength / 2f;
        // 定义起始点
        PointF ptFirst = null;

        // 当前绘制价格的信息 x、y轴位置
        pointInfoList = new ArrayList<PointInfo>();

        if (null != mMinutePriceList) {

            for (int i = 0; i < mMinutePriceList.size(); i++) {
                MinutePrice minutePrice = mMinutePriceList.get(i);
                double xianjia = minutePrice.getNow();// 现价
                // 获取终点Y坐标 （现价- 最低价） / （最高价 - 最低价)
                // 现价- 最低价 得到的是
                float valueY = (float) ((1f - (xianjia - p5) / (p1 - p5)) * (topRecHeight - yLineTopMargin * 2))
                        + yLineTopMargin + legendHight;
                // 如果Y坐标小于p1点的坐标，则将y坐标置为p1点的坐标，如果Y坐标>p5点的坐标，则将y坐标置为p5点的坐标
                if (valueY < yLineTopMargin + legendHight) {
                    valueY = yLineTopMargin + legendHight;
                }

                if (valueY > yLineTopMargin + legendHight + yLineBlank * 4) {
                    valueY = yLineTopMargin + legendHight + yLineBlank * 4;
                }

                // 绘制线条
                if (i == 0) {
                    canvas.drawLine(leftMargin, valueY, startX, valueY, paint);
                } else if (i > 0) {
                    canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, paint);
                }
                // 重置起始点
                ptFirst = new PointF(startX, valueY);

                PointInfo pointInfo = new PointInfo(ptFirst.x, ptFirst.y,
                        minutePrice);
                pointInfoList.add(pointInfo);

                // X位移
                startX = startX + 1 + lineLength;
            }
        }
    }

    /**
     * 画分时图现价填充线 绘制填充线思路 1、两点之间，取高度较低者作为Y轴高度 2、Y轴除以间隔，得到绘制横线的条数 3、p5的y坐标 - Y轴间隔
     * * 第几条曲线 得到每条横线的y坐标 4、x坐标是每个点的X坐标
     */
    private void drawFill() {
        if (null != pointInfoList && pointInfoList.size() > 0) {
            Paint paint = new Paint();
            float lineLength = (topRecWidth / maxPointNum) - 1;
            paint.setStrokeWidth(lineLength + 1);
            paint.setColor(FILL_COLOR);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setAntiAlias(true);

            float startX = 0;
            float startY = 0;
            // float endX = 0;
            // float endY = 0;

            // float valueY = 0;
//			int lines = 0;
            // 每条填充线的高度
            // float fillY = 0;

            // 上面矩形最下边价格线的高度
            float fillHighY = legendHight + topRecHeight;

            // for (int i = 0; i < pointInfoList.size() - 1; i++) {
            // PointInfo pointInfo1 = pointInfoList.get(i);
            // PointInfo pointInfo2 = pointInfoList.get(i + 1);
            //
            // startX = pointInfo1.getStartX();
            // startY = pointInfo1.getValueY();
            //
            // endX = pointInfo2.getStartX();
            // endY = pointInfo2.getValueY();
            //
            // if (startY > endY) {
            // valueY = endY;
            // } else {
            // valueY = startY;
            // }
            //
            // lines = (int) (fillHighY - valueY) / (int) fillLineBlank;
            //
            // for (int j = 1; j < lines; j++) {
            // fillY = fillHighY - j * fillLineBlank;
            // canvas.drawLine(startX, fillY, endX, fillY, paint);
            // }
            //
            // }

            for (int i = 0; i < pointInfoList.size(); i++) {
                PointInfo pointInfo1 = pointInfoList.get(i);

                startX = pointInfo1.getStartX();
                startY = pointInfo1.getValueY();

                canvas.drawLine(startX, startY, startX, fillHighY, paint);

            }

        }
    }

    /**
     * 画停牌价格线
     */
    private void drawTingPaiPrice() {
        // 初始化停牌数据
        initTingPai();
        Paint paint = new Paint();
        paint.setColor(PRICE_LINE_COLOR);
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        canvas.drawLine(
                leftMargin,
                yLineTopMargin + legendHight + yLineBlank * 2,
                (getWidth() - rightMargin - leftMargin)
                        * (mMinutePriceList.size() / 240f) + 10, yLineTopMargin
                        + legendHight + yLineBlank * 2, paint);
    }

    /**
     * 画停牌均线
     */
    private void drawTingPaiAverage() {
        // 初始化停牌数据
        initTingPai();
        Paint paint = new Paint();
        paint.setColor(AVERAGE_PRICE_LINE_COLOR);
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        canvas.drawLine(
                leftMargin,
                yLineTopMargin + legendHight + yLineBlank * 2,
                (getWidth() - rightMargin - leftMargin)
                        * (mMinutePriceList.size() / 240f) + 10, yLineTopMargin
                        + legendHight + yLineBlank * 2, paint);
    }

    /**
     * 画分时图均价弧线
     */
    public void drawAveragePriceLine() {

        initYData();

        Paint paint = new Paint();
        paint.setColor(AVERAGE_PRICE_LINE_COLOR);
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        // 点线距离
        float lineLength = (topRecWidth / maxPointNum) - 1;
        // 起始位置
        float startX = leftMargin + lineLength / 2f;
        // 定义起始点
        PointF ptFirst = null;

        // 当前绘制价格的信息 x、y轴位置
        pointInfoList = new ArrayList<PointInfo>();

        if (null != mMinutePriceList) {
            for (int i = 0; i < mMinutePriceList.size(); i++) {
                MinutePrice minutePrice = mMinutePriceList.get(i);

                double averagePrice = minutePrice.getAvgPrice();// 均价

                // 判断均价是否为0.0
                if (averagePrice > 0) {
                    float valueY = (float) ((1f - (averagePrice - p5)
                            / (p1 - p5)) * (topRecHeight - yLineTopMargin * 2))
                            + yLineTopMargin + legendHight;
                    if (valueY < yLineTopMargin + legendHight) {
                        valueY = yLineTopMargin + legendHight;
                    }

                    if (valueY > yLineTopMargin + legendHight + yLineBlank * 4) {
                        valueY = yLineTopMargin + legendHight + yLineBlank * 4;
                    }
                    // 绘制线条
                    if (i == 0) {
                        canvas.drawLine(leftMargin, valueY, startX, valueY,
                                paint);
                    } else if (i > 0) {
                        canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY,
                                paint);
                    }
                    // 重置起始点
                    ptFirst = new PointF(startX, valueY);
                    // X位移
                    startX = startX + 1 + lineLength;
                }
            }
        }
    }

    /**
     * 画成交量 柱状线
     */
    public void drawValume() {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(textSize);
        canvas.drawText(
                String.valueOf(maxVolumeNum), leftMargin, getHeight() - bRecHeight - textSize/2, textPaint
        );
        textPaint.setPathEffect(DEFAULT_DASH_EFFECT);
        canvas.drawLine(
                leftMargin, (getHeight() - bRecHeight / 2),
                getWidth()-leftMargin, (getHeight() - bRecHeight / 2), textPaint
        );
        // 蜡烛棒宽度
        float stickWidth = (bRecWidth / maxStickDataNum) - 1;
        // 蜡烛棒起始绘制位置
        float stickX = leftMargin + 1;

        Paint mPaintStick = new Paint();
        // 前一分钟价格
        double foreMinutePrice = 0.0;

        if (null != mMinutePriceList) {
            // 判断显示为方柱或显示为线条
            for (int i = 0; i < mMinutePriceList.size(); i++) {
                MinutePrice minutePrice = mMinutePriceList.get(i);
                double nowVolume = minutePrice.getVolume() / unit;
                minVolumeNum = minVolumeNum / unit;

                float highY = (float) ((1f - ((nowVolume - minVolumeNum))
                        / ((maxVolumeNum - minVolumeNum))) * bRecHeight)
                        + getHeight() - bRecHeight + 2;
                if (highY < getHeight() - bRecHeight) {
                    highY = getHeight() - bRecHeight + 2;
                }
                float lowY = getHeight();
                // 根据价格涨跌来确定画笔颜色，来画红绿柱
                if (i == 0) {
                    foreMinutePrice = priceInfo.getYesterday();
                } else {
                    MinutePrice minutePrice1 = mMinutePriceList.get(i - 1);
                    foreMinutePrice = minutePrice1.getNow();
                }

                if (minutePrice.getNow() >= foreMinutePrice) {
                    mPaintStick.setColor(VALUME_RED);
                } else if (minutePrice.getNow() < foreMinutePrice) {
                    mPaintStick.setColor(VALUME_GREEN);
                }

                if (stickWidth >= 2f) {
                    canvas.drawRect(stickX, Math.abs(highY), stickX
                            + stickWidth, Math.abs(lowY), mPaintStick);
                } else {
                    canvas.drawLine(stickX, Math.abs(highY), stickX,
                            Math.abs(lowY), mPaintStick);
                }

                // X位移
                stickX = stickX + 1 + stickWidth;
            }
        }
    }


    /**
     * 画
     *
     * @return
     */

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public ArrayList<MinutePrice> getMinutePriceList() {
        return mMinutePriceList;
    }

    public void setMinutePriceList(ArrayList<MinutePrice> minutePriceList) {
        this.mMinutePriceList = minutePriceList;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    public float getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(float leftMargin) {
        this.leftMargin = leftMargin;
    }

    public float getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(float rightMargin) {
        this.rightMargin = rightMargin;
    }

    public float getTopRecHeight() {
        return topRecHeight;
    }

    public void setTopRecHeight(float topRecHeight) {
        this.topRecHeight = topRecHeight;
    }

    public float getyLineTopMargin() {
        return yLineTopMargin;
    }

    public void setyLineTopMargin(float yLineTopMargin) {
        this.yLineTopMargin = yLineTopMargin;
    }

    public float getTextLineMargin() {
        return textLineMargin;
    }

    public void setTextLineMargin(float textLineMargin) {
        this.textLineMargin = textLineMargin;
    }

    public float getbRecHeight() {
        return bRecHeight;
    }

    public void setbRecHeight(float bRecHeight) {
        this.bRecHeight = bRecHeight;
    }

    public float getTextRightBorderMargin() {
        return textRightBorderMargin;
    }

    public void setTextRightBorderMargin(float textRightBorderMargin) {
        this.textRightBorderMargin = textRightBorderMargin;
    }

    public float getLegendHight() {
        return legendHight;
    }

    public void setLegendHight(float legendHight) {
        this.legendHight = legendHight;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getMaxPointNum() {
        return maxPointNum;
    }

    public void setMaxPointNum(int maxPointNum) {
        this.maxPointNum = maxPointNum;
    }

    public int getMaxStickDataNum() {
        return maxStickDataNum;
    }

    public void setMaxStickDataNum(int maxStickDataNum) {
        this.maxStickDataNum = maxStickDataNum;
    }

    public boolean isTingPaiState() {
        return tingPaiState;
    }

    public void setTingPaiState(boolean tingPaiState) {
        this.tingPaiState = tingPaiState;
    }
}

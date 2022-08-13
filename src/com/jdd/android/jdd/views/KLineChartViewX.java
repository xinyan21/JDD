package com.jdd.android.jdd.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import cn.limc.androidcharts.entity.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.utils.StockUtil;
import com.jdd.android.jdd.utils.Utility;
import com.thinkive.android.app_engine.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * <p>
 * 描述：K线图类，带MACD和KDJ线，由MACandleStickChart、MACDChart 和ColoredSlipStickChart类整合而来
 * </p>
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 14-4-30
 */
public class KLineChartViewX extends TwoXYGridChartView {
    
    public static final short TOUCH_EVENT_TYPE_CROSS_lINE = 1;
    public static final short TOUCH_EVENT_TYPE_MOVE_STICK = 2;
    public static final short TOUCH_EVENT_TYPE_ZOOM = 3;
    
    
    public static final int BOTTOM_CHART_TYPE_BATTLE_MORALE = 0;
    
    public static final int BOTTOM_CHART_TYPE_KDJ = 2;
    
    public static final int BOTTOM_CHART_TYPE_MACD = 1;

    
    private int c = BOTTOM_CHART_TYPE_BATTLE_MORALE;

    
    /**
     * <p>
     * 默认阳线的边框颜色
     * </p>
     */
    public static final int DEFAULT_POSITIVE_STICK_BORDER_COLOR = 0xffff4433;
    /**
     * <p>
     * 默认阳线的填充颜色
     * </p>
     */
    public static final int DEFAULT_POSITIVE_STICK_FILL_COLOR = 0xffd70b17;
    /**
     * <p>
     * 默认阴线的边框颜色
     * </p>
     */
    public static final int DEFAULT_NEGATIVE_STICK_BORDER_COLOR = 0xffff4433;
    /**
     * <p>
     * 默认阴线的填充颜色
     * </p>
     */
    public static final int DEFAULT_NEGATIVE_STICK_FILL_COLOR = 0xff00CFCF;
    /**
     * <p>
     * 默认十字线显示颜色
     * </p>
     */
    public static final int DEFAULT_CROSS_STAR_COLOR = Color.LTGRAY;
    public static final int ZOOM_BASE_LINE_CENTER = 0;
    public static final int ZOOM_BASE_LINE_LEFT = 1;
    public static final int ZOOM_BASE_LINE_RIGHT = 2;
    public static final int DEFAULT_DISPLAY_FROM = 0;
    public static final int DEFAULT_DISPLAY_NUMBER = 40;
    public static final int DEFAULT_MIN_DISPLAY_NUMBER = 20;
    public static final int DEFAULT_ZOOM_BASE_LINE = ZOOM_BASE_LINE_CENTER;
    public static final boolean DEFAULT_AUTO_CALC_VALUE_RANGE = true;
    public static final int DEFAULT_STICK_SPACING = 3;
    public static final int DEFAULT_COLORED_STICK_STYLE_WITH_BORDER = 0;
    
    
    public static final int DEFAULT_COLORED_STICK_STYLE_NO_BORDER = 1;
    public static final int DEFAULT_COLORED_STICK_STYLE = DEFAULT_COLORED_STICK_STYLE_NO_BORDER;
    /**
     * <p>
     * 默认表示柱条的边框颜色
     * </p>
     */
    public static final int DEFAULT_STICK_BORDER_COLOR = Color.RED;
    /**
     * <p>
     * 默认表示柱条的填充颜色
     * </p>
     */
    public static final int DEFAULT_STICK_FILL_COLOR = Color.RED;
    /**
     * <p>
     * 绘制柱条用的数据
     * </p>
     */
    protected IChartData<IStickEntity> a;
    /**
     * <p>
     * 柱条的最大表示数
     * </p>
     */
    protected int b;
    /**
     * <p>
     * -K线图Y的最大表示值
     * </p>
     */
    protected double d;
    /**
     * <p>
     * K线图Y的最小表示值
     * </p>
     */
    protected double minValue;
    protected int h = DEFAULT_DISPLAY_FROM;
    protected int displayNumber = DEFAULT_DISPLAY_NUMBER;
    protected int p = DEFAULT_MIN_DISPLAY_NUMBER;
    protected int zoomBaseLine = DEFAULT_ZOOM_BASE_LINE;
    protected boolean autoCalcValueRange = DEFAULT_AUTO_CALC_VALUE_RANGE;
    protected int stickSpacing = DEFAULT_STICK_SPACING;
    /**
     * <p>
     * 绘制成交量柱条用的数据
     * </p>
     */
    protected IChartData<IStickEntity> volStickData;
    /**
     * <p>
     * Y的最大表示值
     * </p>
     */
    protected double volMaxValue;
    /**
     * <p>
     * Y的最小表示值
     * </p>
     */
    protected double volMinValue;

    /**
     * <p>
     * 默认x轴点的个数--用来计算x轴的最终宽度
     * </p>
     */
    private int defaultNumber = DEFAULT_DISPLAY_NUMBER;
    /**
     * <p>
     * 阳线的边框颜色
     * </p>
     */
    private int positiveStickBorderColor = DEFAULT_POSITIVE_STICK_BORDER_COLOR;
    /**
     * <p>
     * 阳线的填充颜色
     * </p>
     */
    private int positiveStickFillColor = DEFAULT_POSITIVE_STICK_FILL_COLOR;
    /**
     * <p>
     * 阴线的边框颜色
     * </p>
     */

    private int negativeStickBorderColor = DEFAULT_NEGATIVE_STICK_BORDER_COLOR;
    /**
     * <p>
     * 阴线的填充颜色
     * </p>
     */
    private int negativeStickFillColor = DEFAULT_NEGATIVE_STICK_FILL_COLOR;
    /**
     * <p>
     * 十字线显示颜色（十字星,一字平线,T形线的情况）
     * </p>
     */
    private int crossStarColor = DEFAULT_CROSS_STAR_COLOR;
    /**
     * <p>
     * 绘制线条用的数据
     * </p>
     */
    private List<LineEntity<DateValueEntity>> mMALinesData;
    private int coloredStickStyle = DEFAULT_COLORED_STICK_STYLE_NO_BORDER;
    /**
     * <p>
     * 表示柱条的边框颜色
     * </p>
     */
    private int stickBorderColor = DEFAULT_STICK_BORDER_COLOR;
    /**
     * <p>
     * 表示柱条的填充颜色
     * </p>
     */
    private int stickFillColor = DEFAULT_STICK_FILL_COLOR;
    private float candelStickWidth;

    
    
    public static final int MACD_DISPLAY_TYPE_STICK = 1 << 0;
    public static final int MACD_DISPLAY_TYPE_LINE = 1 << 1;
    public static final int MACD_DISPLAY_TYPE_LINE_STICK = 1 << 2;
    public static final int DEFAULT_POSITIVE_STICK_COLOR = Color.RED;
    public static final int DEFAULT_NEGATIVE_STICK_COLOR = Color.BLUE;
    public static final int DEFAULT_MACD_LINE_COLOR = Color.RED;
    public static final int DEFAULT_DIFF_LINE_COLOR = Color.WHITE;
    public static final int DEFAULT_DEA_LINE_COLOR = 0xfff4be04;
    public static final int DEFAULT_MACD_DISPLAY_TYPE = MACD_DISPLAY_TYPE_LINE_STICK;

    private int positiveStickColor = DEFAULT_POSITIVE_STICK_FILL_COLOR;
    private int negativeStickColor = DEFAULT_NEGATIVE_STICK_FILL_COLOR;
    private int macdLineColor = DEFAULT_MACD_LINE_COLOR;
    private int diffLineColor = DEFAULT_DIFF_LINE_COLOR;
    private int deaLineColor = DEFAULT_DEA_LINE_COLOR;
    private int macdDisplayType = DEFAULT_MACD_DISPLAY_TYPE;

    protected double macdMaxValue;
    protected double macdMinValue;
    protected IChartData<IStickEntity> macdStickData;
    
    
    /**
     * <p>
     * 绘制KDJ线条用的数据
     * </p>
     */
    private List<LineEntity<DateValueEntity>> kdjLinesData;
    protected double kdjMaxValue;
    protected double kdjMinValue;

    

    /**
     * 默认均价线与价格线线宽 *
     */
    private static final float DEFAULT_LINE_WIDTH = 2f;

    

    private List<LineEntity<DateValueEntity>> mBattleForceLinesData;
    private List<LineEntity<DateValueEntity>> mBattleMoraleLinesData;
    private double mBattleMoraleMinValue, mBattleMoraleMaxValue;
    
    
    private boolean mIsFirstMoveEvent;      
    private MotionEvent mDownEvent;
    private short mTouchEventType;      
    private int mMovedStickOneTimeCount;     
    private Timer mTouchEventTimer;
    private float mDistanceBetweenFingers;
    
    private int mInitDefaultNumber = defaultNumber;
    private boolean mIsZoomed = false;

    

    public KLineChartViewX(Context context) {
        super(context);
    }

    public KLineChartViewX(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KLineChartViewX(Context context, AttributeSet attrs) {
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
        
        if (this.autoCalcValueRange) {
            
            a();
            
            b();
            
            c();
            
            d();
            e();
        }
        
        initVolAxisY();

        
        initCandleAxisY();

        
        if (BOTTOM_CHART_TYPE_KDJ == c) {

            
            
            initKDJAxisY();

            
            super.onDraw(canvas);
            f(canvas);
            g(canvas);

            
            h(canvas);
        } else if (BOTTOM_CHART_TYPE_MACD == c) {

            
            
            initMACDAxisY();

            
            super.onDraw(canvas);
            f(canvas);
            i(canvas);

            
            o(canvas);
            p(canvas);
        } else if (BOTTOM_CHART_TYPE_BATTLE_MORALE == c) {
            
            setBattleMoraleLatitudeTitles(null);
            super.onDraw(canvas);
            
            q(canvas);
            r(canvas);
        }
        
        s(canvas);

        
        t(canvas);
        sss(canvas);

        xx(canvas);
        
        yy(canvas);
        zz(canvas);

        mm(canvas);
    }

    /**
     * <p>
     * 初始化成交量的X轴的坐标值
     * </p>
     */
    protected void initVolAxisX() {
        List<String> titleX = new ArrayList<String>();
        if (null != volStickData && volStickData.size() > 0) {
            float average = displayNumber / this.getLongitudeNum();
            for (int i = 0; i < this.getLongitudeNum(); i++) {
                int index = (int) Math.floor(i * average);
                if (index > displayNumber - 1) {
                    index = displayNumber - 1;
                }
                index = index + h;
                titleX.add(String.valueOf(volStickData.get(index).getDate())
                        .substring(4));
            }
            titleX.add(String.valueOf(
                    volStickData.get(h + displayNumber - 1).getDate()).substring(4));
        }
        super.setVolLongitudeTitles(titleX);
    }

    /**
     * -初始化成交量的Y轴标题
     */
    protected void initVolAxisY() {
        this.a();
        List<String> titleY = new ArrayList<String>();
        String value;
        String midValue;
        String valueDW;
        int yi = 10000 * 10000;
        String str = "";
        if (volMaxValue > yi) {
            value = Utility.formatDouble2(volMaxValue / yi);
            str = spacesNum(value);
            value = new StringBuffer().append(str + value).toString();

            midValue = Utility.formatDouble2((volMaxValue / 2) / yi);
            str = spacesNum(midValue);
            midValue = new StringBuffer().append(str + midValue).toString();

            valueDW = " 亿手";
        } else if (volMaxValue > 10000) {
            value = Utility.formatDouble2(volMaxValue / 10000);
            str = spacesNum(value);
            value = new StringBuffer().append(str + value).toString();

            midValue = Utility.formatDouble2((volMaxValue / 2) / 10000);
            str = spacesNum(midValue);
            midValue = new StringBuffer().append(str + midValue).toString();

            valueDW = " 万手";
        } else {
            value = Utility.formatDouble2(volMaxValue);
            str = spacesNum(value);
            value = new StringBuffer().append(str + value).toString();

            midValue = Utility.formatDouble2(volMaxValue / 2);
            str = spacesNum(midValue);
            midValue = new StringBuffer().append(str + midValue).toString();

            valueDW = "  手";
        }
        titleY.add(valueDW);
        titleY.add(midValue);
        titleY.add(value);
        super.setVolLatitudeTitles(titleY);
    }

    /**
     * <p>
     * 初始化MACD X轴的坐标值
     * </p>
     */
    protected void initMACDAxisX() {
        List<String> titleX = new ArrayList<String>();
        if (null != macdStickData && macdStickData.size() > 0) {
            float average = displayNumber / this.getLongitudeNum();
            for (int i = 0; i < this.getLongitudeNum(); i++) {
                int index = (int) Math.floor(i * average);
                if (index > displayNumber - 1) {
                    index = displayNumber - 1;
                }
                index = index + h;
                titleX.add(String.valueOf(macdStickData.get(index).getDate())
                        .substring(4));
            }
            titleX.add(String.valueOf(
                    macdStickData.get(h + displayNumber - 1)
                            .getDate()).substring(4));
        }
        super.setVolLongitudeTitles(titleX);
    }

    /**
     * -初始化MACD的Y轴标题
     */
    protected void initMACDAxisY() {
        List<String> titleY = new ArrayList<String>();

        titleY.add(String.valueOf(Utility.format(macdMinValue)));
        titleY.add(String.valueOf(Utility.format(0)));
        titleY.add(String.valueOf(Utility.format(macdMaxValue)));

        
        
        

        super.setBattleMoraleLatitudeTitles(titleY);
    }

    /**
     * <p>
     * 初始化KDJ X轴的坐标值
     * </p>
     */
    protected void initKDJAxisX() {
        List<String> titleX = new ArrayList<String>();
        if (null != kdjLinesData && kdjLinesData.size() > 0) {
            float average = displayNumber / this.getLongitudeNum();
            for (int i = 0; i < this.getLongitudeNum(); i++) {
                int index = (int) Math.floor(i * average);
                if (index > displayNumber - 1) {
                    index = displayNumber - 1;
                }
                index = index + h;
                LineEntity<DateValueEntity> lineEntity = kdjLinesData.get(0);
                titleX.add(String.valueOf(
                        lineEntity.getLineData().get(index).getDate())
                        .substring(4));
            }
            LineEntity<DateValueEntity> lineEntity = kdjLinesData.get(0);
            titleX.add(String.valueOf(
                    lineEntity.getLineData()
                            .get(h + displayNumber - 1).getDate())
                    .substring(4));
        }
        super.setVolLongitudeTitles(titleX);
    }

    /**
     * -初始化KDJ的Y轴标题
     */
    protected void initKDJAxisY() {
        List<String> titleY = new ArrayList<String>();
        titleY.add(String.valueOf(" 0"));
        titleY.add(String.valueOf(" 50"));
        titleY.add(String.valueOf(" 100"));

        super.setBattleMoraleLatitudeTitles(titleY);
    }

    /**
     * <p>
     * initialize degrees on X axis
     * </p>
     * <p>
     * X軸の目盛を初期化
     * </p>
     * <p>
     * 初始化K线图X轴的坐标值
     * </p>
     */
    protected void initCandleAxisX() {
        List<String> titleX = new ArrayList<String>();
        if (null != a && a.size() > 0) {
            float average = b / this.getLongitudeNum();
            for (int i = 0; i < this.getLongitudeNum(); i++) {
                int index = (int) Math.floor(i * average);
                if (index > b - 1) {
                    index = b - 1;
                }
                if (i == 0) {
                    titleX.add("");
                } else {
                    String date = String
                            .valueOf(a.get(index).getDate());
                    titleX.add(date.substring(0, 4) + "-"
                            + date.substring(4, 6));
                }
            }
            String date = String.valueOf(a.get(b - 1)
                    .getDate());
            titleX.add(date.substring(0, 4) + "-" + date.substring(4, 6));
        }
        super.setCandleLongitudeTitles(titleX);
    }

    /**
     * <p>
     * initialize degrees on Y axis
     * </p>
     * <p>
     * Y軸の目盛を初期化
     * </p>
     * <p>
     * 初始化K线图Y轴的坐标值
     * </p>
     */
    protected void initCandleAxisY() {
        List<String> titleY = new ArrayList<String>();
        float unit = 1;
        int yi = 10000 * 10000;
        if (d > yi) {
            unit = yi;
        } else if (unit > 10000) {
            unit = 10000;
        }
        float average = (float) (((d - minValue) / 3) / unit);
        
        for (int i = 0; i < 3; i++) {
            String value = String.valueOf(Utility.formatDouble2PointTwo(minValue + i * average));
            titleY.add(" " + value);
        }
        
        String value = String.valueOf(Utility.formatDouble2PointTwo(d / unit));
        titleY.add(" " + value);

        super.setCandleLatitudeTitles(titleY);
    }

    /**
     * -获取被点中的坐标对应的K线位置
     *
     * @return
     */
    public int getSelectedIndex() {
        if (null == super.getTouchPoint()) {
            return -1;
        }
        float graduate = Float.valueOf(super.getAxisXGraduate(super.getTouchPoint().x));
        int index = (int) Math.floor(graduate * defaultNumber);

        if (index >= displayNumber) {
            index = displayNumber - 1;
        } else if (index < 0) {
            index = 0;
        }
        return index;
    }

    /**
     * -根据标题的长度计算并返回空格数
     *
     * @param value
     * @return
     */
    private String spacesNum(String value) {
        String str = "";
        if (value.length() < super.getLatitudeMaxTitleLength()) {
            for (int j = 0; j < super.getLatitudeMaxTitleLength()
                    - value.length(); j++) {
                str = str + " ";
            }
        }
        return str;
    }

    protected void calcCandleDataValueRange() {
        double maxValue = Double.MIN_VALUE;
        double minValue = Double.MAX_VALUE;
        if (null == a || a.size() < 1) {
            return;
        }
        IMeasurable first;
        first = this.a.get(h);
        
        if (first.getHigh() == 0 && first.getLow() == 0) {

        } else {
            maxValue = first.getHigh();
            minValue = first.getLow();
        }

        for (int i = h; i < h + displayNumber; i++) {
            IMeasurable stick;
            stick = this.a.get(i);
            if (null == stick) {
                continue;
            }
            if (stick.getLow() < minValue) {
                minValue = stick.getLow();
            }

            if (stick.getHigh() > maxValue) {
                maxValue = stick.getHigh();
            }
        }

        this.d = maxValue;
        this.minValue = minValue;
    }

    protected void calcCandleValueRangePaddingZero() {
        double maxValue = this.d;
        double minValue = this.minValue;

        if ((long) maxValue > (long) minValue) {
            if ((maxValue - minValue) < 10 && minValue > 1) {
                this.d = (long) (maxValue + 1);
                this.minValue = (long) (minValue - 1);
            } else {
                this.d = (long) (maxValue + (maxValue - minValue) * 0.1);
                this.minValue = (long) (minValue - (maxValue - minValue) * 0.1);
                if (this.minValue < 0) {
                    this.minValue = 0;
                }
            }
        } else if ((long) maxValue == (long) minValue) {
            if (maxValue <= 10 && maxValue > 1) {
                this.d = maxValue + 1;
                this.minValue = minValue - 1;
            } else if (maxValue <= 100 && maxValue > 10) {
                this.d = maxValue + 10;
                this.minValue = minValue - 10;
            } else if (maxValue <= 1000 && maxValue > 100) {
                this.d = maxValue + 100;
                this.minValue = minValue - 100;
            } else if (maxValue <= 10000 && maxValue > 1000) {
                this.d = maxValue + 1000;
                this.minValue = minValue - 1000;
            } else if (maxValue <= 100000 && maxValue > 10000) {
                this.d = maxValue + 10000;
                this.minValue = minValue - 10000;
            } else if (maxValue <= 1000000 && maxValue > 100000) {
                this.d = maxValue + 100000;
                this.minValue = minValue - 100000;
            } else if (maxValue <= 10000000 && maxValue > 1000000) {
                this.d = maxValue + 1000000;
                this.minValue = minValue - 1000000;
            } else if (maxValue <= 100000000 && maxValue > 10000000) {
                this.d = maxValue + 10000000;
                this.minValue = minValue - 10000000;
            }
        } else {
            this.d = 0;
            this.minValue = 0;
        }

    }

    protected void calcCandleValueRangeFormatForAxis() {
        
        long rate = (long) (this.d - this.minValue) / (this.latitudeNum);
        String strRate = String.valueOf(rate);
        float first = Integer.parseInt(String.valueOf(strRate.charAt(0))) + 1.0f;
        if (first > 0 && strRate.length() > 1) {
            float second = Integer.parseInt(String.valueOf(strRate.charAt(1)));
            if (second < 5) {
                first = first - 0.5f;
            }
            rate = (long) (first * Math.pow(10, strRate.length() - 1));
        } else {
            rate = 1;
        }
        
        if (this.latitudeNum > 0
                && (long) (this.d - this.minValue)
                % (this.latitudeNum * rate) != 0) {
            
            this.d = (long) this.d
                    + (this.latitudeNum * rate)
                    - ((long) (this.d - this.minValue) % (this.latitudeNum * rate));
        }
    }

    /**
     * -计算烛蜡图的值范围
     */
    protected void b() {
        if (this.a != null && this.a.size() > 0) {
            this.calcCandleDataValueRange();
            
        } else {
            this.d = 0;
            this.minValue = 0;
        }

        if (this.d - this.minValue >= 12) {
            this.latitudeNum = 4;
        } else if (this.d - this.minValue >= 6) {
            this.latitudeNum = 3;
        } else {
            this.latitudeNum = 2;
        }


    }

    /**
     * -计算成交量的值范围
     */
    protected void a() {
        if (null == this.volStickData) {
            this.volMaxValue = 0;
            this.volMinValue = 0;
            return;
        }

        if (this.volStickData.size() > 0) {
            this.calcDataValueRange();
            this.calcValueRangePaddingZero();
        } else {
            this.volMaxValue = 0;
            this.volMinValue = 0;
        }

    }

    protected void calcDataValueRange() {

        double maxValue = Double.MIN_VALUE;
        double minValue = Double.MAX_VALUE;

        IMeasurable first = this.volStickData.get(h);
        
        if (first.getHigh() == 0 && first.getLow() == 0) {

        } else {
            maxValue = first.getHigh();
            minValue = first.getLow();
        }

        
        for (int i = this.h; i < this.h + this.displayNumber; i++) {
            IMeasurable stick = this.volStickData.get(i);
            if (null == stick) {
                continue;
            }
            if (stick.getLow() < minValue) {
                minValue = stick.getLow();
            }
            if (stick.getHigh() > maxValue) {
                maxValue = stick.getHigh();
            }
        }
        for (int i = 0; i < mBattleForceLinesData.size(); i++) {
            LineEntity<DateValueEntity> line = mBattleForceLinesData.get(i);
            if (line == null) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }

            for (int j = h; j < h + displayNumber; j++) {
                if (null == lineData.get(j)) {
                    continue;
                }
                double value = lineData.get(j).getValue();
                if (value > maxValue) {
                    maxValue = value;
                } else if (value < minValue) {
                    minValue = value;
                }
            }
        }

        this.volMaxValue = maxValue;
        this.volMinValue = minValue;
    }

    protected void calcValueRangePaddingZero() {
        double maxValue = this.volMaxValue;
        double minValue = this.volMinValue;

        if ((long) maxValue > (long) minValue) {
            if ((maxValue - minValue) < 10 && minValue > 1) {
                this.volMaxValue = (long) (maxValue + 1);
                this.volMinValue = (long) (minValue - 1);
            } else {
                this.volMaxValue = (long) (maxValue + (maxValue - minValue) * 0.1);
                this.volMinValue = (long) (minValue - (maxValue - minValue) * 0.1);
                if (this.volMinValue < 0) {
                    this.volMinValue = 0;
                }
            }
        } else if ((long) maxValue == (long) minValue) {
            if (maxValue <= 10 && maxValue > 1) {
                this.volMaxValue = maxValue + 1;
                this.volMinValue = minValue - 1;
            } else if (maxValue <= 100 && maxValue > 10) {
                this.volMaxValue = maxValue + 10;
                this.volMinValue = minValue - 10;
            } else if (maxValue <= 1000 && maxValue > 100) {
                this.volMaxValue = maxValue + 100;
                this.volMinValue = minValue - 100;
            } else if (maxValue <= 10000 && maxValue > 1000) {
                this.volMaxValue = maxValue + 1000;
                this.volMinValue = minValue - 1000;
            } else if (maxValue <= 100000 && maxValue > 10000) {
                this.volMaxValue = maxValue + 10000;
                this.volMinValue = minValue - 10000;
            } else if (maxValue <= 1000000 && maxValue > 100000) {
                this.volMaxValue = maxValue + 100000;
                this.volMinValue = minValue - 100000;
            } else if (maxValue <= 10000000 && maxValue > 1000000) {
                this.volMaxValue = maxValue + 1000000;
                this.volMinValue = minValue - 1000000;
            } else if (maxValue <= 100000000 && maxValue > 10000000) {
                this.volMaxValue = maxValue + 10000000;
                this.volMinValue = minValue - 10000000;
            }
        } else {
            this.volMaxValue = 0;
            this.volMinValue = 0;
        }

    }

    protected void calcValueRangeFormatForAxis() {
        
        long rate = (long) (this.volMaxValue - this.volMinValue)
                / (this.latitudeNum);
        String strRate = String.valueOf(rate);
        float first = Integer.parseInt(String.valueOf(strRate.charAt(0))) + 1.0f;
        if (first > 0 && strRate.length() > 1) {
            float second = Integer.parseInt(String.valueOf(strRate.charAt(1)));
            if (second < 5) {
                first = first - 0.5f;
            }
            rate = (long) (first * Math.pow(10, strRate.length() - 1));
        } else {
            rate = 1;
        }
        
        if (this.latitudeNum > 0
                && (long) (this.volMaxValue - this.volMinValue)
                % (this.latitudeNum * rate) != 0) {
            
            this.volMaxValue = (long) this.volMaxValue
                    + (this.latitudeNum * rate)
                    - ((long) (this.volMaxValue - this.volMinValue) % (this.latitudeNum * rate));
        }
    }

    /**
     * <p>
     * draw sticks
     * </p>
     * <p>
     * スティックを書く
     * </p>
     * <p>
     * 绘制柱条
     * </p>
     *
     * @param canvas
     */
    protected void s(Canvas canvas) {
        if (null == a) {
            return;
        }
        if (a.size() <= 0) {
            return;
        }

        Paint paintPositive = new Paint();
        paintPositive.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paintPositive.setStrokeCap(Paint.Cap.ROUND);
        paintPositive.setStyle(Paint.Style.STROKE);
        paintPositive.setAntiAlias(true);
        paintPositive.setColor(positiveStickFillColor);

        Paint paintUpLimit = new Paint();
        paintUpLimit.setStyle(Paint.Style.FILL);
        paintUpLimit.setAntiAlias(true);
        paintUpLimit.setColor(0xFFEA00EA);

        Paint paintNegative = new Paint();
        paintNegative.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paintNegative.setStrokeCap(Paint.Cap.ROUND);
        paintNegative.setAntiAlias(true);
        paintNegative.setColor(negativeStickFillColor);

        Paint paintCross = new Paint();
        paintCross.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paintCross.setStrokeCap(Paint.Cap.ROUND);
        paintCross.setAntiAlias(true);
        paintCross.setColor(crossStarColor);

        float stickWidth = getDataQuadrantPaddingWidth() / defaultNumber - stickSpacing;
        candelStickWidth = stickWidth;

        if (axisYPosition == AXIS_Y_POSITION_LEFT) {
            float stickX = getDataQuadrantPaddingStartX() + borderWidth;
            float detailTextHeight = getLongitudeFontSize();
            for (int i = h; i < h + displayNumber; i++) {
                OHLCEntity ohlc = (OHLCEntity) a.get(i);
                if (null == ohlc) {
                    stickX = stickX + stickSpacing + stickWidth;
                    continue;
                }
                float openY = (float) ((1f - (ohlc.getOpen() - minValue)
                        / (d - minValue))
                        * (getCandleDataQuadrantPaddingHeight() - detailTextHeight)
                        + getCandleDataQuadrantPaddingStartY() + detailTextHeight);
                float highY = (float) ((1f - (ohlc.getHigh() - minValue)
                        / (d - minValue))
                        * (getCandleDataQuadrantPaddingHeight() - detailTextHeight)
                        + getCandleDataQuadrantPaddingStartY() + detailTextHeight);
                float lowY = (float) ((1f - (ohlc.getLow() - minValue)
                        / (d - minValue))
                        * (getCandleDataQuadrantPaddingHeight() - detailTextHeight)
                        + getCandleDataQuadrantPaddingStartY() + detailTextHeight);
                float closeY = (float) ((1f - (ohlc.getClose() - minValue)
                        / (d - minValue))
                        * (getCandleDataQuadrantPaddingHeight() - detailTextHeight)
                        + getCandleDataQuadrantPaddingStartY() + detailTextHeight);

                if (ohlc.getOpen() <= ohlc.getClose()) {
                    
                    if (stickWidth >= 2f) {
                        if ((openY - closeY) >= 2) {
                            canvas.drawRect(stickX, closeY, stickX + stickWidth, openY, paintPositive);
                        } else {
                            canvas.drawLine(stickX, closeY, stickX + stickWidth, closeY, paintPositive);
                        }
                    }
                    canvas.drawLine(
                            stickX + stickWidth / 2f, highY,
                            stickX + stickWidth / 2f, closeY, paintPositive
                    );
                    canvas.drawLine(
                            stickX + stickWidth / 2f, openY,
                            stickX + stickWidth / 2f, lowY, paintPositive
                    );
                    if (i > 0) {
                        double preClose = ((OHLCEntity) a.get(i - 1)).getClose();
                        if ((ohlc.getClose() - preClose) / ohlc.getClose() > 0.09) {
                            float stickHeight = openY - closeY;
                            if (stickHeight >= 2f) {
                                canvas.drawRect(
                                        stickX + stickWidth / 4, closeY + stickHeight / 4,
                                        (float) (stickX + stickWidth * 0.75), openY - stickHeight / 4, paintUpLimit
                                );
                            } else {
                                canvas.drawLine(
                                        stickX + stickWidth / 4, closeY,
                                        (float) (stickX + stickWidth * 0.75), closeY, paintUpLimit
                                );
                            }
                        }
                    }
                } else if (ohlc.getOpen() > ohlc.getClose()) {
                    
                    if (stickWidth >= 2f) {
                        canvas.drawRect(
                                stickX, openY, stickX + stickWidth,
                                closeY, paintNegative
                        );
                    }
                    canvas.drawLine(
                            stickX + stickWidth / 2f, highY,
                            stickX + stickWidth / 2f, lowY, paintNegative
                    );
                }














                
                stickX = stickX + stickSpacing + stickWidth;
            }
        }
    }

    /**
     * <p>
     * draw lines
     * </p>
     * <p>
     * ラインを書く
     * </p>
     * <p>
     * 绘制线条——————MA线
     * </p>
     *
     * @param canvas
     */
    protected void t(Canvas canvas) {
        if (null == this.mMALinesData) {
            return;
        }
        
        float lineLength = getDataQuadrantPaddingWidth() / defaultNumber - 1;
        
        float startX;
        Paint paint = new Paint();
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        redPaint.setStrokeCap(Paint.Cap.ROUND);
        redPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 1));
        redPaint.setAntiAlias(true);
        redPaint.setColor(Color.RED);
        Paint greenPaint = new Paint();
        greenPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        greenPaint.setStrokeCap(Paint.Cap.ROUND);
        greenPaint.setAntiAlias(true);
        greenPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 1));
        greenPaint.setColor(Color.GREEN);
        
        for (int i = 0; i < mMALinesData.size(); i++) {
            LineEntity<DateValueEntity> line = mMALinesData.get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false || "离兵线".equals(line.getTitle())) {
                continue;
            }
            if (Configuration.ORIENTATION_PORTRAIT
                            == getContext().getResources().getConfiguration().orientation) {
                if ("兵".equals(line.getTitle()) || "帅".equals(line.getTitle())) {
                    continue;
                }
            }
            boolean isDottedLine = false;
            if ("压力|方向".contains(line.getTitle())) {
                isDottedLine = true;
                paint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 1));
            } else {
                paint.setPathEffect(null);
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null || lineData.size() <= h) {
                continue;
            }
            paint.setColor(line.getLineColor());
            
            PointF ptFirst = null;
            
            startX = getDataQuadrantPaddingStartX() + lineLength / 2;
            int haveDataIndex = h;
            double preValue = -1;
            for (int j = h; j < h + displayNumber; j++) {
                if (null == lineData.get(j) || 0 == lineData.get(j).getValue()) {
                    startX = startX + 1 + lineLength;
                    haveDataIndex = j + 1;
                    continue;
                }
                double value = lineData.get(j).getValue();
                
                float valueY = (float) ((1f - (value - minValue)
                        / (d - minValue))
                        * (getCandleDataQuadrantPaddingHeight() - getLongitudeFontSize()))
                        + getCandleDataQuadrantPaddingStartY() + getLongitudeFontSize();
                
                if (j > haveDataIndex && null != ptFirst
                        && value <= d && value >= minValue) {
                    if (isDottedLine) {
                        if ("压力".equals(line.getTitle())) {
                            canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, paint);
                        } else {
                            if (-1 != preValue && value > preValue) {
                                canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, redPaint);
                            } else {
                                canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, greenPaint);
                            }
                        }
                    } else {
                        canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, paint);
                    }
                }
                
                ptFirst = new PointF(startX, valueY);
                startX = startX + 1 + lineLength;
                preValue = value;
            }
        }
    }

    /**
     * -绘制成交量图
     *
     * @param canvas
     */
    protected void yy(Canvas canvas) {
        if (null == volStickData) {
            return;
        }
        if (volStickData.size() == 0) {
            return;
        }
        float stickWidth = (getDataQuadrantPaddingWidth() / defaultNumber) / 2;
        float stickSpacing = getDataQuadrantPaddingWidth() / defaultNumber - stickWidth;
        float stickX = getStickDataQuadrantPaddingStartX() + stickSpacing / 2;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        for (int i = h; i < h + displayNumber; i++) {
            ColoredStickEntity entity = (ColoredStickEntity) volStickData.get(i);

            float highY = (float) ((1f - (entity.getHigh() - volMinValue)
                    / (volMaxValue - volMinValue))
                    * (getStickDataQuadrantPaddingHeight()) + getStickDataQuadrantPaddingStartY());
            float lowY = (float) ((1f - (entity.getLow() - volMinValue)
                    / (volMaxValue - volMinValue))
                    * (getStickDataQuadrantPaddingHeight()) + getStickDataQuadrantPaddingStartY());

            paint.setColor(entity.getColor());
            
            if (stickWidth >= 2f) {
                canvas.drawRect(stickX, highY, stickX + stickWidth, lowY, paint);
            } else {
                canvas.drawLine(stickX, highY, stickX, lowY, paint);
            }

            
            stickX = stickX + stickSpacing + stickWidth;
        }
    }

    /**
     * '
     * 绘制多空力量的三条线：多头、空头、人心
     *
     * @param canvas
     */
    protected void zz(Canvas canvas) {
        if (null == this.mBattleForceLinesData) {
            return;
        }
        
        float lineLength = getDataQuadrantPaddingWidth() / defaultNumber - 1;
        
        float startX;

        
        for (int i = 0; i < mBattleForceLinesData.size(); i++) {
            LineEntity<DateValueEntity> line = (LineEntity<DateValueEntity>) mBattleForceLinesData
                    .get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false || "力".equals(line.getTitle())) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null || lineData.size() < h) {
                continue;
            }

            Paint paint = new Paint();
            paint.setColor(line.getLineColor());
            paint.setAntiAlias(true);

            
            PointF ptFirst = null;
            
            startX = getDataQuadrantPaddingStartX() + lineLength / 2;
            int firstHaveData = h;
            for (int j = h; j < h + displayNumber; j++) {
                if (null == lineData.get(j) || 0 == lineData.get(j).getValue()) {
                    startX = startX + lineLength + 1;
                    firstHaveData += 1;
                    continue;
                }

                double value = lineData.get(j).getValue();
                
                float valueY = (float) ((1f - (value - volMinValue)
                        / (volMaxValue - volMinValue))
                        * (getStickDataQuadrantPaddingHeight() - getLongitudeFontSize()))
                        + getStickDataQuadrantPaddingStartY() + getLongitudeFontSize();

                
                if (j > firstHaveData) {
                    canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, paint);
                }
                
                ptFirst = new PointF(startX, valueY);
                startX = startX + lineLength + 1;
            }
        }
    }

    /*
     *
     * @param event
     *
     * @see android.view.View#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < getDataQuadrantPaddingStartX()
                || event.getX() > getDataQuadrantPaddingEndX()) {
            return false;
        }

        if (event.getY() < getDataQuadrantPaddingStartY()
                || event.getY() > getDataQuadrantPaddingEndY()) {
            return false;
        }

        
        if (event.getX() > getStickDataQuadrantStartX()
                && event.getY() > getBattleMoraleQuadrantStartY()
                && event.getAction() == MotionEvent.ACTION_DOWN) {
            c = (c + 1) % 3;
            invalidate();

            return true;
        }

        if (isZoomInClickEvent(event) && event.getAction() == MotionEvent.ACTION_DOWN) {
            zoomIn(displayNumber / 2);
            invalidate();

            return true;
        } else if (isZoomOutClickEvent(event) && event.getAction() == MotionEvent.ACTION_DOWN) {
            zoomOut(displayNumber / 2);
            invalidate();

            return true;
        }

        
        if (!isResponseTouchEvent()) {
            return false;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownEvent = MotionEvent.obtain(event);
                mIsFirstMoveEvent = false;
                mMovedStickOneTimeCount = 0;
                mTouchEventType = -1;
                
                postDelayed(new LongClickRunnable(), 1500);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mDistanceBetweenFingers = spaceFingerDistance(event);
                mTouchEventType = TOUCH_EVENT_TYPE_ZOOM;
                mIsZoomed = false;

                break;
            case MotionEvent.ACTION_MOVE:
                
                if (!mIsFirstMoveEvent && mTouchEventType == -1
                        && Math.abs(mDownEvent.getX() - event.getX()) > 10) {
                    mIsFirstMoveEvent = true;
                    mTouchEventType = TOUCH_EVENT_TYPE_MOVE_STICK;
                }
                switch (mTouchEventType) {
                    case TOUCH_EVENT_TYPE_CROSS_lINE:
                        drawCrossLine(event);

                        break;
                    case TOUCH_EVENT_TYPE_MOVE_STICK:
                        float flingDistance = event.getX() - mDownEvent.getX();
                        int stickNum = (int) (Math.abs(flingDistance) / candelStickWidth);
                        if (stickNum > displayNumber / 3) {
                            stickNum = displayNumber / 3;
                        }
                        
                        if (mMovedStickOneTimeCount > displayNumber / 3) {
                            break;
                        }
                        if (flingDistance > 0) {
                            moveStickForward(stickNum);
                        } else {
                            moveStickBackward(stickNum);
                        }
                        super.invalidate();
                        mMovedStickOneTimeCount += stickNum;

                        break;
                    case TOUCH_EVENT_TYPE_ZOOM:
                        float zoomDistance = spaceFingerDistance(event) - mDistanceBetweenFingers;
                        float minZoomWidth = getStickDataQuadrantPaddingWidth() / 8;
                        if (Math.abs(zoomDistance) > minZoomWidth
                                && zoomDistance > 0 && !mIsZoomed) {
                            
                            zoomIn(zoomCount(zoomDistance));
                            mIsZoomed = true;
                        } else if (Math.abs(zoomDistance) > minZoomWidth
                                && zoomDistance < 0 && !mIsZoomed) {
                            
                            zoomOut(zoomCount(zoomDistance));
                            mIsZoomed = true;
                        }

                        break;
                }

                break;
            case MotionEvent.ACTION_UP:
                super.touchPoint = null;
                super.invalidate();
                super.notifyEventAll(this);

                break;
        }

        return true;
    }

    private boolean isZoomInClickEvent(MotionEvent event) {
        int btnWidthHeight = (int) ScreenUtils.dpToPx(getContext(), 30);
        int startX = (int) (getStickDataQuadrantPaddingStartX()
                + ScreenUtils.dpToPx(getContext(), 15));
        int startY = (int) (getStickDataQuadrantStartY() - ScreenUtils.dpToPx(getContext(), 15));
        Rect rDstZoomIn = new Rect(
                startX, startY - btnWidthHeight, startX + btnWidthHeight, startY
        );
        if (rDstZoomIn.contains((int) event.getX(), (int) event.getY())) {
            return true;
        }
        return false;
    }

    private boolean isZoomOutClickEvent(MotionEvent event) {
        int btnWidthHeight = (int) ScreenUtils.dpToPx(getContext(), 30);
        int startX = (int) (getStickDataQuadrantPaddingStartX()
                + ScreenUtils.dpToPx(getContext(), 15));
        int startY = (int) (getStickDataQuadrantStartY() - ScreenUtils.dpToPx(getContext(), 15));
        Rect rDstZoomOut = new Rect(
                startX + btnWidthHeight * 2, startY - btnWidthHeight,
                startX + btnWidthHeight * 3, startY
        );
        if (rDstZoomOut.contains((int) event.getX(), (int) event.getY())) {
            return true;
        }
        return false;
    }

    private void drawCrossLine(MotionEvent event) {
        
        super.touchPoint = new PointF(event.getX(), event.getY());

        
        
        int index = getSelectedIndex();
        
        float closeY = (float) ((1f - (((OHLCEntity) a.get(index + h)).getClose() - minValue)
                / (d - minValue))
                * (getCandleDataQuadrantPaddingHeight() - getLongitudeFontSize())
                + getCandleDataQuadrantPaddingStartY()) + getLongitudeFontSize();
        float middleX = super.getDataQuadrantPaddingStartX() + (float) (index)
                / (float) defaultNumber * super.getDataQuadrantPaddingWidth() + candelStickWidth / 2;
        event.setLocation(middleX, closeY);

        
        super.touchPoint = new PointF(middleX, closeY);

        super.refreshVerticalLine(event);
        super.notifyEventAll(this);
    }

    /**
     * -向前移动K线，每次移动stickNum根
     */
    private boolean moveStickForward(int stickNum) {
        
        if (0 == h) {
            return false;
        }

        if (h - stickNum < 0) {
            h = 0;
        } else {
            h -= stickNum;
        }
        return true;
    }

    /**
     * -向后移动K线，每次移动stickNum根
     */
    private boolean moveStickBackward(int stickNum) {
        
        if (a.size() == h + displayNumber) {
            return false;
        }
        if (h + stickNum + displayNumber >= a.size()) {
            h = a.size() - displayNumber;
        } else {
            h += stickNum;
        }
        return true;
    }

    /**
     * -计算MACD值范围
     */
    protected void c() {
        if (macdStickData == null) {
            return;
        }
        if (macdStickData.size() <= 0) {
            return;
        }
        double macdMaxValue = Double.MIN_VALUE;
        double macdMinValue = Double.MAX_VALUE;

        IMeasurable first = macdStickData.get(h);
        macdMaxValue = Math.max(first.getHigh(), macdMaxValue);
        macdMinValue = Math.min(first.getLow(), macdMinValue);
        
        for (int i = h; i < h + displayNumber; i++) {
            if (i > macdStickData.size() - 1) {
                return;
            }

            IMeasurable macd = macdStickData.get(i);
            macdMaxValue = Math.max(macd.getHigh(), macdMaxValue);
            macdMinValue = Math.min(macd.getLow(), macdMinValue);
        }
        macdMaxValue = Math.abs(macdMaxValue);
        macdMinValue = Math.abs(macdMinValue);
        macdMaxValue = macdMaxValue > macdMinValue ? macdMaxValue : macdMinValue;

        this.macdMaxValue = macdMaxValue;

        
        this.macdMinValue = -macdMaxValue;
    }

    /**
     * -绘制成交量描述
     */
    protected void xx(Canvas canvas) {
        String text = " 多空力量";
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getLongitudeFontSize());
        paint.setColor(Color.GRAY);
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        redPaint.setAntiAlias(true);
        redPaint.setTextSize(getLongitudeFontSize());
        redPaint.setColor(Color.RED);
        Paint greenPaint = new Paint();
        greenPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setTextSize(getLongitudeFontSize());

        float startY = getStickDataQuadrantStartY() + ScreenUtils.dpToPx(getContext(), 2);
        float startX = getDataQuadrantPaddingStartX();
        canvas.drawText(text, startX, startY, paint);

        if (null == this.mBattleForceLinesData) {
            return;
        }

        
        float textWidth = paint.measureText(text);
        
        paint.setTextSize(getLongitudeFontSize());

        
        for (int i = 0; i < mBattleForceLinesData.size(); i++) {
            LineEntity<DateValueEntity> line
                    = (LineEntity<DateValueEntity>) mBattleForceLinesData.get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }
            paint.setColor(line.getLineColor());
            text = "   " + line.getTitle() + ": ";
            int index = lineData.size() - 1;
            if (-1 != getSelectedIndex()) {
                index = getSelectedIndex() + h;
            }
            if (null != lineData.get(index)) {
                text = text + Utility.formatVolume1WithoutUnit(lineData.get(index).getValue());
            }
            canvas.drawText(text, startX + textWidth, startY, paint);
            textWidth = textWidth + paint.measureText(text);
            if (index > 0 &&
                    lineData.get(index).getValue() < lineData.get(index - 1).getValue()) {
                canvas.drawText("↓", startX + textWidth, startY, greenPaint);
            } else if (index > 0 &&
                    lineData.get(index).getValue() > lineData.get(index - 1).getValue()) {
                canvas.drawText("↑", startX + textWidth, startY, redPaint);
            }
        }
        text = "   量: ";
        int index = volStickData.size() - 1;
        if (-1 != getSelectedIndex()) {
            index = getSelectedIndex() + h;
        }
        ColoredStickEntity entity = (ColoredStickEntity) volStickData.get(index);
        if (null != volStickData.get(index)) {
            text = text + Utility.formatVolume1WithoutUnit(entity.getHigh());
        }
        paint.setColor(Color.RED);
        canvas.drawText(text, startX + textWidth, startY, paint);
    }

    /**
     * -绘制MACD参数及详细描述
     */

    protected void i(Canvas canvas) {
        if (macdStickData == null || null == macdStickData.get(macdStickData.size() - 1)) {
            return;
        }
        if (macdStickData.size() <= 0) {
            return;
        }
        String text = " MACD (12,26,9)";
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getLongitudeFontSize());
        paint.setColor(getLatitudeFontColor());
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        redPaint.setAntiAlias(true);
        redPaint.setTextSize(getLongitudeFontSize());
        redPaint.setColor(Color.RED);
        Paint greenPaint = new Paint();
        greenPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setTextSize(getLongitudeFontSize());
        float startY = getBattleMoraleQuadrantStartY() + ScreenUtils.dpToPx(getContext(), 2);
        float startX = getDataQuadrantPaddingStartX();

        canvas.drawText(text, startX, startY, paint);

        
        float textWidth = paint.measureText(text);
        int index = macdStickData.size() - 1;
        if (-1 != getSelectedIndex()) {
            index = getSelectedIndex() + h;
        }
        if (null == macdStickData.get(index)) {
            return;
        }
        MACDEntity entity = ((MACDEntity) macdStickData.get(index));
        text = "   DIF:";
        String dif = Utility.formatDouble2PointTwo(entity.getDiff());
        paint.setColor(diffLineColor);
        text = text + dif;

        canvas.drawText(text, startX + textWidth, startY, paint);
        textWidth += paint.measureText(text);
        if (index > 0 &&
                entity.getDiff() < ((MACDEntity) macdStickData.get(index - 1)).getDiff()) {
            canvas.drawText("↓", startX + textWidth, startY, greenPaint);
        } else if (index > 0 &&
                entity.getDiff() > ((MACDEntity) macdStickData.get(index - 1)).getDiff()) {
            canvas.drawText("↑", startX + textWidth, startY, redPaint);
        }

        text = "   DEA:";
        String dea = Utility.formatDouble2PointTwo(entity.getDea());
        paint.setColor(deaLineColor);
        text = text + dea;

        canvas.drawText(text, startX + textWidth, startY, paint);
        textWidth += paint.measureText(text);
        if (index > 0 &&
                entity.getDea() < ((MACDEntity) macdStickData.get(index - 1)).getDea()) {
            canvas.drawText("↓", startX + textWidth, startY, greenPaint);
        } else if (index > 0 &&
                entity.getDea() > ((MACDEntity) macdStickData.get(index - 1)).getDea()) {
            canvas.drawText("↑", startX + textWidth, startY, redPaint);
        }

        text = "   MACD:";
        String macd = Utility.formatDouble2PointTwo(entity.getMacd());
        try {
            paint.setColor(kdjLinesData.get(2).getLineColor());
        } catch (Exception e) {
            paint.setColor(0xff9932CD);
        }
        text = text + macd;

        canvas.drawText(text, startX + textWidth, startY, paint);
        textWidth += paint.measureText(text);
        if (index > 0 &&
                entity.getMacd() < ((MACDEntity) macdStickData.get(index - 1)).getMacd()) {
            canvas.drawText("↓", startX + textWidth, startY, greenPaint);
        } else if (index > 0 &&
                entity.getMacd() > ((MACDEntity) macdStickData.get(index - 1)).getMacd()) {
            canvas.drawText("↑", startX + textWidth, startY, redPaint);
        }
    }

    /**
     * -绘制MACD参数及详细描述
     */
    protected void g(Canvas canvas) {
        float startY = getBattleMoraleQuadrantStartY() + ScreenUtils.dpToPx(getContext(), 2);
        float startX = getDataQuadrantPaddingStartX();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getLongitudeFontSize());
        paint.setColor(getLatitudeFontColor());
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        redPaint.setAntiAlias(true);
        redPaint.setTextSize(getLongitudeFontSize());
        redPaint.setColor(Color.RED);
        Paint greenPaint = new Paint();
        greenPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setTextSize(getLongitudeFontSize());

        String text = " KDJ (9,3,3)";
        canvas.drawText(text, startX, startY, paint);

        if (null == this.kdjLinesData) {
            return;
        }

        
        float textWidth = paint.measureText(text);
        
        paint.setTextSize(getLongitudeFontSize());

        
        for (int i = 0; i < kdjLinesData.size(); i++) {
            LineEntity<DateValueEntity> line = (LineEntity<DateValueEntity>) kdjLinesData.get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }
            paint.setColor(line.getLineColor());
            if (0 == i) {
                text = "   K:";
            } else if (1 == i) {
                text = "   D:";
            } else if (2 == i) {
                text = "   J:";
            }
            int index = lineData.size() - 1;
            if (-1 != getSelectedIndex()) {
                index = getSelectedIndex() + h;
            }
            if (null != lineData.get(index)) {
                text = text
                        + Utility.formatDouble2PointTwo(lineData.get(index).getValue());
            }
            canvas.drawText(text, startX + textWidth, startY, paint);
            textWidth = textWidth + paint.measureText(text);
            if (index > 0 &&
                    lineData.get(index).getValue() < lineData.get(index - 1).getValue()) {
                canvas.drawText("↓", startX + textWidth, startY, greenPaint);
            } else if (index > 0 &&
                    lineData.get(index).getValue() > lineData.get(index - 1).getValue()) {
                canvas.drawText("↑", startX + textWidth, startY, redPaint);
            }
        }
    }

    /**
     * 绘制MA线的最新数据详细
     *
     * @param canvas
     */
    protected void sss(Canvas canvas) {
        float startY = getCandleDataQuadrantPaddingStartY() + ScreenUtils.dpToPx(getContext(), 5);
        float startX = getDataQuadrantPaddingStartX();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setTextSize(getLongitudeFontSize());
        paint.setColor(Color.GRAY);
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        redPaint.setAntiAlias(true);
        redPaint.setTextSize(getLongitudeFontSize());
        redPaint.setColor(Color.RED);
        Paint greenPaint = new Paint();
        greenPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setTextSize(getLongitudeFontSize());

        String text = " 多空方向";
        canvas.drawText(text, startX, startY, paint);

        if (null == this.mMALinesData) {
            return;
        }

        
        float textWidth = paint.measureText(text);
        
        paint.setTextSize(getLongitudeFontSize());

        for (int i = 0; i < mMALinesData.size(); i++) {
            LineEntity<DateValueEntity> line = mMALinesData.get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }
            if (Configuration.ORIENTATION_PORTRAIT
                    == getContext().getResources().getConfiguration().orientation) {
                if ("兵".equals(line.getTitle()) || "帅".equals(line.getTitle())) {
                    continue;
                }
            }
            short roseOrDrop = 1;     
            paint.setColor(line.getLineColor());
            text = "    " + line.getTitle() + ": ";
            int index = lineData.size() - 1;
            if (-1 != getSelectedIndex()) {
                index = getSelectedIndex() + h;
            }
            if (index > 0 &&
                    lineData.get(index).getValue() > lineData.get(index - 1).getValue()) {
                roseOrDrop = 1;
            } else if (index > 0 &&
                    lineData.get(index).getValue() < lineData.get(index - 1).getValue()) {
                roseOrDrop = -1;
            }
            if (null != lineData.get(index)) {
                text = text + Utility.formatDouble2PointTwo(Math.abs(lineData.get(index).getValue()));
            }
















            if ("方向".equals(line.getTitle())) {
                if (1 == roseOrDrop) {
                    canvas.drawText(text, startX + textWidth, startY, redPaint);
                } else if (-1 == roseOrDrop) {
                    canvas.drawText(text, startX + textWidth, startY, greenPaint);
                }
            } else {
                canvas.drawText(text, startX + textWidth, startY, paint);
            }
            textWidth = textWidth + paint.measureText(text);
            if (1 == roseOrDrop) {
                canvas.drawText("↑ ", startX + textWidth, startY, redPaint);
            } else if (-1 == roseOrDrop) {
                canvas.drawText("↓ ", startX + textWidth, startY, greenPaint);
            }

        }
    }

    /**
     * -绘制底部框的中线
     *
     * @param canvas
     */
    protected void f(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getLatitudeColor());

        
        float lineLength = getDataQuadrantPaddingWidth();
        
        float startX = getDataQuadrantPaddingStartX();
        float y = ((float) 1 / 2 * getBattleMoraleQuadrantHeight() + getBattleMoraleQuadrantStartY());
        canvas.drawLine(startX, y, startX + lineLength, y, paint);
    }

    /**
     * -绘制KDJ的0和100线
     *
     * @param canvas
     */
    protected void drawKDJLine(Canvas canvas) {
        if (!(kdjMinValue < 0 && kdjMaxValue > 100)) {
            return;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getLatitudeColor());

        
        float lineLength = getDataQuadrantPaddingWidth();
        
        float startX = getDataQuadrantPaddingStartX();
        float y = (float) (getStickDataQuadrantPaddingStartX() + ((kdjMinValue - 100) / (kdjMaxValue - kdjMinValue))
                * getStickDataQuadrantPaddingHeight());
        canvas.drawLine(startX, y, startX + lineLength, y, paint);

        
        y = (float) (getStickDataQuadrantPaddingStartX() + (kdjMaxValue / (kdjMaxValue - kdjMinValue))
                * getStickDataQuadrantPaddingHeight());
        canvas.drawLine(startX, y, startX + lineLength, y, paint);
    }

    /**
     * -绘制MACD柱状图
     *
     * @param canvas
     */
    protected void o(Canvas canvas) {
        if (macdStickData == null) {
            return;
        }
        if (macdStickData.size() <= 0) {
            return;
        }

        if (macdDisplayType == MACD_DISPLAY_TYPE_LINE) {
            this.drawMACDLine(canvas);
            return;
        }

        Paint paintStick = new Paint();
        paintStick.setAntiAlias(true);

        float stickWidth = getDataQuadrantPaddingWidth() / defaultNumber - stickSpacing;
        float stickX = getDataQuadrantPaddingStartX() + borderWidth;

        
        for (int i = h; i < h + displayNumber; i++) {
            if (i > macdStickData.size() - 1) {
                return;
            }
            MACDEntity stick = (MACDEntity) macdStickData.get(i);

            float highY = 0;
            float lowY = 0;
            if (stick.getMacd() == 0) {
                
                continue;
            }
            
            if (stick.getMacd() > 0) {
                paintStick.setColor(positiveStickColor);
                highY = (float) ((1 - stick.getMacd() / macdMaxValue)
                        * (getStickDataQuadrantPaddingHeight() / 2) + getBattleMoraleQuadrantStartY());
                lowY = ((float) 1 / 2 * getBattleMoraleQuadrantPaddingHeight()
                        + getBattleMoraleQuadrantStartY());
            } else if (stick.getMacd() < 0) {
                paintStick.setColor(negativeStickColor);
                highY = ((float) 1 / 2 * getBattleMoraleQuadrantPaddingHeight()
                        + getBattleMoraleQuadrantStartY());

                lowY = (float) (highY + Math.abs(stick.getMacd())
                        / macdMaxValue
                        * (getBattleMoraleQuadrantPaddingHeight() / 2));
            }

            if (macdDisplayType == MACD_DISPLAY_TYPE_STICK) {
                
                if (stickWidth >= 2) {
                    canvas.drawRect(stickX, highY, stickX + stickWidth, lowY, paintStick);
                } else {
                    canvas.drawLine(stickX, highY, stickX, lowY, paintStick);
                }
            } else if (macdDisplayType == MACD_DISPLAY_TYPE_LINE_STICK) {
                canvas.drawLine(
                        stickX + stickWidth / 2, highY, stickX
                                + stickWidth / 2, lowY, paintStick
                );
            }

            
            stickX = stickX + stickSpacing + stickWidth;
        }
    }

    protected void drawDiffLine(Canvas canvas) {

        if (null == this.macdStickData) {
            return;
        }
        Paint mPaintStick = new Paint();
        mPaintStick.setAntiAlias(true);
        mPaintStick.setColor(diffLineColor);

        
        float lineLength = getDataQuadrantPaddingWidth() / defaultNumber
                - borderWidth;
        
        float startX = getDataQuadrantPaddingStartX() + lineLength / 2;
        
        PointF ptFirst = null;
        for (int i = h; i < h + displayNumber; i++) {
            if (i > macdStickData.size() - 1) {
                return;
            }

            MACDEntity entity = (MACDEntity) macdStickData.get(i);
            
            float valueY = (float) ((1f - (entity.getDiff() - macdMinValue)
                    / (macdMaxValue - macdMinValue)) * getBattleMoraleQuadrantPaddingHeight())
                    + getBattleMoraleQuadrantStartY();
            
            
            
            
            
            
            
            
            
            
            
            
            
            

            
            if (i > h) {
                canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY,
                        mPaintStick);
            }
            
            ptFirst = new PointF(startX, valueY);
            startX = startX + 1 + lineLength;
        }
    }

    protected void drawDeaLine(Canvas canvas) {

        Paint mPaintStick = new Paint();
        mPaintStick.setAntiAlias(true);
        mPaintStick.setColor(deaLineColor);
        
        float lineLength = getDataQuadrantPaddingWidth() / defaultNumber - borderWidth;
        
        float startX = getDataQuadrantPaddingStartX() + lineLength / 2;
        
        PointF ptFirst = null;
        for (int i = h; i < h + displayNumber; i++) {
            if (i > macdStickData.size() - 1) {
                return;
            }

            MACDEntity entity = (MACDEntity) macdStickData.get(i);
            
            float valueY = (float) ((1f - (entity.getDea() - macdMinValue)
                    / (macdMaxValue - macdMinValue)) * getBattleMoraleQuadrantPaddingHeight())
                    + getBattleMoraleQuadrantStartY();
            
            
            
            
            
            
            
            
            
            
            
            
            if (i > h) {
                canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY,
                        mPaintStick);
            }
            
            ptFirst = new PointF(startX, valueY);
            startX = startX + 1 + lineLength;
        }
    }

    protected void drawMACDLine(Canvas canvas) {
        Paint mPaintStick = new Paint();
        mPaintStick.setAntiAlias(true);
        mPaintStick.setColor(macdLineColor);

        
        float lineLength = getDataQuadrantPaddingWidth() / defaultNumber - 1;
        
        float startX = getDataQuadrantPaddingStartX() + lineLength / 2;
        
        PointF ptFirst = null;
        for (int i = h; i < h + displayNumber; i++) {
            if (i > macdStickData.size() - 1) {
                return;
            }

            MACDEntity entity = (MACDEntity) macdStickData.get(i);
            
            float valueY = (float) ((1f - (entity.getMacd() - macdMinValue)
                    / (macdMaxValue - macdMinValue)) * getBattleMoraleQuadrantPaddingHeight())
                    + getBattleMoraleQuadrantStartY() + borderWidth;

            
            if (i > h) {
                canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY,
                        mPaintStick);
            }
            
            ptFirst = new PointF(startX, valueY);
            startX = startX + 1 + lineLength;
        }
    }

    protected void p(Canvas canvas) {

        if (macdStickData == null) {
            return;
        }
        if (macdStickData.size() <= 0) {
            return;
        }

        drawDeaLine(canvas);
        drawDiffLine(canvas);
    }

    /**
     * -计算KDJ值范围
     */
    protected void d() {
        if (kdjLinesData == null) {
            return;
        }
        if (kdjLinesData.size() <= 0) {
            return;
        }

        kdjMaxValue = kdjMinValue = 0;
        for (int i = 0; i < kdjLinesData.size(); i++) {
            LineEntity<DateValueEntity> line = (LineEntity<DateValueEntity>) kdjLinesData.get(i);
            if (line == null) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }

            for (int j = this.h; j < this.h + this.displayNumber; j++) {
                double value = lineData.get(j).getValue();
                if (value > kdjMaxValue) {
                    kdjMaxValue = value;
                } else if (value < kdjMinValue) {
                    kdjMinValue = value;
                }
            }
        }
    }

    protected void e() {
        if (mBattleMoraleLinesData == null) {
            return;
        }
        if (mBattleMoraleLinesData.size() <= 0) {
            return;
        }

        mBattleMoraleMinValue = mBattleMoraleMaxValue = 0;
        for (int i = 0; i < mBattleMoraleLinesData.size(); i++) {
            LineEntity<DateValueEntity> line = mBattleMoraleLinesData.get(i);
            if (line == null) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }

            for (int j = h; j < h + displayNumber; j++) {
                if (null == lineData.get(j)) {
                    continue;
                }
                double value = lineData.get(j).getValue();
                if (value > mBattleMoraleMaxValue) {
                    mBattleMoraleMaxValue = value;
                } else if (value < mBattleMoraleMinValue) {
                    mBattleMoraleMinValue = value;
                }
            }
        }
        if (mBattleMoraleMaxValue < 100) {
            mBattleMoraleMaxValue = 100;
        }
        if (mBattleMoraleMinValue > -5) {
            mBattleMoraleMinValue = -5;
        }
    }

    /**
     * -绘制KDJ线条
     *
     * @param canvas
     */
    protected void h(Canvas canvas) {
        if (null == this.kdjLinesData) {
            return;
        }
        
        float lineLength = getDataQuadrantPaddingWidth() / defaultNumber - 1;
        
        float startX;

        
        for (int i = 0; i < kdjLinesData.size(); i++) {
            LineEntity<DateValueEntity> line = kdjLinesData.get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }

            Paint paint = new Paint();
            paint.setColor(line.getLineColor());
            paint.setAntiAlias(true);

            
            PointF ptFirst = null;
            
            startX = getDataQuadrantPaddingStartX() + lineLength / 2;
            for (int j = this.h; j < this.h + this.displayNumber; j++) {
                double value = lineData.get(j).getValue();
                
                float valueY = (float) ((1f - (value - kdjMinValue)
                        / (kdjMaxValue - kdjMinValue)) * getBattleMoraleQuadrantHeight())
                        + getBattleMoraleQuadrantStartY();

                
                if (j > h) {
                    canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, paint);
                }
                
                ptFirst = new PointF(startX, valueY);
                startX = startX + lineLength + 1;
            }
        }
    }

    /**
     * 绘制多空士气，其中两根线是用来绘制彩带的
     *
     * @param canvas
     */
    protected void q(Canvas canvas) {
        
        float[] frameLine = new float[]{98, 82, 16, 0};
        Paint innerFramePaint = new Paint();
        innerFramePaint.setColor(Color.RED);
        innerFramePaint.setStrokeWidth(ScreenUtils.dpToPx(getContext(), 0.2f));
        Paint outerFramePaint = new Paint();
        outerFramePaint.setColor(Color.DKGRAY);
        Paint yTitlePaint = new Paint();
        yTitlePaint.setAntiAlias(true);
        yTitlePaint.setColor(getLatitudeFontColor());
        yTitlePaint.setTextSize(getLatitudeFontSize());
        for (int i = 0; i < frameLine.length; i++) {
            float valueY = (float) ((1f - (frameLine[i] - mBattleMoraleMinValue)
                    / (mBattleMoraleMaxValue - mBattleMoraleMinValue))
                    * (getBattleMoraleQuadrantHeight() - getLongitudeFontSize() / 2))
                    + getBattleMoraleQuadrantStartY() + getLongitudeFontSize() / 2;











            float offset = borderWidth + dataQuadrantPaddingLeft + axisYTitleQuadrantWidth - 5;
            canvas.drawText(String.valueOf(frameLine[i]), offset - yTitlePaint.measureText(String.valueOf(frameLine[i])),
                    valueY + getLongitudeFontSize() / 2, yTitlePaint);
            frameLine[i] = valueY;
        }
        canvas.drawRect(
                getDataQuadrantPaddingStartX(), frameLine[0],
                getDataQuadrantPaddingEndX(), frameLine[1],
                outerFramePaint
        );
        canvas.drawRect(
                getDataQuadrantPaddingStartX(), frameLine[2],
                getDataQuadrantPaddingEndX(), frameLine[3],
                outerFramePaint
        );

        if (null == this.mBattleMoraleLinesData || mBattleMoraleLinesData.size() < 1) {
            return;
        }
        
        float lineLength = getDataQuadrantPaddingWidth() / defaultNumber - 1;
        
        float startX;

        
        for (int i = 0; i < 2; i++) {
            LineEntity<DateValueEntity> line = mBattleMoraleLinesData.get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(line.getLineColor());

            
            PointF ptFirst = null;
            
            startX = getDataQuadrantPaddingStartX() + lineLength / 2;
            int firstHaveData = h;
            for (int j = this.h; j < this.h + this.displayNumber; j++) {
                if (null == lineData.get(j) || 0 == lineData.get(j).getValue()) {
                    startX = startX + lineLength + 1;
                    firstHaveData += 1;
                    continue;
                }
                double value = lineData.get(j).getValue();
                value = fixBattleMoraleValue(value);
                
                float valueY = (float) ((1f - (value - mBattleMoraleMinValue)
                        / (mBattleMoraleMaxValue - mBattleMoraleMinValue))
                        * (getBattleMoraleQuadrantHeight() - getLongitudeFontSize() / 2))
                        + getBattleMoraleQuadrantStartY() + getLongitudeFontSize() / 2;

                
                if (j > firstHaveData) {
                    canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, paint);
                }
                
                ptFirst = new PointF(startX, valueY);
                startX = startX + lineLength + 1;
            }
        }
        
        startX = getDataQuadrantPaddingStartX() + lineLength / 2;
        
        Path areaPath = new Path();
        List<DateValueEntity> mainLineData = mBattleMoraleLinesData.get(2).getLineData();
        List<DateValueEntity> trendLineData = mBattleMoraleLinesData.get(3).getLineData();
        Paint mainPaint = new Paint();
        Paint trendPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(Color.parseColor("#9f019c"));
        trendPaint.setAntiAlias(true);
        trendPaint.setColor(Color.parseColor("#0f919e"));
        float lastMainY = 0;
        float lastTrendY = 0;
        float lastX = getDataQuadrantPaddingStartX() + lineLength / 2;
        short currentTrend = -1;   
        short preTrend = currentTrend;      
        for (int i = h; i < h + displayNumber; i++) {
            if (null == mainLineData.get(i) || null == trendLineData.get(i)
                    || (0 == mainLineData.get(i).getValue() && 0 == trendLineData.get(i).getValue())) {
                startX = startX + lineLength + 1;
                continue;
            }
            double mainValue = mainLineData.get(i).getValue();
            double trendValue = trendLineData.get(i).getValue();
            double preMainValue;
            double preTrendValue;
            if (0 == i) {
                preMainValue = mainValue;
                preTrendValue = trendValue;
            } else {
                preMainValue = mainLineData.get(i - 1).getValue();
                preTrendValue = trendLineData.get(i - 1).getValue();
            }
            if (mainValue > preMainValue && trendValue > preTrendValue) {
                currentTrend = 0;
            } else if (mainValue < preMainValue && trendValue < preTrendValue) {
                currentTrend = 1;
            } else {
                currentTrend = -1;
            }
            mainValue = fixBattleMoraleValue(mainValue);
            trendValue = fixBattleMoraleValue(trendValue);
            preMainValue = fixBattleMoraleValue(preMainValue);
            preTrendValue = fixBattleMoraleValue(preTrendValue);

            
            float mainYValue = (float) ((1f - (mainValue - mBattleMoraleMinValue)
                    / (mBattleMoraleMaxValue - mBattleMoraleMinValue))
                    * (getBattleMoraleQuadrantHeight() - getLongitudeFontSize() / 2))
                    + getBattleMoraleQuadrantStartY() + getLongitudeFontSize() / 2;
            float trendYValue = (float) ((1f - (trendValue - mBattleMoraleMinValue)
                    / (mBattleMoraleMaxValue - mBattleMoraleMinValue))
                    * (getBattleMoraleQuadrantHeight() - getLongitudeFontSize() / 2))
                    + getBattleMoraleQuadrantStartY() + getLongitudeFontSize() / 2;
            if (i == h) {
                
                lastMainY = mainYValue;
                lastTrendY = trendYValue;
            }
            if (-1 == currentTrend) {
                if (0 == preTrend) {
                    areaPath.close();
                    canvas.drawPath(areaPath, mainPaint);
                } else if (1 == preTrend) {
                    areaPath.close();
                    canvas.drawPath(areaPath, trendPaint);
                }
                if (mainValue > preMainValue) {
                    canvas.drawLine(lastX, lastMainY, startX, mainYValue, mainPaint);
                } else {
                    canvas.drawLine(lastX, lastMainY, startX, mainYValue, trendPaint);
                }
                if (trendValue > preTrendValue) {
                    canvas.drawLine(lastX, lastTrendY, startX, trendYValue, mainPaint);
                } else {
                    canvas.drawLine(lastX, lastTrendY, startX, trendYValue, trendPaint);
                }
            } else {
                if (0 == currentTrend) {
                    if (0 != preTrend && null != areaPath) {
                        if (1 == preTrend) {
                            areaPath.close();
                            canvas.drawPath(areaPath, trendPaint);
                        }
                        areaPath = new Path();
                        areaPath.moveTo(startX, mainYValue);
                    }
                } else {
                    if (1 != preTrend && null != areaPath) {
                        if (0 == preTrend) {
                            areaPath.close();
                            canvas.drawPath(areaPath, mainPaint);
                        }
                        areaPath = new Path();
                        areaPath.moveTo(startX, mainYValue);
                    }
                }

                
                if (i == h) {
                    areaPath.moveTo(startX, mainYValue);
                    areaPath.lineTo(startX, trendYValue);
                    areaPath.moveTo(startX, mainYValue);
                } else {
                    areaPath.lineTo(startX, mainYValue);
                    areaPath.lineTo(startX, trendYValue);
                    areaPath.lineTo(lastX, lastTrendY);
                    areaPath.close();
                    areaPath.moveTo(startX, mainYValue);
                }
                if (i == h + displayNumber - 1) {
                    areaPath.close();
                    if (0 == currentTrend) {
                        canvas.drawPath(areaPath, mainPaint);
                    } else if (1 == currentTrend) {
                        canvas.drawPath(areaPath, trendPaint);
                    }
                }
            }
            preTrend = currentTrend;
            lastX = startX;
            lastMainY = mainYValue;
            lastTrendY = trendYValue;
            startX = startX + lineLength + 1;
        }
    }

    private double fixBattleMoraleValue(double value) {
        if (value > 110) {
            value = 110;
        } else if (value < -10) {
            value = -10;
        }
        return value;
    }

    /**
     * 绘制多空士气详细数据
     *
     * @param canvas
     */
    protected void r(Canvas canvas) {
        float startY = getBattleMoraleQuadrantStartY() + ScreenUtils.dpToPx(getContext(), 2);
        float startX = getDataQuadrantPaddingStartX();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getLongitudeFontSize());
        paint.setColor(Color.GRAY);
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        redPaint.setAntiAlias(true);
        redPaint.setTextSize(getLongitudeFontSize());
        redPaint.setColor(Color.RED);
        Paint greenPaint = new Paint();
        greenPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(Color.GREEN);
        greenPaint.setTextSize(getLongitudeFontSize());

        String text = " 多空士气";
        canvas.drawText(text, startX, startY, paint);

        if (null == this.mBattleMoraleLinesData) {
            return;
        }

        
        float textWidth = paint.measureText(text);
        
        paint.setTextSize(getLongitudeFontSize());

        for (int i = 0; i < mBattleMoraleLinesData.size(); i++) {
            LineEntity<DateValueEntity> line = mBattleMoraleLinesData.get(i);
            if (line == null) {
                continue;
            }
            if (line.isDisplay() == false) {
                continue;
            }
            List<DateValueEntity> lineData = line.getLineData();
            if (lineData == null) {
                continue;
            }
            paint.setColor(line.getLineColor());
            text = "    " + line.getTitle() + ": ";
            int index = lineData.size() - 1;
            if (-1 != getSelectedIndex()) {
                index = getSelectedIndex() + h;
            }
            if (null != lineData.get(index)) {
                text = text + Utility.formatDouble2PointTwo(lineData.get(index).getValue());
            }
            canvas.drawText(text, startX + textWidth, startY, paint);
            textWidth = textWidth + paint.measureText(text);
            if (index > 0 &&
                    lineData.get(index).getValue() < lineData.get(index - 1).getValue()) {
                canvas.drawText("↓", startX + textWidth, startY, greenPaint);
            } else if (index > 0 &&
                    lineData.get(index).getValue() > lineData.get(index - 1).getValue()) {
                canvas.drawText("↑", startX + textWidth, startY, redPaint);
            }
        }
    }

    private void mm(Canvas canvas) {
        int btnWidthHeight = (int) ScreenUtils.dpToPx(getContext(), 30);
        int startX = (int) (getStickDataQuadrantPaddingStartX()
                + ScreenUtils.dpToPx(getContext(), 15));
        int startY = (int) (getStickDataQuadrantStartY() - ScreenUtils.dpToPx(getContext(), 15));
        Bitmap bZoomIn = BitmapFactory.decodeResource(getResources(), R.drawable.btn_zoom_in);
        Bitmap bZoomOut = BitmapFactory.decodeResource(getResources(), R.drawable.btn_zoom_out);
        Rect rDstZoomIn = new Rect(
                startX, startY - btnWidthHeight, startX + btnWidthHeight, startY
        );
        Rect rDstZoomOut = new Rect(
                startX + btnWidthHeight * 2, startY - btnWidthHeight,
                startX + btnWidthHeight * 3, startY
        );
        Rect srcRect = new Rect(0, 0, bZoomIn.getWidth(), bZoomIn.getHeight());
        Paint paint = new Paint();
        canvas.drawBitmap(bZoomIn, srcRect, rDstZoomIn, paint);
        canvas.drawBitmap(bZoomOut, srcRect, rDstZoomOut, paint);
    }

    class LongClickRunnable implements Runnable {
        @Override
        public void run() {
            if (!mIsFirstMoveEvent && mTouchEventType != TOUCH_EVENT_TYPE_ZOOM) {
                mTouchEventType = TOUCH_EVENT_TYPE_CROSS_lINE;
                drawCrossLine(mDownEvent);
            }
        }
    }

    private float spaceFingerDistance(MotionEvent event) {
        if (event.getPointerCount() <= 1) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 放大
     */
    protected void zoomIn(int sticks) {
        if (displayNumber <= mInitDefaultNumber / 4 || a.size() <= defaultNumber) {
            return;
        } else {
            h = h + sticks;
            displayNumber -= sticks;
            defaultNumber = displayNumber;
            if (h + displayNumber >= a.size()) {
                h = a.size() - displayNumber;
            }
        }
    }

    /**
     * 缩小
     */
    protected void zoomOut(int sticks) {
        if (displayNumber >= 2 * mInitDefaultNumber || a.size() <= defaultNumber) {
            return;
        } else {
            h = h - sticks;
            displayNumber += sticks;
            if (h < 0) {
                displayNumber -= h;
                h = 0;
            }
            defaultNumber = displayNumber;
        }
    }

    private int zoomCount(float zoomDistance) {
        if (zoomDistance < getStickDataQuadrantPaddingWidth() / 4) {
            return displayNumber / 4;
        } else if (zoomDistance < getStickDataQuadrantPaddingWidth() / 3) {
            return displayNumber / 2;
        } else {
            return (int) (displayNumber * 0.8);
        }
    }

    /**
     * @return the positiveStickColor
     */
    public int getPositiveStickColor() {
        return positiveStickColor;
    }

    /**
     * @param positiveStickColor the positiveStickColor to set
     */
    public void setPositiveStickColor(int positiveStickColor) {
        this.positiveStickColor = positiveStickColor;
    }

    /**
     * @return the negativeStickColor
     */
    public int getNegativeStickColor() {
        return negativeStickColor;
    }

    /**
     * @param negativeStickColor the negativeStickColor to set
     */
    public void setNegativeStickColor(int negativeStickColor) {
        this.negativeStickColor = negativeStickColor;
    }

    /**
     * @return the macdLineColor
     */
    public int getMacdLineColor() {
        return macdLineColor;
    }

    /**
     * @param macdLineColor the macdLineColor to set
     */
    public void setMacdLineColor(int macdLineColor) {
        this.macdLineColor = macdLineColor;
    }

    /**
     * @return the diffLineColor
     */
    public int getDiffLineColor() {
        return diffLineColor;
    }

    /**
     * @param diffLineColor the diffLineColor to set
     */
    public void setDiffLineColor(int diffLineColor) {
        this.diffLineColor = diffLineColor;
    }

    /**
     * @return the deaLineColor
     */
    public int getDeaLineColor() {
        return deaLineColor;
    }

    /**
     * @param deaLineColor the deaLineColor to set
     */
    public void setDeaLineColor(int deaLineColor) {
        this.deaLineColor = deaLineColor;
    }

    /**
     * @return the macdDisplayType
     */
    public int getMacdDisplayType() {
        return macdDisplayType;
    }

    /**
     * @param macdDisplayType the macdDisplayType to set
     */
    public void setMacdDisplayType(int macdDisplayType) {
        this.macdDisplayType = macdDisplayType;
    }

    public double getMacdMaxValue() {
        return macdMaxValue;
    }

    public void setMacdMaxValue(double macdMaxValue) {
        this.macdMaxValue = macdMaxValue;
    }

    public double getMacdMinValue() {
        return macdMinValue;
    }

    public void setMacdMinValue(double macdMinValue) {
        this.macdMinValue = macdMinValue;
    }

    public IChartData<IStickEntity> getMacdStickData() {
        return macdStickData;
    }

    public void setMacdStickData(IChartData<IStickEntity> macdStickData) {
        this.macdStickData = macdStickData;
    }

    /*
     * (non-Javadoc)
     *
     * @param value
     *
     * @see
     * com.thinkive.android.invest.interfaces.ITouchEventResponseX#notifyEvent
     * (TwoXYGridChartView)
     */
    @Override
    public void notifyEvent(TwoXYGridChartView chart) {
        KLineChartViewX kLineChart = (KLineChartViewX) chart;

        

        
        
        super.notifyEvent(chart);
        
        super.notifyEventAll(this);
    }

    /**
     * @return the coloredStickStyle
     */
    public int getColoredStickStyle() {
        return coloredStickStyle;
    }

    /**
     * @param coloredStickStyle the coloredStickStyle to set
     */
    public void setColoredStickStyle(int coloredStickStyle) {
        this.coloredStickStyle = coloredStickStyle;
    }

    /**
     * <p>
     * add a new stick data to sticks and refresh this chart
     * </p>
     * <p>
     * 新しいスティックデータを追加する，フラフをレフレシューする
     * </p>
     * <p>
     * 追加一条新数据并刷新当前图表
     * </p>
     *
     * @param entity <p>
     *               data
     *               </p>
     *               <p>
     *               データ
     *               </p>
     *               <p>
     *               新数据
     *               </p>
     */
    public void pushData(StickEntity entity) {
        if (null != entity) {
            addData(entity);
            super.postInvalidate();
        }
    }

    /**
     * <p>
     * add a new stick data to sticks
     * </p>
     * <p>
     * 新しいスティックデータを追加する
     * </p>
     * <p>
     * 追加一条新数据
     * </p>
     *
     * @param entity <p>
     *               data
     *               </p>
     *               <p>
     *               データ
     *               </p>
     *               <p>
     *               新数据
     *               </p>
     */
    public void addData(IStickEntity entity) {
        if (null != entity) {
            
            volStickData.add(entity);
            if (this.volMaxValue < entity.getHigh()) {
                this.volMaxValue = 100 + ((int) entity.getHigh()) / 100 * 100;
            }

        }
    }

    /**
     * @return the h
     */
    public int getDisplayFrom() {
        return h;
    }

    /**
     * @param displayFrom the h to set
     */
    public void setDisplayFrom(int displayFrom) {
        this.h = displayFrom;
    }

    /**
     * @return the displayNumber
     */
    public int getDisplayNumber() {
        return displayNumber;
    }

    /**
     * @param displayNumber the displayNumber to set
     */
    public void setDisplayNumber(int displayNumber) {
        this.displayNumber = displayNumber;
    }

    /**
     * @return the p
     */
    public int getMinDisplayNumber() {
        return p;
    }

    /**
     * @param minDisplayNumber the p to set
     */
    public void setMinDisplayNumber(int minDisplayNumber) {
        this.p = minDisplayNumber;
    }

    /**
     * @return the zoomBaseLine
     */
    public int getZoomBaseLine() {
        return zoomBaseLine;
    }

    /**
     * @param zoomBaseLine the zoomBaseLine to set
     */
    public void setZoomBaseLine(int zoomBaseLine) {
        this.zoomBaseLine = zoomBaseLine;
    }

    /**
     * @return the stickBorderColor
     */
    public int getStickBorderColor() {
        return stickBorderColor;
    }

    /**
     * @param stickBorderColor the stickBorderColor to set
     */
    public void setStickBorderColor(int stickBorderColor) {
        this.stickBorderColor = stickBorderColor;
    }

    /**
     * @return the stickFillColor
     */
    public int getStickFillColor() {
        return stickFillColor;
    }

    /**
     * @param stickFillColor the stickFillColor to set
     */
    public void setStickFillColor(int stickFillColor) {
        this.stickFillColor = stickFillColor;
    }

    /**
     * @return the volStickData
     */
    public IChartData<IStickEntity> getVolStickData() {
        return volStickData;
    }

    /**
     * @param volStickData the volStickData to set
     */
    public void setVolStickData(IChartData<IStickEntity> volStickData) {
        this.volStickData = volStickData;
    }

    /**
     * @return the d
     */
    public double getVolMaxValue() {
        return volMaxValue;
    }

    /**
     * @param volMaxValue the d to set
     */
    public void setVolMaxValue(double volMaxValue) {
        this.volMaxValue = volMaxValue;
    }

    /**
     * @return the minValue
     */
    public double getVolMinValue() {
        return volMinValue;
    }

    /**
     * @param volMinValue the minValue to set
     */
    public void setVolMinValue(double volMinValue) {
        this.volMinValue = volMinValue;
    }

    /**
     * @return the stickSpacing
     */
    public int getStickSpacing() {
        return stickSpacing;
    }

    /**
     * @param stickSpacing the stickSpacing to set
     */
    public void setStickSpacing(int stickSpacing) {
        this.stickSpacing = stickSpacing;
    }

    public IChartData<IStickEntity> getKLineData() {
        return a;
    }

    public void setKLineData(IChartData<IStickEntity> KLineData) {
        this.a = KLineData;

        
        new Thread(new Runnable() {
            @Override
            public void run() {
                macdStickData = new ListChartData<IStickEntity>(StockUtil.calcMACD(
                        a, 12, 26, 9));
                kdjLinesData = StockUtil.calcKDJ(a, 9);
            }
        }).start();
    }

    public void setBattleForceLinesData(List<LineEntity<DateValueEntity>> battleForceLinesData) {
        mBattleForceLinesData = battleForceLinesData;
    }

    /**
     * @return the mMALinesData
     */
    public List<LineEntity<DateValueEntity>> getMALinesData() {
        return mMALinesData;
    }

    /**
     * @param MALinesData the mMALinesData to set
     */
    public void setMALinesData(List<LineEntity<DateValueEntity>> MALinesData) {
        this.mMALinesData = MALinesData;
    }

    public int getMaxSticksNum() {
        return b;
    }

    public void setMaxSticksNum(int maxSticksNum) {
        this.b = maxSticksNum;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
        mInitDefaultNumber = defaultNumber;
    }

    public List<LineEntity<DateValueEntity>> getKdjLinesData() {
        return kdjLinesData;
    }

    public void setKdjLinesData(List<LineEntity<DateValueEntity>> kdjLinesData) {
        this.kdjLinesData = kdjLinesData;
    }

    public List<LineEntity<DateValueEntity>> getBattleMoraleLinesData() {
        return mBattleMoraleLinesData;
    }

    public void setBattleMoraleLinesData(List<LineEntity<DateValueEntity>> battleMoraleLinesData) {
        mBattleMoraleLinesData = battleMoraleLinesData;
    }
}

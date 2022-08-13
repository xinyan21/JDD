
package cn.limc.androidcharts.view;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import cn.limc.androidcharts.entity.TitleValueColorEntity;

import java.util.List;

/**
 * <p>
 * 描述：资金流量图
 * </p>
 * 
 * @author 叶青
 * @version 1.0
 * @corporation 思迪信息科技有限公司
 * @date 2014-6-09
 */
public class CapitalFlowsChart extends BaseChart {

    /**
     * <p>
     * default title
     * </p>
     * <p>
     * タイトルのデフォルト値
     * </p>
     * <p>
     * 默认图表标题
     * </p>
     */
    public static final String DEFAULT_TITLE = "CapitalFlowsChart";
    /**
     * <p>
     * default should display longitude lines
     * </p>
     * <p>
     * 経線を表示する
     * </p>
     * <p>
     * 默认是否显示经线
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_RADIUS = true;
    /**
     * <p>
     * default radius length
     * </p>
     * <p>
     * 半径の長さのデフォルト値
     * </p>
     * <p>
     * 默认半径长度
     * </p>
     */
    public static final int DEFAULT_RADIUS_LENGTH = 80;
    /**
     * <p>
     * default color for longitude lines
     * </p>
     * <p>
     * 経線の色のデフォルト値
     * </p>
     * <p>
     * 默认经线颜色
     * </p>
     */
    public static final int DEFAULT_RADIUS_COLOR = Color.BLACK;
    /**
     * <p>
     * default color for circle's border
     * </p>
     * <p>
     * 円弧の色のデフォルト値
     * </p>
     * <p>
     * 默认圆弧的颜色
     * </p>
     */
    public static final int DEFAULT_CIRCLE_BORDER_COLOR = Color.WHITE;
    /**
     * <p>
     * default position
     * </p>
     * <p>
     * 中心の位置のデフォルト値
     * </p>
     * <p>
     * 默认绘图中心位置
     * </p>
     */
    public static final Point DEFAULT_POSITION = new Point(0, 0);
    /**
     * <p>
     * Data
     * </p>
     * <p>
     * データ
     * </p>
     * <p>
     * 图表数据
     * </p>
     */
    private List<TitleValueColorEntity> data;
    /**
     * <p>
     * title
     * </p>
     * <p>
     * タイトル
     * </p>
     * <p>
     * 图表标题
     * </p>
     */
    private String title = DEFAULT_TITLE;
    /**
     * <p>
     * position
     * </p>
     * <p>
     * 中心位置
     * </p>
     * <p>
     * 绘图中心位置
     * </p>
     */
    private Point position = DEFAULT_POSITION;
    /**
     * <p>
     * radius length
     * </p>
     * <p>
     * 半径の長さ
     * </p>
     * <p>
     * 半径长度
     * </p>
     */
    private int radiusLength = DEFAULT_RADIUS_LENGTH;
    /**
     * <p>
     * Color for longitude lines
     * </p>
     * <p>
     * 経線の色
     * </p>
     * <p>
     * 经线颜色
     * </p>
     */
    private int radiusColor = DEFAULT_RADIUS_COLOR;
    /**
     * <p>
     * Color for circle's border
     * </p>
     * <p>
     * 円弧の色
     * </p>
     * <p>
     * 圆弧的颜色
     * </p>
     */
    private int circleBorderColor = DEFAULT_CIRCLE_BORDER_COLOR;
    /**
     * <p>
     * should display the longitude lines
     * </p>
     * <p>
     * 経線を表示する?
     * </p>
     * <p>
     * 是否显示经线
     * </p>
     */
    private boolean displayRadius = DEFAULT_DISPLAY_RADIUS;

    /**
     * <p>
     * 字体大小
     * </p>
     */
    private int textFontSize = 20;
    /**
     * <p>
     * title字体大小
     * </p>
     */
    private int titleFontSize = 20;

    /**
     * <p>
     * 屏幕宽度
     * </p>
     */
    private int screenWidth = 480;
    /**
     * <p>
     * Capital Flows字体大小
     * </p>
     */
    private int capitalflowsSize = 14;

    /*
     * (non-Javadoc)
     * @param context
     * @see cn.limc.cn.limc.androidcharts.view.BaseChart#BaseChart(Context)
     */
    public CapitalFlowsChart(Context context) {
        super(context);
    }

    /*
     * (non-Javadoc)
     * @param context
     * @param attrs
     * @param defStyle
     * @see cn.limc.cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
     * AttributeSet, int)
     */
    public CapitalFlowsChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * (non-Javadoc)
     * @param context
     * @param attrs
     * @see cn.limc.cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
     * AttributeSet)
     */
    public CapitalFlowsChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * (non-Javadoc) <p>Called when is going to draw this chart</p>
     * <p>チャートを書く前、メソッドを呼ぶ</p> <p>绘制图表时调用</p>
     * @param canvas
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TODO calculate radius length
        // radiusLength = (int) (((super.getWidth() * 4 / 5) / 2f) * 0.77);
        radiusLength = (int) ((super.getWidth() / 4) * 0.90);

        // calculate position
        position = new Point((int) (getWidth() / 2f), (int) (getHeight() / 2f));

        drawBgCircle(canvas);
        drawData(canvas);
        drawPartitionCircle(canvas);
        drawCenterCircle(canvas);
    }

    /**
     * <p>
     * Draw a circle
     * </p>
     * <p>
     * 満丸を書く
     * </p>
     * <p>
     * 绘制一个圆
     * </p>
     * 
     * @param canvas
     */
    protected void drawCircle(Canvas canvas) {

        Paint mPaintCircleBorder = new Paint();
        mPaintCircleBorder.setColor(Color.WHITE);
        mPaintCircleBorder.setStyle(Style.STROKE);
        mPaintCircleBorder.setStrokeWidth(2);
        mPaintCircleBorder.setAntiAlias(true);

        // draw a circle
        canvas.drawCircle(position.x, position.y, radiusLength,
                mPaintCircleBorder);
    }

    /**
     * -画最外层黑色圆形背景
     * 
     * @param canvas
     */
    protected void drawBgCircle(Canvas canvas) {

        Paint mPaintCircleBorder = new Paint();
        mPaintCircleBorder.setColor(Color.BLACK);
        mPaintCircleBorder.setStyle(Style.STROKE);
        mPaintCircleBorder.setStrokeWidth(2);
        mPaintCircleBorder.setAntiAlias(true);

        // draw a circle
        canvas.drawCircle(position.x, position.y, radiusLength,
                mPaintCircleBorder);

        Paint BgCirclePaint = new Paint();
        BgCirclePaint.setColor(0xff2c2f30);
        BgCirclePaint.setStyle(Style.FILL);
        BgCirclePaint.setAntiAlias(true);

        // draw a circle
        canvas.drawCircle(position.x, position.y, radiusLength,
                BgCirclePaint);
    }

    /**
     * -画隔断黑色环
     * 
     * @param canvas
     */
    protected void drawPartitionCircle(Canvas canvas) {

        Paint mPaintCircleBorder = new Paint();
        mPaintCircleBorder.setColor(0xff2d3031);
        mPaintCircleBorder.setStyle(Style.STROKE);
        mPaintCircleBorder.setStrokeWidth(4);
        mPaintCircleBorder.setAntiAlias(true);
        // draw a circle
        canvas.drawCircle(position.x, position.y, radiusLength * 6 / 10,
                mPaintCircleBorder);

        Paint BgCirclePaint = new Paint();
        BgCirclePaint.setColor(0xff2f353e);
        BgCirclePaint.setStyle(Style.FILL);
        BgCirclePaint.setAntiAlias(true);

        // draw a circle
        canvas.drawCircle(position.x, position.y, radiusLength * 6 / 10,
                BgCirclePaint);
    }

    /**
     * -画中心圆和标题
     * 
     * @param canvas
     */
    protected void drawCenterCircle(Canvas canvas) {

        Paint mPaintCircleBorder = new Paint();
        mPaintCircleBorder.setColor(0xff1b2024);
        mPaintCircleBorder.setStyle(Style.STROKE);
        mPaintCircleBorder.setStrokeWidth(2);
        mPaintCircleBorder.setAntiAlias(true);
        // color="#1b2024"
        // draw a circle
        canvas.drawCircle(position.x, position.y, radiusLength * 4 / 10,
                mPaintCircleBorder);

        Paint bgCirclePaint = new Paint();
        bgCirclePaint.setColor(0xff1b2024);
        bgCirclePaint.setStyle(Style.FILL);
        bgCirclePaint.setAntiAlias(true);

        // draw a circle
        canvas.drawCircle(position.x, position.y, radiusLength * 4 / 10,
                bgCirclePaint);

        Paint fontPaint = new Paint();
        fontPaint.setColor(Color.WHITE);
        fontPaint.setTextSize(textFontSize);
        fontPaint.setAntiAlias(true);
        fontPaint.setTextAlign(Paint.Align.CENTER);
        // 计算文字所在矩形，可以得到宽高
        Rect rect = new Rect();
        fontPaint.getTextBounds("今日资金", 0,
                "今日资金".length(), rect);
        int w = rect.width();
        int h = rect.height();

        canvas.drawText("今日资金", position.x, position.y, fontPaint);

        fontPaint.setColor(0xff4d555a);
        fontPaint.setTextSize(capitalflowsSize);

        canvas.drawText("Capital Flows", position.x, position.y + h + 2,
                fontPaint);
    }

    /**
     * <p>
     * Draw the data
     * </p>
     * <p>
     * チャートでデータを書く
     * </p>
     * <p>
     * 将数据绘制在图表上
     * </p>
     * 
     * @param canvas
     */
    protected void drawData(Canvas canvas) {
        if (null != data) {
            // sum all data's value
            float sum = 0;
            for (int i = 0; i < data.size(); i++) {
                sum = sum + data.get(i).getValue();
            }
            // 饼图颜色填充
            Paint paint = new Paint();
            // paint.setStyle(Style.FILL);
            paint.setAntiAlias(true);
            // 饼图颜色填充
            Paint paintBorder = new Paint();
            paintBorder.setStyle(Style.STROKE);
            paintBorder.setStrokeWidth(2);
            paintBorder.setColor(radiusColor);
            paintBorder.setAntiAlias(true);

            // 数据圆环角度位移
            int offset = 0;
            // 冒泡注释起点角度位移
            float bubbleOffset = 0;

            // draw arcs of every piece
            for (int j = 0; j < data.size(); j++) {
                TitleValueColorEntity e = data.get(j);

                // get color
                paint.setColor(e.getColor());

                // percentage
                int percentage = Math.round(e.getValue() / sum * 100);

                // 外层颜色填充
                int radius = radiusLength * 28 / 30;
                RectF oval = new RectF(position.x - radius, position.y - radius,
                        position.x + radius, position.y + radius);
                int sweep = Math.round(e.getValue() / sum * 360f);
                canvas.drawArc(oval, offset, sweep, true, paint);
                canvas.drawArc(oval, offset, sweep, true, paintBorder);
                offset = offset + sweep;

                // 先取一半用来计算泡泡的起点和终点
                bubbleOffset += sweep / 2;

                // 圆心与冒泡起点连线与圆线交点的坐标
                float angle = bubbleOffset % 90f;
                if (bubbleOffset >= 90f && bubbleOffset <= 135f) {
                    angle = 90f - angle;
                } else if (bubbleOffset > 135f && bubbleOffset < 180f) {
                    // 180不能取等号
                    angle = 90f - angle;
                } else if (bubbleOffset >= 270f && bubbleOffset < 360f) {
                    // 270一定要取等号
                    angle = 90f - angle;
                }
                // 角度转弧度
                angle = (float) (Math.PI * angle / 180f);

                float x1 = 0;
                float y1 = 0;
                int x2 = 0;
                int y2 = 0;

                // 泡泡连接线 弧线长度
                float cosValue1 = (float) Math.abs(Math.cos(angle) * (1.1 * radiusLength));
                float sinValue1 = (float) Math.abs(Math.sin(angle) * (1.1 * radiusLength));
                float cosValue2 = (float) Math.abs(Math.cos(angle) * (1.3 * radiusLength));
                float sinValue2 = (float) Math.abs(Math.sin(angle) * (1.3 * radiusLength));

                Log.i("bubble", "angle: " + angle);

                if (bubbleOffset <= 90) {
                    x1 = (position.x + cosValue1);
                    y1 = (position.y + sinValue1);

                    x2 = (int) (position.x + cosValue2);
                    y2 = (int) (position.y + sinValue2);
                } else if (bubbleOffset <= 180) {
                    x1 = (position.x - cosValue1);
                    y1 = (position.y + sinValue1);

                    x2 = (int) (position.x - cosValue2);
                    y2 = (int) (position.y + sinValue2);
                } else if (bubbleOffset <= 270) {
                    x1 = (position.x - cosValue1);
                    y1 = (position.y - sinValue1);

                    x2 = (int) (position.x - cosValue2);
                    y2 = (int) (position.y - sinValue2);
                } else {
                    x1 = (position.x + cosValue1);
                    y1 = (position.y - sinValue1);

                    x2 = (int) (position.x + cosValue2);
                    y2 = (int) (position.y - sinValue2);
                }
                int bubbleRadius = 0;
                if (screenWidth <= 480) {
                    bubbleRadius = radius / 5 + 20;
                } else if (screenWidth > 480 && screenWidth <= 720) {
                    bubbleRadius = radius / 5 + 30;
                } else if (screenWidth > 720 && screenWidth <= 1080) {
                    bubbleRadius = radius / 5 + 40;
                } else if (screenWidth > 1080) {
                }
                // 画泡泡连接线_控制线的长度

                RectF bubbleOval = new RectF(x1 - bubbleRadius, y1 - bubbleRadius,
                        x1 + bubbleRadius, y1 + bubbleRadius);
                canvas.drawArc(bubbleOval, bubbleOffset - 8, 20, true, paint);

                // 画泡泡矩形内文字框
                Paint boxLinePaint = new Paint();
                boxLinePaint.setColor(Color.WHITE);
                boxLinePaint.setAntiAlias(true);
                boxLinePaint.setTextSize(this.getTextFontSize());

                // 画泡泡矩形下面title文字框
                Paint titlePaint = new Paint();
                titlePaint.setColor(0Xff000000);
                titlePaint.setAntiAlias(true);
                titlePaint.setTextSize(this.getTitleFontSize());
                Point start = new Point();

                // 计算文字所在矩形，可以得到宽高
                Rect rect = new Rect();
                boxLinePaint.getTextBounds(String.valueOf(percentage) + "%", 0,
                        (String.valueOf(percentage) + "%").length(), rect);
                int w = rect.width();
                int h = rect.height();

                // 计算文字所在矩形，可以得到宽高
                Rect rectTitle = new Rect();
                titlePaint.getTextBounds(String.valueOf(e.getTitle()), 0,
                        String.valueOf(e.getTitle()).length(), rectTitle);
                int wTitle = rectTitle.width();

                Log.i("wTitle", wTitle + "-------------" + w);

                // 計算画数字内容起点位置信息
                if (bubbleOffset > 0 && bubbleOffset < 30) {
                    start.x = x2 + w / 3;
                    start.y = y2 + h;
                } else if (bubbleOffset >= 30 && bubbleOffset < 60) {
                    start.x = x2;
                    start.y = y2 + 4 * h / 3;
                } else if (bubbleOffset >= 60 && bubbleOffset <= 90) {
                    start.x = x2 - w / 3;
                    start.y = y2 + 3 * h / 2;
                } else if (bubbleOffset > 90 && bubbleOffset <= 120) {
                    start.x = x2 - w / 2;
                    start.y = y2 + 3 * h / 2;
                } else if (bubbleOffset > 120 && bubbleOffset <= 150) {
                    start.x = x2 - 4 * w / 3;
                    start.y = y2 + h;
                } else if (bubbleOffset > 150 && bubbleOffset <= 180) {
                    start.x = x2 - 3 * w / 2;
                    start.y = y2 + 2 * h / 3;
                } else if (bubbleOffset > 180 && bubbleOffset < 210) {
                    start.x = x2 - 3 * w / 2;
                    start.y = y2 + h / 4;
                } else if (bubbleOffset >= 210 && bubbleOffset < 240) {
                    start.x = x2 - w;
                    start.y = y2 - h / 3;
                } else if (bubbleOffset >= 240 && bubbleOffset <= 270) {
                    start.x = x2 - w / 2;
                    start.y = y2 - h * 2 / 3;
                } else if (bubbleOffset > 270 && bubbleOffset <= 300) {
                    start.x = x2 - w / 3;
                    start.y = y2 - h * 2 / 3;
                } else if (bubbleOffset > 300 && bubbleOffset <= 330) {
                    start.x = x2 + w / 3;
                    start.y = y2;
                } else if (bubbleOffset > 330 && bubbleOffset <= 360) {
                    start.x = x2 + w / 2;
                    start.y = y2 + h / 3;
                }

                // 画title，根据不同分辨率设置float x, float y
                float xTitle = start.x -
                        (wTitle - w) / 2;
                float yTitle = 0;
                // 画框参数
                RectF rectF = null;
                if (screenWidth <= 480) {
                    if (bubbleOffset > 210 && bubbleOffset < 330) {
                        yTitle = start.y - h * 3 / 2 - 7;
                    } else {
                        yTitle = start.y + h + 10;
                    }
                    // 画框
                    rectF = new RectF(start.x + wTitle - (wTitle - w) * 2 /
                            3, start.y - h -
                            5f, start.x - (wTitle - w) / 3,
                            start.y + 5f);
                } else if (screenWidth > 480 && screenWidth <= 720) {
                    if (bubbleOffset > 210 && bubbleOffset < 330) {
                        yTitle = start.y - h * 3 / 2 - 9;
                    } else {
                        yTitle = start.y + h + 12;
                    }
                    // 画框
                    rectF = new RectF(start.x + wTitle - (wTitle - w) * 2 /
                            3, start.y - h -
                            7f, start.x - (wTitle - w) / 3,
                            start.y + 7f);
                } else if (screenWidth > 720 && screenWidth <= 1080) {
                    if (bubbleOffset > 210 && bubbleOffset < 330) {
                        yTitle = start.y - h * 3 / 2 - 11;
                    } else {
                        yTitle = start.y + h + 15;
                    }
                    // 画框
                    rectF = new RectF(start.x + wTitle - (wTitle - w) * 2 /
                            3, start.y - h -
                            10f, start.x - (wTitle - w) / 3,
                            start.y + 10f);
                } else if (screenWidth > 1080) {
                    if (bubbleOffset > 210 && bubbleOffset < 330) {
                        yTitle = start.y - h * 3 / 2 - 13;
                    } else {
                        yTitle = start.y + h + 17;
                    }
                    // 画框
                    rectF = new RectF(start.x + wTitle - (wTitle - w) * 2 /
                            3, start.y - h -
                            12f, start.x - (wTitle - w) / 3,
                            start.y + 12f);
                }

                // 画title
                canvas.drawText(String.valueOf(e.getTitle()), xTitle,
                        yTitle, titlePaint);
                // 画框
                canvas.drawRect(rectF, paint);
                // 画数字
                canvas.drawText(String.valueOf(percentage) + "%", start.x,
                        start.y, boxLinePaint);
                // 累加以便下次使用
                bubbleOffset += sweep / 2;
            }
        }
    }

    /**
     * @return the data
     */
    public List<TitleValueColorEntity> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<TitleValueColorEntity> data) {
        this.data = data;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * @return the radiusLength
     */
    public int getRadiusLength() {
        return radiusLength;
    }

    /**
     * @param radiusLength the radiusLength to set
     */
    public void setRadiusLength(int radiusLength) {
        this.radiusLength = radiusLength;
    }

    /**
     * @return the radiusColor
     */
    public int getRadiusColor() {
        return radiusColor;
    }

    /**
     * @param radiusColor the radiusColor to set
     */
    public void setRadiusColor(int radiusColor) {
        this.radiusColor = radiusColor;
    }

    /**
     * @return the circleBorderColor
     */
    public int getCircleBorderColor() {
        return circleBorderColor;
    }

    /**
     * @param circleBorderColor the circleBorderColor to set
     */
    public void setCircleBorderColor(int circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
    }

    /**
     * @return the displayRadius
     */
    public boolean isDisplayRadius() {
        return displayRadius;
    }

    /**
     * @param displayRadius the displayRadius to set
     */
    public void setDisplayRadius(boolean displayRadius) {
        this.displayRadius = displayRadius;
    }

    public int getTextFontSize() {
        return textFontSize;
    }

    public void setTextFontSize(int textFontSize) {
        this.textFontSize = textFontSize;
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getCapitalflowsSize() {
        return capitalflowsSize;
    }

    public void setCapitalflowsSize(int capitalflowsSize) {
        this.capitalflowsSize = capitalflowsSize;
    }
}

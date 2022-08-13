/*
 * MACandleStickChart.java
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

package cn.limc.androidcharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import cn.limc.androidcharts.entity.*;

import java.util.List;

/**
 * 
 * <p>
 * MACandleStickChart is inherits from CandleStickChart which can display moving
 * average lines on this graph.
 * </p>
 * <p>
 * MACandleStickChartはグラフの一種です、移動平均線など分析線がこのグラフで表示は可能です。
 * </p>
 * <p>
 * MACandleStickChart继承于CandleStickChart的，可以在CandleStickChart基础上
 * 显示移动平均等各种分析指标数据。
 * </p>
 * 
 * @author limc
 * @version v1.0 2011/05/30 14:49:02
 * @see cn.limc.androidcharts.view.CandleStickChart
 * @see cn.limc.androidcharts.view.StickChart
 *
 */
public class MACandleStickWithVolChart extends CandleStickChart {

    public static final int DEFAULT_COLORED_STICK_STYLE_WITH_BORDER = 0;
    public static final int DEFAULT_COLORED_STICK_STYLE_NO_BORDER = 1;
    public static final int DEFAULT_COLORED_STICK_STYLE = DEFAULT_COLORED_STICK_STYLE_NO_BORDER;

    private int coloredStickStyle = DEFAULT_COLORED_STICK_STYLE_NO_BORDER;

    public static final int ZOOM_BASE_LINE_CENTER = 0;
    public static final int ZOOM_BASE_LINE_LEFT = 1;
    public static final int ZOOM_BASE_LINE_RIGHT = 2;

    public static final int DEFAULT_DISPLAY_FROM = 0;
    public static final int DEFAULT_DISPLAY_NUMBER = 50;
    public static final int DEFAULT_MIN_DISPLAY_NUMBER = 20;
    public static final int DEFAULT_ZOOM_BASE_LINE = ZOOM_BASE_LINE_CENTER;
    public static final boolean DEFAULT_AUTO_CALC_VALUE_RANGE = true;

    public static final int DEFAULT_STICK_SPACING = 1;

    protected int displayFrom = DEFAULT_DISPLAY_FROM;
    protected int displayNumber = DEFAULT_DISPLAY_NUMBER;
    protected int minDisplayNumber = DEFAULT_MIN_DISPLAY_NUMBER;
    protected int zoomBaseLine = DEFAULT_ZOOM_BASE_LINE;
    protected boolean autoCalcValueRange = DEFAULT_AUTO_CALC_VALUE_RANGE;
    protected int stickSpacing = DEFAULT_STICK_SPACING;

    /**
     * <p>
     * default color for display stick border
     * </p>
     * <p>
     * 表示スティックのボーダーの色のデフォルト値
     * </p>
     * <p>
     * 默认表示柱条的边框颜色
     * </p>
     */
    public static final int DEFAULT_STICK_BORDER_COLOR = Color.RED;

    /**
     * <p>
     * default color for display stick
     * </p>
     * <p>
     * 表示スティックの色のデフォルト値
     * </p>
     * <p>
     * 默认表示柱条的填充颜色
     * </p>
     */
    public static final int DEFAULT_STICK_FILL_COLOR = Color.RED;

    /**
     * <p>
     * Color for display stick border
     * </p>
     * <p>
     * 表示スティックのボーダーの色
     * </p>
     * <p>
     * 表示柱条的边框颜色
     * </p>
     */
    private int stickBorderColor = DEFAULT_STICK_BORDER_COLOR;

    /**
     * <p>
     * Color for display stick
     * </p>
     * <p>
     * 表示スティックの色
     * </p>
     * <p>
     * 表示柱条的填充颜色
     * </p>
     */
    private int stickFillColor = DEFAULT_STICK_FILL_COLOR;

    /**
     * <p>
     * data to draw sticks
     * </p>
     * <p>
     * スティックを書く用データ
     * </p>
     * <p>
     * 绘制成交量柱条用的数据
     * </p>
     */
    protected IChartData<IStickEntity> volStickData;

    /**
     * <p>
     * max value of Y axis
     * </p>
     * <p>
     * Y軸の最大値
     * </p>
     * <p>
     * Y的最大表示值
     * </p>
     */
    protected double maxValue;

    /**
     * <p>
     * min value of Y axis
     * </p>
     * <p>
     * Y軸の最小値
     * </p>
     * <p>
     * Y的最小表示值
     * </p>
     */
    protected double minValue;

	/**
	 * <p>
	 * data to draw lines
	 * </p>
	 * <p>
	 * ラインを書く用データ
	 * </p>
	 * <p>
	 * 绘制线条用的数据
	 * </p>
	 */
	private List<LineEntity<DateValueEntity>> linesData;

	/*
	 * (non-Javadoc)
	 *
	 * @param context
	 *
	 * @see cn.limc.cn.limc.androidcharts.view.GridChart#GridChart(Context)
	 */
	public MACandleStickWithVolChart(Context context) {
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
	 * @see cn.limc.cn.limc.androidcharts.view.GridChart#GridChart(Context,
	 * AttributeSet, int)
	 */
	public MACandleStickWithVolChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @param context
	 *
	 * @param attrs
	 *
	 *
	 *
	 * @see cn.limc.cn.limc.androidcharts.view.GridChart#GridChart(Context,
	 * AttributeSet)
	 */
	public MACandleStickWithVolChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void calcDataValueRange() {
		super.calcDataValueRange();

		double maxValue = this.maxValue;
		double minValue = this.minValue;
		// 逐条输出MA线
		for (int i = 0; i < this.linesData.size(); i++) {
			LineEntity<DateValueEntity> line = (LineEntity<DateValueEntity>) linesData
					.get(i);
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

			// 判断显示为方柱或显示为线条
			for (int j = 0; j < this.maxSticksNum; j++) {
				DateValueEntity entity;
				if (axisYPosition == AXIS_Y_POSITION_LEFT) {
					entity = line.getLineData().get(j);
				} else {
					entity = line.getLineData().get(lineData.size() - 1 - j);
				}
				if (entity.getValue() < minValue) {
					minValue = entity.getValue();
				}
				if (entity.getValue() > maxValue) {
					maxValue = entity.getValue();
				}
			}
		}
		this.maxValue = maxValue;
		this.minValue = minValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * <p>Called when is going to draw this chart<p> <p>チャートを書く前、メソッドを呼ぶ<p>
	 * <p>绘制图表时调用<p>
	 * 
	 * @param canvas
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// draw lines
		if (null != this.linesData) {
			if (0 != this.linesData.size()) {
				drawLines(canvas);
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
	 * 绘制线条
	 * </p>
	 * 
	 * @param canvas
	 */
	protected void drawLines(Canvas canvas) {
		if (null == this.linesData) {
			return;
		}
		// distance between two points
		float lineLength = getDataQuadrantPaddingWidth() / maxSticksNum - 1;
		// start point‘s X
		float startX;

		// draw MA lines
		for (int i = 0; i < linesData.size(); i++) {
			LineEntity<DateValueEntity> line = (LineEntity<DateValueEntity>) linesData
					.get(i);
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

			Paint mPaint = new Paint();
			mPaint.setColor(line.getLineColor());
			mPaint.setAntiAlias(true);

			// start point
			PointF ptFirst = null;
			if (axisYPosition == AXIS_Y_POSITION_LEFT) {
				// set start point’s X
				startX = getDataQuadrantPaddingStartX() + lineLength / 2;
				for (int j = 0; j < lineData.size(); j++) {
                    double value = lineData.get(j).getValue();
					// calculate Y
					float valueY = (float) ((1f - (value - minValue)
							/ (maxValue - minValue)) * getDataQuadrantPaddingHeight())
							+ getDataQuadrantPaddingStartY();

					// if is not last point connect to previous point
					if (j > 0) {
						canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY,
								mPaint);
					}
					// reset
					ptFirst = new PointF(startX, valueY);
					startX = startX + 1 + lineLength;
				}
			} else {
				// set start point’s X
				startX = getDataQuadrantPaddingEndX() - lineLength / 2;
				for (int j = lineData.size() - 1; j >= 0; j--) {
                    double value = lineData.get(j).getValue();
					// calculate Y
					float valueY = (float) ((1f - (value - minValue)
							/ (maxValue - minValue)) * getDataQuadrantPaddingHeight())
							+ getDataQuadrantPaddingStartY();

					// if is not last point connect to previous point
					if (j < lineData.size() - 1) {
						canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY,
								mPaint);
					}
					// reset
					ptFirst = new PointF(startX, valueY);
					startX = startX - 1 - lineLength;
				}
			}
		}
	}

    @Override
    protected void drawSticks(Canvas canvas) {

        //绘制烛蜡图
        super.drawSticks(canvas);

        //绘制成交量
        if (null == volStickData) {
            return;
        }
        if (volStickData.size() == 0) {
            return;
        }

        float stickWidth = getDataQuadrantPaddingWidth() / displayNumber
          - stickSpacing;
        float stickX = getDataQuadrantPaddingStartX();

        Paint mPaintStick = new Paint();
        for (int i = displayFrom; i < displayFrom + displayNumber; i++) {
            ColoredStickEntity entity = (ColoredStickEntity) volStickData.get(i);

            float highY = (float) ((1f - (entity.getHigh() - minValue)
              / (maxValue - minValue))
              * (getDataQuadrantPaddingHeight()) + getDataQuadrantPaddingStartY());
            float lowY = (float) ((1f - (entity.getLow() - minValue)
              / (maxValue - minValue))
              * (getDataQuadrantPaddingHeight()) + getDataQuadrantPaddingStartY());

            mPaintStick.setColor(entity.getColor());
            // stick or line?
            if (stickWidth >= 2f) {
                canvas.drawRect(stickX, highY+150, stickX + stickWidth, lowY+150,
                  mPaintStick);
            } else {
                canvas.drawLine(stickX, highY+150, stickX, lowY+150, mPaintStick);
            }

            // next x
            stickX = stickX + stickSpacing + stickWidth;
        }
    }

    protected final int NONE = 0;
    protected final int ZOOM = 1;
    protected final int DOWN = 2;

    protected float olddistance = 0f;
    protected float newdistance = 0f;

    protected int touchMode;

    protected PointF startPoint;
    protected PointF startPointA;
    protected PointF startPointB;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == volStickData || volStickData.size() == 0) {
            return false;
        }

        final float MIN_LENGTH = (super.getWidth() / 40) < 5 ? 5 : (super
          .getWidth() / 50);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 设置拖拉模式
            case MotionEvent.ACTION_DOWN:
                touchMode = DOWN;
                if (event.getPointerCount() == 1) {
                    startPoint = new PointF(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                touchMode = NONE;
                startPointA = null;
                startPointB = null;
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_POINTER_UP:
                touchMode = NONE;
                startPointA = null;
                startPointB = null;
                return super.onTouchEvent(event);
            // 设置多点触摸模式
            case MotionEvent.ACTION_POINTER_DOWN:
                olddistance = calcDistance(event);
                if (olddistance > MIN_LENGTH) {
                    touchMode = ZOOM;
                    startPointA = new PointF(event.getX(0), event.getY(0));
                    startPointB = new PointF(event.getX(1), event.getY(1));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchMode == ZOOM) {
                    newdistance = calcDistance(event);
                    if (newdistance > MIN_LENGTH) {
                        if (startPointA.x >= event.getX(0)
                          && startPointB.x >= event.getX(1)) {
                            if (displayFrom + displayNumber + 2 < volStickData.size()) {
                                displayFrom = displayFrom + 2;
                            }
                        } else if (startPointA.x <= event.getX(0)
                          && startPointB.x <= event.getX(1)) {
                            if (displayFrom > 2) {
                                displayFrom = displayFrom - 2;
                            }
                        } else {
                            if (Math.abs(newdistance - olddistance) > MIN_LENGTH) {
                                if (newdistance > olddistance) {
                                    zoomIn();
                                } else {
                                    zoomOut();
                                }
                                // 重置距离
                                olddistance = newdistance;
                            }
                        }
                        startPointA = new PointF(event.getX(0), event.getY(0));
                        startPointB = new PointF(event.getX(1), event.getY(1));

                        super.postInvalidate();
                        super.notifyEventAll(this);
                    }
                } else {
                    // 单点拖动效果
                    if (event.getPointerCount() == 1) {
                        float moveXdistance = Math.abs(event.getX() - startPoint.x);
                        float moveYdistance = Math.abs(event.getY() - startPoint.y);

                        if (moveXdistance > 1 || moveYdistance > 1) {

                            super.onTouchEvent(event);

                            startPoint = new PointF(event.getX(), event.getY());
                        }
                    }
                }
                break;
        }
        return true;
    }

    /**
     * <p>
     * calculate the distance between two touch points
     * </p>
     * <p>
     * 複数タッチしたポイントの距離
     * </p>
     * <p>
     * 计算两点触控时两点之间的距离
     * </p>
     *
     * @param event
     * @return float
     *         <p>
     *         distance
     *         </p>
     *         <p>
     *         距離
     *         </p>
     *         <p>
     *         距离
     *         </p>
     */
    protected float calcDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
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
        if (displayNumber > minDisplayNumber) {
            // 区分缩放方向
            if (zoomBaseLine == ZOOM_BASE_LINE_CENTER) {
                displayNumber = displayNumber - 2;
                displayFrom = displayFrom + 1;
            } else if (zoomBaseLine == ZOOM_BASE_LINE_LEFT) {
                displayNumber = displayNumber - 2;
            } else if (zoomBaseLine == ZOOM_BASE_LINE_RIGHT) {
                displayNumber = displayNumber - 2;
                displayFrom = displayFrom + 2;
            }

            // 处理displayNumber越界
            if (displayNumber < minDisplayNumber) {
                displayNumber = minDisplayNumber;
            }

            // 处理displayFrom越界
            if (displayFrom + displayNumber >= volStickData.size()) {
                displayFrom = volStickData.size() - displayNumber;
            }
        }
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
        if (displayNumber < volStickData.size() - 1) {
            if (displayNumber + 2 > volStickData.size() - 1) {
                displayNumber = volStickData.size() - 1;
                displayFrom = 0;
            } else {
                // 区分缩放方向
                if (zoomBaseLine == ZOOM_BASE_LINE_CENTER) {
                    displayNumber = displayNumber + 2;
                    if (displayFrom > 1) {
                        displayFrom = displayFrom - 1;
                    } else {
                        displayFrom = 0;
                    }
                } else if (zoomBaseLine == ZOOM_BASE_LINE_LEFT) {
                    displayNumber = displayNumber + 2;
                } else if (zoomBaseLine == ZOOM_BASE_LINE_RIGHT) {
                    displayNumber = displayNumber + 2;
                    if (displayFrom > 2) {
                        displayFrom = displayFrom - 2;
                    } else {
                        displayFrom = 0;
                    }
                }
            }

            if (displayFrom + displayNumber >= volStickData.size()) {
                displayNumber = volStickData.size() - displayFrom;
            }
        }
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
     * @param entity
     *            <p>
     *            data
     *            </p>
     *            <p>
     *            データ
     *            </p>
     *            <p>
     *            新数据
     *            </p>
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
     * @param entity
     *            <p>
     *            data
     *            </p>
     *            <p>
     *            データ
     *            </p>
     *            <p>
     *            新数据
     *            </p>
     */
    public void addData(IStickEntity entity) {
        if (null != entity) {
            // add
            volStickData.add(entity);
            if (this.maxValue < entity.getHigh()) {
                this.maxValue = 100 + ((int) entity.getHigh()) / 100 * 100;
            }

        }
    }

    /**
     * @return the h
     */
    public int getDisplayFrom() {
        return displayFrom;
    }

    /**
     * @param displayFrom
     *            the h to set
     */
    public void setDisplayFrom(int displayFrom) {
        this.displayFrom = displayFrom;
    }

    /**
     * @return the displayNumber
     */
    public int getDisplayNumber() {
        return displayNumber;
    }

    /**
     * @param displayNumber
     *            the displayNumber to set
     */
    public void setDisplayNumber(int displayNumber) {
        this.displayNumber = displayNumber;
    }

    /**
     * @return the p
     */
    public int getMinDisplayNumber() {
        return minDisplayNumber;
    }

    /**
     * @param minDisplayNumber
     *            the p to set
     */
    public void setMinDisplayNumber(int minDisplayNumber) {
        this.minDisplayNumber = minDisplayNumber;
    }

    /**
     * @return the zoomBaseLine
     */
    public int getZoomBaseLine() {
        return zoomBaseLine;
    }

    /**
     * @param zoomBaseLine
     *            the zoomBaseLine to set
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
     * @param stickBorderColor
     *            the stickBorderColor to set
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
     * @param stickFillColor
     *            the stickFillColor to set
     */
    public void setStickFillColor(int stickFillColor) {
        this.stickFillColor = stickFillColor;
    }

    /**
     * @return the a
     */
    public IChartData<IStickEntity> getVolStickData() {
        return volStickData;
    }

    /**
     * @param volStickData
     *            the a to set
     */
    public void setVolStickData(IChartData<IStickEntity> volStickData) {
        this.volStickData = volStickData;
    }

    /**
     * @return the d
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue
     *            the d to set
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the minValue
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * @param minValue
     *            the minValue to set
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the stickSpacing
     */
    public int getStickSpacing() {
        return stickSpacing;
    }

    /**
     * @param stickSpacing
     *            the stickSpacing to set
     */
    public void setStickSpacing(int stickSpacing) {
        this.stickSpacing = stickSpacing;
    }

    /**
     * @return the coloredStickStyle
     */
    public int getColoredStickStyle() {
        return coloredStickStyle;
    }

    /**
     * @param coloredStickStyle
     *            the coloredStickStyle to set
     */
    public void setColoredStickStyle(int coloredStickStyle) {
        this.coloredStickStyle = coloredStickStyle;
    }

	/**
	 * @return the linesData
	 */
	public List<LineEntity<DateValueEntity>> getLinesData() {
		return linesData;
	}

	/**
	 * @param linesData
	 *            the linesData to set
	 */
	public void setLinesData(List<LineEntity<DateValueEntity>> linesData) {
		this.linesData = linesData;
	}
}

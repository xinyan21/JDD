package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cn.limc.androidcharts.entity.*;
import cn.limc.androidcharts.view.GridChart;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.ProjectConfigConstant;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.controllers.BigStockChartController;
import com.jdd.android.jdd.entities.MinutePrice;
import com.jdd.android.jdd.entities.PriceInfo;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.interfaces.ITouchEventResponseX;
import com.jdd.android.jdd.others.StockPeriod;
import com.jdd.android.jdd.requests.QueryKLineRequest;
import com.jdd.android.jdd.utils.FormatUtils;
import com.jdd.android.jdd.utils.LongShortArtOfWarUtil;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.StockUtil;
import com.jdd.android.jdd.views.KLineChartViewX;
import com.jdd.android.jdd.views.StockMinutePriceView;
import com.jdd.android.jdd.views.TwoXYGridChartView;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.ScreenUtils;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.*;

/**
 * 描述：大股票图
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-14
 * @last_edit 2016-01-14
 * @since 1.0
 */
public class BigStockChartActivity extends TKActivity implements IQueryServer, ITouchEventResponseX {
    public static final short CURRENT_CHART_MINUTE_PRICE = 1;      //分时
    public static final short CURRENT_CHART_K_LINE = 2;        //K线
    //Intent参数key
    public static final String KEY_STOCK_ENTITY = "STOCK_ENTITY";       //股票实体类
    public static final String KEY_CURRENT_CHART = "CURRENT_CHART";     //当前是什么图
    public static final String KEY_MINUTE_PRICE_DATA = "MINUTE_PRICE";       //分时数据在缓存的key
    public static final String KEY_CURRENT_PERIOD = "PERIOD";   //当前周期

    public ViewFlipper containerFlipper;
    public Button btnFiveMinute, btnFifteenMinute, btnThirtyMinute;
    public Button btnSixtyMinute, btnDayLine, btnWeekLine, btnMonthLine;
    public View boldLineFiveMinute, boldLineFifteenMinute, boldLineThirtyMinute;
    public View boldLineSixtyMinute, boldLineDayLine, boldLineWeekLine, boldLineMonthLine;
    public ImageButton ibtnSwitchChart;
    public View periodSwitcher;
    public Button btnSwitchPeriod;
    public TextView tvSelectedPeriod;

    public StockEntity stockEntity;
    public short currentChart = CURRENT_CHART_MINUTE_PRICE;

    private ImageButton mIBtnBack;
    private TextView mTVStockNameAndCode;
    private StockMinutePriceView mMinutePriceView;
    private KLineChartViewX mKLine;
    private TextView mTVOpen, mTVNow, mTVChangeValue, mTVVolume;
    private TextView mTVChangePercent, mTVHigh, mTVLow;
    private TextView mTVSelectedDate, mTVCandleExplanation, mTVVolExplanation;
    private LinearLayout mLLLongShortExplanation;
    private Dialog mDlgLoading;

    private MemberCache mCache = DataCache.getInstance().getCache();
    private BigStockChartController mController;
    private String mMinutePriceDataKey;
    private Timer mTimer;
    private StockPeriod mCurrentPeriod;
    private int mCurrentPeriodViewID = 0;   //当前周期的按钮id，用来防止重复点击

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_stock_chart);

        Intent intent = getIntent();
        try {
            currentChart = intent.getShortExtra(KEY_CURRENT_CHART, CURRENT_CHART_MINUTE_PRICE);
            mMinutePriceDataKey = intent.getStringExtra(KEY_MINUTE_PRICE_DATA);
            mCurrentPeriod = (StockPeriod) intent.getSerializableExtra(KEY_CURRENT_PERIOD);
            if (intent.hasExtra(KEY_STOCK_ENTITY)) {
                stockEntity = (StockEntity) intent.getSerializableExtra(KEY_STOCK_ENTITY);
            } else {
                stockEntity = new StockEntity();
            }
            if (null == mMinutePriceDataKey) {
                mMinutePriceDataKey = "";
            }
        } catch (Exception e) {
            stockEntity = new StockEntity();
        }
        mTimer = new Timer();

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CURRENT_CHART_K_LINE == currentChart) {
            containerFlipper.showNext();
            btnSwitchPeriod.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void findViews() {
        containerFlipper = (ViewFlipper) findViewById(R.id.vf_price_chart_container);
        btnFiveMinute = (Button) findViewById(R.id.btn_five_minute);
        btnFifteenMinute = (Button) findViewById(R.id.btn_fifteen_minute);
        btnThirtyMinute = (Button) findViewById(R.id.btn_thirty_minute);
        btnSixtyMinute = (Button) findViewById(R.id.btn_sixty_minute);
        btnDayLine = (Button) findViewById(R.id.btn_day_line);
        btnWeekLine = (Button) findViewById(R.id.btn_week_line);
        btnMonthLine = (Button) findViewById(R.id.btn_month_line);
        boldLineDayLine = findViewById(R.id.v_bold_line_day_line);
        boldLineFifteenMinute = findViewById(R.id.v_bold_line_fifteen_minute);
        boldLineFiveMinute = findViewById(R.id.v_bold_line_five_minute);
        boldLineSixtyMinute = findViewById(R.id.v_bold_line_sixty_minute);
        boldLineThirtyMinute = findViewById(R.id.v_bold_line_thirty_minute);
        boldLineWeekLine = findViewById(R.id.v_bold_line_week_line);
        boldLineMonthLine = findViewById(R.id.v_bold_line_month_line);
        mMinutePriceView = (StockMinutePriceView) findViewById(R.id.smp_minute_price);
        mKLine = (KLineChartViewX) findViewById(R.id.klc_kline_big);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTVStockNameAndCode = (TextView) findViewById(R.id.tv_stock_name_code);
        ibtnSwitchChart = (ImageButton) findViewById(R.id.ibtn_switch_chart);
        mTVOpen = (TextView) findViewById(R.id.tv_open_value);
        mTVNow = (TextView) findViewById(R.id.tv_now);
        mTVChangePercent = (TextView) findViewById(R.id.tv_change_percent);
        mTVChangeValue = (TextView) findViewById(R.id.tv_change_value);
        mTVHigh = (TextView) findViewById(R.id.tv_high_value);
        mTVLow = (TextView) findViewById(R.id.tv_low_value);
        mTVVolume = (TextView) findViewById(R.id.tv_vol_value);
        mTVCandleExplanation = (TextView) findViewById(R.id.tv_candle_explanation);
        mTVVolExplanation = (TextView) findViewById(R.id.tv_vol_explanation);
        mTVSelectedDate = (TextView) findViewById(R.id.tv_selected_date);
        mLLLongShortExplanation = (LinearLayout) findViewById(R.id.ll_long_short_explanation);
        mDlgLoading = PopUpComponentUtil.createLoadingProgressDialog(this, "加载中...", true, true);
        btnSwitchPeriod = (Button) findViewById(R.id.btn_switch_period);
        periodSwitcher = findViewById(R.id.ll_switch_period);
        tvSelectedPeriod = (TextView) findViewById(R.id.tv_selected_period);
    }

    @Override
    protected void setListeners() {
        mController = new BigStockChartController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnFiveMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnFifteenMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnThirtyMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnSixtyMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnDayLine, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnWeekLine, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, ibtnSwitchChart, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnMonthLine, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnSwitchPeriod, mController);
        //设置滑动监听数据回调
        mKLine.addNotify(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setHigh(stockEntity.getHigh());
        priceInfo.setLow(stockEntity.getLow());
        priceInfo.setYesterday(stockEntity.getPreClose());

        final ArrayList<MinutePrice> minutePrices
                = (ArrayList<MinutePrice>) mCache.getCacheItem(mMinutePriceDataKey);

        String baseCacheKey = stockEntity.getMarket()
                + stockEntity.getCode() + mCurrentPeriod.toString();
        ArrayList<IStickEntity> ohlcs
                = (ArrayList<IStickEntity>) mCache.getCacheItem(baseCacheKey + "ohlc");

        if (null == minutePrices || null == ohlcs) {
            //如果没有数据就一直刷新等待详情界面的数据到来
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initViews();
                        }
                    });
                }
            }, ProjectConfigConstant.REFRESH_PERIOD);
            return;
        }
        initViewsCanBeCallRepeatedly(
                stockEntity, ohlcs.size() - 1, ohlcs.get(ohlcs.size() - 1).getDate()
        );

        mMinutePriceView.setMinutePriceList(minutePrices);
        mMinutePriceView.setPriceInfo(priceInfo);
        mMinutePriceView.invalidate();

        if (mCurrentPeriod.equals(StockPeriod.DAY)) {
            btnDayLine.performClick();
        } else if (mCurrentPeriod.equals(StockPeriod.MIN_5)) {
            btnFiveMinute.performClick();
        } else if (mCurrentPeriod.equals(StockPeriod.MIN_15)) {
            btnFifteenMinute.performClick();
        } else if (mCurrentPeriod.equals(StockPeriod.MIN_30)) {
            btnThirtyMinute.performClick();
        } else if (mCurrentPeriod.equals(StockPeriod.MIN_60)) {
            btnSixtyMinute.performClick();
        } else if (mCurrentPeriod.equals(StockPeriod.WEEK)) {
            btnWeekLine.performClick();
        } else if (mCurrentPeriod.equals(StockPeriod.MONTH)) {
            btnMonthLine.performClick();
        }
    }

    /**
     * 初始化可以重复地调用的View
     */
    private void initViewsCanBeCallRepeatedly(StockEntity entity, int index, int date) {
        mTVOpen.setText(FormatUtils.format2PointTwo(entity.getOpen()));
        mTVNow.setText(FormatUtils.format2PointTwo(entity.getNow()));
        mTVHigh.setText(FormatUtils.format2PointTwo(entity.getHigh()));
        mTVLow.setText(FormatUtils.format2PointTwo(entity.getLow()));
        mTVChangePercent.setText(FormatUtils.format2PointTwo(entity.getChangePercent()) + "%");
        mTVChangeValue.setText(FormatUtils.format2PointTwo(entity.getChangeValue()));
        mTVVolume.setText(FormatUtils.formatMoney(entity.getVolume()));
        if (!StringUtils.isEmpty(entity.getName())) {
            mTVStockNameAndCode.setText(entity.getName() + "\n" + entity.getCode());
        }

        double preClose = entity.getPreClose();
        mTVNow.setTextColor(StockUtil.getColorByValue(this, entity.getNow() - preClose));
        mTVChangePercent.setTextColor(StockUtil.getColorByValue(this, entity.getChangePercent()));
        mTVChangeValue.setTextColor(StockUtil.getColorByValue(this, entity.getChangeValue()));
        mTVHigh.setTextColor(StockUtil.getColorByValue(this, entity.getHigh() - preClose));
        mTVLow.setTextColor(StockUtil.getColorByValue(this, entity.getLow() - preClose));

        //多空兵解
//        String datetime = Utilities.formatDateTime(
//                String.valueOf(date), "yyyyMMDD", "yyyy-MM-DD"
//        );
        mTVSelectedDate.setText(String.valueOf(date));
        String baseCacheKey = entity.getMarket()
                + entity.getCode() + mCurrentPeriod.toString();
        List<LineEntity<DateValueEntity>> longShortExplanation
                = (List<LineEntity<DateValueEntity>>) mCache.getCacheItem(baseCacheKey + "explanation");
        if (null != longShortExplanation && longShortExplanation.size() > 0) {
            HashMap<String, String> tipMap
                    = (HashMap<String, String>) mCache.getCacheItem(baseCacheKey + "tips");
            LongShortArtOfWarUtil.analyzeLongShortExplanation(
                    longShortExplanation, index, entity, tipMap
            );
        }
        if (null != entity.getCandleExplanation()) {
            mTVCandleExplanation.setText(entity.getCandleExplanation());
        }
        if (null != entity.getVolExplanation()) {
            mTVVolExplanation.setText(entity.getVolExplanation());
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissLoadingDialog();
        switch (taskId) {
            case TaskId.TASK_ID_SECOND:
                bindKLineData(mCurrentPeriod);

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {

    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {

    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {

    }

    @Override
    public void notifyEvent(TwoXYGridChartView chart) {
        if (!(chart instanceof KLineChartViewX)) {
            return;
        }
        KLineChartViewX kline = (KLineChartViewX) chart;
        int index = kline.getSelectedIndex() + kline.getDisplayFrom();
        IChartData<IStickEntity> ohclData = kline.getKLineData();
        IChartData<IStickEntity> volData = kline.getVolStickData();
        int endIndex = ohclData.size() - 1;
        //getSelectedIndex返回的是在K线图中的索引，并不是数据在数组中的索引
        if (-1 == kline.getSelectedIndex()) {
            //重置为最新 的数据
            initViewsCanBeCallRepeatedly(stockEntity, endIndex, ohclData.get(endIndex).getDate());
            return;
        }
        if (index < 0 || index > ohclData.size()) {
            return;
        }
        OHLCEntity selectedOHLC = (OHLCEntity) ohclData.get(index);
        OHLCEntity preOHLC;

        if (index >= ohclData.size()) {
            index = ohclData.size() - 1;
        }

        if (index > 0) {
            preOHLC = (OHLCEntity) ohclData.get(index - 1);
        } else {
            preOHLC = selectedOHLC;
        }

        StockEntity selectedEntity = new StockEntity();
        selectedEntity.setName(stockEntity.getName());
        selectedEntity.setCode(stockEntity.getCode());
        selectedEntity.setMarket(stockEntity.getMarket());
        selectedEntity.setNow(selectedOHLC.getClose());
        selectedEntity.setChangeValue(selectedOHLC.getClose() - preOHLC.getClose());
        selectedEntity.setChangePercent(
                (float) (selectedEntity.getChangeValue() / preOHLC.getClose()) * 100);
        selectedEntity.setOpen(selectedOHLC.getOpen());
        selectedEntity.setHigh(selectedOHLC.getHigh());
        selectedEntity.setLow(selectedOHLC.getLow());
        selectedEntity.setVolume(volData.get(index).getHigh());
        selectedEntity.setPreClose(preOHLC.getClose());

        initViewsCanBeCallRepeatedly(selectedEntity, index, ohclData.get(index).getDate());
    }

    public void onSwitchPeriodData(int periodViewId) {
        if (mCurrentPeriodViewID == periodViewId) {
            return;
        }
        periodSwitcher.setVisibility(View.GONE);
        mCurrentPeriodViewID = periodViewId;
        switch (periodViewId) {
            case R.id.btn_five_minute:
                bindKLineData(StockPeriod.MIN_5);
                mCurrentPeriod = StockPeriod.MIN_5;
                tvSelectedPeriod.setText("5分钟");

                break;
            case R.id.btn_fifteen_minute:
                bindKLineData(StockPeriod.MIN_15);
                mCurrentPeriod = StockPeriod.MIN_15;
                tvSelectedPeriod.setText("15分钟");

                break;
            case R.id.btn_thirty_minute:
                bindKLineData(StockPeriod.MIN_30);
                mCurrentPeriod = StockPeriod.MIN_30;
                tvSelectedPeriod.setText("30分钟");

                break;
            case R.id.btn_sixty_minute:
                bindKLineData(StockPeriod.MIN_60);
                mCurrentPeriod = StockPeriod.MIN_60;
                tvSelectedPeriod.setText("60分钟");

                break;
            case R.id.btn_day_line:
                bindKLineData(StockPeriod.DAY);
                mCurrentPeriod = StockPeriod.DAY;
                tvSelectedPeriod.setText("日线");

                break;
            case R.id.btn_week_line:
                bindKLineData(StockPeriod.WEEK);
                mCurrentPeriod = StockPeriod.WEEK;
                tvSelectedPeriod.setText("周线");

                break;
            case R.id.btn_month_line:
                bindKLineData(StockPeriod.MONTH);
                mCurrentPeriod = StockPeriod.MONTH;
                tvSelectedPeriod.setText("月线");

                break;
        }
    }

    private void bindKLineData(StockPeriod period) {
        String baseCacheKey = stockEntity.getMarket() + stockEntity.getCode() + period.toString();
        List<LineEntity<DateValueEntity>> battleForceLineData
                = (List<LineEntity<DateValueEntity>>) mCache.getCacheItem(baseCacheKey + "force");
        List<LineEntity<DateValueEntity>> battleDirectionLineData
                = (List<LineEntity<DateValueEntity>>) mCache.getCacheItem(baseCacheKey + "direction");
        List<LineEntity<DateValueEntity>> battleMoraleLinesData
                = (List<LineEntity<DateValueEntity>>) mCache.getCacheItem(baseCacheKey + "morale");
        ArrayList<IStickEntity> ohlcs
                = (ArrayList<IStickEntity>) mCache.getCacheItem(baseCacheKey + "ohlc");
        ArrayList<IStickEntity> vols
                = (ArrayList<IStickEntity>) mCache.getCacheItem(baseCacheKey + "vol");

        if (null == ohlcs) {
            queryKLine(period);

            return;
        }

        if (ohlcs.size() <= 80) {
            mKLine.setDisplayFrom(0);
            mKLine.setDisplayNumber(ohlcs.size());
        } else {
            mKLine.setDisplayFrom(ohlcs.size() - 80);
            mKLine.setDisplayNumber(80);
        }
        mKLine.setLongitudeNum(0);
        mKLine.setLatitudeNum(1);
        mKLine.setDefaultNumber(80);
        mKLine.setZoomBaseLine(0);
        mKLine.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        mKLine.setAxisYPosition(GridChart.AXIS_Y_POSITION_LEFT);
        mKLine.setMaxSticksNum(ohlcs.size());
        mKLine.setResponseTouchEvent(true);
        mKLine.setDisplayCrossXOnTouch(true);
        mKLine.setCrossLinesColor(getResources().getColor(R.color.white));
        mKLine.setLatitudeColor(getResources().getColor(R.color.text_gray));
        mKLine.setLatitudeFontColor(Color.GRAY);
        mKLine.setLongitudeFontSize((int) ScreenUtils.spToPx(this, 10));

        mKLine.setMaxSticksNum(ohlcs.size());
        mKLine.setKLineData(new ListChartData<IStickEntity>(ohlcs));
        mKLine.setBattleForceLinesData(battleForceLineData);
        mKLine.setMALinesData(battleDirectionLineData);
        mKLine.setBattleMoraleLinesData(battleMoraleLinesData);
        mKLine.setVolStickData(new ListChartData<IStickEntity>(vols));
        mKLine.invalidate();

        //调用这个方法主要是刷新兵解
        initViewsCanBeCallRepeatedly(
                stockEntity, ohlcs.size() - 1, ohlcs.get(ohlcs.size() - 1).getDate()
        );
    }

    private void queryKLine(StockPeriod period) {
        Parameter parameter = new Parameter();
        parameter.addParameter("market", stockEntity.getMarket());
        parameter.addParameter("code", stockEntity.getCode());
        parameter.addParameter("type", period.toString());
        startTask(new QueryKLineRequest(
                parameter, TaskId.TASK_ID_SECOND, period, new QueryAction(this)
        ));
        mDlgLoading.show();
    }

    private void dismissLoadingDialog() {
        if (mDlgLoading.isShowing()) {
            mDlgLoading.dismiss();
        }
    }
}

package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;
import cn.limc.androidcharts.entity.DateValueEntity;
import cn.limc.androidcharts.entity.IStickEntity;
import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.entity.ListChartData;
import cn.limc.androidcharts.view.GridChart;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.FiveOrderAdapter;
import com.jdd.android.jdd.constants.ProjectConfigConstant;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.controllers.StockDetailsController;
import com.jdd.android.jdd.db.SelfSelectionStocksManager;
import com.jdd.android.jdd.entities.FifthOrderEntity;
import com.jdd.android.jdd.entities.MinutePrice;
import com.jdd.android.jdd.entities.PriceInfo;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.others.StockPeriod;
import com.jdd.android.jdd.requests.QueryKLineRequest;
import com.jdd.android.jdd.requests.QueryRealTimePriceRequest;
import com.jdd.android.jdd.requests.QueryStockDetailsRequest;
import com.jdd.android.jdd.utils.FormatUtils;
import com.jdd.android.jdd.utils.LongShortArtOfWarUtil;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.StockUtil;
import com.jdd.android.jdd.views.KLineChartViewX;
import com.jdd.android.jdd.views.StockChartViewFlipper;
import com.jdd.android.jdd.views.StockMinutePriceView;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKFragmentActivity;
import com.thinkive.android.app_engine.utils.ScreenUtils;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.*;

/**
 * 描述：股票详情
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-07-02
 * @since 1.0
 */
public class StockDetailsActivity extends TKFragmentActivity implements IQueryServer {
    public static final String KEY_IN_PARAM = "in_param";       //入参的key
    private static final String INDEX_CODES = "000001|399001|399006";

    public StockChartViewFlipper containerFlipper;
    public Button btnMinutePrice, btnLongShortTapeReading, btnLongShortExplanation;
    public Button btnFiveMinute, btnFifteenMinute, btnThirtyMinute;
    public Button btnSixtyMinute, btnDayLine, btnWeekLine, btnMonthLine;
    public ImageButton ibtnAddToSelfSelections;
    public View boldLineMinutePrice, boldLineLongShortTapeReading, boldLineExplanation;
    public View boldLineFiveMinute, boldLineFifteenMinute, boldLineThirtyMinute;
    public View boldLineSixtyMinute, boldLineDayLine, boldLineWeekLine, boldLineMonthLine;
    public PopupWindow pwLongShortExplanation;    //不是有弹出窗，用Frame布局解决适配问题
    public TextView tvLongShortExplanation;
    public Dialog dialogMoreDetails;

    public StockEntity stockEntity;
    public SelfSelectionStocksManager selfSelectionManager;
    public String minutePriceDataKey;
    public StockPeriod currentPeriod = StockPeriod.DAY;
    //当前可视的股票图id，此变量用来确定大图进去后显示哪个图
    //默认为分时图
    public int currentChartId = R.id.smp_minute_price;
    public MemberCache cache = DataCache.getInstance().getCache();

    private ImageButton mIBtnBack, mIBtnPreStock, mIBtnNextStock;
    private TextView mTVStockNameAndCode;
    private ImageButton mIBtnSearch;
    private TextView mTVNow, mTVChangeValue, mTVVolume;
    private TextView mTVChangePercent, mTVHigh, mTVLow, mTVHandOver, mTVHandOverName;
    private TextView mTVCanBeFinanced;      //融资融券标识
    private StockMinutePriceView mMinutePriceView;
    private ListView mLVBuyFiveOrder, mLVSellFiveOrder;      //买卖五档
    private KLineChartViewX mKLine;
    //弹出窗的详情变量
    private TextView mDlgTVOpen, mDlgTVHigh, mDlgTVLow, mDlgTVClose;
    private TextView mDlgTVUpLimit, mDlgTVDownLimit, mDlgTVNow;
    private TextView mDlgTVHandOver, mDlgTVTotalVol;
    private TextView mDlgTVInVol, mDlgTVOutVol;
    private TextView mDlgTVNegotiableCapitalization, mDlgTVMarketValue;
    private TextView mDlgTVProfit, mDlgTVTotalShares;
    private TextView mDlgTVPB, mDlgTVPE;
    private ImageButton mIBtnCloseMoreDialog, mIBtnOpenMoreDialog;
    private Dialog mDlgLoading;
    private LinearLayout mLLFifthOrder;

    private StockDetailsController mController;
    private List<FifthOrderEntity> mBuyFifthOrderData, mSellFifthOrderData;
    private FiveOrderAdapter mBuyFiveOrderAdapter, mSellFiveOrderAdapter;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private SpannableStringBuilder mLongShortExplanation;
    private int mCurrentPeriodViewID = 0;   //当前周期的按钮id，用来防止重复点击

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Intent intent = getIntent();
        try {
            if (intent.hasExtra(KEY_IN_PARAM)) {
                stockEntity = (StockEntity) intent.getSerializableExtra(KEY_IN_PARAM);
            } else {
                stockEntity = new StockEntity();
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
    protected void findViews() {
        containerFlipper = (StockChartViewFlipper) findViewById(R.id.vf_price_chart_container);
        boldLineMinutePrice = findViewById(R.id.v_bold_line_minute_price);
        boldLineLongShortTapeReading = findViewById(R.id.v_bold_line_long_short_tape_reading);
        btnMinutePrice = (Button) findViewById(R.id.btn_minute_price);
        btnLongShortTapeReading = (Button) findViewById(R.id.btn_long_short_tape_reading);
        ibtnAddToSelfSelections = (ImageButton) findViewById(R.id.ibtn_add_self_selection);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mIBtnNextStock = (ImageButton) findViewById(R.id.ibtn_next_stock);
        mIBtnPreStock = (ImageButton) findViewById(R.id.ibtn_pre_stock);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mTVStockNameAndCode = (TextView) findViewById(R.id.tv_stock_name_code);
        mTVNow = (TextView) findViewById(R.id.tv_now);
        mTVChangePercent = (TextView) findViewById(R.id.tv_change_percent);
        mTVChangeValue = (TextView) findViewById(R.id.tv_change_value);
        mTVHigh = (TextView) findViewById(R.id.tv_high_value);
        mTVLow = (TextView) findViewById(R.id.tv_low_value);
        mTVHandOver = (TextView) findViewById(R.id.tv_turn_over_value);
        mTVHandOverName = (TextView) findViewById(R.id.tv_turn_over);
        mTVVolume = (TextView) findViewById(R.id.tv_vol_value);
        mMinutePriceView = (StockMinutePriceView) findViewById(R.id.smp_minute_price);
        mLVBuyFiveOrder = (ListView) findViewById(R.id.lv_buy_five_order);
        mLVSellFiveOrder = (ListView) findViewById(R.id.lv_sell_five_order);
        mKLine = (KLineChartViewX) findViewById(R.id.klc_kline_small);
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
        View pwExplanation
                = LayoutInflater.from(this).inflate(R.layout.pw_long_short_explanation, null);
        tvLongShortExplanation = (TextView) pwExplanation.findViewById(R.id.tv_explanation_text);
        pwLongShortExplanation = new PopupWindow(pwExplanation);
        btnLongShortExplanation = (Button) findViewById(R.id.btn_long_short_explanation);
        boldLineExplanation = findViewById(R.id.v_bold_line_long_short_explanation);
//        tvLongShortExplanation = (TextView) findViewById(R.id.tv_explanation_text);
        mIBtnOpenMoreDialog = (ImageButton) findViewById(R.id.ibtn_show_more_stock_details);
        //弹出窗详细相关变量初始化
        dialogMoreDetails = new Dialog(this);
        dialogMoreDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMoreDetails.setContentView(R.layout.dialog_stock_details);
        dialogMoreDetails.setCanceledOnTouchOutside(true);
        mDlgTVNegotiableCapitalization = (TextView)
                dialogMoreDetails.findViewById(R.id.tv_dialog_negotiable_capitalization);
        mDlgTVDownLimit = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_down_limit);
        mDlgTVHandOver = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_hand_over);
        mDlgTVHigh = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_high);
        mDlgTVInVol = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_in);
        mDlgTVLow = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_low);
        mDlgTVMarketValue = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_market_value);
        mDlgTVNow = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_now);
        mDlgTVOpen = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_open);
        mDlgTVUpLimit = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_up_limit);
        mDlgTVTotalShares = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_total_shares);
        mDlgTVTotalVol = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_total_vol);
        mDlgTVProfit = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_profit);
        mDlgTVOutVol = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_out);
        mDlgTVPB = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_pb);
        mDlgTVPE = (TextView) dialogMoreDetails.findViewById(R.id.tv_dialog_pe);
        mIBtnCloseMoreDialog = (ImageButton) dialogMoreDetails.findViewById(R.id.ibtn_dialog_close);
        mDlgLoading = PopUpComponentUtil.createLoadingProgressDialog(this, "加载中...", true, true);
        mLLFifthOrder = (LinearLayout) findViewById(R.id.ll_fifth_order);
    }

    @Override
    protected void setListeners() {
        mController = new StockDetailsController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnMinutePrice, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnLongShortTapeReading, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnLongShortExplanation, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, ibtnAddToSelfSelections, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnNextStock, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnPreStock, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnFiveMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnFifteenMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnThirtyMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnSixtyMinute, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnDayLine, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnWeekLine, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnMonthLine, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnCloseMoreDialog, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnOpenMoreDialog, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVBuyFiveOrder, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVSellFiveOrder, mController);
        registerListener(ListenerControllerAdapter.ON_TOUCH, containerFlipper, mController);
    }

    @Override
    protected void initData() {
        mBuyFifthOrderData = stockEntity.getBuyFifthOrder();
        mSellFifthOrderData = stockEntity.getSellFifthOrder();
        selfSelectionManager = SelfSelectionStocksManager.getInstance(this);
        //搜索入口进来没有五档数据
        if (null == mBuyFifthOrderData) {
            mBuyFifthOrderData = new ArrayList<>();
        }
        if (null == mSellFifthOrderData) {
            mSellFifthOrderData = new ArrayList<>();
        }
    }

    @Override
    protected void initViews() {
        initViewsCanBeCallRepeatedly();

        if (null != mBuyFifthOrderData) {
            mBuyFiveOrderAdapter = new FiveOrderAdapter(this, mBuyFifthOrderData, stockEntity);
        }
        if (null != mSellFifthOrderData) {
            mSellFiveOrderAdapter = new FiveOrderAdapter(this, mSellFifthOrderData, stockEntity);
        }
        mLVSellFiveOrder.setAdapter(mSellFiveOrderAdapter);
        mLVBuyFiveOrder.setAdapter(mBuyFiveOrderAdapter);

        //查询自选状态，如果已经添加自选，那么显示删除自选按钮
        if (null != selfSelectionManager.query(stockEntity)) {
            ibtnAddToSelfSelections.setImageResource(R.drawable.ibtn_delete_self_selection);
        }
        pwLongShortExplanation.setWindowLayoutMode(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pwLongShortExplanation.setOutsideTouchable(true);

        //指数隐藏五档，换手改为量比
        if (null != stockEntity && INDEX_CODES.contains(stockEntity.getCode())) {
            mLLFifthOrder.setVisibility(View.GONE);
            mTVHandOverName.setText("量比");
            mTVHandOver.setText(FormatUtils.format2PointTwo(stockEntity.getQuantityRelative()) + "%");
        }
    }

    /**
     * 初始化可以重复地调用的View
     */
    private void initViewsCanBeCallRepeatedly() {
        mTVNow.setText(FormatUtils.format2PointTwo(stockEntity.getNow()));
        mTVHigh.setText(FormatUtils.format2PointTwo(stockEntity.getHigh()));
        mTVLow.setText(FormatUtils.format2PointTwo(stockEntity.getLow()));
        mTVChangePercent.setText(FormatUtils.format2PointTwo(stockEntity.getChangePercent()) + "%");
        mTVChangeValue.setText(FormatUtils.format2PointTwo(stockEntity.getChangeValue()));
        mTVVolume.setText(FormatUtils.formatMoney(stockEntity.getTurnover() * 10000));
        if (!StringUtils.isEmpty(stockEntity.getName())) {
            mTVStockNameAndCode.setText(stockEntity.getName() + "\n" + stockEntity.getCode());
        }
        //如果不是非数字，才显示，发现指数的换手和流通市值都是NaN
        if (!Double.isNaN(stockEntity.getHandOver())) {
            mTVHandOver.setText(FormatUtils.format2PointTwo(stockEntity.getHandOver()) + "%");
        }

        //设置字体颜色
        double preClose = stockEntity.getPreClose();
        mTVChangePercent.setTextColor(StockUtil.getColorByValue(this, stockEntity.getChangePercent()));
        mTVChangeValue.setTextColor(StockUtil.getColorByValue(this, stockEntity.getChangeValue()));
        mTVNow.setTextColor(StockUtil.getColorByValue(this, stockEntity.getNow() - preClose));
        mTVHigh.setTextColor(StockUtil.getColorByValue(this, stockEntity.getHigh() - preClose));
        mTVLow.setTextColor(StockUtil.getColorByValue(this, stockEntity.getLow() - preClose));

        //弹出窗部分初始化
        mDlgTVOpen.setText(FormatUtils.format2PointTwo(stockEntity.getOpen()));
        mDlgTVNow.setText(FormatUtils.format2PointTwo(stockEntity.getNow()));
        mDlgTVHigh.setText(FormatUtils.format2PointTwo(stockEntity.getHigh()));
        mDlgTVLow.setText(FormatUtils.format2PointTwo(stockEntity.getLow()));
        mDlgTVTotalVol.setText(FormatUtils.formatMoney(stockEntity.getVolume()));
        if (!Double.isNaN(stockEntity.getHandOver())) {
            mDlgTVHandOver.setText(FormatUtils.format2PointTwo(stockEntity.getHandOver()) + "%");
        }
        mDlgTVOutVol.setText(getSppanedString(
                FormatUtils.formatMoney(stockEntity.getOutVol()),
                getResources().getColor(R.color.stock_green))
        );
        mDlgTVProfit.setText(String.valueOf(stockEntity.getProfitPerShare()));
        mDlgTVTotalShares.setText(FormatUtils.formatMoney(stockEntity.getTotalShares()));
        mDlgTVUpLimit.setText(FormatUtils.format2PointTwo(stockEntity.getLimitUp()));
        mDlgTVNegotiableCapitalization.setText(getSppanedString(
                stockEntity.getNegotiableMarketCapitalization() + "亿",
                getResources().getColor(R.color.pink))
        );
        mDlgTVDownLimit.setText(FormatUtils.format2PointTwo(stockEntity.getLimitDown()));
        if (!Double.isNaN(stockEntity.getMarketValue())) {
            mDlgTVMarketValue.setText(stockEntity.getMarketValue() + "亿");
        }
        mDlgTVInVol.setText(getSppanedString(
                FormatUtils.formatMoney(stockEntity.getInVol()),
                getResources().getColor(R.color.text_red))
        );
        mDlgTVPB.setText(String.valueOf(stockEntity.getPb()));
        mDlgTVPE.setText(String.valueOf(stockEntity.getPe()));

        //设置弹出窗的变量字体颜色
        mDlgTVNow.setTextColor(StockUtil.getColorByValue(this, stockEntity.getNow() - preClose));
        mDlgTVHigh.setTextColor(StockUtil.getColorByValue(this, stockEntity.getHigh() - preClose));
        mDlgTVLow.setTextColor(StockUtil.getColorByValue(this, stockEntity.getLow() - preClose));
        mDlgTVUpLimit.setTextColor(StockUtil.getColorByValue(this, 1));
        mDlgTVDownLimit.setTextColor(StockUtil.getColorByValue(this, -1));
        mDlgTVOpen.setTextColor(StockUtil.getColorByValue(this, stockEntity.getOpen() - preClose));
    }

    private SpannableStringBuilder getSppanedString(String text, int color) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        if (text.contains("万") || text.contains("亿")) {
            ssb.setSpan(
                    new ForegroundColorSpan(color), 0,
                    text.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            ssb.setSpan(
                    new ForegroundColorSpan(getResources().getColor(R.color.menu_text_gray)),
                    text.length() - 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        } else {
            ssb.setSpan(
                    new ForegroundColorSpan(color), 0,
                    text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        return ssb;
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnDayLine.performClick();
        queryStockDetails();
        queryRealTimePrice();
        if (StockUtil.isTradingTime()) {
            if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    queryStockDetails();
                    queryRealTimePrice();
                }
            };
            mTimer.schedule(mTimerTask, 0, ProjectConfigConstant.REFRESH_PERIOD);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mTimerTask) {
            mTimerTask.cancel();
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissLoadingDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                PriceInfo priceInfo = new PriceInfo();
                priceInfo.setHigh(stockEntity.getHigh());
                priceInfo.setLow(stockEntity.getLow());
                priceInfo.setYesterday(stockEntity.getPreClose());

                minutePriceDataKey = bundle.getString(String.valueOf(taskId));
                final ArrayList<MinutePrice> minutePrices = (ArrayList<MinutePrice>)
                        cache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == minutePrices || minutePrices.size() < 1) {
                    return;
                }
                mMinutePriceView.setMinutePriceList(minutePrices);
                mMinutePriceView.setPriceInfo(priceInfo);
                mMinutePriceView.invalidate();

                break;
            case TaskId.TASK_ID_SECOND:
                bindKLineData(currentPeriod);

                break;
            case TaskId.TASK_ID_THIRD:
                StockEntity entity = (StockEntity)
                        cache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                stockEntity = entity;
                initViewsCanBeCallRepeatedly();
                mBuyFifthOrderData.clear();
                mSellFifthOrderData.clear();
                mBuyFifthOrderData.addAll(stockEntity.getBuyFifthOrder());
                mSellFifthOrderData.addAll(stockEntity.getSellFifthOrder());
                //因为搜索界面进来，stockEntity没有昨收数据，所以需要更新
                mBuyFiveOrderAdapter.setStockEntity(stockEntity);
                mSellFiveOrderAdapter.setStockEntity(stockEntity);
                mBuyFiveOrderAdapter.notifyDataSetChanged();
                mSellFiveOrderAdapter.notifyDataSetChanged();

                //如果不是交易时间，在查询到股票详情之后重新查询一遍分时数据
                //防止在没有高低价时分时初始化分时图失败
//                if (!StockUtil.isTradingTime()) {
                queryRealTimePrice();
//                }

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        onQueryFailed(taskId);
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        onQueryFailed(taskId);
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        onQueryFailed(taskId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();

        if (!StringUtils.isEmpty(minutePriceDataKey)) {
            cache.remove(minutePriceDataKey);
        }
        for (StockPeriod period : StockPeriod.values()) {
            String baseCacheKey = stockEntity.getMarket()
                    + stockEntity.getCode() + period.toString();
            String key = baseCacheKey + "force";
            removePriceData(key);
            key = baseCacheKey + "direction";
            removePriceData(key);
            key = baseCacheKey + "morale";
            removePriceData(key);
            key = baseCacheKey + "ohlc";
            removePriceData(key);
            key = baseCacheKey + "vol";
            removePriceData(key);
            key = baseCacheKey + "explanation";
            removePriceData(key);
        }
    }

    private void removePriceData(String key) {
        if (cache.isExistKey(key)) {
            cache.remove(key);
        }
    }

    private void onQueryFailed(int taskId) {
        dismissLoadingDialog();
    }

    public void showLongShortExplanation() {
        if (pwLongShortExplanation.isShowing() || null == mLongShortExplanation) {
            return;
        }
        tvLongShortExplanation.setText(mLongShortExplanation);
        pwLongShortExplanation.showAtLocation(
                btnLongShortExplanation, Gravity.LEFT,
                (int) mKLine.getDataQuadrantPaddingStartX(),
                -(int) ScreenUtils.dpToPx(this, 37));
    }

    public void onSwitchPeriodData(int periodViewId) {
        if (mCurrentPeriodViewID == periodViewId) {
            return;
        }
        mCurrentPeriodViewID = periodViewId;
        switch (periodViewId) {
            case R.id.btn_five_minute:
                bindKLineData(StockPeriod.MIN_5);
                currentPeriod = StockPeriod.MIN_5;

                break;
            case R.id.btn_fifteen_minute:
                bindKLineData(StockPeriod.MIN_15);
                currentPeriod = StockPeriod.MIN_15;

                break;
            case R.id.btn_thirty_minute:
                bindKLineData(StockPeriod.MIN_30);
                currentPeriod = StockPeriod.MIN_30;

                break;
            case R.id.btn_sixty_minute:
                bindKLineData(StockPeriod.MIN_60);
                currentPeriod = StockPeriod.MIN_60;

                break;
            case R.id.btn_day_line:
                bindKLineData(StockPeriod.DAY);
                currentPeriod = StockPeriod.DAY;

                break;
            case R.id.btn_week_line:
                bindKLineData(StockPeriod.WEEK);
                currentPeriod = StockPeriod.WEEK;

                break;
            case R.id.btn_month_line:
                bindKLineData(StockPeriod.MONTH);
                currentPeriod = StockPeriod.MONTH;

                break;
        }
    }

    private void bindKLineData(StockPeriod period) {
        String baseCacheKey = stockEntity.getMarket() + stockEntity.getCode() + period.toString();
        List<LineEntity<DateValueEntity>> battleForceLineData
                = (List<LineEntity<DateValueEntity>>) cache.getCacheItem(baseCacheKey + "force");
        List<LineEntity<DateValueEntity>> battleDirectionLineData
                = (List<LineEntity<DateValueEntity>>) cache.getCacheItem(baseCacheKey + "direction");
        List<LineEntity<DateValueEntity>> battleMoraleLinesData
                = (List<LineEntity<DateValueEntity>>) cache.getCacheItem(baseCacheKey + "morale");
        ArrayList<IStickEntity> ohlcs
                = (ArrayList<IStickEntity>) cache.getCacheItem(baseCacheKey + "ohlc");
        ArrayList<IStickEntity> vols
                = (ArrayList<IStickEntity>) cache.getCacheItem(baseCacheKey + "vol");
        List<LineEntity<DateValueEntity>> longShortExplanation
                = (List<LineEntity<DateValueEntity>>) cache.getCacheItem(baseCacheKey + "explanation");

        if (null == ohlcs) {
            queryKLine(period);

            return;
        }

        if (ohlcs.size() <= 50) {
            mKLine.setDisplayFrom(0);
            mKLine.setDisplayNumber(ohlcs.size());
        } else {
            mKLine.setDisplayFrom(ohlcs.size() - 50);
            mKLine.setDisplayNumber(50);
        }
        mKLine.setLongitudeNum(0);
        mKLine.setLatitudeNum(1);
        mKLine.setLatitudeFontColor(Color.GRAY);
        mKLine.setLatitudeColor(getResources().getColor(R.color.text_gray));
        mKLine.setLongitudeFontSize((int) ScreenUtils.spToPx(this, 10));
        mKLine.setLongitudeColor(Color.WHITE);
        mKLine.setDefaultNumber(50);
        mKLine.setZoomBaseLine(0);
        mKLine.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        mKLine.setAxisYPosition(GridChart.AXIS_Y_POSITION_LEFT);
        mKLine.setMaxSticksNum(ohlcs.size());

        mKLine.setMaxSticksNum(ohlcs.size());
        mKLine.setKLineData(new ListChartData<IStickEntity>(ohlcs));
        mKLine.setBattleForceLinesData(battleForceLineData);
        mKLine.setMALinesData(battleDirectionLineData);
        mKLine.setBattleMoraleLinesData(battleMoraleLinesData);
        mKLine.setVolStickData(new ListChartData<IStickEntity>(vols));
        mKLine.requestFocus();
        mKLine.invalidate();

        if (null != longShortExplanation && longShortExplanation.size() > 0) {
            HashMap<String, String> tipMap
                    = (HashMap<String, String>) cache.getCacheItem(baseCacheKey + "tips");
            mLongShortExplanation = LongShortArtOfWarUtil.analyzeLongShortExplanationTip(
                    longShortExplanation,
                    longShortExplanation.get(0).getLineData().size() - 1,
                    tipMap
            );
            tvLongShortExplanation.setText(mLongShortExplanation);
        }
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

    private void queryRealTimePrice() {
        Parameter parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(TaskId.TASK_ID_FIRST));
        parameter.addParameter("market", stockEntity.getMarket());
        parameter.addParameter("code", stockEntity.getCode());
        startTask(new QueryRealTimePriceRequest(parameter, new QueryAction(StockDetailsActivity.this)));
    }

    private void queryStockDetails() {
        Parameter parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(TaskId.TASK_ID_THIRD));
        parameter.addParameter("market", stockEntity.getMarket());
        parameter.addParameter("code", stockEntity.getCode());
        startTask(new QueryStockDetailsRequest(parameter, new QueryAction(StockDetailsActivity.this)));
    }
}

package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.AssociatedStockListAdapter;
import com.jdd.android.jdd.adapters.FiveOrderAdapter;
import com.jdd.android.jdd.entities.FifthOrderEntity;
import com.jdd.android.jdd.entities.MinutePrice;
import com.jdd.android.jdd.entities.PriceInfo;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryAssociatedStocksRequest;
import com.jdd.android.jdd.requests.QueryRealTimePriceRequest;
import com.jdd.android.jdd.utils.FormatUtils;
import com.jdd.android.jdd.views.StockMinutePriceView;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.android.app_engine.engine.TKFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：股票详情，分时、五档
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-07-01
 * @since 1.0
 */
@Deprecated
public class StockDetailsFragment extends TKFragment implements IQueryServer {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private View mLayout;
    private ListView mBuyFifthOrder, mSellFifthOrder;
    private ListView mAssociatedStocks;
    private TextView mNow, mAveragePrice, mChangeValue, mOpen;
    private TextView mChangePercent, mHigh, mLow, mHandOver;
    private TextView mRiseLimit, mDropLimit;
    private TextView mQuantityRelative, mVolume;
    private TextView mNegotiableMarketCapitalization;
    private StockMinutePriceView mMinutePriceView;

    private StockEntity mStockEntity;
    private List<FifthOrderEntity> mBuyFifthOrderData, mSellFifthOrderData;
    private List<StockEntity> mAssociatedStocksData;

    private FiveOrderAdapter mBuyFiveOrderAdapter, mSellFiveOrderAdapter;
    private AssociatedStockListAdapter mAssociatedStockListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_stock_details, container, false);

        findViews();
        setListeners();
        initData();
        initViews();

        return mLayout;
    }

    @Override
    protected void findViews() {
        mBuyFifthOrder = (ListView) mLayout.findViewById(R.id.lv_buy_five_order);
        mSellFifthOrder = (ListView) mLayout.findViewById(R.id.lv_sell_five_order);
        mAssociatedStocks = (ListView) mLayout.findViewById(R.id.lv_associated_stocks);
        mNow = (TextView) mLayout.findViewById(R.id.tv_now);
        mAveragePrice = (TextView) mLayout.findViewById(R.id.tv_average_price);
        mChangePercent = (TextView) mLayout.findViewById(R.id.tv_change_percent);
        mChangeValue = (TextView) mLayout.findViewById(R.id.tv_change_value);
        mOpen = (TextView) mLayout.findViewById(R.id.tv_open);
        mHigh = (TextView) mLayout.findViewById(R.id.tv_high);
        mLow = (TextView) mLayout.findViewById(R.id.tv_low);
        mRiseLimit = (TextView) mLayout.findViewById(R.id.tv_rise_limit);
        mDropLimit = (TextView) mLayout.findViewById(R.id.tv_drop_limit);
        mQuantityRelative = (TextView) mLayout.findViewById(R.id.tv_quantity_relative);
        mVolume = (TextView) mLayout.findViewById(R.id.tv_volumn);
        mNegotiableMarketCapitalization
                = (TextView) mLayout.findViewById(R.id.tv_negotiable_market_capitalization);
        mMinutePriceView = (StockMinutePriceView) mLayout.findViewById(R.id.smp_minute_price);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mStockEntity = (StockEntity) bundle.getSerializable("stock_entity");
        mBuyFifthOrderData = mStockEntity.getBuyFifthOrder();
        mSellFifthOrderData = mStockEntity.getSellFifthOrder();

        if (null != mBuyFifthOrderData) {
            mBuyFiveOrderAdapter = new FiveOrderAdapter(getActivity(), mBuyFifthOrderData, mStockEntity);
        }
        if (null != mSellFifthOrderData) {
            mSellFiveOrderAdapter = new FiveOrderAdapter(getActivity(), mSellFifthOrderData, mStockEntity);
        }

        Parameter parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(300));
        parameter.addParameter("market", mStockEntity.getMarket());
        parameter.addParameter("code", mStockEntity.getCode());
        startTask(new QueryAssociatedStocksRequest(parameter, new QueryAction(this)));

        parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(301));
        parameter.addParameter("market", mStockEntity.getMarket());
        parameter.addParameter("code", mStockEntity.getCode());
        startTask(new QueryRealTimePriceRequest(parameter, new QueryAction(this)));
    }

    @Override
    protected void initViews() {
        if (null == mStockEntity) {
            return;
        }
        mNow.setText(FormatUtils.format2PointTwo(mStockEntity.getNow()));
        mOpen.setText(FormatUtils.format2PointTwo(mStockEntity.getOpen()));
        mHigh.setText(FormatUtils.format2PointTwo(mStockEntity.getHigh()));
        mLow.setText(FormatUtils.format2PointTwo(mStockEntity.getLow()));
        mChangePercent.setText(FormatUtils.format2PointTwo(mStockEntity.getChangePercent()) + "%");
        mChangeValue.setText(FormatUtils.format2PointTwo(mStockEntity.getChangeValue()));
//        mAveragePrice.setText(FormatUtils.format2PointTwo(stockEntity.get));
        mRiseLimit.setText(FormatUtils.format2PointTwo(mStockEntity.getLimitUp()));
        mDropLimit.setText(FormatUtils.format2PointTwo(mStockEntity.getLimitDown()));
        mQuantityRelative.setText(FormatUtils.format2PointTwo(mStockEntity.getQuantityRelative()));
        mVolume.setText(FormatUtils.formatMoney(mStockEntity.getVolume()));
        mNegotiableMarketCapitalization.setText(
                FormatUtils.formatMoney(mStockEntity.getNegotiableMarketCapitalization()));

        if (null != mSellFiveOrderAdapter) {
            mSellFifthOrder.setAdapter(mSellFiveOrderAdapter);
        }
        if (null != mBuyFiveOrderAdapter) {
            mBuyFifthOrder.setAdapter(mBuyFiveOrderAdapter);
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        switch (taskId) {
            case 300:       //相关股票
                mAssociatedStocksData = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == mAssociatedStocksData || mAssociatedStocksData.size() < 1) {
                    return;
                }
                mAssociatedStocks.post(new Runnable() {
                    @Override
                    public void run() {
                        mAssociatedStockListAdapter = null;
                        mAssociatedStockListAdapter
                                = new AssociatedStockListAdapter(getActivity(), mAssociatedStocksData);
                        mAssociatedStocks.setAdapter(mAssociatedStockListAdapter);
                    }
                });

                break;
            case 301:   //分时数据
                PriceInfo priceInfo = new PriceInfo();
                priceInfo.setHigh(mStockEntity.getHigh());
                priceInfo.setLow(mStockEntity.getLow());
                priceInfo.setYesterday(mStockEntity.getPreClose());

                final ArrayList<MinutePrice> minutePrices = (ArrayList<MinutePrice>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == minutePrices || minutePrices.size() < 1) {
                    return;
                }
                mMinutePriceView.setMinutePriceList(minutePrices);
                mMinutePriceView.setPriceInfo(priceInfo);
                mMinutePriceView.invalidate();

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
}

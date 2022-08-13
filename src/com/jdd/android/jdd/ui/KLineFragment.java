package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.limc.androidcharts.entity.DateValueEntity;
import cn.limc.androidcharts.entity.IStickEntity;
import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.entity.ListChartData;
import cn.limc.androidcharts.view.GridChart;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryKLineRequest;
import com.jdd.android.jdd.views.KLineChartViewX;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.android.app_engine.engine.TKFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-07-09
 * @since 1.0
 */
@Deprecated
public class KLineFragment extends TKFragment implements IQueryServer {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private View mLayout;

    private KLineChartViewX mKLine;

    private StockEntity mStockEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_kline, container, false);

        findViews();
        setListeners();
        initData();
        initViews();

        return mLayout;
    }

    @Override
    protected void findViews() {
        mKLine = (KLineChartViewX) mLayout.findViewById(R.id.kline_main);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mStockEntity = (StockEntity) bundle.getSerializable("stock_entity");

        Parameter parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(400));
        parameter.addParameter("market", mStockEntity.getMarket());
        parameter.addParameter("code", mStockEntity.getCode());
        parameter.addParameter("type", "5");
//        startTask(new QueryKLineRequest(parameter, new QueryAction(this)));
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        String baseCacheKey = mStockEntity.getMarket() + mStockEntity.getCode();
        switch (taskId) {
            case 400:
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

                if (ohlcs.size() < 20) {
                    mKLine.setLongitudeNum(1);
                } else if (ohlcs.size() < 30) {
                    mKLine.setLongitudeNum(2);
                } else {
                    mKLine.setLongitudeNum(3);
                }

                if (ohlcs.size() <= 100) {
                    mKLine.setDisplayFrom(0);
                    mKLine.setDisplayNumber(ohlcs.size());
                } else {
                    mKLine.setDisplayFrom(ohlcs.size() - 100);
                    mKLine.setDisplayNumber(100);
                }
                mKLine.setDefaultNumber(100);
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
                mKLine.invalidate();

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

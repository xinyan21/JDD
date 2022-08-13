package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.IndexGridAdapter;
import com.jdd.android.jdd.adapters.SelfSelectionStocksAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.ProjectConfigConstant;
import com.jdd.android.jdd.constants.StockMarket;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.controllers.PriceIndexController;
import com.jdd.android.jdd.db.SelfSelectionStocksManager;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryStockListDetailsRequest;
import com.jdd.android.jdd.utils.StockUtil;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：自选股票池
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class SelfSelectionStocksFragment extends TKFragment implements IQueryServer {
    public static int sDefaultUserID = 1;  //默认用户id，未登录统一用这个id保存自选股
    private MemberCache mCache = DataCache.getInstance().getCache();

    private View mLayout;
    private ListView mLVSelfSelections;
    private GridView mGVIndex;
    private TextView mTVNoSelfSelection;

    private SelfSelectionStocksManager mDBManager;
    private SelfSelectionStocksAdapter mAdapter;
    private IndexGridAdapter mIndexGridAdapter;
    private List<StockEntity> mStocks, mIndexEntities;

    private PriceIndexController mController;
    private PriceIndexActivity mActivity;
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_self_selection, container, false);

        mTimer = new Timer();

        initData();
        findViews();
        setListeners();
        initViews();

        return mLayout;
    }

    @Override
    protected void findViews() {
        mLVSelfSelections = (ListView) mLayout.findViewById(R.id.lv_self_selection_stocks);
        mGVIndex = (GridView) mLayout.findViewById(R.id.gv_index);
        mTVNoSelfSelection = (TextView) mLayout.findViewById(R.id.tv_no_self_selections);
    }

    @Override
    protected void setListeners() {
        mActivity = (PriceIndexActivity) getActivity();
        mController = new PriceIndexController(mActivity);
        mActivity.registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, mLVSelfSelections, mController);
        mActivity.registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, mGVIndex, mController);
    }

    @Override
    protected void initData() {
        mIndexEntities = generateDefaultSelfSelectionStocks();
        mDBManager = SelfSelectionStocksManager.getInstance(getActivity());
        UserEntity loginUser = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
        long userId = sDefaultUserID;
        if (null != loginUser) {
            userId = loginUser.getUserId();
        }
        mStocks = mDBManager.queryUserStocks(userId);
        if (null == mStocks) {
            mStocks = new ArrayList<>();
        }
        mAdapter = new SelfSelectionStocksAdapter(getActivity(), mStocks);
        mIndexGridAdapter = new IndexGridAdapter(getActivity(), mIndexEntities);
    }

    @Override
    protected void initViews() {
        mLVSelfSelections.setAdapter(mAdapter);
        mGVIndex.setAdapter(mIndexGridAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserEntity loginUser = (UserEntity)
                DataCache.getInstance().getCache().getCacheItem(
                        CacheKey.KEY_CURRENT_LOGIN_USER_INFO
                );
        long userId = SelfSelectionStocksFragment.sDefaultUserID;
        if (null != loginUser) {
            userId = loginUser.getUserId();
        }
        mStocks.clear();
        mStocks.addAll(mDBManager.queryUserStocks(userId));
        if (mStocks.size() > 0) {
            mAdapter.notifyDataSetChanged();
            mLVSelfSelections.setVisibility(View.VISIBLE);
            mTVNoSelfSelection.setVisibility(View.GONE);
        } else {
            mLVSelfSelections.setVisibility(View.GONE);
            mTVNoSelfSelection.setVisibility(View.VISIBLE);
        }

        if (StockUtil.isTradingTime()) {
            if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    queryLatestPrice();
                }
            };
            mTimer.schedule(mTimerTask, 0, ProjectConfigConstant.REFRESH_PERIOD);
        } else {
            queryLatestPrice();
        }
    }

    private void queryLatestPrice() {
        //请求最新数据
        startTask(new QueryStockListDetailsRequest(
                mStocks, TaskId.TASK_ID_FIRST,
                CacheKey.KEY_STOCK_SELF_SELECTIONS,
                new QueryAction(SelfSelectionStocksFragment.this)));
        startTask(new QueryStockListDetailsRequest(
                mIndexEntities,
                TaskId.TASK_ID_SECOND,
                CacheKey.KEY_STOCK_INDEX_LIST,
                new QueryAction(SelfSelectionStocksFragment.this)));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mTimerTask) {
            mTimerTask.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final List<StockEntity> latestDataStockEntities = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == latestDataStockEntities || latestDataStockEntities.size() < 1) {
                    return;
                }
                mStocks.clear();
                mStocks.addAll(latestDataStockEntities);
                latestDataStockEntities.clear();
                mAdapter.notifyDataSetChanged();
                //保存最新数据到数据库
                mDBManager.update(mStocks);

                break;
            case TaskId.TASK_ID_SECOND:
                final List<StockEntity> latestIndexEntities = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == latestIndexEntities || latestIndexEntities.size() < 1) {
                    return;
                }
                mIndexEntities.clear();
                mIndexEntities.addAll(latestIndexEntities);
                latestIndexEntities.clear();
                mIndexGridAdapter.notifyDataSetChanged();

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

    private List<StockEntity> generateDefaultSelfSelectionStocks() {
        List<StockEntity> stocks = new ArrayList<>();
        StockEntity entity = new StockEntity();
        entity.setMarket(StockMarket.SH);
        entity.setCode("000001");
        entity.setName("上证指数");
        entity.setUserId(sDefaultUserID);
        stocks.add(entity);

        entity = new StockEntity();
        entity.setMarket(StockMarket.SZ);
        entity.setCode("399001");
        entity.setName("深证指数");
        entity.setUserId(sDefaultUserID);
        stocks.add(entity);

        entity = new StockEntity();
        entity.setMarket(StockMarket.SZ);
        entity.setCode("399006");
        entity.setName("创业扳指");
        entity.setUserId(sDefaultUserID);
        stocks.add(entity);

        return stocks;
    }
}

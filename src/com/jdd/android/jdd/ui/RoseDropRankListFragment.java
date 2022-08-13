package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryRoseListAction;
import com.jdd.android.jdd.adapters.RoseDropRankAdapter;
import com.jdd.android.jdd.constants.ProjectConfigConstant;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.controllers.RoseDropRankController;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryRoseListRequest;
import com.jdd.android.jdd.utils.StockUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKFragment;
import com.thinkive.android.app_engine.engine.TKFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：涨跌排名
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class RoseDropRankListFragment extends TKFragment implements IQueryServer {
    public String roseDataKey, dropDataKey;

    private MemberCache mCache = DataCache.getInstance().getCache();

    private View mLayout;
    private ListView mRoseRankList;
    private ListView mDropRankList;
    private TextView mLimitUpCount, mLimitDownCount;
    private Button mBtnMoreRose, mBtnMoreDrop;

    private List<StockEntity> mRoseRankStocks, mDropRankStocks;
    private RoseDropRankAdapter mRoseRankAdapter, mDropRankAdapter;

    private RoseDropRankController mController;
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_rose_drop_rank_list, container, false);

        mTimer = new Timer();

        initData();
        findViews();
        setListeners();
        initViews();

        return mLayout;
    }

    @Override
    protected void findViews() {
        mRoseRankList = (ListView) mLayout.findViewById(R.id.lv_rose_list);
        mDropRankList = (ListView) mLayout.findViewById(R.id.lv_drop_list);
        mLimitDownCount = (TextView) mLayout.findViewById(R.id.tv_limit_down_count);
        mLimitUpCount = (TextView) mLayout.findViewById(R.id.tv_limit_up_count);
        mBtnMoreDrop = (Button) mLayout.findViewById(R.id.btn_more_rose);
        mBtnMoreRose = (Button) mLayout.findViewById(R.id.btn_more_drop);
    }

    @Override
    protected void setListeners() {
        mController = new RoseDropRankController(this);
        TKFragmentActivity activity = (TKFragmentActivity) getActivity();
        activity.registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mRoseRankList, mController);
        activity.registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mDropRankList, mController);
        activity.registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreDrop, mController);
        activity.registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreRose, mController);
    }

    @Override
    protected void initData() {
        mRoseRankStocks = new ArrayList<>();
        mDropRankStocks = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        mRoseRankAdapter = new RoseDropRankAdapter(getActivity(), mRoseRankStocks);
        mDropRankAdapter = new RoseDropRankAdapter(getActivity(), mDropRankStocks);
        mRoseRankList.setAdapter(mRoseRankAdapter);
        mDropRankList.setAdapter(mDropRankAdapter);
    }

    private void queryLatestPrice() {
        Parameter parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(TaskId.TASK_ID_FIRST));
        parameter.addParameter("sortFlag", "up");
        startTask(new QueryRoseListRequest(
                parameter, new QueryRoseListAction(RoseDropRankListFragment.this)));

        parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(TaskId.TASK_ID_SECOND));
        parameter.addParameter("sortFlag", "down");
        startTask(new QueryRoseListRequest(
                parameter, new QueryRoseListAction(RoseDropRankListFragment.this)));
    }

    @Override
    public void onResume() {
        super.onResume();
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
                roseDataKey = bundle.getString(String.valueOf(taskId));
                final List<StockEntity> roseRankStocks
                        = (List<StockEntity>) mCache.getCacheItem(roseDataKey);
                if (null == roseRankStocks || roseRankStocks.size() < 1) {
                    return;
                }
                mRoseRankStocks.clear();
                mRoseRankStocks.addAll(roseRankStocks.subList(0, 10));
                mRoseRankAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_SECOND:
                dropDataKey = bundle.getString(String.valueOf(taskId));
                final List<StockEntity> dropRankStocks
                        = (List<StockEntity>) mCache.getCacheItem(dropDataKey);
                if (null == dropRankStocks || dropRankStocks.size() < 1) {
                    return;
                }
                mDropRankStocks.clear();
                mDropRankStocks.addAll(dropRankStocks.subList(0, 10));
                mDropRankAdapter.notifyDataSetChanged();

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

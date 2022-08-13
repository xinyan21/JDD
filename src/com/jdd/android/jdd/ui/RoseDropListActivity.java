package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryRoseListAction;
import com.jdd.android.jdd.adapters.RoseDropRankAdapter;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.RankFunction;
import com.jdd.android.jdd.controllers.RoseDropListActivityController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryRankRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：涨跌幅榜
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-04-08
 * @since 1.0
 */
public class RoseDropListActivity extends TKActivity implements IQueryServer {
    public static final String LIST_TYPE_ROSE = "涨幅榜";
    public static final String LIST_TYPE_DROP = "跌幅榜";
    public static final String KEY_DATA_KEY = "DATA_KEY";
    public static final String KEY_LIST_TYPE = "LIST_TYPE";
    public short currentPage = 1;
    public String rankDirection = RankFunction.RANK_DIRECTION_UP;
    public List<StockEntity> stocks;
    public TextView tvChangePercent;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private PullToRefreshListView mLVMain;

    private Dialog mDialogLoading;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private RoseDropListActivityController mController;
    private RoseDropRankAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rose_drop_list);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mLVMain = (PullToRefreshListView) findViewById(R.id.lv_main);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(
                this, "加载中，请稍候...", true, true
        );
        tvChangePercent = (TextView) findViewById(R.id.tv_change_percent);
    }

    @Override
    protected void setListeners() {
        mController = new RoseDropListActivityController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, tvChangePercent, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVMain, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVMain, mController);
    }

    @Override
    protected void initData() {
        stocks = new ArrayList<>();
        if (null != stocks) {
            mAdapter = new RoseDropRankAdapter(this, stocks);
        }
        if (LIST_TYPE_ROSE.equals(getIntent().getStringExtra(KEY_LIST_TYPE))) {
            rankDirection = RankFunction.RANK_DIRECTION_UP;
        } else {
            rankDirection = RankFunction.RANK_DIRECTION_DOWN;
        }
        queryStocks();
    }

    @Override
    protected void initViews() {
        mTVTitle.setText(getIntent().getStringExtra(KEY_LIST_TYPE));
        if (null != mAdapter) {
            mLVMain.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
        mLVMain.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    public void queryStocks() {
        mAdapter.notifyDataSetChanged();
        Parameter parameter = new Parameter();
        parameter.addParameter(RankFunction.IN_CATEGORY, RankFunction.CATEGORY_A_SHARE);
        parameter.addParameter(RankFunction.IN_PAGE_SIZE, "10");
        parameter.addParameter(RankFunction.IN_RANK_FIELD, RankFunction.RANK_FIELD_CHANGE_PERCENT);
        parameter.addParameter(RankFunction.IN_PAGE, String.valueOf(currentPage));
        parameter.addParameter(RankFunction.IN_RANK_DIRECTION, rankDirection);
        startTask(new QueryRankRequest(TaskId.TASK_ID_FIRST, parameter, new QueryRoseListAction(this)));
        mDialogLoading.show();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        onLoadComplete();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final List<StockEntity> courses = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == courses || courses.size() < 1) {
                    if (1 != currentPage) {
                        Toast.makeText(this, "没有更多股票啦", Toast.LENGTH_SHORT).show();
                    } else {
                        stocks.clear();
                        Toast.makeText(this, "没有搜索到相关股票", Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.notifyDataSetChanged();
                    return;
                }

                this.stocks.addAll(courses);
                courses.clear();
                mAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        onLoadComplete();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        onLoadComplete();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        onLoadComplete();
    }

    private void onLoadComplete() {
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
        mLVMain.onRefreshComplete();
    }
}

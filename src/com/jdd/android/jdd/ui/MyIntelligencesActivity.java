package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.MyArticlesAdapter;
import com.jdd.android.jdd.adapters.NewMyArticlesAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.ArticleFunction;
import com.jdd.android.jdd.constants.function.QueryMyIntelsFunction;
import com.jdd.android.jdd.controllers.MyIntelligencesController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryMyIntelsRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：我的情报
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class MyIntelligencesActivity extends TKActivity implements IQueryServer {
    public static final short INTEL_ALL = 1;
    public static final short INTEL_MY_BOUGHT = 2;
    public static final short INTEL_SEARCH = 3;
    public int currentAllPage = 1, currentBoughtPage = 1, currentSearchPage = 1;
    public String searchKey = "";
    public short currentIntelType = INTEL_ALL;
    public List<ArticleEntity> allIntelEntities, boughtIntelEntities, searchResultEntities;
    public EditText etSearchKey;
    public Button btnIMyBoughtIntels, btnMyPublishedIntels;
    public View boldLineMyBoughtIntels, boldLineMyPublishedIntels;

    private ImageButton mIBtnSearch;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private PullToRefreshListView mLVIntelligences;
    private Dialog mLoadingDialog;

    private MyIntelligencesController mController;
    private NewMyArticlesAdapter mAllIntelsAdapter, mBoughtIntelsAdapter, mSearchResultAdapter;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intellligences);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etSearchKey = (EditText) findViewById(R.id.et_search_key);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        boldLineMyBoughtIntels = findViewById(R.id.v_bold_line_my_bought_intels);
        btnIMyBoughtIntels = (Button) findViewById(R.id.btn_my_published_intels);
        btnMyPublishedIntels = (Button) findViewById(R.id.btn_my_bought_intels);
        boldLineMyPublishedIntels = findViewById(R.id.v_bold_line_my_published_intels);
        mLVIntelligences = (PullToRefreshListView) findViewById(R.id.lv_my_intelligences);
        mLoadingDialog = PopUpComponentUtil.createLoadingProgressDialog(
                this, "加载情报中，请稍候...", true, true
        );
    }

    @Override
    protected void setListeners() {
        mController = new MyIntelligencesController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnIMyBoughtIntels, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnMyPublishedIntels, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVIntelligences, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVIntelligences, mController);
    }

    @Override
    protected void initData() {
        allIntelEntities = new ArrayList<>();
        boughtIntelEntities = new ArrayList<>();
        searchResultEntities = new ArrayList<>();
        mAllIntelsAdapter = new NewMyArticlesAdapter(this, allIntelEntities);
        mBoughtIntelsAdapter = new NewMyArticlesAdapter(this, boughtIntelEntities);
        mSearchResultAdapter = new NewMyArticlesAdapter(this, searchResultEntities);
        queryMyIntels(false);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("我的情报");
        mLVIntelligences.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLVIntelligences.setAdapter(mAllIntelsAdapter);
    }

    public void queryMyIntels(boolean isLoadMorePage) {
        Parameter param = new Parameter();
        param.addParameter(QueryMyIntelsFunction.IN_SEARCH_KEY, searchKey);

        String cacheKey = "";
        if (INTEL_MY_BOUGHT == currentIntelType) {
            if (boughtIntelEntities.size() > 0 && !isLoadMorePage) {
                return;
            }
            if (isLoadMorePage) {
                currentBoughtPage += 1;
            }
            cacheKey = CacheKey.KEY_MY_BOUGHT_INTELS;
            param.addParameter("idx", "1");
            param.addParameter("type", "C");
            param.addParameter(ArticleFunction.IN_PAGE, String.valueOf(currentBoughtPage));
        } else if (INTEL_ALL == currentIntelType) {
            if (allIntelEntities.size() > 0 && !isLoadMorePage) {
                return;
            }
            if (isLoadMorePage) {
                currentAllPage += 1;
            }
            cacheKey = CacheKey.KEY_MY_PUBLISHED_INTELS;
            param.addParameter("idx", "0");
            param.addParameter("type", "");
            param.addParameter(ArticleFunction.IN_PAGE, String.valueOf(currentAllPage));
        } else {
            if (searchResultEntities.size() > 0 && !isLoadMorePage) {
                return;
            }
            if (isLoadMorePage) {
                currentSearchPage += 1;
            }
            cacheKey = CacheKey.KEY_INTEL_SEARCH_RESULT;
            param.addParameter(ArticleFunction.IN_PAGE, String.valueOf(currentSearchPage));
            searchResultEntities.clear();
        }
        int taskId = TaskId.TASK_ID_FIRST;
        startTask(new QueryMyIntelsRequest(taskId, cacheKey, param, new QueryAction(this)));
        mLoadingDialog.show();
    }

    public void switchAdapter() {
        switch (currentIntelType) {
            case INTEL_ALL:
                mLVIntelligences.setAdapter(mAllIntelsAdapter);
                mAllIntelsAdapter.notifyDataSetChanged();

                break;
            case INTEL_MY_BOUGHT:
                mLVIntelligences.setAdapter(mBoughtIntelsAdapter);
                mBoughtIntelsAdapter.notifyDataSetChanged();

                break;
            case INTEL_SEARCH:
                mLVIntelligences.setAdapter(mSearchResultAdapter);
                mSearchResultAdapter.notifyDataSetChanged();

                break;
        }
    }

    private void dismissLoading() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLVIntelligences.onRefreshComplete();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissLoading();
        final ArrayList<IntelligenceEntity> intels = (ArrayList<IntelligenceEntity>)
                mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
        if (null == intels || intels.size() < 1) {
            Toast.makeText(this, "您的情报已全部加载", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                onDataReceived(intels);

                break;
            case TaskId.TASK_ID_SECOND:

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissLoading();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissLoading();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissLoading();
    }

    private void onDataReceived(List<IntelligenceEntity> intels) {
        switch (currentIntelType) {
            case INTEL_ALL:
                allIntelEntities.addAll(intels);
                mAllIntelsAdapter.notifyDataSetChanged();

                break;
            case INTEL_MY_BOUGHT:
                boughtIntelEntities.addAll(intels);
                mBoughtIntelsAdapter.notifyDataSetChanged();

                break;
            case INTEL_SEARCH:
                searchResultEntities.addAll(intels);
                mSearchResultAdapter.notifyDataSetChanged();

                break;
        }
        intels.clear();
    }
}

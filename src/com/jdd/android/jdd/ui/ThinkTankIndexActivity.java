package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.ExperiencesIndexAdapter;
import com.jdd.android.jdd.adapters.IndexLatestArticlesAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryExperiencesRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IModule;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：智库首页
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class ThinkTankIndexActivity extends TKActivity implements IModule, IQueryServer {
    public static final String KEY_ARTICLE_CATEGORY = "CATEGORY";
    public static final String CATEGORY_SKILL_STUDY = "专业技能学习";
    public static final String CATEGORY_EXPERIENCES = "创富感悟";
    public static final short LOADING_TYPE_REFRESH = 1;
    public static final short LOADING_TYPE_MORE = 2;

    public short loadingType = LOADING_TYPE_REFRESH;
    public int currentPage = 1;
    public List<ArticleEntity> articleEntities;

    public EditText etExperienceSearchKey;
    public Button btnLatestPublished, btnLatestSelected;

    private PullToRefreshListView mLVExperiences;
    private Button mBtnFilter;
    private ImageButton mIBtnSearch;
    private Dialog mLoadingDialog;

    private ThinkTankController mController;
    private IndexLatestArticlesAdapter mAdapter;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private String mStrSearchKey = "";
    private String mStrIndustry = "";
    private String mStrType = "";
    private String mStrOrderName = "timeOrder";
    private String mStrOrderValue = "down";
    private CategoryReceiver mReceiver = new CategoryReceiver();
    private boolean mIsFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_think_tank_index);

        findViews();
        setListeners();
        initData();
        initViews();
        IntentFilter filter = new IntentFilter(KEY_ARTICLE_CATEGORY);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void findViews() {
        etExperienceSearchKey = (EditText) findViewById(R.id.et_key_for_search_experiences);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mBtnFilter = (Button) findViewById(R.id.btn_filter);
        btnLatestPublished = (Button) findViewById(R.id.btn_latest_published);
        btnLatestSelected = (Button) findViewById(R.id.btn_latest_selected);
        mLVExperiences = (PullToRefreshListView) findViewById(R.id.lv_experiences);
        mLoadingDialog = PopUpComponentUtil.createLoadingProgressDialog(this, "加载智文中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new ThinkTankController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnFilter, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnLatestPublished, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnLatestSelected, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVExperiences, mController);
        registerListener(ThinkTankController.ON_ITEM_CLICK, mLVExperiences, mController);
    }

    @Override
    protected void initData() {
        articleEntities = new ArrayList<>();
        mAdapter = new IndexLatestArticlesAdapter(this, articleEntities);
        //为了解决首页跳转跟第一次加载数据多线程请求导致数据被清空问题、
        //在onCreate的时候进行加载数据
        queryArticles();
    }

    @Override
    protected void initViews() {
        //设置刷新模式为支持上拉和下来两种模式
        mLVExperiences.setMode(PullToRefreshBase.Mode.BOTH);
        mLVExperiences.setAdapter(mAdapter);
    }

    public void queryArticles() {
        Parameter proExParam = new Parameter();
        proExParam.addParameter(QueryExperiencesFunction.IN_SEARCH_KEY, mStrSearchKey);
        proExParam.addParameter(QueryExperiencesFunction.IN_INDUSTRY, mStrIndustry);
        proExParam.addParameter(QueryExperiencesFunction.IN_TYPE_, mStrType);
        proExParam.addParameter(QueryExperiencesFunction.IN_ORDER_NAME, mStrOrderName);
        proExParam.addParameter(QueryExperiencesFunction.IN_ORDER_VALUE, mStrOrderValue);
        proExParam.addParameter(QueryExperiencesFunction.IN_PAGE, String.valueOf(currentPage));

        startTask(new QueryExperiencesRequest(
                TaskId.TASK_ID_FIRST, CacheKey.KEY_EXPERIENCES_FOR_EX_INDEX,
                proExParam, new QueryAction(this)));
        if (!mIsFirstLoad) {
            mLoadingDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (articleEntities.size() < 1 && mIsFirstLoad) {
            queryArticles();
            mIsFirstLoad = false;
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean init(IAppContext iAppContext) {
        return false;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onMessage(AppMsg appMsg) {
        if (null == appMsg) {
            return;
        }
        mStrType = appMsg.getMessage().optString(KEY_ARTICLE_CATEGORY);
        queryArticles();
    }

    @Override
    public String onCallMessage(AppMsg appMsg) {
        return null;
    }

    @Override
    public void onUnload() {

    }

    private void dismissLoading() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public synchronized void onQuerySuccess(int taskId, Bundle bundle) {
        dismissLoading();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final ArrayList<ExperienceEntity> articles = (ArrayList<ExperienceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (1 == currentPage || loadingType == LOADING_TYPE_REFRESH) {
//                    Toast.makeText(this, "文章已是最新...", Toast.LENGTH_SHORT).show();
                    articleEntities.clear();
                    mAdapter.notifyDataSetChanged();
                }
                if (null == articles || articles.size() < 1) {
                    Toast.makeText(this, "已加载全部数据...", Toast.LENGTH_SHORT).show();
                    mLVExperiences.onRefreshComplete();
                    return;
                }
                articleEntities.addAll(articles);
                articles.clear();
                mAdapter.notifyDataSetChanged();
                mLVExperiences.onRefreshComplete();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissLoading();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                mLVExperiences.onRefreshComplete();
                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissLoading();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                mLVExperiences.onRefreshComplete();
                break;
        }
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissLoading();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                mLVExperiences.onRefreshComplete();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (1 == requestCode && 1 == resultCode && null != data) {
            mStrIndustry = data.getStringExtra(ExperienceFilterActivity.KEY_INDUSTRY_RESULT);
            mStrType = data.getStringExtra(ExperienceFilterActivity.KEY_CATEGORY_RESULT);

            queryArticles();
        }
    }

    public void setStrOrderName(String strOrderName) {
        mStrOrderName = strOrderName;
    }

    class CategoryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mStrType = intent.getStringExtra(KEY_ARTICLE_CATEGORY);
            queryArticles();
        }
    }
}

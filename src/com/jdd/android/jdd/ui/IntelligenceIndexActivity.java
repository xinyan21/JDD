package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.IndexLatestArticlesAdapter;
import com.jdd.android.jdd.adapters.IntelligencesIndexAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.controllers.IntelligenceIndexController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryIntelligencesRequest;
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
 * 描述：情报首页
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class IntelligenceIndexActivity extends TKActivity implements IModule, IQueryServer {
    public static final short LOADING_TYPE_REFRESH = 1;
    public static final short LOADING_TYPE_MORE = 2;

    public short loadingType = LOADING_TYPE_REFRESH;
    public int currentPage = 1;
    public List<ArticleEntity> intelligenceEntities;

    public EditText etSearchKey;
    public Button btnGoodComments, btnTime;
    public CheckBox cbOnlyCharges;

    private Button mBtnFilter;
    private ImageButton mIBtnSearch;
    private PullToRefreshListView mLVIntelligences;
    private TextView mTVOnlyCharges;
    private Dialog mLoadingDialog;

    private IntelligenceIndexController mController;
    private IndexLatestArticlesAdapter mIntelligencesIndexAdapter;

    private MemberCache mCache = DataCache.getInstance().getCache();
    //查询参数值
    private String mStrSearchKey = "";
    private String mStrIndustry = "";
    private String mStrType = "";
    //timeOrder : 时间排序,hotOrder:人气，topOrder：好评
    private String mStrOrderName = QueryIntelligencesFunction.ORDER_NAME_TOP;
    private String mStrOrderValue = QueryIntelligencesFunction.ORDER_VALUE_DOWN;
    private String mStrTheme = "";
    private String mStrStyle = "";
    private String mStrArea = "";
    private String mStrUserId = "";
    private String mStrDurations = "";
    private String mStrLongShortPositions = "";
    private String mStrFractions = "";
    private String mStrNeedPayOnly = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligence_index);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        etSearchKey = (EditText) findViewById(R.id.et_search_key);
        btnGoodComments = (Button) findViewById(R.id.btn_sort_by_good_comment);
        btnTime = (Button) findViewById(R.id.btn_sort_by_time);
        cbOnlyCharges = (CheckBox) findViewById(R.id.cb_only_charges);
        mBtnFilter = (Button) findViewById(R.id.btn_filter);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mLVIntelligences = (PullToRefreshListView) findViewById(R.id.lv_intelligences);
        mTVOnlyCharges = (TextView) findViewById(R.id.tv_only_charges);
        mLoadingDialog = PopUpComponentUtil.createLoadingProgressDialog(this, "加载情报中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new IntelligenceIndexController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnGoodComments, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnTime, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnFilter, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mTVOnlyCharges, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVIntelligences, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVIntelligences, mController);
    }

    @Override
    protected void initData() {
        intelligenceEntities = new ArrayList<>();
        mIntelligencesIndexAdapter = new IndexLatestArticlesAdapter(this, intelligenceEntities);
    }

    @Override
    protected void initViews() {
        //设置刷新模式为支持上拉和下来两种模式
        mLVIntelligences.setMode(PullToRefreshBase.Mode.BOTH);
        mLVIntelligences.setAdapter(mIntelligencesIndexAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (intelligenceEntities.size() < 1) {
            queryArticles();
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public void queryArticles() {
        if (cbOnlyCharges.isChecked()) {
            mStrNeedPayOnly = "C";
        } else {
            mStrNeedPayOnly = "";
        }
        Parameter param = new Parameter();
        param.addParameter(QueryIntelligencesFunction.IN_ORDER_NAME, mStrOrderName);
        param.addParameter(QueryIntelligencesFunction.IN_AREA, mStrArea);
        param.addParameter(QueryIntelligencesFunction.IN_INDUSTRY, mStrIndustry);
        param.addParameter(QueryIntelligencesFunction.IN_ORDER_VALUE, mStrOrderValue);
        param.addParameter(QueryIntelligencesFunction.IN_SEARCH_KEY, mStrSearchKey);
        param.addParameter(QueryIntelligencesFunction.IN_PAGE, String.valueOf(currentPage));
        param.addParameter(QueryIntelligencesFunction.IN_STYLE, mStrStyle);
        param.addParameter(QueryIntelligencesFunction.IN_THEME, mStrTheme);
        param.addParameter(QueryIntelligencesFunction.IN_TYPE_, mStrType);
        param.addParameter(QueryIntelligencesFunction.IN_USER_ID, mStrUserId);
        param.addParameter(QueryIntelligencesFunction.IN_NEED_PAY, mStrNeedPayOnly);
        param.addParameter(QueryIntelligencesFunction.IN_DURATION, mStrDurations);

        startTask(new QueryIntelligencesRequest(
                TaskId.TASK_ID_FIRST, CacheKey.KEY_INTELLIGENCES_FOR_EX_INDEX,
                param, new QueryAction(this))
        );
        mLoadingDialog.show();
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

    }

    @Override
    public String onCallMessage(AppMsg appMsg) {
        return null;
    }

    @Override
    public void onUnload() {

    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final ArrayList<IntelligenceEntity> articles = (ArrayList<IntelligenceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (1 == currentPage || loadingType == LOADING_TYPE_REFRESH) {
//                    Toast.makeText(this, "文章已是最新...", Toast.LENGTH_SHORT).show();
                    intelligenceEntities.clear();
                }
                if (null == articles || articles.size() < 1) {
                    Toast.makeText(this, "已加载全部数据...", Toast.LENGTH_SHORT).show();
                    mLVIntelligences.onRefreshComplete();
                    return;
                }
                intelligenceEntities.addAll(articles);
                articles.clear();
                mIntelligencesIndexAdapter.notifyDataSetChanged();
                mLVIntelligences.onRefreshComplete();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (1 == requestCode && 1 == resultCode && null != data) {
            mStrArea = data.getStringExtra(IntelligenceFilterActivity.KEY_RESULT_AREAS);
            mStrIndustry = data.getStringExtra(IntelligenceFilterActivity.KEY_RESULT_INDUSTRIES);
            mStrStyle = data.getStringExtra(IntelligenceFilterActivity.KEY_RESULT_INVEST_STYLES);
            mStrTheme = data.getStringExtra(IntelligenceFilterActivity.KEY_RESULT_THEMES);
            mStrDurations = data.getStringExtra(IntelligenceFilterActivity.KEY_RESULT_DURATIONS);
            mStrFractions = data.getStringExtra(IntelligenceFilterActivity.KEY_RESULT_FRACTIONS);
            mStrTheme = data.getStringExtra(IntelligenceFilterActivity.KEY_RESULT_THEMES);
            queryArticles();
        }
    }

    public void setStrOrderName(String strOrderName) {
        mStrOrderName = strOrderName;
    }

    public void setStrOrderValue(String strOrderValue) {
        mStrOrderValue = strOrderValue;
    }
}

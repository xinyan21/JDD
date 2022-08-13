package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.IndexLatestArticlesAdapter;
import com.jdd.android.jdd.adapters.StockSearchResultAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.controllers.SearchController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryExperiencesRequest;
import com.jdd.android.jdd.requests.QueryIntelligencesRequest;
import com.jdd.android.jdd.requests.StockSearchRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：搜索
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-05
 * @last_edit 2016-01-05
 * @since 1.0
 */
public class SearchActivity extends TKActivity implements IQueryServer {
    public final static String KEY_SEARCH_TYPE = "SEARCH_TYPE";
    public final static String KEY_SEARCH_KEY = "SEARCH_KEY";
    public final static int CURRENT_SEARCH_TYPE_PRICE = 1;
    public final static int CURRENT_SEARCH_TYPE_INTEL = 2;
    public final static int CURRENT_SEARCH_TYPE_EXPERIENCE = 3;
    public final static int CURRENT_SEARCH_TYPE_COURSE = 4;

    //默认当前搜索为行情
    public int currentSearchType = CURRENT_SEARCH_TYPE_PRICE;
    public int currentIntelPage = 1, currentExpPage = 1, currentCoursePage = 1;
    public List<StockEntity> searchStockEntities;
    public List<ArticleEntity> intelligenceEntities;
    public List<ArticleEntity> experienceEntities;

    public EditText etSearchKey;
    public Button btnPrice, btnIntelligences, btnExperiences, btnCourses;
    public View boldLinePrice, boldLineIntels, boldLineExperiences, boldLineCourses;

    private ImageButton mIBtnBack, mIBtnSearch;
    private PullToRefreshListView mLVSearchResults;
    private Dialog mDialogLoading;

    private SearchController mController;
    private long mLastTextChangedTime = 0;
    private Handler mHandler;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private StockSearchResultAdapter mStockSearchResultAdapter;
    private IndexLatestArticlesAdapter mIntelligencesIndexAdapter;
    private IndexLatestArticlesAdapter mExperiencesIndexAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        etSearchKey = (EditText) findViewById(R.id.et_search_key);
        boldLineCourses = findViewById(R.id.v_bold_line_courses);
        boldLineExperiences = findViewById(R.id.v_bold_line_experiences);
        boldLineIntels = findViewById(R.id.v_bold_line_intelligences);
        boldLinePrice = findViewById(R.id.v_bold_line_price);
        btnCourses = (Button) findViewById(R.id.btn_courses);
        btnExperiences = (Button) findViewById(R.id.btn_experiences_study);
        btnIntelligences = (Button) findViewById(R.id.btn_intelligences);
        btnPrice = (Button) findViewById(R.id.btn_sort_by_price);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mLVSearchResults = (PullToRefreshListView) findViewById(R.id.lv_search_results);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(this, "加载中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new SearchController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCourses, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnExperiences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnIntelligences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnPrice, mController);
        registerListener(SearchController.ON_TEXT_CHANGE, etSearchKey, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVSearchResults, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVSearchResults, mController);

        etSearchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        searchStockEntities = new ArrayList<>();
        intelligenceEntities = new ArrayList<>();
        experienceEntities = new ArrayList<>();
        mStockSearchResultAdapter = new StockSearchResultAdapter(this, searchStockEntities);
        mIntelligencesIndexAdapter = new IndexLatestArticlesAdapter(this, intelligenceEntities);
        mExperiencesIndexAdapter = new IndexLatestArticlesAdapter(this, experienceEntities);
    }

    @Override
    protected void initViews() {
        //设置只支持上拉加载更多
        mLVSearchResults.setMode(PullToRefreshBase.Mode.DISABLED);
        currentSearchType = getIntent().getIntExtra(KEY_SEARCH_TYPE, CURRENT_SEARCH_TYPE_PRICE);
        etSearchKey.setText(getIntent().getStringExtra(KEY_SEARCH_KEY));
        switch (currentSearchType) {
            case CURRENT_SEARCH_TYPE_COURSE:

                break;
            case CURRENT_SEARCH_TYPE_EXPERIENCE:
                mLVSearchResults.setAdapter(mExperiencesIndexAdapter);
                btnExperiences.performClick();

                break;
            case CURRENT_SEARCH_TYPE_INTEL:
                mLVSearchResults.setAdapter(mIntelligencesIndexAdapter);
                btnIntelligences.performClick();

                break;
            case CURRENT_SEARCH_TYPE_PRICE:
                mLVSearchResults.setAdapter(mStockSearchResultAdapter);
                btnPrice.performClick();

                break;
        }
        //如果传进来key，那么1秒后自动查询
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (etSearchKey.getText().length() > 0) {
                    mIBtnSearch.performClick();
                }
            }
        }, 1000);
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(etSearchKey, InputMethodManager.SHOW_FORCED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CURRENT_SEARCH_TYPE_PRICE == currentSearchType) {
            //需要更新的原因是用户点击股票进去可能操作自选状态
            mStockSearchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final List<StockEntity> searchStockEntities = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == searchStockEntities) {
                    return;
                }
                this.searchStockEntities.clear();
                this.searchStockEntities.addAll(searchStockEntities);
                searchStockEntities.clear();
                mStockSearchResultAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_SECOND:
                final ArrayList<ExperienceEntity> experiences = (ArrayList<ExperienceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == experiences || experiences.size() < 1) {
                    if (1 != currentExpPage) {
                        Toast.makeText(this, "没有更多智文啦", Toast.LENGTH_SHORT).show();
                    } else {
                        experienceEntities.clear();
                        mExperiencesIndexAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "没有搜索到相关智文", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (1 == currentExpPage) {
                    experienceEntities.clear();
                }
                experienceEntities.addAll(experiences);
                experiences.clear();
                mExperiencesIndexAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_THIRD:
                final ArrayList<IntelligenceEntity> intels = (ArrayList<IntelligenceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == intels || intels.size() < 1) {
                    if (1 != currentIntelPage) {
                        Toast.makeText(this, "没有更多情报啦", Toast.LENGTH_SHORT).show();
                    } else {
                        intelligenceEntities.clear();
                        mIntelligencesIndexAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "没有搜索到相关情报", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (1 == currentIntelPage) {
                    intelligenceEntities.clear();
                }
                intelligenceEntities.addAll(intels);
                intels.clear();
                mIntelligencesIndexAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
    }

    public void setSelectedState(int searchType) {
        this.currentSearchType = searchType;
        switch (currentSearchType) {
            case CURRENT_SEARCH_TYPE_COURSE:

                break;
            case CURRENT_SEARCH_TYPE_EXPERIENCE:
                mLVSearchResults.setAdapter(mExperiencesIndexAdapter);
                mExperiencesIndexAdapter.notifyDataSetChanged();

                break;
            case CURRENT_SEARCH_TYPE_INTEL:
                mLVSearchResults.setAdapter(mIntelligencesIndexAdapter);
                mIntelligencesIndexAdapter.notifyDataSetChanged();

                break;
            case CURRENT_SEARCH_TYPE_PRICE:
                mLVSearchResults.setAdapter(mStockSearchResultAdapter);
                mStockSearchResultAdapter.notifyDataSetChanged();

                break;
        }
    }

    public void doSearch() {
        String key = etSearchKey.getText().toString();
        if (StringUtils.isEmpty(key)) {
            return;
        }
        Parameter parameter = new Parameter();
        switch (currentSearchType) {
            case CURRENT_SEARCH_TYPE_COURSE:

                break;
            case CURRENT_SEARCH_TYPE_EXPERIENCE:
                parameter.addParameter(QueryExperiencesFunction.IN_SEARCH_KEY, key);
                parameter.addParameter(QueryExperiencesFunction.IN_INDUSTRY, "");
                parameter.addParameter(QueryExperiencesFunction.IN_TYPE_, "");
                parameter.addParameter(
                        QueryExperiencesFunction.IN_ORDER_NAME,
                        QueryExperiencesFunction.ORDER_NAME_TIME
                );
                parameter.addParameter(QueryExperiencesFunction.IN_ORDER_VALUE, "down");
                parameter.addParameter(
                        QueryExperiencesFunction.IN_PAGE, String.valueOf(currentExpPage)
                );

                startTask(new QueryExperiencesRequest(
                        TaskId.TASK_ID_SECOND, CacheKey.KEY_EXPERIENCES_FOR_EX_INDEX,
                        parameter, new QueryAction(this)));

                break;
            case CURRENT_SEARCH_TYPE_INTEL:
                parameter.addParameter(
                        QueryIntelligencesFunction.IN_ORDER_NAME,
                        QueryIntelligencesFunction.ORDER_NAME_TIME
                );
                parameter.addParameter(QueryIntelligencesFunction.IN_AREA, "");
                parameter.addParameter(QueryIntelligencesFunction.IN_INDUSTRY, "");
                parameter.addParameter(QueryIntelligencesFunction.IN_ORDER_VALUE, "down");
                parameter.addParameter(QueryIntelligencesFunction.IN_SEARCH_KEY, key);
                parameter.addParameter(
                        QueryIntelligencesFunction.IN_PAGE, String.valueOf(currentIntelPage)
                );
                parameter.addParameter(QueryIntelligencesFunction.IN_STYLE, "");
                parameter.addParameter(QueryIntelligencesFunction.IN_THEME, "");
                parameter.addParameter(QueryIntelligencesFunction.IN_TYPE_, "");
                parameter.addParameter(QueryIntelligencesFunction.IN_USER_ID, "");

                startTask(new QueryIntelligencesRequest(
                        TaskId.TASK_ID_THIRD, CacheKey.KEY_INTELLIGENCES_FOR_EX_INDEX,
                        parameter, new QueryAction(this))
                );

                break;
            case CURRENT_SEARCH_TYPE_PRICE:
                parameter.addParameter("search", key);
                parameter.addParameter("taskId", String.valueOf(TaskId.TASK_ID_FIRST));
                startTask(new StockSearchRequest(parameter, new QueryAction(this)));

                break;
        }
        //加载框会夺取焦点导致输入法关闭
//        mDialogLoading.show();
    }

    private void dismissDialog() {
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
    }
}

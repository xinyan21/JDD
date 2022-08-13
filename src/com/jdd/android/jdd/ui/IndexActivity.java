package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.IndexLatestArticlesAdapter;
import com.jdd.android.jdd.adapters.IndexLatestCoursesAdapter;
import com.jdd.android.jdd.adapters.SelfSelectionStocksAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.ProjectConfigConstant;
import com.jdd.android.jdd.constants.StockMarket;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.controllers.IndexController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryExperiencesRequest;
import com.jdd.android.jdd.requests.QueryIntelligencesRequest;
import com.jdd.android.jdd.requests.QueryLongShortCoursesRequest;
import com.jdd.android.jdd.requests.QueryStockListDetailsRequest;
import com.jdd.android.jdd.utils.StockUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.constants.msg.EngineMsgList;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IAppControl;
import com.thinkive.android.app_engine.interfaces.IID;
import com.thinkive.android.app_engine.interfaces.IModule;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：首页
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class IndexActivity extends TKActivity implements IModule, IQueryServer {
    public IAppControl appControl;
    public ListView lvLatestIntelligence;
    public ListView lvLatestWealthyThought;
    public ListView lvLatestProfessionExperience;
    public ListView lvIndexPrices;
    public ListView lvLatestLongShortBattles;
    public ListView lvLatestLongShortArtOfWar;
    public ListView lvLatestTalks;
    //数据
    public List<ArticleEntity> latestIntelsEntities, latestWealthyThoughtsEntities;
    public List<ArticleEntity> latestProfessionExperiencesEntities;
    public List<StockEntity> indexEntities;
    public List<CourseEntity> latestBattleEntities, latestArtOfWarEntities, latestTAlkEntities;

    private Button mBtnLogin;
    private ImageButton mIBtnSearch;
    private LinearLayout mLLLongShortCourseEntry;
    private ViewFlipper mVFAds;
    private ImageView mIVAdOne, mIVAdTwo, mIVAdThree;

    private ImageView mIVPageIndicatorOne, mIVPageIndicatorTwo, mIVPageIndicatorThree;
    private Button mBtnMoreIntelligences, mBtnMoreWealthyThoughts, mBtnMoreProfessionExperiences;
    private Button mBtnMoreArtOfWar,mBtnMoreBattles,mBtnMoreTalks;
    private IndexController mController;
    //适配器
    private IndexLatestArticlesAdapter mLatestIntelsAdapter;
    private IndexLatestArticlesAdapter mLatestProfessionExperiencesAdapter;
    private IndexLatestArticlesAdapter mLatestWealthyThoughtsAdapter;
    private IndexLatestCoursesAdapter mLatestArtOfWarAdapter;
    private IndexLatestCoursesAdapter mLatestBattlesAdapters;
    private IndexLatestCoursesAdapter mLatestTalksAdapter;
    private SelfSelectionStocksAdapter mPriceAdapter;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mController = new IndexController(this);
        mTimer = new Timer();

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        lvLatestIntelligence = (ListView) findViewById(R.id.lv_latest_intels);
        lvLatestProfessionExperience = (ListView) findViewById(R.id.lv_latest_profession_experiences);
        lvLatestWealthyThought = (ListView) findViewById(R.id.lv_latest_wealthy_thoughts);
        lvIndexPrices = (ListView) findViewById(R.id.lv_stock_index_list);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mLLLongShortCourseEntry = (LinearLayout) findViewById(R.id.ll_long_short_course_entry);
        mVFAds = (ViewFlipper) findViewById(R.id.vf_ads);
        mIVAdOne = (ImageView) findViewById(R.id.iv_ad_one);
        mIVAdTwo = (ImageView) findViewById(R.id.iv_ad_two);
        mIVAdThree = (ImageView) findViewById(R.id.iv_ad_three);
        mIVPageIndicatorOne = (ImageView) findViewById(R.id.iv_page_indicator_one);
        mIVPageIndicatorTwo = (ImageView) findViewById(R.id.iv_page_indicator_two);
        mIVPageIndicatorThree = (ImageView) findViewById(R.id.iv_page_indicator_three);
        mBtnMoreIntelligences = (Button) findViewById(R.id.btn_more_intelligences);
        mBtnMoreProfessionExperiences = (Button) findViewById(R.id.btn_more_profession_experiences);
        mBtnMoreWealthyThoughts = (Button) findViewById(R.id.btn_more_wealthy_thoughts);
        lvLatestLongShortArtOfWar = (ListView) findViewById(R.id.lv_long_short_art_of_war);
        lvLatestLongShortBattles = (ListView) findViewById(R.id.lv_latest_long_short_battles);
        lvLatestTalks = (ListView) findViewById(R.id.lv_latest_talks);
        mBtnMoreArtOfWar = (Button) findViewById(R.id.btn_more_long_short_art_of_war);
        mBtnMoreBattles = (Button) findViewById(R.id.btn_more_long_short_battles);
        mBtnMoreTalks = (Button) findViewById(R.id.btn_more_talks);
    }

    @Override
    protected void setListeners() {
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnLogin, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreIntelligences, mController);
        registerListener(
                ListenerControllerAdapter.ON_CLICK, mBtnMoreProfessionExperiences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreWealthyThoughts, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLLongShortCourseEntry, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreArtOfWar, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreBattles, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreTalks, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIVAdOne, mController);
        registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, lvLatestProfessionExperience, mController);
        registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, lvLatestWealthyThought, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, lvIndexPrices, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, lvLatestIntelligence, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, lvLatestTalks, mController);
        registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, lvLatestLongShortBattles, mController);
        registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, lvLatestLongShortArtOfWar, mController);
    }

    @Override
    protected void initData() {
        indexEntities = generateDefaultIndex();
        mPriceAdapter = new SelfSelectionStocksAdapter(this, indexEntities);
        latestProfessionExperiencesEntities = new ArrayList<>();
        mLatestProfessionExperiencesAdapter
                = new IndexLatestArticlesAdapter(this, latestProfessionExperiencesEntities);
        latestWealthyThoughtsEntities = new ArrayList<>();
        mLatestWealthyThoughtsAdapter
                = new IndexLatestArticlesAdapter(this, latestWealthyThoughtsEntities);
        latestIntelsEntities = new ArrayList<>();
        mLatestIntelsAdapter = new IndexLatestArticlesAdapter(this, latestIntelsEntities);

        latestArtOfWarEntities = new ArrayList<>();
        latestBattleEntities = new ArrayList<>();
        latestTAlkEntities = new ArrayList<>();
        mLatestBattlesAdapters = new IndexLatestCoursesAdapter(this, latestBattleEntities);
        mLatestArtOfWarAdapter = new IndexLatestCoursesAdapter(this, latestArtOfWarEntities);
        mLatestTalksAdapter = new IndexLatestCoursesAdapter(this, latestTAlkEntities);
    }

    @Override
    protected void initViews() {
        lvIndexPrices.setAdapter(mPriceAdapter);
        lvLatestProfessionExperience.setAdapter(mLatestProfessionExperiencesAdapter);
        lvLatestIntelligence.setAdapter(mLatestIntelsAdapter);
        lvLatestWealthyThought.setAdapter(mLatestWealthyThoughtsAdapter);
        lvLatestLongShortBattles.setAdapter(mLatestBattlesAdapters);
        lvLatestLongShortArtOfWar.setAdapter(mLatestArtOfWarAdapter);
        lvLatestTalks.setAdapter(mLatestTalksAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        queryArticles();
        queryCourses();
        if (StockUtil.isTradingTime()) {
            if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    //请求最新数据
                    startTask(new QueryStockListDetailsRequest(
                            indexEntities, TaskId.TASK_ID_FIRST, new QueryAction(IndexActivity.this)));
                }
            };
            mTimer.schedule(mTimerTask, 0, ProjectConfigConstant.REFRESH_PERIOD);
        } else {
            //请求最新数据
            startTask(new QueryStockListDetailsRequest(
                    indexEntities,
                    TaskId.TASK_ID_FIRST,
                    CacheKey.KEY_STOCK_INDEX_LIST,
                    new QueryAction(IndexActivity.this)));
        }
        //如果会员已经过期，跳转到我界面
        if ("true".equals(mCache.getStringCacheItem(CacheKey.KEY_IS_VIP_EXPIRED))) {
            appControl.sendMessage(
                    new AppMsg("mine", String.valueOf(EngineMsgList.OPEN_MODULE), null));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (null != mTimerTask) {
            mTimerTask.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    public boolean init(IAppContext iAppContext) {
        if (null != iAppContext) {
            appControl = (IAppControl) iAppContext.queryInterface(this, IID.IID_IAppControl);
        }
        return true;
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
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final List<StockEntity> latestDataStockEntities = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == latestDataStockEntities || latestDataStockEntities.size() < 1) {
                    return;
                }
                indexEntities.clear();
                indexEntities.addAll(latestDataStockEntities);
                latestDataStockEntities.clear();
                mPriceAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_SECOND:
                final List<CourseEntity> artOfWarEntities = (List<CourseEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == artOfWarEntities || artOfWarEntities.size() < 1) {
                    return;
                }
                latestArtOfWarEntities.clear();
                latestArtOfWarEntities.addAll(artOfWarEntities);
                mLatestArtOfWarAdapter.notifyDataSetChanged();
                artOfWarEntities.clear();

                break;
            case TaskId.TASK_ID_THIRD:
                final List<CourseEntity> battleEntities = (List<CourseEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == battleEntities || battleEntities.size() < 1) {
                    return;
                }
                latestBattleEntities.clear();
                latestBattleEntities.addAll(battleEntities);
                mLatestBattlesAdapters.notifyDataSetChanged();
                battleEntities.clear();

                break;
            case TaskId.TASK_ID_FOURTH:
                final List<CourseEntity> talkEntities = (List<CourseEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == talkEntities || talkEntities.size() < 1) {
                    return;
                }
                latestTAlkEntities.clear();
                latestTAlkEntities.addAll(talkEntities);
                mLatestTalksAdapter.notifyDataSetChanged();
                talkEntities.clear();

                break;
//            case TaskId.TASK_ID_SECOND:
//                final List<ArticleEntity> latestProExEntities = (List<ArticleEntity>)
//                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
//                if (null == latestProExEntities || latestProExEntities.size() < 1) {
//                    return;
//                }
//                latestProfessionExperiencesEntities.clear();
//                latestProfessionExperiencesEntities.addAll(latestProExEntities);
//                latestProExEntities.clear();
//                mLatestProfessionExperiencesAdapter.notifyDataSetChanged();
//
//                break;
//            case TaskId.TASK_ID_THIRD:
//                final List<ArticleEntity> latestWealThoughtEntities = (List<ArticleEntity>)
//                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
//                if (null == latestWealThoughtEntities || latestWealThoughtEntities.size() < 1) {
//                    return;
//                }
//                this.latestWealthyThoughtsEntities.clear();
//                this.latestWealthyThoughtsEntities.addAll(latestWealThoughtEntities);
//                latestWealThoughtEntities.clear();
//                mLatestWealthyThoughtsAdapter.notifyDataSetChanged();
//
//                break;
//            case TaskId.TASK_ID_FOURTH:
//                final List<ArticleEntity> latestIntelEntities = (List<ArticleEntity>)
//                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
//                if (null == latestIntelEntities || latestIntelEntities.size() < 1) {
//                    return;
//                }
//                this.latestIntelsEntities.clear();
//                this.latestIntelsEntities.addAll(latestIntelEntities);
//                latestIntelEntities.clear();
//                mLatestIntelsAdapter.notifyDataSetChanged();
//
//                break;
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

    private void queryCourses() {
        String courseCategory = "多空兵法";
        Parameter param = new Parameter();
        param.addParameter(QueryCoursesFunction.IN_CATEGORY, getCourseType(courseCategory));
        param.addParameter(QueryCoursesFunction.IN_ORDER_VALUE, "time-down");
        param.addParameter(QueryCoursesFunction.IN_PAGE, String.valueOf(1));

        startTask(new QueryLongShortCoursesRequest(
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        ));
        courseCategory = "多空实战";
        param = new Parameter();
        param.addParameter(QueryCoursesFunction.IN_CATEGORY, getCourseType(courseCategory));
        param.addParameter(QueryCoursesFunction.IN_ORDER_VALUE, "time-up");
        param.addParameter(QueryCoursesFunction.IN_PAGE, String.valueOf(1));

        startTask(new QueryLongShortCoursesRequest(
                TaskId.TASK_ID_THIRD, param, new QueryAction(this)
        ));
        courseCategory = "道德经杂谈";
        param = new Parameter();
        param.addParameter(QueryCoursesFunction.IN_CATEGORY, getCourseType(courseCategory));
        param.addParameter(QueryCoursesFunction.IN_ORDER_VALUE, "time-up");
        param.addParameter(QueryCoursesFunction.IN_PAGE, String.valueOf(1));

        startTask(new QueryLongShortCoursesRequest(
                TaskId.TASK_ID_FOURTH, param, new QueryAction(this)
        ));
    }

    private String getCourseType(String courseCategory) {
        if (courseCategory.equals("多空兵法")) {
            courseCategory = QueryCoursesFunction.CATEGORY_LONG_SHORT_BASIC;
        } else if (courseCategory.equals("道德经杂谈")) {
            courseCategory = QueryCoursesFunction.CATEGORY_VALUE_SPECULATION;
        } else if (courseCategory.equals("多空实战")) {
            courseCategory = QueryCoursesFunction.CATEGORY_LONG_SHORT_BATTLE;
        } else if (courseCategory.equals("实盘面授")) {
            courseCategory = QueryCoursesFunction.CATEGORY_BINDING_OFFER;
        }
        return courseCategory;
    }

    /**
     * 查询文章列表（智文、情报、创富感悟）
     */
    private void queryArticles() {
        //职业心得文章查询参数
        Parameter proExParam = new Parameter();
        proExParam.addParameter(QueryExperiencesFunction.IN_SEARCH_KEY, "");
        proExParam.addParameter(QueryExperiencesFunction.IN_INDUSTRY, "");
        proExParam.addParameter(QueryExperiencesFunction.IN_TYPE_, "专业技能学习");
        proExParam.addParameter(QueryExperiencesFunction.IN_ORDER_NAME, "timeOrder");
        proExParam.addParameter(QueryExperiencesFunction.IN_ORDER_VALUE, "down");
        proExParam.addParameter(QueryExperiencesFunction.IN_PAGE, "1");
        //创富感悟文章查询参数
        Parameter wealthyThoughtParam = new Parameter();
        wealthyThoughtParam.addParameter(QueryExperiencesFunction.IN_SEARCH_KEY, "");
        wealthyThoughtParam.addParameter(QueryExperiencesFunction.IN_INDUSTRY, "");
        wealthyThoughtParam.addParameter(QueryExperiencesFunction.IN_TYPE_, "创富感悟");
        wealthyThoughtParam.addParameter(QueryExperiencesFunction.IN_ORDER_NAME, "timeOrder");
        wealthyThoughtParam.addParameter(QueryExperiencesFunction.IN_ORDER_VALUE, "down");
        wealthyThoughtParam.addParameter(QueryExperiencesFunction.IN_PAGE, "1");
        //情报文章查询参数
        Parameter paramIntel = new Parameter();
        paramIntel.addParameter(QueryIntelligencesFunction.IN_ORDER_NAME, "timeOrder");
        paramIntel.addParameter(QueryIntelligencesFunction.IN_AREA, "");
        paramIntel.addParameter(QueryIntelligencesFunction.IN_INDUSTRY, "");
        paramIntel.addParameter(QueryIntelligencesFunction.IN_ORDER_VALUE, "down");
        paramIntel.addParameter(QueryIntelligencesFunction.IN_SEARCH_KEY, "");
        paramIntel.addParameter(QueryIntelligencesFunction.IN_PAGE, String.valueOf(1));
        paramIntel.addParameter(QueryIntelligencesFunction.IN_STYLE, "");
        paramIntel.addParameter(QueryIntelligencesFunction.IN_THEME, "");
        paramIntel.addParameter(QueryIntelligencesFunction.IN_TYPE_, "");

        startTask(new QueryExperiencesRequest(
                TaskId.TASK_ID_SECOND, CacheKey.KEY_EXPERIENCES_FOR_MAIN,
                proExParam, new QueryAction(this))
        );
        startTask(new QueryExperiencesRequest(
                TaskId.TASK_ID_THIRD, CacheKey.KEY_EXPERIENCES_FOR_MAIN,
                wealthyThoughtParam, new QueryAction(this))
        );
        startTask(new QueryIntelligencesRequest(
                TaskId.TASK_ID_FOURTH, CacheKey.KEY_INTELLIGENCES_FOR_MAIN,
                paramIntel, new QueryAction(this))
        );
    }

    private List<StockEntity> generateDefaultIndex() {
        List<StockEntity> stocks = new ArrayList<>();
        StockEntity entity = new StockEntity();
        entity.setMarket(StockMarket.SH);
        entity.setCode("000001");
        entity.setName("上证指数");
        stocks.add(entity);

        entity = new StockEntity();
        entity.setMarket(StockMarket.SZ);
        entity.setCode("399001");
        entity.setName("深证指数");
        stocks.add(entity);

        entity = new StockEntity();
        entity.setMarket(StockMarket.SZ);
        entity.setCode("399006");
        entity.setName("创业扳指");
        stocks.add(entity);

        return stocks;
    }
}

package com.jdd.android.jdd.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.IndexGridAdapter;
import com.jdd.android.jdd.adapters.IndexLatestArticlesAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.ProjectConfigConstant;
import com.jdd.android.jdd.constants.StockMarket;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.controllers.NewIndexController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryExperiencesRequest;
import com.jdd.android.jdd.requests.QueryIntelligencesRequest;
import com.jdd.android.jdd.requests.QueryStockListDetailsRequest;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.jdd.android.jdd.utils.StockUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
import com.thinkive.android.app_engine.utils.StringUtils;
import com.thinkive.android.app_engine.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-06-26
 * @since 1.0
 */
public class NewIndexActivity extends TKActivity implements IModule, IQueryServer {
    public IAppControl appControl;
    private Button mBtnStartMorality, mBtnExpsStudy, mBtnWealthyTank, mBtnPrice;
    private Button mBtnMoreCourses, mBtnMoreIntels, mBtnMoreExperiences;
    private GridView mGVIndex;
    private View mStockReports, mLongShortArt, mLongShortBattle;
    private View mSearchLayout;
    private ListView mLVLatestIntels, mLVLatestExps;
    private ImageView mIVHeadPortrait;
    private TextView mTVFakeInput;

    private NewIndexController mController;

    //数据
    public List<ArticleEntity> latestIntelsEntities;
    public List<ArticleEntity> latestProfessionExperiencesEntities;
    public List<StockEntity> indexEntities;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Timer mTimer;
    private TimerTask mTimerTask;

    //适配器
    private IndexLatestArticlesAdapter mLatestIntelsAdapter;
    private IndexLatestArticlesAdapter mLatestProfessionExperiencesAdapter;
    private IndexGridAdapter mIndexGridAdapter;
    private UserEntity mUserEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_new);
        mTimer = new Timer();
        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mBtnWealthyTank = (Button) findViewById(R.id.btn_wealthy_tank);
        mBtnExpsStudy = (Button) findViewById(R.id.btn_experiences_study);
        mBtnMoreCourses = (Button) findViewById(R.id.btn_more_courses);
        mBtnMoreExperiences = (Button) findViewById(R.id.btn_more_experiences);
        mBtnMoreIntels = (Button) findViewById(R.id.btn_more_intelligences);
        mBtnPrice = (Button) findViewById(R.id.btn_stock_intel);
        mBtnStartMorality = (Button) findViewById(R.id.btn_start_morality);
        mGVIndex = (GridView) findViewById(R.id.gv_index);
        mStockReports = findViewById(R.id.ll_stock_reports);
        mLongShortArt = findViewById(R.id.ll_long_short_course_entry);
        mLongShortBattle = findViewById(R.id.ll_long_short_battle_entry);
        mLVLatestExps = (ListView) findViewById(R.id.lv_latest_experiences);
        mLVLatestIntels = (ListView) findViewById(R.id.lv_latest_intels);
        mIVHeadPortrait = (ImageView) findViewById(R.id.rci_head_portrait);
        mSearchLayout = findViewById(R.id.fl_search);
        mTVFakeInput = (TextView) findViewById(R.id.tv_fake_input);
    }

    @Override
    protected void setListeners() {
        mController = new NewIndexController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnWealthyTank, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnExpsStudy, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreCourses, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreExperiences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreIntels, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnPrice, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnStartMorality, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mStockReports, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLongShortArt, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLongShortBattle, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIVHeadPortrait, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mTVFakeInput, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mSearchLayout, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVLatestIntels, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVLatestExps, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mGVIndex, mController);
    }

    @Override
    protected void initData() {
        mUserEntity = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
        indexEntities = generateDefaultIndex();
        mIndexGridAdapter = new IndexGridAdapter(this, indexEntities);
        mIndexGridAdapter.setNewLayout(true);
        latestProfessionExperiencesEntities = new ArrayList<>();
        mLatestProfessionExperiencesAdapter
                = new IndexLatestArticlesAdapter(this, latestProfessionExperiencesEntities);
        latestIntelsEntities = new ArrayList<>();
        mLatestIntelsAdapter = new IndexLatestArticlesAdapter(this, latestIntelsEntities);
    }

    @Override
    protected void initViews() {
        mLVLatestExps.setAdapter(mLatestProfessionExperiencesAdapter);
        mLVLatestIntels.setAdapter(mLatestIntelsAdapter);
        mGVIndex.setAdapter(mIndexGridAdapter);
        if (null != mUserEntity && !StringUtils.isEmpty(mUserEntity.getAvatarUrl())) {
            ImageLoader.getInstance().loadImage(ProjectUtil.getFullUrl(mUserEntity.getAvatarUrl()),
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            final Bitmap temp = ProjectUtil.toRoundBitmap(bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIVHeadPortrait.setImageBitmap(temp);
                                }
                            });
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryArticles();
        MobclickAgent.onResume(this);
        if (StockUtil.isTradingTime()) {
            if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    //请求最新数据
                    startTask(new QueryStockListDetailsRequest(
                            indexEntities, TaskId.TASK_ID_FIRST, new QueryAction(NewIndexActivity.this)));
                }
            };
            mTimer.schedule(mTimerTask, 0, ProjectConfigConstant.REFRESH_PERIOD);
        } else {
            //请求最新数据
            startTask(new QueryStockListDetailsRequest(
                    indexEntities,
                    TaskId.TASK_ID_FIRST,
                    CacheKey.KEY_STOCK_INDEX_LIST,
                    new QueryAction(NewIndexActivity.this)));
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
        startTask(new QueryIntelligencesRequest(
                TaskId.TASK_ID_FOURTH, CacheKey.KEY_INTELLIGENCES_FOR_MAIN,
                paramIntel, new QueryAction(this))
        );
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
                mIndexGridAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_SECOND:
                final List<ArticleEntity> latestProExEntities = (List<ArticleEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == latestProExEntities || latestProExEntities.size() < 1) {
                    return;
                }
                latestProfessionExperiencesEntities.clear();
                latestProfessionExperiencesEntities.addAll(latestProExEntities.subList(0, 4));
                latestProExEntities.clear();
                mLatestProfessionExperiencesAdapter.notifyDataSetChanged();
                ViewUtils.setListViewHeightBasedOnChildren(mLVLatestExps);

                break;
            case TaskId.TASK_ID_FOURTH:
                final List<ArticleEntity> latestIntelEntities = (List<ArticleEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == latestIntelEntities || latestIntelEntities.size() < 1) {
                    return;
                }
                this.latestIntelsEntities.clear();
                this.latestIntelsEntities.addAll(latestIntelEntities.subList(0, 4));
                latestIntelEntities.clear();
                mLatestIntelsAdapter.notifyDataSetChanged();
                ViewUtils.setListViewHeightBasedOnChildren(mLVLatestIntels);

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

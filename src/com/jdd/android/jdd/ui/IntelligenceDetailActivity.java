package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.CommentArticleFunction;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.controllers.IntelligenceDetailController;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.entities.StockPlanEntity;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.*;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：情报详情
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-07
 * @since 1.0
 */
public class IntelligenceDetailActivity extends TKActivity implements IQueryServer {
    public static final String KEY_INTENT = "KEY_INTENT";
    //点评类型字典字符串，按照牛-差排序
    private static final String COMMENT_TYPES = CommentArticleFunction.COMMENT_TYPE_GREAT +
            CommentArticleFunction.COMMENT_TYPE_GOOD + CommentArticleFunction.COMMENT_TYPE_ORDINARY +
            CommentArticleFunction.COMMENT_TYPE_BAD + CommentArticleFunction.COMMENT_TYPE_TERRIBLE;
    public IntelligenceEntity articleEntity = null;
    public StockEntity stockEntity = null;

    private ImageButton mBtnBack;     //
    private Button mBtnAboutAuthor;     //关于作者
    private TextView mTVIntelTitle;     //标题
    private TextView mTVDate;     //日期
    private TextView mTVAuthorName;     //作者姓名
    private TextView mTVDigest;     //摘要
    private TextView mTVStockNameAndCode;     //股票名称和代码
    private Button mBtnCheckPrice;     //看行情
    private TextView mTVBuyPrice, mTVSellPrice;     //买入、卖出价格
    private TextView mTVStopProfit, mTVStopLoss;     //止盈、至顺
    private TextView mTVHoldingDay, mTVPositions;     //持仓天数和仓位
    private ImageButton mIBtnCollect, mIBtnReport;     //收藏、举报
    private LinearLayout mLLGreat, mLLGood, mLLOrdinary, mLLBad, mLLTerrible;     //牛、好、行、差、烂点击监听视图
    private TextView mTVGreatCount, mTVGoodCount, mTVOrdinaryCount;     //牛、好、行计数
    private TextView mTVBadCount, mTVTerribleCount;     //差、烂计数
    private ImageButton mIBtnReward;     //打赏
    private TextView mTVIntelPrice;     //情报价格
    private Button mBtnBuy;     //购买
    private WebView mWVRecommendReason, mWVFundamentals, mWVFuture, mWVRisk;
    //内容布局变量，方便按付费显示内容
    private LinearLayout mLLDigest, mLLChargeContent, mLLBuy;
    private TextView mTVFraction, mTVLongShortPosition, mTVTheme, mTVIndustry;
    private TextView mTVInvestStyle, mTVArea;
    private Dialog mDialogProcessing;

    private IntelligenceDetailController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private boolean mIsUsersArticle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligence_detail);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mBtnAboutAuthor = (Button) findViewById(R.id.tv_about_author);
        mBtnBuy = (Button) findViewById(R.id.btn_buy);
        mBtnCheckPrice = (Button) findViewById(R.id.btn_check_price);
        mIBtnCollect = (ImageButton) findViewById(R.id.ibtn_collect);
        mIBtnReport = (ImageButton) findViewById(R.id.ibtn_report);
        mIBtnReward = (ImageButton) findViewById(R.id.ibtn_reward);
        mLLBad = (LinearLayout) findViewById(R.id.ll_bad);
        mLLGood = (LinearLayout) findViewById(R.id.ll_good);
        mLLGreat = (LinearLayout) findViewById(R.id.ll_great);
        mLLOrdinary = (LinearLayout) findViewById(R.id.ll_ordinary);
        mLLTerrible = (LinearLayout) findViewById(R.id.ll_terrible);
        mTVAuthorName = (TextView) findViewById(R.id.tv_author_name);
        mTVBuyPrice = (TextView) findViewById(R.id.tv_buy_price_value);
        mTVDate = (TextView) findViewById(R.id.tv_date);
        mTVDigest = (TextView) findViewById(R.id.tv_digest);
        mTVBadCount = (TextView) findViewById(R.id.tv_bad_cmt_count);
        mTVGoodCount = (TextView) findViewById(R.id.tv_good_cmt_count);
        mTVGreatCount = (TextView) findViewById(R.id.tv_great_cmt_count);
        mTVOrdinaryCount = (TextView) findViewById(R.id.tv_ordinary_cmt_count);
        mTVTerribleCount = (TextView) findViewById(R.id.tv_terrible_cmt_count);
        mTVHoldingDay = (TextView) findViewById(R.id.tv_holding_day_value);
        mTVIntelPrice = (TextView) findViewById(R.id.tv_price_value);
        mTVIntelTitle = (TextView) findViewById(R.id.tv_intelligence_title);
        mTVPositions = (TextView) findViewById(R.id.tv_positions_value);
        mTVSellPrice = (TextView) findViewById(R.id.tv_sell_price_value);
        mTVStockNameAndCode = (TextView) findViewById(R.id.tv_stock_name_code);
        mTVStopLoss = (TextView) findViewById(R.id.tv_stop_loss_value);
        mTVStopProfit = (TextView) findViewById(R.id.tv_stop_profit_value);
        mWVFundamentals = (WebView) findViewById(R.id.wv_fundamentals);
        mWVFuture = (WebView) findViewById(R.id.wv_future);
        mWVRecommendReason = (WebView) findViewById(R.id.wv_recommend_reason);
        mWVRisk = (WebView) findViewById(R.id.wv_risk);
        mLLDigest = (LinearLayout) findViewById(R.id.ll_digest);
        mLLChargeContent = (LinearLayout) findViewById(R.id.ll_charge_contents);
        mLLBuy = (LinearLayout) findViewById(R.id.ll_buy);
        mTVFraction = (TextView) findViewById(R.id.tv_fraction_value);
        mTVLongShortPosition = (TextView) findViewById(R.id.tv_long_short_position_value);
        mTVTheme = (TextView) findViewById(R.id.tv_theme_value);
        mTVIndustry = (TextView) findViewById(R.id.tv_industry_value);
        mTVInvestStyle = (TextView) findViewById(R.id.tv_invest_style_value);
        mTVArea = (TextView) findViewById(R.id.tv_area_value);
        mDialogProcessing = PopUpComponentUtil.createLoadingProgressDialog(this, "处理中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new IntelligenceDetailController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnAboutAuthor, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnBuy, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnCheckPrice, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnCollect, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnReport, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnReward, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLBad, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLGood, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLGreat, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLOrdinary, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLTerrible, mController);
    }

    @Override
    protected void initData() {
        try {
            articleEntity = (IntelligenceEntity) getIntent().getSerializableExtra(KEY_INTENT);
        } catch (Exception e) {
            articleEntity = new IntelligenceEntity();
        }
        queryDetail();
    }

    @Override
    public void initViews() {
        initWebView(mWVFundamentals);
        initWebView(mWVFuture);
        initWebView(mWVRecommendReason);
        initWebView(mWVRisk);
        if (null == articleEntity) {
            return;
        }
        if (!StringUtils.isEmpty(articleEntity.getTitle())) {
            mTVIntelTitle.setText(articleEntity.getTitle());
        }
        if (!StringUtils.isEmpty(articleEntity.getAuthorName())) {
            mTVAuthorName.setText(articleEntity.getAuthorName());
        }
        if (articleEntity.getCreateDate() > 0) {
            String datetime = DateFormat.format(
                    "yyyy-MM-dd hh:mm", articleEntity.getCreateDate()).toString();
            mTVDate.setText(datetime);
        }
        if (!StringUtils.isEmpty(articleEntity.getSummary())) {
            mTVDigest.setText(articleEntity.getSummary());
        }
        if (!StringUtils.isEmpty(articleEntity.getCategory())) {
            mTVFraction.setText(articleEntity.getCategory());
        }
        if (!StringUtils.isEmpty(articleEntity.getLongShortPosition())) {
            mTVLongShortPosition.setText(articleEntity.getLongShortPosition());
        }
        if (!StringUtils.isEmpty(articleEntity.getTheme())) {
            mTVTheme.setText(articleEntity.getTheme());
        }
        if (!StringUtils.isEmpty(articleEntity.getIndustry())) {
            mTVIndustry.setText(articleEntity.getIndustry());
        }
        if (!StringUtils.isEmpty(articleEntity.getInvestStyle())) {
            mTVInvestStyle.setText(articleEntity.getInvestStyle());
        }
        if (!StringUtils.isEmpty(articleEntity.getArea())) {
            mTVArea.setText(articleEntity.getArea());
        }
        mTVGreatCount.setText(String.valueOf(articleEntity.getGreatNum()));
        mTVGoodCount.setText(String.valueOf(articleEntity.getGoodNum()));
        mTVOrdinaryCount.setText(String.valueOf(articleEntity.getNormalNum()));
        mTVBadCount.setText(String.valueOf(articleEntity.getBadNum()));
        mTVTerribleCount.setText(String.valueOf(articleEntity.getTerribleNum()));
        if (articleEntity.getPrice() > 0) {
            mTVIntelPrice.setText(String.valueOf(articleEntity.getPrice()));
        }
        if (!mIsUsersArticle && (!articleEntity.isPayed() && articleEntity.getPrice() > 0)) {
            mLLChargeContent.setVisibility(View.GONE);
            mLLDigest.setVisibility(View.VISIBLE);
            mLLBuy.setVisibility(View.VISIBLE);
            return;
        } else {
            mLLChargeContent.setVisibility(View.VISIBLE);
            mLLDigest.setVisibility(View.GONE);
            mLLBuy.setVisibility(View.GONE);
        }
        //付费内容
        if (!StringUtils.isEmpty(articleEntity.getRecommendReason())) {
            mWVRecommendReason.loadDataWithBaseURL(
                    "", articleEntity.getRecommendReason(), "html/text", "UTF-8", ""
            );
        }
        if (!StringUtils.isEmpty(articleEntity.getFundamentals())) {
            mWVFundamentals.loadDataWithBaseURL(
                    "", articleEntity.getFundamentals(), "html/text", "UTF-8", ""
            );
        }
        if (!StringUtils.isEmpty(articleEntity.getFuture())) {
            mWVFuture.loadDataWithBaseURL(
                    "", articleEntity.getFuture(), "html/text", "UTF-8", ""
            );
        }
        if (!StringUtils.isEmpty(articleEntity.getRisk())) {
            mWVRisk.loadDataWithBaseURL(
                    "", articleEntity.getRisk(), "html/text", "UTF-8", ""
            );
        }
        StockPlanEntity stockPlanEntity = articleEntity.getStockPlanEntity();
        if (null == stockPlanEntity) {
            return;
        }
        if (!StringUtils.isEmpty(stockPlanEntity.getStockName()) &&
                !StringUtils.isEmpty(stockPlanEntity.getStockCode())) {
            mTVStockNameAndCode.setText(
                    stockPlanEntity.getStockName() + "(" + stockPlanEntity.getStockCode() + ")");
        }
        if (stockPlanEntity.getBuyPrice() > 0) {
            mTVBuyPrice.setText(String.valueOf(stockPlanEntity.getBuyPrice()));
        }
        if (stockPlanEntity.getSellPrice() > 0) {
            mTVSellPrice.setText(String.valueOf(stockPlanEntity.getSellPrice()));
        }
        if (stockPlanEntity.getStopProfit() > 0) {
            mTVStopProfit.setText(stockPlanEntity.getStopProfit() + "%");
        }
        if (stockPlanEntity.getStopLoss() > 0) {
            mTVStopLoss.setText(stockPlanEntity.getStopLoss() + "%");
        }
        if (stockPlanEntity.getHoldingDay() > 0) {
            mTVHoldingDay.setText(stockPlanEntity.getHoldingDay() + "天");
        }
        if (stockPlanEntity.getPositions() > 0) {
            mTVPositions.setText(stockPlanEntity.getPositions() + "%");
        }
    }

    private void queryDetail() {
        //查询正文
        String url;
        UserEntity user = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
        if (user.getUserId() == articleEntity.getAuthorId()) {
            url = ProjectUtil.getFullMainServerUrl("URL_QUERY_MY_INTELLIGENCE_DETAIL");
            mIsUsersArticle = true;
        } else {
            url = ProjectUtil.getFullMainServerUrl("URL_QUERY_INTELLIGENCE_DETAIL");
        }
        Parameter param = new Parameter();
        param.addParameter(
                QueryIntelligencesFunction.IN_ARTICLE_ID,
                String.valueOf(articleEntity.getArticleId()));
        startTask(new QueryIntelligenceDetailRequest(
                TaskId.TASK_ID_FIRST, url, param, new QueryAction(this)));
        mDialogProcessing.show();
    }

    private void initWebView(WebView wv) {
        wv.getSettings().setDefaultFontSize(16);
        wv.getSettings().setSupportZoom(false);
    }

    private void searchStock() {
        if (null == articleEntity.getStockPlanEntity() ||
                StringUtils.isEmpty(articleEntity.getStockPlanEntity().getStockCode())) {
            return;
        }
        Parameter parameter = new Parameter();
        parameter.addParameter("search", articleEntity.getStockPlanEntity().getStockCode());
        parameter.addParameter("taskId", String.valueOf(TaskId.TASK_ID_FIFTH));
        startTask(new StockSearchRequest(parameter, new QueryAction(this)));
    }

    public void commentIntel(String type) {
        Parameter param = new Parameter();
        param.addParameter(
                CommentArticleFunction.IN_ID, String.valueOf(articleEntity.getArticleId())
        );
        param.addParameter(CommentArticleFunction.IN_COMMENT_TYPE, type);

        startTask(new CommentArticleRequest(
                ProjectUtil.getFullMainServerUrl("URL_COMMENT_INTEL"),
                TaskId.TASK_ID_SECOND, type, param, new QueryAction(this)));
        mDialogProcessing.show();
    }

    public void collectIntel() {
        Parameter param = new Parameter();
        param.addParameter(
                CommentArticleFunction.IN_ID, String.valueOf(articleEntity.getArticleId())
        );

        startTask(new CollectIntelRequest(TaskId.TASK_ID_THIRD, param, new QueryAction(this)));
        mDialogProcessing.show();
    }

    public void buyIntel() {
        Parameter param = new Parameter();
        param.addParameter(
                CommentArticleFunction.IN_ID, String.valueOf(articleEntity.getArticleId())
        );

        startTask(new BuyIntelRequest(TaskId.TASK_ID_FOURTH, param, new QueryAction(this)));
        mDialogProcessing.show();
    }

    private void dismissDialog() {
        if (mDialogProcessing.isShowing()) {
            mDialogProcessing.dismiss();
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                articleEntity = (IntelligenceEntity)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                initViews();
                searchStock();

                break;
            case TaskId.TASK_ID_SECOND:
                String cmtType = bundle.getString(String.valueOf(taskId));
                onCommentSuccess(cmtType);

                break;
            case TaskId.TASK_ID_THIRD:
                Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();

                break;
            case TaskId.TASK_ID_FOURTH:
                Toast.makeText(this, "购买成功", Toast.LENGTH_SHORT).show();
                articleEntity.setPayed(true);
                initViews();
                queryDetail();

                break;
            case TaskId.TASK_ID_FIFTH:
                final List<StockEntity> searchStockEntities = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null != searchStockEntities && searchStockEntities.size() > 0) {
                    for (StockEntity entity : searchStockEntities) {
                        if (entity.getName().equals(articleEntity.getStockPlanEntity().getStockName()) &&
                                entity.getCode().equals(articleEntity.getStockPlanEntity().getStockCode())) {
                            stockEntity = entity;
                        }
                    }
                }

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                Toast.makeText(this, "查询情报失败", Toast.LENGTH_SHORT).show();

                break;
            case TaskId.TASK_ID_SECOND:
            case TaskId.TASK_ID_THIRD:
            case TaskId.TASK_ID_FOURTH:
                String errMsg = bundle.getString(CommentArticleFunction.ERR_MSG);
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
        Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
//        Toast.makeText(this, "解析错误", Toast.LENGTH_SHORT).show();
    }

    private void onCommentSuccess(String type) {
        switch (COMMENT_TYPES.indexOf(type)) {
            case 0:
                articleEntity.setGreatNum(articleEntity.getGreatNum() + 1);
                mTVGreatCount.setText(String.valueOf(articleEntity.getGreatNum()));

                break;
            case 1:
                articleEntity.setGoodNum(articleEntity.getGoodNum() + 1);
                mTVGoodCount.setText(String.valueOf(articleEntity.getGoodNum()));

                break;
            case 2:
                articleEntity.setNormalNum(articleEntity.getNormalNum() + 1);
                mTVOrdinaryCount.setText(String.valueOf(articleEntity.getNormalNum()));

                break;
            case 3:
                articleEntity.setBadNum(articleEntity.getBadNum() + 1);
                mTVBadCount.setText(String.valueOf(articleEntity.getBadNum()));

                break;
            case 4:
                articleEntity.setTerribleNum(articleEntity.getTerribleNum() + 1);
                mTVTerribleCount.setText(articleEntity.getTerribleNum());

                break;
        }
    }
}

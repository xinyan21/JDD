package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.webkit.WebView;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.CommentArticleFunction;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.controllers.ExperienceDetailController;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.CollectIntelRequest;
import com.jdd.android.jdd.requests.CommentArticleRequest;
import com.jdd.android.jdd.requests.QueryExperienceDetailRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

/**
 * 描述：智文详情
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-03
 * @since 1.0
 */
public class ExperienceDetailActivity extends TKActivity implements IQueryServer {
    public static final String KEY_INTENT = "KEY_INTENT";
    private static final String COMMENT_TYPES = CommentArticleFunction.COMMENT_TYPE_GREAT +
            CommentArticleFunction.COMMENT_TYPE_GOOD + CommentArticleFunction.COMMENT_TYPE_ORDINARY;
    public ExperienceEntity articleEntity;
    private ImageButton mBtnBack;     //
    private Button mBtnAboutAuthor;     //关于作者
    private TextView mTVArticleTitle;     //文章标题
    private TextView mTVDate;     //日期
    private TextView mTVAuthorName;     //作者姓名
    private WebView mWVContent;     //正文
    private ImageButton mIBtnCollect, mIBtnReport;     //收藏、举报
    private LinearLayout mLLGreat, mLLGood, mLLOrdinary;     //牛、好、行、差、烂点击监听视图
    private TextView mTVGreatCount, mTVGoodCount, mTVOrdinaryCount;     //牛、好、行计数
    private TextView mTVActivityTitle;
    private Dialog mDialogProcessing;

    private ExperienceDetailController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_detail);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mBtnAboutAuthor = (Button) findViewById(R.id.btn_about_author);
        mIBtnCollect = (ImageButton) findViewById(R.id.ibtn_collect);
        mIBtnReport = (ImageButton) findViewById(R.id.ibtn_report);
        mLLGood = (LinearLayout) findViewById(R.id.ll_good);
        mLLGreat = (LinearLayout) findViewById(R.id.ll_great);
        mLLOrdinary = (LinearLayout) findViewById(R.id.ll_ordinary);
        mTVAuthorName = (TextView) findViewById(R.id.tv_author_name);
        mWVContent = (WebView) findViewById(R.id.wv_content);
        mTVDate = (TextView) findViewById(R.id.tv_date);
        mTVGoodCount = (TextView) findViewById(R.id.tv_good_cmt_count);
        mTVGreatCount = (TextView) findViewById(R.id.tv_great_cmt_count);
        mTVArticleTitle = (TextView) findViewById(R.id.tv_title);
        mTVOrdinaryCount = (TextView) findViewById(R.id.tv_ordinary_cmt_count);
        mTVActivityTitle = (TextView) findViewById(R.id.tv_activity_title);
        mDialogProcessing = PopUpComponentUtil.createLoadingProgressDialog(this, "处理中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new ExperienceDetailController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnAboutAuthor, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnCollect, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnReport, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLGood, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLGreat, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mLLOrdinary, mController);
    }

    @Override
    protected void initData() {
        try {
            articleEntity = (ExperienceEntity) getIntent().getSerializableExtra(KEY_INTENT);
        } catch (Exception e) {
            articleEntity = new ExperienceEntity();
        }
        //查询正文
        String url;
        UserEntity user = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
        if (user.getUserId() == articleEntity.getAuthorId()) {
            url = ProjectUtil.getFullMainServerUrl("URL_QUERY_MY_EXPERIENCE_DETAIL");
        } else {
            url = ProjectUtil.getFullMainServerUrl("URL_QUERY_EXPERIENCE_DETAIL");
        }
        Parameter param = new Parameter();
        param.addParameter(
                QueryExperiencesFunction.IN_ARTICLE_ID,
                String.valueOf(articleEntity.getArticleId()));
        startTask(new QueryExperienceDetailRequest(
                TaskId.TASK_ID_FIRST, url, param, new QueryAction(this)));
    }

    @Override
    protected void initViews() {
        mTVActivityTitle.setText("看智文");
        initWebView(mWVContent);
        if (null == articleEntity) {
            return;
        }
        if (!StringUtils.isEmpty(articleEntity.getTitle())) {
            mTVArticleTitle.setText(articleEntity.getTitle());
        }
        if (!StringUtils.isEmpty(articleEntity.getAuthorName())) {
            mTVAuthorName.setText(articleEntity.getAuthorName());
        }
        if (articleEntity.getCreateDate() > 0) {
            String datetime = DateFormat.format(
                    "yyyy-MM-dd hh:mm", articleEntity.getCreateDate()).toString();
            mTVDate.setText(datetime);
        }
        if (!StringUtils.isEmpty(articleEntity.getContent())) {
            mWVContent.loadDataWithBaseURL("", articleEntity.getContent(), "html/text", "utf-8", "");
        }
        mTVOrdinaryCount.setText(String.valueOf(articleEntity.getNormalNum()));
        mTVGoodCount.setText(String.valueOf(articleEntity.getGoodNum()));
        mTVGreatCount.setText(String.valueOf(articleEntity.getGreatNum()));
    }

    private void initWebView(WebView wv) {
        wv.getSettings().setDefaultFontSize(14);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(false);
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
                articleEntity = (ExperienceEntity)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                initViews();

                break;
            case TaskId.TASK_ID_SECOND:
                String cmtType = bundle.getString(String.valueOf(taskId));
                commentSuccessResponse(cmtType);

                break;

            case TaskId.TASK_ID_THIRD:
                Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                Toast.makeText(this, "查询智文失败", Toast.LENGTH_SHORT).show();

                break;
            case TaskId.TASK_ID_SECOND:
            case TaskId.TASK_ID_THIRD:
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

    public void collectIntel() {
        Parameter param = new Parameter();
        param.addParameter(
                CommentArticleFunction.IN_ID, String.valueOf(articleEntity.getArticleId())
        );

        startTask(new CollectIntelRequest(TaskId.TASK_ID_THIRD, param, new QueryAction(this)));
        mDialogProcessing.show();
    }

    public void commentArticle(String type) {
        Parameter param = new Parameter();
        param.addParameter(
                CommentArticleFunction.IN_SID, String.valueOf(articleEntity.getArticleId())
        );
        param.addParameter(CommentArticleFunction.IN_COMMENT_TYPE, type);

        startTask(new CommentArticleRequest(
                ProjectUtil.getFullMainServerUrl("URL_COMMENT_EXP"),
                TaskId.TASK_ID_SECOND, type, param, new QueryAction(this)));
        mDialogProcessing.show();
    }

    private void commentSuccessResponse(String type) {
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
        }
    }
}

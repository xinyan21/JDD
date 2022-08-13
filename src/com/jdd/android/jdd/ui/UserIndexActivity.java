package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.IndexLatestArticlesAdapter;
import com.jdd.android.jdd.adapters.UserIndexExperiencesAdapter;
import com.jdd.android.jdd.adapters.UserIndexFollowsAdapter;
import com.jdd.android.jdd.adapters.UserIndexIntelsAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.ArticleFunction;
import com.jdd.android.jdd.constants.function.QueryFavoritesFunction;
import com.jdd.android.jdd.constants.function.QueryFollowsFunction;
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.controllers.UserIndexController;
import com.jdd.android.jdd.entities.*;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.*;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：用户主页
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-03
 * @since 1.0
 */
public class UserIndexActivity extends TKActivity implements IQueryServer {
    public static final String KEY_IN_PARAM = "in_param";       //入参的key
    //列表类型
    public static final short LIST_INTEL = 1;     //情报
    public static final short LIST_EXP = 2;     //智文
    public static final short LIST_FOLLOW = 3;     //关注的人
    public PullToRefreshListView lvUserDataList;
    public Button btnIntelligences, btnExperiences, btnFollows;
    public View boldLineIntelligence, boldLineExperience, boldLineFollows;

    public List<UserEntity> followsEntities;
    public List<ArticleEntity> intelEntities;
    public List<ArticleEntity> experiencesEntities;
    public short currentIntelPage = 1, currentExpPage = 1, currentFollowsPage = 1;
    public UserEntity userEntity = new UserEntity();
    public UserEntity userRegisterInfo;
    public boolean isCurrentAnalystFollowed = false;

    private ImageButton mIBtnSearch;
    private ImageButton mIBtnBack;
    private Button mBtnFollow, mBtnReward, mBtnMoreUserInfo;
    private TextView mTVTopTitle;
    private ImageView mIVAvatar, mIVGender;
    private TextView mTVUserName;
    private ImageView mIVIsVerified;
    private ImageView mIVRank;
    private TextView mTVAddress;
    private TextView mTVProfession;
    private Dialog mDialogLoading, mDialogCannotCancel;
    private ScrollView mScrollView;

    private UserIndexController mController;
    private IndexLatestArticlesAdapter mExperiencesAdapter;
    private IndexLatestArticlesAdapter mIntelsAdapter;
    private UserIndexFollowsAdapter mFollowsAdapter;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private short mCurrentList = LIST_INTEL;
    private int mCurrentFollowPositionProcessing = 0;     //当前正在处理中的关注人列表索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mScrollView = (ScrollView) findViewById(R.id.sv_main);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mTVTopTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIVAvatar = (ImageView) findViewById(R.id.iv_user_portrait);
        mIVGender = (ImageView) findViewById(R.id.iv_gender);
        mIVIsVerified = (ImageView) findViewById(R.id.iv_verified);
        mIVRank = (ImageView) findViewById(R.id.iv_rank_icon);
        mTVAddress = (TextView) findViewById(R.id.tv_address);
        mTVProfession = (TextView) findViewById(R.id.tv_profession);
        btnExperiences = (Button) findViewById(R.id.btn_experiences_study);
        btnFollows = (Button) findViewById(R.id.btn_follows);
        btnIntelligences = (Button) findViewById(R.id.btn_intelligences);
        lvUserDataList = (PullToRefreshListView) findViewById(R.id.lv_user_index_data_list);
        boldLineExperience = findViewById(R.id.v_bold_line_experience);
        boldLineFollows = findViewById(R.id.v_bold_line_follows);
        boldLineIntelligence = findViewById(R.id.v_bold_line_intelligence);
        mBtnFollow = (Button) findViewById(R.id.btn_follow);
        mBtnReward = (Button) findViewById(R.id.btn_reward);
        mTVUserName = (TextView) findViewById(R.id.tv_user_name);
        mBtnMoreUserInfo = (Button) findViewById(R.id.btn_more_user_info);
        mDialogLoading
                = PopUpComponentUtil.createLoadingProgressDialog(this, "加载中，请稍候...", true, true);
        mDialogCannotCancel
                = PopUpComponentUtil.createLoadingProgressDialog(this, "处理中，请稍候...", false, true);
    }

    @Override
    protected void setListeners() {
        mController = new UserIndexController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnExperiences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnFollows, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnIntelligences, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnFollow, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnReward, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnMoreUserInfo, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, lvUserDataList, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, lvUserDataList, mController);
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra(KEY_IN_PARAM)) {
            userEntity.setUserId(getIntent().getLongExtra(KEY_IN_PARAM, 0));
        }
        followsEntities = new ArrayList<>();
        intelEntities = new ArrayList<>();
        experiencesEntities = new ArrayList<>();
        mIntelsAdapter = new IndexLatestArticlesAdapter(this, intelEntities);
        mExperiencesAdapter = new IndexLatestArticlesAdapter(this, experiencesEntities);
        mFollowsAdapter = new UserIndexFollowsAdapter(this, followsEntities);

        queryUserInfo();
        queryUserData();
    }

    @Override
    protected void initViews() {
        mTVTopTitle.setText("分析师");
        //设置只支持上拉加载更多
        lvUserDataList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        lvUserDataList.setAdapter(mIntelsAdapter);
    }

    private void initUserInfo() {
        if (null == userEntity) {
            return;
        }
        if (!StringUtils.isEmpty(userEntity.getNickName())) {
            mTVUserName.setText(userEntity.getNickName());
        } else if (!StringUtils.isEmpty(userEntity.getName())) {
            mTVUserName.setText(userEntity.getName());
        }
        if (!StringUtils.isEmpty(userEntity.getGender())) {
            if ("wom".equals(userEntity.getGender())) {
                mIVGender.setImageResource(R.drawable.female_b);
            } else if ("man".equals(userEntity.getGender())) {
                mIVGender.setImageResource(R.drawable.male_b);
            }
        } else {
            mIVGender.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(userEntity.getAvatarUrl())) {
            ImageLoader.getInstance().displayImage(
                    ProjectUtil.getFullUrl(userEntity.getAvatarUrl()), mIVAvatar
            );
        }
        if (!StringUtils.isEmpty(userEntity.getCity())) {
            mTVAddress.setText(userEntity.getCity());
        }
        if (!StringUtils.isEmpty(userEntity.getProfession())) {
            mTVProfession.setText(userEntity.getProfession());
        }
    }

    private void onLoadingComplete() {
        if (mDialogCannotCancel.isShowing()) {
            mDialogCannotCancel.dismiss();
        }
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
        lvUserDataList.onRefreshComplete();
    }

    private void queryUserInfo() {
        Parameter param = new Parameter();
        param.addParameter(
                QueryUserInfoFunction.IN_USER_ID, String.valueOf(userEntity.getUserId())
        );
        startTask(new QueryUserInfoRequest(
                TaskId.TASK_ID_SIXTH, CacheKey.KEY_USER_INFO, param, new QueryAction(this)
        ));
    }

    public void queryUserData() {
        switch (mCurrentList) {
            case LIST_FOLLOW:
                Parameter followParam = new Parameter();
                followParam.addParameter(
                        QueryFollowsFunction.IN_PAGE, String.valueOf(currentFollowsPage)
                );
                followParam.addParameter(
                        QueryFollowsFunction.IN_USER_ID, String.valueOf(userEntity.getUserId())
                );
                startTask(new QueryFollowsRequest(
                        TaskId.TASK_ID_THIRD, followParam, new QueryAction(this)
                ));

                break;
            case LIST_EXP:
                Parameter expParam = new Parameter();
                expParam.addParameter(
                        QueryFavoritesFunction.IN_IDX, QueryFavoritesFunction.IDX_EXPERIENCES);
                expParam.addParameter(
                        QueryFavoritesFunction.IN_PAGE, String.valueOf(currentExpPage));
                expParam.addParameter(
                        ArticleFunction.IN_USER_ID, String.valueOf(userEntity.getUserId())
                );

                startTask(new QueryExperiencesRequest(
                        TaskId.TASK_ID_SECOND, CacheKey.KEY_MY_FAVORITE_EXPS,
                        expParam, new QueryAction(this)
                ));

                break;
            case LIST_INTEL:
                Parameter intelParam = new Parameter();
                intelParam.addParameter(
                        QueryFavoritesFunction.IN_IDX, QueryFavoritesFunction.IDX_INTELS);
                intelParam.addParameter(
                        QueryFavoritesFunction.IN_PAGE, String.valueOf(currentIntelPage));
                intelParam.addParameter(
                        ArticleFunction.IN_USER_ID, String.valueOf(userEntity.getUserId())
                );

                startTask(new QueryIntelligencesRequest(
                        TaskId.TASK_ID_FIRST, CacheKey.KEY_MY_FAVORITE_COURSES,
                        intelParam, new QueryAction(this)
                ));

                break;
        }
        mDialogLoading.show();
    }

    public void setSelectedState(short selectedList) {
        mCurrentList = selectedList;
        switch (mCurrentList) {
            case LIST_EXP:
                lvUserDataList.setAdapter(mExperiencesAdapter);
                mExperiencesAdapter.notifyDataSetChanged();
                if (experiencesEntities.size() < 1) {
                    queryUserData();
                }

                break;
            case LIST_FOLLOW:
                if (followsEntities.size() < 1) {
                    queryUserData();
                }
                lvUserDataList.setAdapter(mFollowsAdapter);
                mFollowsAdapter.notifyDataSetChanged();

                break;
            case LIST_INTEL:
                if (intelEntities.size() < 1) {
                    queryUserData();
                }
                lvUserDataList.setAdapter(mIntelsAdapter);
                mIntelsAdapter.notifyDataSetChanged();

                break;
        }
//        ViewUtils.setListViewHeightBasedOnChildren(lvUserDataList.getRefreshableView());
    }

    public void loadMore() {
        switch (mCurrentList) {
            case LIST_EXP:
                currentExpPage += 1;

                break;
            case LIST_FOLLOW:
                currentFollowsPage += 1;

                break;
            case LIST_INTEL:
                currentIntelPage += 1;

                break;
        }
        queryUserData();
    }

    public void onItemClick(int position) {
        switch (mCurrentList) {
            case LIST_FOLLOW:
                Intent followIntent = new Intent(this, UserIndexActivity.class);
                followIntent.putExtra(KEY_IN_PARAM, followsEntities.get(position).getUserId());
                startActivity(followIntent);

                break;
            case LIST_EXP:
                Intent expIntent = new Intent(this, ExperienceDetailActivity.class);
                expIntent.putExtra(
                        ExperienceDetailActivity.KEY_INTENT, experiencesEntities.get(position - 1));
                startActivity(expIntent);

                break;
            case LIST_INTEL:
                Intent intelIntent = new Intent(this, IntelligenceDetailActivity.class);
                intelIntent.putExtra(
                        IntelligenceDetailActivity.KEY_INTENT, intelEntities.get(position - 1));
                startActivity(intelIntent);

                break;
        }
    }

    public void follow(int position) {
        mCurrentFollowPositionProcessing = position;
        String userId;
        if (position < 0) {
            userId = String.valueOf(userEntity.getUserId());
        } else {
            userId = String.valueOf(followsEntities.get(position).getUserId());
        }
        Parameter param = new Parameter();
        param.addParameter("cId", userId);
        startTask(new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_FOLLOW"),
                TaskId.TASK_ID_FOURTH, param, new QueryAction(this)
        ));
        mDialogCannotCancel.show();
    }

    public void cancelFollow(int position) {
        mCurrentFollowPositionProcessing = position;
        String userId;
        if (position < 0) {
            userId = String.valueOf(userEntity.getUserId());
        } else {
            userId = String.valueOf(followsEntities.get(position).getUserId());
        }
        Parameter param = new Parameter();
        param.addParameter("cId", userId);
        startTask(new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_CANCEL_FOLLOW"),
                TaskId.TASK_ID_FIFTH, param, new QueryAction(this)
        ));
        mDialogCannotCancel.show();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        onLoadingComplete();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final ArrayList<IntelligenceEntity> intels = (ArrayList<IntelligenceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == intels || intels.size() < 1) {
                    if (1 != currentIntelPage) {
                        Toast.makeText(this, "没有更多情报啦", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                intelEntities.addAll(intels);
                intels.clear();
                mIntelsAdapter.notifyDataSetChanged();
                mScrollView.scrollTo(0, 0);

                break;
            case TaskId.TASK_ID_SECOND:
                final ArrayList<ExperienceEntity> experiences = (ArrayList<ExperienceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == experiences || experiences.size() < 1) {
                    if (1 != currentExpPage) {
                        Toast.makeText(this, "没有更多智文啦", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                experiencesEntities.addAll(experiences);
                experiences.clear();
                mExperiencesAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_THIRD:
                final ArrayList<UserEntity> users = (ArrayList<UserEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == users || users.size() < 1) {
                    if (1 != currentFollowsPage) {
                        Toast.makeText(this, "没有更多关注啦", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                followsEntities.addAll(users);
                users.clear();
                mFollowsAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_FOURTH:
                if (mCurrentFollowPositionProcessing < 0) {
                    mBtnFollow.setBackgroundResource(R.color.bg_black);
                    mBtnFollow.setText("已关注");
                    isCurrentAnalystFollowed = true;
                } else {
                    followsEntities.get(mCurrentFollowPositionProcessing).setFollowed(true);
                    mFollowsAdapter.notifyDataSetChanged();
                }

                break;
            case TaskId.TASK_ID_FIFTH:
                if (mCurrentFollowPositionProcessing < 0) {
                    mBtnFollow.setBackgroundResource(R.color.menu_text_orange);
                    mBtnFollow.setText("关注");
                    isCurrentAnalystFollowed = false;
                } else {
                    followsEntities.get(mCurrentFollowPositionProcessing).setFollowed(false);
                    mFollowsAdapter.notifyDataSetChanged();
                }

                break;
            case TaskId.TASK_ID_SIXTH:
                userEntity = (UserEntity) mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                initUserInfo();
                Parameter param = new Parameter();
                param.addParameter(
                        QueryUserInfoFunction.IN_USER_ID, String.valueOf(userEntity.getUserId())
                );
                startTask(new QueryRegisterInfoRequest(
                        TaskId.TASK_ID_SEVENTH, param, userEntity, new QueryAction(this)
                ));

                break;
            case TaskId.TASK_ID_SEVENTH:
                userEntity = (UserEntity) mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                initUserInfo();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        onLoadingComplete();
        switch (taskId) {
            case TaskId.TASK_ID_FOURTH:
                PopUpComponentUtil.showShortToast(this, "您已关注该用户，不能重复关注");

                break;
            case TaskId.TASK_ID_FIFTH:
                PopUpComponentUtil.showShortToast(this, "取消关注失败");

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        onLoadingComplete();
        Toast.makeText(this, R.string.error_net, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        onLoadingComplete();
        Toast.makeText(this, R.string.error_internal, Toast.LENGTH_SHORT).show();
    }
}

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
import com.jdd.android.jdd.adapters.FavoriteCoursesAdapter;
import com.jdd.android.jdd.adapters.NewMyArticlesAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryFavoritesFunction;
import com.jdd.android.jdd.controllers.FavoritesController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.*;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：收藏
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-03
 * @last_edit 2016-01-03
 * @since 1.0
 */
public class FavoritesActivity extends TKActivity implements IQueryServer {
    public static final short FAVORITE_INTEL = 1;     //情报
    public static final short FAVORITE_EXP = 2;     //智文
    public static final short FAVORITE_COURSE = 3;     //课程

    public List<CourseEntity> favoriteCourseEntities;
    public List<ArticleEntity> favoriteIntelEntities, favoriteExperienceEntities;
    public short currentIntelPage = 1, currentExpPage = 1, currentCoursePage = 1;
    public View vBoldLineCourse, vBoldLineIntelligence, vBoldLineExperience;
    public Button btnCourse, btnIntelligence, btnExperience;

    private ImageButton mIBtnBack;
    private TextView mTVTopTitle;
    private PullToRefreshListView mLVFavorites;
    private Dialog mDeletingDialog, mLoadingDialog;

    private FavoriteCoursesAdapter mFavoriteCoursesAdapter;
    private NewMyArticlesAdapter mFavoriteExperiencesAdapter;
    private NewMyArticlesAdapter mFavoriteIntelsAdapter;

    private short mCurrentFavorite = FAVORITE_COURSE;      //默认为情报
    private FavoritesController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private int mCurrentDeletePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mController = new FavoritesController(this);
        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        btnCourse = (Button) findViewById(R.id.btn_courses);
        btnExperience = (Button) findViewById(R.id.btn_experiences_study);
        btnIntelligence = (Button) findViewById(R.id.btn_intelligences);
        mTVTopTitle = (TextView) findViewById(R.id.tv_activity_title);
        mLVFavorites = (PullToRefreshListView) findViewById(R.id.lv_favorite_list);
        vBoldLineCourse = findViewById(R.id.v_bold_line_course);
        vBoldLineExperience = findViewById(R.id.v_bold_line_experience);
        vBoldLineIntelligence = findViewById(R.id.v_bold_line_intelligence);
        mDeletingDialog = PopUpComponentUtil.createLoadingProgressDialog(this, "删除收藏中，请稍候...", false, true);
        mLoadingDialog = PopUpComponentUtil.createLoadingProgressDialog(this, "加载收藏中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnCourse, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnExperience, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnIntelligence, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVFavorites, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVFavorites, mController);
    }

    @Override
    protected void initData() {
        favoriteCourseEntities = new ArrayList<>();
        favoriteExperienceEntities = new ArrayList<>();
        favoriteIntelEntities = new ArrayList<>();
        mFavoriteCoursesAdapter = new FavoriteCoursesAdapter(this, favoriteCourseEntities);
        mFavoriteExperiencesAdapter = new NewMyArticlesAdapter(
                this, favoriteExperienceEntities
        );
        mFavoriteIntelsAdapter = new NewMyArticlesAdapter(this, favoriteIntelEntities);

        queryFavorites();
    }

    @Override
    protected void initViews() {
        mTVTopTitle.setText("收藏夹");
        //设置只支持上拉加载更多
        mLVFavorites.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLVFavorites.setAdapter(mFavoriteCoursesAdapter);
    }

    public void loadMore() {
        switch (mCurrentFavorite) {
            case FAVORITE_COURSE:
                currentCoursePage += 1;

                break;
            case FAVORITE_EXP:
                currentExpPage += 1;

                break;
            case FAVORITE_INTEL:
                currentIntelPage += 1;

                break;
        }
        queryFavorites();
    }

    public void onItemClick(int position) {
        switch (mCurrentFavorite) {
            case FAVORITE_COURSE:
                Intent intent = new Intent(this, CourseVideoActivity.class);
                intent.putExtra(
                        CourseVideoActivity.KEY_COURSE_ENTITY,
                        favoriteCourseEntities.get(position - 1)
                );
                startActivity(intent);

                break;
            case FAVORITE_EXP:
                Intent expIntent = new Intent(this, ExperienceDetailActivity.class);
                expIntent.putExtra(
                        ExperienceDetailActivity.KEY_INTENT, favoriteExperienceEntities.get(position - 1));
                startActivity(expIntent);

                break;
            case FAVORITE_INTEL:
                Intent intelIntent = new Intent(this, IntelligenceDetailActivity.class);
                intelIntent.putExtra(
                        IntelligenceDetailActivity.KEY_INTENT, favoriteIntelEntities.get(position - 1));
                startActivity(intelIntent);

                break;
        }
    }

    public void queryFavorites() {
        Parameter parameter = new Parameter();
        parameter.addParameter(
                QueryFavoritesFunction.IN_IDX, QueryFavoritesFunction.IDX_EXPERIENCES);
        parameter.addParameter(
                QueryFavoritesFunction.IN_STATUS, QueryFavoritesFunction.STATUS_S1);
        switch (mCurrentFavorite) {
            case FAVORITE_COURSE:
                parameter.addParameter(
                        QueryFavoritesFunction.IN_PAGE, String.valueOf(currentCoursePage));
                startTask(new QueryFavoriteCoursesRequest(
                        TaskId.TASK_ID_THIRD, parameter, new QueryAction(this)
                ));

                break;
            case FAVORITE_EXP:
                parameter.addParameter(
                        QueryFavoritesFunction.IN_PAGE, String.valueOf(currentExpPage));

                startTask(new QueryFavoriteExperiencesRequest(
                        TaskId.TASK_ID_SECOND, CacheKey.KEY_MY_FAVORITE_EXPS,
                        parameter, new QueryAction(this)
                ));

                break;
            case FAVORITE_INTEL:
                parameter.addParameter(
                        QueryFavoritesFunction.IN_PAGE, String.valueOf(currentIntelPage));

                startTask(new QueryFavoriteIntelsRequest(
                        TaskId.TASK_ID_FIRST, CacheKey.KEY_MY_FAVORITE_COURSES,
                        parameter, new QueryAction(this)
                ));

                break;
        }
        mLoadingDialog.show();
    }

    public void setSelectedState(short currentFavorite) {
        mCurrentFavorite = currentFavorite;
        switch (mCurrentFavorite) {
            case FAVORITE_COURSE:
                if (favoriteCourseEntities.size() < 1) {
                    queryFavorites();
                }
                mLVFavorites.setAdapter(mFavoriteCoursesAdapter);
                mFavoriteCoursesAdapter.notifyDataSetChanged();

                break;
            case FAVORITE_EXP:
                if (favoriteExperienceEntities.size() < 1) {
                    queryFavorites();
                }
                mLVFavorites.setAdapter(mFavoriteExperiencesAdapter);
                mFavoriteExperiencesAdapter.notifyDataSetChanged();

                break;
            case FAVORITE_INTEL:
                if (favoriteIntelEntities.size() < 1) {
                    queryFavorites();
                }
                mLVFavorites.setAdapter(mFavoriteIntelsAdapter);
                mFavoriteIntelsAdapter.notifyDataSetChanged();

                break;
        }
    }

    public void deleteFavorite(int position) {
        mCurrentDeletePosition = position;
        Parameter param = new Parameter();
        switch (mCurrentFavorite) {
            case FAVORITE_COURSE:
                long courseId = favoriteCourseEntities.get(position).getId();
                param.addParameter("cId", String.valueOf(courseId));
                startTask(new PushRequest(
                        ProjectUtil.getFullMainServerUrl("URL_DELETE_FAVORITE_COURSES"),
                        TaskId.TASK_ID_FOURTH, param, new QueryAction(this)
                ));

                break;
            case FAVORITE_EXP:
                long expId = favoriteExperienceEntities.get(position).getArticleId();
                param.addParameter("sId", String.valueOf(expId));
                startTask(new DeleteFavoriteExpRequest(
                        TaskId.TASK_ID_FOURTH, param, new QueryAction(this)
                ));

                break;
            case FAVORITE_INTEL:
                long intelId = favoriteIntelEntities.get(position).getArticleId();
                param.addParameter("id", String.valueOf(intelId));
                startTask(new DeleteFavoriteIntelRequest(
                        TaskId.TASK_ID_FOURTH, param, new QueryAction(this)
                ));

                break;
        }
        mDeletingDialog.show();
    }

    private void onDeleteSuccess() {
        switch (mCurrentFavorite) {
            case FAVORITE_COURSE:
                favoriteCourseEntities.remove(mCurrentDeletePosition);
                mFavoriteCoursesAdapter.notifyDataSetChanged();

                break;
            case FAVORITE_EXP:
                favoriteExperienceEntities.remove(mCurrentDeletePosition);
                mFavoriteExperiencesAdapter.notifyDataSetChanged();

                break;
            case FAVORITE_INTEL:
                favoriteIntelEntities.remove(mCurrentDeletePosition);
                mFavoriteIntelsAdapter.notifyDataSetChanged();

                break;
        }
    }

    private void dismissDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        if (mDeletingDialog.isShowing()) {
            mDeletingDialog.dismiss();
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        mLVFavorites.onRefreshComplete();
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final ArrayList<IntelligenceEntity> intels = (ArrayList<IntelligenceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == intels || intels.size() < 1) {
                    if (1 == currentIntelPage) {
                        Toast.makeText(this, "您收藏的情报已全部加载", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                favoriteIntelEntities.addAll(intels);
                intels.clear();
                mFavoriteIntelsAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_SECOND:
                final ArrayList<ExperienceEntity> experiences = (ArrayList<ExperienceEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == experiences || experiences.size() < 1) {
                    if (1 == currentExpPage) {
                        Toast.makeText(this, "您还未收藏智文...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "您收藏的智文已全部加载", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                favoriteExperienceEntities.addAll(experiences);
                experiences.clear();
                mFavoriteExperiencesAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_THIRD:
                final ArrayList<CourseEntity> courses = (ArrayList<CourseEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == courses || courses.size() < 1) {
                    if (1 == currentCoursePage) {
                        Toast.makeText(this, "您还未收藏课程...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "您收藏的课程已全部加载", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                favoriteCourseEntities.addAll(courses);
                courses.clear();
                mFavoriteExperiencesAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_FOURTH:
                onDeleteSuccess();
                Toast.makeText(this, "成功删除收藏", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        mLVFavorites.onRefreshComplete();
        dismissDialog();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        mLVFavorites.onRefreshComplete();
        dismissDialog();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        mLVFavorites.onRefreshComplete();
        dismissDialog();
    }
}

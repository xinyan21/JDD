package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.LongShortCoursesAdapter;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.constants.function.QueryFavoritesFunction;
import com.jdd.android.jdd.controllers.BoughtCoursesController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryFavoriteCoursesRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：我购买的课程
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class BoughtCoursesActivity extends TKActivity implements IQueryServer {
    public String courseCategory = QueryCoursesFunction.CATEGORY_LONG_SHORT_BASIC;
    public int currentPage = 1;
    public List<CourseEntity> courseEntities;
    public EditText etSearchKey;
    public Button btnLongShortBasic, btnValueSpeculate;
    public Button btnLongShortPractise, btnFirmOfferTeaching;
    public View boldLineLongShortBasic, boldLineValueSpeculate;
    public View boldLineLongShortPractise, boldLineFirmOfferTeaching;
    private ImageButton mIBtnSearch;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private PullToRefreshListView mLVCourses;
    private Dialog mLoadingDialog;

    private BoughtCoursesController mController;
    private LongShortCoursesAdapter mCoursesAdapter;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private String mLastCategory = courseCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bought_courses);

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
        boldLineFirmOfferTeaching = findViewById(R.id.v_bold_line_firm_offer_teaching);
        boldLineLongShortBasic = findViewById(R.id.v_bold_line_long_short_basic);
        boldLineLongShortPractise = findViewById(R.id.v_bold_line_long_short_practise);
        boldLineValueSpeculate = findViewById(R.id.v_bold_line_value_speculatet);
        btnFirmOfferTeaching = (Button) findViewById(R.id.btn_firm_offer_teaching);
        btnLongShortBasic = (Button) findViewById(R.id.btn_long_short_basic);
        btnLongShortPractise = (Button) findViewById(R.id.btn_long_short_practise);
        btnValueSpeculate = (Button) findViewById(R.id.btn_value_speculate);
        mLVCourses = (PullToRefreshListView) findViewById(R.id.lv_bought_courses);
        mLoadingDialog = PopUpComponentUtil.createLoadingProgressDialog(this, "加载课程中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new BoughtCoursesController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnFirmOfferTeaching, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnLongShortBasic, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnLongShortPractise, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnValueSpeculate, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVCourses, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVCourses, mController);
    }

    @Override
    protected void initData() {
        courseEntities = new ArrayList<>();
        mCoursesAdapter = new LongShortCoursesAdapter(this, courseEntities);
        queryCourses();
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("购买的课程");
        //设置只支持上拉加载更多
        mLVCourses.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLVCourses.setAdapter(mCoursesAdapter);
    }

    public void queryCourses() {
        Parameter parameter = new Parameter();
        parameter.addParameter(
                QueryFavoritesFunction.IN_PAGE, String.valueOf(currentPage));
        parameter.addParameter("type", courseCategory);
        startTask(new QueryFavoriteCoursesRequest(
                TaskId.TASK_ID_FIRST, parameter, new QueryAction(this)
        ));
        mLoadingDialog.show();
    }

    private void dismissDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        mLVCourses.onRefreshComplete();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                if (!mLastCategory.equals(courseCategory)) {
                    courseEntities.clear();
                }
                final ArrayList<CourseEntity> courses = (ArrayList<CourseEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == courses || courses.size() < 1) {
                    if (1 == currentPage) {
                        Toast.makeText(this, "您还未购买过该分类的课程...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "您购买的课程已全部加载", Toast.LENGTH_SHORT).show();
                    }
                    mCoursesAdapter.notifyDataSetChanged();
                    return;
                }
                courseEntities.addAll(courses);
                courses.clear();
                mCoursesAdapter.notifyDataSetChanged();
                mLastCategory = courseCategory;

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        mLVCourses.onRefreshComplete();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
        mLVCourses.onRefreshComplete();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
        mLVCourses.onRefreshComplete();
    }
}

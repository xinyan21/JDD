package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.LongShortCoursesAdapter;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.controllers.LongShortCoursesController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryLongShortCoursesRequest;
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
 * 描述：多空学堂课程列表界面
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-01
 * @since 1.0
 */
public class LongShortCoursesActivity extends TKActivity implements IQueryServer {
    public static final String KEY_CATEGORY = "CATEGORY";

    public String order = "time-down";        //排序
    public short currentPage = 1;
    public String teachingMode = "";

    public Button btnPrice, btnTime, btnHot;
    public LinearLayout llFilter;
    public Button btnFilter, btnVideo, btnPicture, btnVideoAndPPT;
    private TextView mTitle;
    private ImageButton mBack;
    private PullToRefreshListView mLVLongShortCourseList;
    private Dialog mDialogLoading;

    private LongShortCoursesAdapter mCourseAdapter;
    private List<CourseEntity> mCourseEntities;
    private LongShortCoursesController mController;
    private String mCourseCategory;
    private String mLastTeachingMode = "";       //上一次查询时的授课模式
    private String mLastOrder = "";
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_short_course_list);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mLVLongShortCourseList = (PullToRefreshListView) findViewById(R.id.lv_long_short_course_list);
        mTitle = (TextView) findViewById(R.id.tv_activity_title);
        mBack = (ImageButton) findViewById(R.id.ibtn_back);
        btnHot = (Button) findViewById(R.id.btn_hot);
        btnPrice = (Button) findViewById(R.id.btn_sort_by_price);
        btnTime = (Button) findViewById(R.id.btn_sort_by_time);
        btnFilter = (Button) findViewById(R.id.btn_filter);
        btnPicture = (Button) findViewById(R.id.btn_picture);
        btnVideoAndPPT = (Button) findViewById(R.id.btn_video_pic);
        btnVideo = (Button) findViewById(R.id.btn_video);
        llFilter = (LinearLayout) findViewById(R.id.ll_filter);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(this, "加载中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new LongShortCoursesController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnHot, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnPrice, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnTime, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnFilter, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnPicture, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnVideoAndPPT, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnVideo, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVLongShortCourseList, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVLongShortCourseList, mController);
    }

    @Override
    protected void initData() {
        mCourseCategory = getIntent().getStringExtra(KEY_CATEGORY);
        if (mCourseCategory.equals("多空兵法")) {
            mCourseCategory = QueryCoursesFunction.CATEGORY_LONG_SHORT_BASIC;
        } else if (mCourseCategory.equals("道德经杂谈")) {
            mCourseCategory = QueryCoursesFunction.CATEGORY_VALUE_SPECULATION;
        } else if (mCourseCategory.equals("多空实战")) {
            mCourseCategory = QueryCoursesFunction.CATEGORY_LONG_SHORT_BATTLE;
        } else if (mCourseCategory.equals("实盘面授")) {
            mCourseCategory = QueryCoursesFunction.CATEGORY_BINDING_OFFER;
        }
        mCourseEntities = new ArrayList<>();
        mCourseAdapter = new LongShortCoursesAdapter(this, mCourseEntities);
        queryCourses();
    }

    @Override
    protected void initViews() {
        mTitle.setText(getIntent().getStringExtra(KEY_CATEGORY));
        //设置只支持上拉加载更多
        mLVLongShortCourseList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLVLongShortCourseList.setAdapter(mCourseAdapter);
    }

    public void queryCourses() {
        Parameter param = new Parameter();
        param.addParameter(QueryCoursesFunction.IN_CATEGORY, mCourseCategory);
        if (!StringUtils.isEmpty(teachingMode)) {
            param.addParameter(QueryCoursesFunction.IN_TEACHING_MODE, teachingMode);
        }
        param.addParameter(QueryCoursesFunction.IN_ORDER_VALUE, order);
        param.addParameter(QueryCoursesFunction.IN_PAGE, String.valueOf(currentPage));

        startTask(new QueryLongShortCoursesRequest(
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        ));
        mDialogLoading.show();
    }

    private void onLoadComplete() {
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
        mLVLongShortCourseList.onRefreshComplete();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        onLoadComplete();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                //如果修改了查询条件，需要清空历史数据
                if (!mLastTeachingMode.equals(teachingMode) || !mLastOrder.equals(order)) {
                    mCourseEntities.clear();
                    mLastTeachingMode = teachingMode;
                    mLastOrder = order;
                }
                final List<CourseEntity> courses = (List<CourseEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == courses || courses.size() < 1) {
                    if (1 != currentPage) {
                        Toast.makeText(this, "没有更多课程啦", Toast.LENGTH_SHORT).show();
                    } else {
                        mCourseEntities.clear();
                        Toast.makeText(this, "没有搜索到相关课程", Toast.LENGTH_SHORT).show();
                    }
                    mCourseAdapter.notifyDataSetChanged();
                    return;
                }

                this.mCourseEntities.addAll(courses);
                courses.clear();
                mCourseAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        onLoadComplete();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        onLoadComplete();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        onLoadComplete();
    }
}

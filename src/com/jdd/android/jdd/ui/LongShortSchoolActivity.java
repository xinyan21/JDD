package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.CourseCategoriesAdapter;
import com.jdd.android.jdd.adapters.HotCoursesAdapter;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.controllers.LongShortSchoolController;
import com.jdd.android.jdd.entities.CourseCategoryEntity;
import com.jdd.android.jdd.entities.CourseEntity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：多空学堂
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-08-25
 * @since 1.0
 */
public class LongShortSchoolActivity extends TKActivity implements IModule {
    private ImageButton mIBtnBack,mIBtnSearch;
    private TextView mTVTitle;
    private ListView mCourseCategories;
    private ListView mHotCourses;

    private CourseCategoriesAdapter mCourseCategoriesAdapter;
    private HotCoursesAdapter mHotCoursesAdapter;

    private LongShortSchoolController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_short_school);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
//        mHotCourses = (ListView) findViewById(R.id.lv_hot_courses);
        mCourseCategories = (ListView) findViewById(R.id.lv_course_categories);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
    }

    @Override
    protected void setListeners() {
        mController = new LongShortSchoolController(this);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mCourseCategories, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
    }

    @Override
    protected void initData() {
        List<CourseCategoryEntity> courseCategoryEntities = new ArrayList<>();
        CourseCategoryEntity categoryEntity = new CourseCategoryEntity();
        categoryEntity.setAbout("分析股市的本质，股票买卖的对手，亏钱的根本原因； 了解多空思想来源，多空理念概论，树立正确的投资理念； 多空课堂导论。 兵法资本分析决策系统逻辑来源。");
        categoryEntity.setCategoryName("多空兵法");
        categoryEntity.setDetailCategory("试听课程");
        categoryEntity.setTotalTime(120);
        categoryEntity.setIconResName("ic_long_short_course");
        courseCategoryEntities.add(categoryEntity);

        categoryEntity = new CourseCategoryEntity();
        categoryEntity.setAbout("每周不低于两次关键点位的早盘分析，多空兵法教你8分钟看大盘趋势。理论结合实践，实盘操作见真知。 公司运用多空兵法进行股票买卖的实盘视频操作分享。");
        categoryEntity.setCategoryName("多空实战");
        categoryEntity.setDetailCategory("必修课程");
        categoryEntity.setTotalTime(120);
        categoryEntity.setIconResName("ic_long_short_practise_course");
        courseCategoryEntities.add(categoryEntity);

        categoryEntity = new CourseCategoryEntity();
        categoryEntity.setAbout("根据个人性格、思维方式、人生经历、世界观、价值观，全方位交流，量身定制个性化的股票投资之路。 现场观摩 公司多空兵法实时股票买卖交易。客户自己选股，小资金按多空逻辑去学习分析与操作总结。");
        categoryEntity.setCategoryName("实盘面授");
        categoryEntity.setDetailCategory("线下培训");
        categoryEntity.setTotalTime(120);
        categoryEntity.setIconResName("ic_binding_offer_course");
        courseCategoryEntities.add(categoryEntity);

//        categoryEntity = new CourseCategoryEntity();
//        categoryEntity.setAbout("初级课针对的受众是具有一定财富自由概念的零基础小白。初级课主要介绍了如何看公司年报，从年报去分析公司的优劣点，如何判断公司的护城河，如何给公司进行简单的估值等。");
//        categoryEntity.setCategoryName("价值投机");
//        categoryEntity.setDetailCategory("必修课程");
//        categoryEntity.setTotalTime(120);
//        categoryEntity.setIconResName("ic_value_speculate_course");
//        courseCategoryEntities.add(categoryEntity);

        categoryEntity = new CourseCategoryEntity();
        categoryEntity.setAbout("推广道德经，讲述自然和人的关系 内容以一批信仰者和跟随者的 座谈时聊天记录特别随意和任性。");
        categoryEntity.setCategoryName("道德经杂谈");
        categoryEntity.setDetailCategory("必修课程");
        categoryEntity.setTotalTime(120);
        categoryEntity.setIconResName("ic_value_speculate_course");
        courseCategoryEntities.add(categoryEntity);

        mCourseCategoriesAdapter = new CourseCategoriesAdapter(this, courseCategoryEntities);

        List<CourseEntity> courseEntities = new ArrayList<>();
        CourseEntity course = new CourseEntity();

        course.setCourseName("股票操盘手");
        courseEntities.add(course);

        course = new CourseEntity();
        course.setCourseName("短线狙击手速成班-猎庄");
        courseEntities.add(course);

        course = new CourseEntity();
        course.setCourseName("股指期货操盘手");
        courseEntities.add(course);

        course = new CourseEntity();
        course.setCourseName("期权操盘手");
        courseEntities.add(course);

//        mHotCoursesAdapter = new HotCoursesAdapter(this, courseEntities);
    }

    @Override
    protected void initViews() {
//        mHotCourses.setAdapter(mHotCoursesAdapter);
        mCourseCategories.setAdapter(mCourseCategoriesAdapter);
        mTVTitle.setText("多空学堂");
//        mIBtnSearch.setVisibility(View.VISIBLE);
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
}

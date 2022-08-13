package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.CourseImagePagerAdapter;
import com.jdd.android.jdd.controllers.CoursePPTController;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：课程PPT浏览器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-05-23
 * @last_edit 2016-05-23
 * @since 1.0
 */
public class CoursePPTActivity extends TKActivity {
    public static final String KEY_COURSE_ENTITY = "ENTITY";

    private TextView mTVActivityTitle;
    private ImageButton mBack;
    private Gallery mGalleryPics;

    private CourseEntity mCourseEntity;
    private CourseImagePagerAdapter mAdapter;
    private List<String> mPicUrls;
    private CoursePPTController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_ppt);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mGalleryPics = (Gallery) findViewById(R.id.gl_ppt_pics);
        mTVActivityTitle = (TextView) findViewById(R.id.tv_activity_title);
        mBack = (ImageButton) findViewById(R.id.ibtn_back);
    }

    @Override
    protected void setListeners() {
        mController = new CoursePPTController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBack, mController);
    }

    @Override
    protected void initData() {
        mCourseEntity = (CourseEntity) getIntent().getSerializableExtra(KEY_COURSE_ENTITY);
        if (!StringUtils.isEmpty(mCourseEntity.getPptUrl()) &&
                !"null".equals(mCourseEntity.getPptUrl()) && mCourseEntity.getPptCount() > 0) {
            mPicUrls = new ArrayList<>();
            for (int i = 1; i <= mCourseEntity.getPptCount(); i++) {
                mPicUrls.add(ProjectUtil.getPPTUrl(mCourseEntity.getPptUrl(), i));
            }
            mAdapter = new CourseImagePagerAdapter(this, mPicUrls);
        }
    }

    @Override
    protected void initViews() {
        mGalleryPics.setAdapter(mAdapter);
        mTVActivityTitle.setText("PPT浏览");
    }
}

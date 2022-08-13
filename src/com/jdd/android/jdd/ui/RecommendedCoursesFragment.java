package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.RecommendedCoursesAdapter;
import com.jdd.android.jdd.entities.CourseEntity;
import com.thinkive.android.app_engine.engine.TKFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：推荐课程Fragment
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-08
 * @since 1.0
 */
public class RecommendedCoursesFragment extends TKFragment {
    private View mContentView;
    private GridView mRecommendedCourses;
    private Button mMoreCourses;

    private RecommendedCoursesAdapter mRecommendedCoursesAdapter;
    private List<CourseEntity> mCoursesData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_recommended_courses, null);

        Bundle args = getArguments();
        findViews();
        initData();
        initViews();

        return mContentView;
    }

    @Override
    protected void findViews() {
        mRecommendedCourses = (GridView) mContentView.findViewById(R.id.lv_recommended_courses);
        mMoreCourses = (Button) mContentView.findViewById(R.id.btn_more_courses);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initData() {
        mCoursesData = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mCoursesData.add(new CourseEntity());
        }
    }

    @Override
    protected void initViews() {
        mRecommendedCoursesAdapter = new RecommendedCoursesAdapter(getActivity(), mCoursesData);
        mRecommendedCourses.setAdapter(mRecommendedCoursesAdapter);
    }
}

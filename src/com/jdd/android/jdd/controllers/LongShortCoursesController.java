package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.ui.CoursePPTActivity;
import com.jdd.android.jdd.ui.CourseVideoActivity;
import com.jdd.android.jdd.ui.LongShortCoursesActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.utils.StringUtils;

/**
 * 描述：课程列表控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-10-01
 * @since 1.0
 */
public class LongShortCoursesController extends ListenerControllerAdapter implements
        AdapterView.OnItemClickListener, View.OnClickListener, PullToRefreshBase.OnRefreshListener {
    private static final short DIRECTION_UP = 2;
    private static final short DIRECTION_DOWN = 3;

    short mLastPriceDirection = DIRECTION_DOWN;
    short mLastTimeDirection = DIRECTION_DOWN;

    private boolean mIsFilterLayoutVisible = false;
    private LongShortCoursesActivity mActivity;

    public LongShortCoursesController(LongShortCoursesActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof PullToRefreshListView) {
                    ((PullToRefreshListView) view).setOnItemClickListener(this);
                }

                break;
            case ThinkTankController.ON_PULL_REFRESH:
                if (view instanceof PullToRefreshListView) {
                    ((PullToRefreshListView) view).setOnRefreshListener(this);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CourseEntity entity=(CourseEntity) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent();
        if (StringUtils.isEmpty(entity.getVideoUrl())) {
            intent.setClass(mActivity, CoursePPTActivity.class);
        } else {
            intent.setClass(mActivity, CourseVideoActivity.class);
        }
        intent.putExtra(
                CourseVideoActivity.KEY_COURSE_ENTITY,
                entity
        );
        mActivity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                mActivity.finish();

                break;
            case R.id.btn_sort_by_time:
                StringBuffer sbTimeOrder = new StringBuffer("time-");
                if (DIRECTION_DOWN == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_orange, 0);
                    mLastTimeDirection = DIRECTION_UP;
                    sbTimeOrder.append("up");
                } else if (DIRECTION_UP == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_orange, 0);
                    mLastTimeDirection = DIRECTION_DOWN;
                    sbTimeOrder.append("down");
                }
                if (DIRECTION_UP == mLastPriceDirection) {
                    mActivity.btnPrice.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_gray, 0);
                } else if (DIRECTION_DOWN == mLastPriceDirection) {
                    mActivity.btnPrice.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_gray, 0);
                }
                mActivity.btnPrice.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnTime.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_orange));
                mActivity.btnHot.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));

                mActivity.order = sbTimeOrder.toString();
                mActivity.queryCourses();

                break;
            case R.id.btn_sort_by_price:
                StringBuffer sbPriceOrder = new StringBuffer("price-");
                if (DIRECTION_DOWN == mLastPriceDirection) {
                    mActivity.btnPrice.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_orange, 0);
                    mLastPriceDirection = DIRECTION_UP;
                    sbPriceOrder.append("up");
                } else if (DIRECTION_UP == mLastPriceDirection) {
                    mActivity.btnPrice.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_orange, 0);
                    mLastPriceDirection = DIRECTION_DOWN;
                    sbPriceOrder.append("down");
                }
                if (DIRECTION_UP == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_gray, 0);
                } else if (DIRECTION_DOWN == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_gray, 0);
                }
                mActivity.btnPrice.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_orange));
                mActivity.btnTime.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnHot.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));

                mActivity.order = sbPriceOrder.toString();
                mActivity.queryCourses();

                break;
            case R.id.btn_hot:
                mActivity.btnHot.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_orange));
                //重置价格和时间按钮为未选中状态
                if (DIRECTION_UP == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_gray, 0);
                } else if (DIRECTION_DOWN == mLastTimeDirection) {
                    mActivity.btnTime.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_gray, 0);
                }
                if (DIRECTION_UP == mLastPriceDirection) {
                    mActivity.btnPrice.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_up_gray, 0);
                } else if (DIRECTION_DOWN == mLastPriceDirection) {
                    mActivity.btnPrice.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_triangle_down_gray, 0);
                }
                mActivity.btnPrice.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnTime.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));

                mActivity.order = "hot-down";
                mActivity.queryCourses();

                break;
            case R.id.btn_filter:
                if (mIsFilterLayoutVisible) {
                    mActivity.llFilter.setVisibility(View.GONE);
                    mIsFilterLayoutVisible = false;
                } else {
                    mActivity.llFilter.setVisibility(View.VISIBLE);
                    mIsFilterLayoutVisible = true;
                }

                break;
            case R.id.btn_video:
                mActivity.btnVideo.setTextColor(
                        mActivity.getResources().getColor(R.color.white));
                mActivity.btnVideo.setBackgroundResource(R.drawable.shape_btn_rounded_orange);
                mActivity.btnVideoAndPPT.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnVideoAndPPT.setBackgroundResource(
                        R.drawable.shape_rounded_solid_gray_stroke_gray);
                mActivity.btnPicture.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnPicture.setBackgroundResource(
                        R.drawable.shape_rounded_solid_gray_stroke_gray);

                mActivity.teachingMode = QueryCoursesFunction.TEACHING_MODE_VIDEO;
                mActivity.currentPage = 1;
                mActivity.queryCourses();

                break;
            case R.id.btn_video_pic:
                mActivity.btnVideo.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnVideo.setBackgroundResource(R.drawable.shape_rounded_solid_gray_stroke_gray);
                mActivity.btnVideoAndPPT.setTextColor(
                        mActivity.getResources().getColor(R.color.white));
                mActivity.btnVideoAndPPT.setBackgroundResource(
                        R.drawable.shape_btn_rounded_orange);
                mActivity.btnPicture.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnPicture.setBackgroundResource(
                        R.drawable.shape_rounded_solid_gray_stroke_gray);

                mActivity.teachingMode = QueryCoursesFunction.TEACHING_MODE_VIDEO_PIC_TEXT;
                mActivity.currentPage = 1;
                mActivity.queryCourses();

                break;
            case R.id.btn_picture:
                mActivity.btnVideo.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnVideo.setBackgroundResource(R.drawable.shape_rounded_solid_gray_stroke_gray);
                mActivity.btnVideoAndPPT.setTextColor(
                        mActivity.getResources().getColor(R.color.text_gray));
                mActivity.btnVideoAndPPT.setBackgroundResource(
                        R.drawable.shape_rounded_solid_gray_stroke_gray);
                mActivity.btnPicture.setTextColor(
                        mActivity.getResources().getColor(R.color.white));
                mActivity.btnPicture.setBackgroundResource(
                        R.drawable.shape_btn_rounded_orange);

                mActivity.teachingMode = QueryCoursesFunction.TEACHING_MODE_PIC_TEXT;
                mActivity.currentPage = 1;
                mActivity.queryCourses();

                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mActivity.currentPage += 1;
        mActivity.queryCourses();
    }
}

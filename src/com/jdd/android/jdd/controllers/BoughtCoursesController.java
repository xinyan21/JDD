package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.ui.BoughtCoursesActivity;
import com.jdd.android.jdd.ui.CourseVideoActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：购买的课程控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class BoughtCoursesController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    private BoughtCoursesActivity mActivity;

    public BoughtCoursesController(BoughtCoursesActivity activity) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                mActivity.finish();

                break;
            case R.id.ibtn_search:


                break;
            case R.id.btn_long_short_basic:
                mActivity.btnLongShortBasic.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineLongShortBasic.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnValueSpeculate.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineValueSpeculate.setBackgroundResource(R.color.transparent);
                mActivity.btnLongShortPractise.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineLongShortPractise.setBackgroundResource(R.color.transparent);
                mActivity.btnFirmOfferTeaching.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineFirmOfferTeaching.setBackgroundResource(R.color.transparent);

                mActivity.courseCategory = QueryCoursesFunction.CATEGORY_LONG_SHORT_BASIC;
                mActivity.queryCourses();

                break;
            case R.id.btn_value_speculate:
                mActivity.btnLongShortBasic.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineLongShortBasic.setBackgroundResource(R.color.transparent);
                mActivity.btnValueSpeculate.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineValueSpeculate.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnLongShortPractise.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineLongShortPractise.setBackgroundResource(R.color.transparent);
                mActivity.btnFirmOfferTeaching.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineFirmOfferTeaching.setBackgroundResource(R.color.transparent);

                mActivity.courseCategory = QueryCoursesFunction.CATEGORY_VALUE_SPECULATION;
                mActivity.queryCourses();

                break;
            case R.id.btn_long_short_practise:
                mActivity.btnLongShortBasic.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineLongShortBasic.setBackgroundResource(R.color.transparent);
                mActivity.btnValueSpeculate.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineValueSpeculate.setBackgroundResource(R.color.transparent);
                mActivity.btnLongShortPractise.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineLongShortPractise.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnFirmOfferTeaching.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineFirmOfferTeaching.setBackgroundResource(R.color.transparent);

                mActivity.courseCategory = QueryCoursesFunction.CATEGORY_LONG_SHORT_BATTLE;
                mActivity.queryCourses();

                break;
            case R.id.btn_firm_offer_teaching:
                mActivity.btnLongShortBasic.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineLongShortBasic.setBackgroundResource(R.color.transparent);
                mActivity.btnValueSpeculate.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineValueSpeculate.setBackgroundResource(R.color.transparent);
                mActivity.btnLongShortPractise.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineLongShortPractise.setBackgroundResource(R.color.transparent);
                mActivity.btnFirmOfferTeaching.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineFirmOfferTeaching.setBackgroundResource(R.color.menu_text_orange);

                mActivity.courseCategory = QueryCoursesFunction.CATEGORY_BINDING_OFFER;
                mActivity.queryCourses();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mActivity, CourseVideoActivity.class);
        intent.putExtra(
                CourseVideoActivity.KEY_COURSE_ENTITY,
                mActivity.courseEntities.get(position - 1)
        );
        mActivity.startActivity(intent);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }
}

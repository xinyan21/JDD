package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.*;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.constants.msg.EngineMsgList;

/**
 * 描述：首页控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class IndexController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private IndexActivity mActivity;

    public IndexController(IndexActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof ListView) {
                    ((ListView) view).setOnItemClickListener(this);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mActivity.startActivity(new Intent(mActivity, LoginActivity.class));

                break;
            case R.id.ibtn_search:
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));

                break;
            case R.id.ll_long_short_course_entry:
                mActivity.startActivity(new Intent(mActivity, LongShortSchoolActivity.class));

                break;
            case R.id.btn_more_intelligences:
                mActivity.appControl.sendMessage(
                        new AppMsg("intelligence", String.valueOf(EngineMsgList.OPEN_MODULE), null));

                break;
            case R.id.btn_more_wealthy_thoughts:
                mActivity.appControl.sendMessage(
                        new AppMsg("think_tank", String.valueOf(EngineMsgList.OPEN_MODULE), null));

                break;
            case R.id.btn_more_profession_experiences:
                mActivity.appControl.sendMessage(
                        new AppMsg("think_tank", String.valueOf(EngineMsgList.OPEN_MODULE), null));

                break;
            case R.id.btn_more_long_short_art_of_war:
                moreCourses("多空兵法");

                break;
            case R.id.btn_more_long_short_battles:
                moreCourses("多空实战");

                break;
            case R.id.btn_more_talks:
                moreCourses("道德经杂谈");

                break;
            case R.id.iv_ad_one:
                moreCourses("实盘面授");

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_latest_intels:
                IntelligenceEntity intel
                        = (IntelligenceEntity) mActivity.latestIntelsEntities.get(position);
                Intent intelIntent = new Intent(mActivity, IntelligenceDetailActivity.class);
                intelIntent.putExtra(IntelligenceDetailActivity.KEY_INTENT, intel);
                mActivity.startActivity(intelIntent);

                break;
            case R.id.lv_latest_wealthy_thoughts:
                ExperienceEntity wealThought
                        = (ExperienceEntity) mActivity.latestWealthyThoughtsEntities.get(position);
                Intent wealThoughtIntent = new Intent(mActivity, ExperienceDetailActivity.class);
                wealThoughtIntent.putExtra(ExperienceDetailActivity.KEY_INTENT, wealThought);
                mActivity.startActivity(wealThoughtIntent);

                break;
            case R.id.lv_latest_profession_experiences:
                ExperienceEntity proExperience = (ExperienceEntity)
                        mActivity.latestProfessionExperiencesEntities.get(position);
                Intent proExperienceIntent = new Intent(mActivity, ExperienceDetailActivity.class);
                proExperienceIntent.putExtra(ExperienceDetailActivity.KEY_INTENT, proExperience);
                mActivity.startActivity(proExperienceIntent);

                break;
            case R.id.lv_stock_index_list:
                StockEntity entity = (StockEntity) (parent.getAdapter()).getItem(position);
                Intent intent = new Intent(mActivity, StockDetailsActivity.class);
                intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, entity);
                mActivity.startActivity(intent);

                break;
            case R.id.lv_latest_talks:
            case R.id.lv_long_short_art_of_war:
            case R.id.lv_latest_long_short_battles:
                Intent courseIntent = new Intent(mActivity, CourseVideoActivity.class);
                courseIntent.putExtra(
                        CourseVideoActivity.KEY_COURSE_ENTITY,
                        (CourseEntity) parent.getAdapter().getItem(position)
                );
                mActivity.startActivity(courseIntent);

                break;
        }
    }

    private void moreCourses(String category) {
        Intent intent = new Intent(mActivity, LongShortCoursesActivity.class);
        intent.putExtra(LongShortCoursesActivity.KEY_CATEGORY, category);
        mActivity.startActivity(intent);
    }
}

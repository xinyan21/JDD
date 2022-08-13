package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.*;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.constants.msg.EngineMsgList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：新首页控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class NewIndexController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {
    private NewIndexActivity mActivity;

    public NewIndexController(NewIndexActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemClickListener(this);
                }

                break;
            case ON_TOUCH:
                view.setOnTouchListener(this);

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
            case R.id.btn_start_morality:
                moreCourses("道德经杂谈");

                break;
            case R.id.ll_long_short_course_entry:
                moreCourses("多空兵法");

                break;
            case R.id.ll_long_short_battle_entry:
                moreCourses("多空实战");

                break;

            case R.id.btn_more_courses:
                startActivity(LongShortSchoolActivity.class);

                break;
            case R.id.btn_experiences_study:
                JSONObject expStudyParam = new JSONObject();
                try {
                    expStudyParam.put(
                            ThinkTankIndexActivity.KEY_ARTICLE_CATEGORY,
                            ThinkTankIndexActivity.CATEGORY_SKILL_STUDY
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.appControl.sendMessage(new AppMsg(
                        "think_tank", String.valueOf(EngineMsgList.OPEN_MODULE), null)
                );
                mActivity.appControl.sendMessage(new AppMsg(
                        "think_tank", "10000", expStudyParam)
                );

                break;
            case R.id.btn_more_experiences:
                mActivity.appControl.sendMessage(
                        new AppMsg("think_tank", String.valueOf(EngineMsgList.OPEN_MODULE), null));

                break;
            case R.id.btn_wealthy_tank:
                JSONObject wealthyParam = new JSONObject();
                try {
                    wealthyParam.put(
                            ThinkTankIndexActivity.KEY_ARTICLE_CATEGORY,
                            ThinkTankIndexActivity.CATEGORY_EXPERIENCES
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.appControl.sendMessage(new AppMsg(
                        "think_tank", String.valueOf(EngineMsgList.OPEN_MODULE), null)
                );
                mActivity.appControl.sendMessage(new AppMsg(
                        "think_tank", "10000", wealthyParam)
                );

                break;
            case R.id.btn_stock_intel:
            case R.id.btn_more_intelligences:
            case R.id.ll_stock_reports:
                mActivity.appControl.sendMessage(
                        new AppMsg("intelligence", String.valueOf(EngineMsgList.OPEN_MODULE), null));

                break;
            case R.id.rci_head_portrait:
                startActivity(MineActivity.class);

                break;
            case R.id.fl_search:
            case R.id.tv_fake_input:
                startActivity(SearchActivity.class);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StockEntity entity;
        Intent intent;
        switch (parent.getId()) {
            case R.id.gv_index:
                entity = (StockEntity) parent.getAdapter().getItem(position);
                intent = new Intent(mActivity, StockDetailsActivity.class);
                intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, entity);
                mActivity.startActivity(intent);

                break;
            case R.id.lv_latest_intels:
                IntelligenceEntity intel = (IntelligenceEntity) parent.getAdapter().getItem(position);
                Intent intelIntent = new Intent(mActivity, IntelligenceDetailActivity.class);
                intelIntent.putExtra(IntelligenceDetailActivity.KEY_INTENT, intel);
                mActivity.startActivity(intelIntent);
                break;
            case R.id.lv_latest_experiences:
                Intent expIntent = new Intent(mActivity, ExperienceDetailActivity.class);
                expIntent.putExtra(
                        ExperienceDetailActivity.KEY_INTENT,
                        (ExperienceEntity) parent.getAdapter().getItem(position)
                );
                mActivity.startActivity(expIntent);

                break;
        }
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(mActivity, cls);
        mActivity.startActivity(intent);
    }

    private void moreCourses(String category) {
        Intent intent = new Intent(mActivity, LongShortCoursesActivity.class);
        intent.putExtra(LongShortCoursesActivity.KEY_CATEGORY, category);
        mActivity.startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            startActivity(SearchActivity.class);

            return true;
        }
        return false;
    }
}

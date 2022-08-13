package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.CoursePPTActivity;
import com.jdd.android.jdd.ui.CourseVideoActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：课程详情控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-04
 * @last_edit 2016-03-04
 * @since 1.0
 */
public class CourseDetailController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private CourseVideoActivity mActivity;

    public CourseDetailController(CourseVideoActivity activity) {
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
            case R.id.ibtn_back:
                mActivity.finish();

                break;
            case R.id.btn_play:
                mActivity.onPlay();

                break;
            case R.id.btn_confirm:
                mActivity.isPlayWithoutWifi = true;
                mActivity.onPlay();
                mActivity.dialogConfirmToPlay.dismiss();

                break;
            case R.id.btn_cancel:
                mActivity.dialogConfirmToPlay.dismiss();

                break;
            case R.id.ibtn_to_ppt:
                Intent intent = new Intent(mActivity, CoursePPTActivity.class);
                intent.putExtra(CoursePPTActivity.KEY_COURSE_ENTITY, mActivity.courseEntity);
                mActivity.startActivity(intent);

                break;
            case R.id.btn_about:
                if (mActivity.tvAbout.getVisibility() == View.VISIBLE) {
                    mActivity.tvAbout.setVisibility(View.GONE);
                } else {
                    mActivity.tvAbout.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}

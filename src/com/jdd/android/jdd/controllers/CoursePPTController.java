package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.CoursePPTActivity;
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
public class CoursePPTController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private CoursePPTActivity mActivity;

    public CoursePPTController(CoursePPTActivity activity) {
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
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}

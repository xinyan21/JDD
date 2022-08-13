package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.CourseCategoryEntity;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.ui.LongShortCoursesActivity;
import com.jdd.android.jdd.ui.LongShortSchoolActivity;
import com.jdd.android.jdd.ui.SearchActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-10-01
 * @since 1.0
 */
public class LongShortSchoolController extends ListenerControllerAdapter
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    private LongShortSchoolActivity mActivity;

    public LongShortSchoolController(LongShortSchoolActivity activity) {
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(mActivity, LongShortCoursesActivity.class);
        intent.putExtra(LongShortCoursesActivity.KEY_CATEGORY,
                ((CourseCategoryEntity) adapterView.getAdapter().getItem(i)).getCategoryName());
        mActivity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                mActivity.finish();

                break;
            case R.id.ibtn_search:
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));

                break;
        }
    }
}

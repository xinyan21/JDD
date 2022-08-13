package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.SelectableEntity;
import com.jdd.android.jdd.ui.ExperienceFilterActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.List;

/**
 * 描述：智文筛选控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class ExperienceFilterController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private ExperienceFilterActivity mActivity;

    public ExperienceFilterController(ExperienceFilterActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof GridView) {
                    ((AdapterView) view).setOnItemClickListener(this);
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
            case R.id.btn_confirm:
                mActivity.confirmFiltersSelected();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setSelectStateByDefaultRule(mActivity.industryEntities, position);
        mActivity.adapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch ( buttonView.getId()){
            case R.id.cb_select_all:
                if (isChecked) {
                    mActivity.optAll(false);
                } else {
                    mActivity.optAll(true);
                }

                break;
        }
    }

    private void setSelectStateByDefaultRule(List<SelectableEntity> list, int position) {
        SelectableEntity entity = list.get(position);
        if (entity.isSelected()) {
            entity.setSelected(false);
        } else {
            entity.setSelected(true);
        }
        //第一个值为不限，如果选择了不限，那么后面的选中全部清除
        if (0 == position) {
            for (int i = 1; i < list.size(); i++) {
                list.get(i).setSelected(false);
            }
        } else {
            list.get(0).setSelected(false);
        }
    }
}

package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.SelectableEntity;
import com.jdd.android.jdd.ui.IntelligenceFilterActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.adf.log.Logger;

import java.util.List;

/**
 * 描述：情报筛选控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class IntelligenceFilterController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private IntelligenceFilterActivity mActivity;
    private boolean mIsMultiChoice = false;

    public IntelligenceFilterController(IntelligenceFilterActivity activity) {
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
                } else if (view instanceof GridView) {
                    ((GridView) view).setOnItemClickListener(this);
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
            case R.id.btn_reset:
                mActivity.resetSelectState();

                break;
            case R.id.btn_confirm:
                mActivity.confirmFiltersSelected();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SelectableEntity entity = null;
        switch (parent.getId()) {
            case R.id.lv_father_filter_category:
                entity = mActivity.fatherCategories.get(position);
                if (IntelligenceFilterActivity.CATEGORIES_SINGLE_CHOICE.contains(entity.getData())) {
                    mIsMultiChoice = false;
                } else {
                    mIsMultiChoice = true;
                }
                for (int i = 0; i < mActivity.fatherCategories.size(); i++) {
                    SelectableEntity item = mActivity.fatherCategories.get(i);
                    if (i == position) {
                        item.setSelected(true);
                    } else {
                        item.setSelected(false);
                    }
                }
                mActivity.fatherCategoriesAdapter.notifyDataSetChanged();
                mActivity.childCategories.get(mActivity.lastSelectedItemPosition).clear();
                //如果是用表格显示，则切换到表格，在清除缓存列表数据之前，需要把修改还原到源数组里面
                if (IntelligenceFilterActivity.CATEGORIES_USE_GRID_TO_SHOW.contains(entity.getData())) {
                    if (View.VISIBLE == mActivity.gvChildCategory.getVisibility() &&
                            mActivity.childCategoryForGrid.size() > 0) {
                        mActivity.childCategories.get(mActivity.lastSelectedItemPosition)
                                .addAll(mActivity.childCategoryForGrid);
                    } else {
                        mActivity.childCategories.get(mActivity.lastSelectedItemPosition)
                                .addAll(mActivity.childCategoryForList);
                    }
                    mActivity.childCategoryForGrid.clear();
                    mActivity.childCategoryForGrid.addAll(mActivity.childCategories.get(position));
                    mActivity.gvChildCategory.setVisibility(View.VISIBLE);
                    mActivity.lvChildCategory.setVisibility(View.GONE);
                    mActivity.childCategoriesGridAdapter.notifyDataSetChanged();
                } else {
                    if (View.VISIBLE == mActivity.lvChildCategory.getVisibility() &&
                            mActivity.childCategoryForList.size() > 0) {
                        mActivity.childCategories.get(mActivity.lastSelectedItemPosition)
                                .addAll(mActivity.childCategoryForList);
                    } else {
                        mActivity.childCategories.get(mActivity.lastSelectedItemPosition)
                                .addAll(mActivity.childCategoryForGrid);
                    }
                    mActivity.childCategoryForList.clear();
                    mActivity.childCategoryForList.addAll(mActivity.childCategories.get(position));
                    mActivity.gvChildCategory.setVisibility(View.GONE);
                    mActivity.lvChildCategory.setVisibility(View.VISIBLE);
                    mActivity.childCategoriesListAdapter.notifyDataSetChanged();
                }
                mActivity.lastSelectedItemPosition = position;

                break;
            case R.id.lv_child_filter_category:
                setSelectStateByDefaultRule(mActivity.childCategoryForList, position);
                mActivity.childCategoriesListAdapter.notifyDataSetChanged();

                break;
            case R.id.gv_child_filter_category:
                setSelectStateByDefaultRule(mActivity.childCategoryForGrid, position);
                mActivity.childCategoriesGridAdapter.notifyDataSetChanged();

                break;
        }
    }

    private void setSelectStateByDefaultRule(List<SelectableEntity> list, int position) {
        SelectableEntity entity = list.get(position);
        if (entity.isSelected()) {
            // 如果是单选，重复点击一项，不取消选中状态
            if (!mIsMultiChoice) {
                entity.setSelected(false);
            }
        } else {
            entity.setSelected(true);
            //如果是单选，那么就要取消其它的选中状态
            if (!mIsMultiChoice) {
                for (int i = 0; i < list.size(); i++) {
                    if (i != position) {
                        list.get(i).setSelected(false);
                    }
                }
            }
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

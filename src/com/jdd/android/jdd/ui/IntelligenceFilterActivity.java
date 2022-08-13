package com.jdd.android.jdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.SearchFilterAdapter;
import com.jdd.android.jdd.adapters.FatherIntelFilterCategoriesAdapter;
import com.jdd.android.jdd.controllers.IntelligenceFilterController;
import com.jdd.android.jdd.entities.SelectableEntity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：情报筛选
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class IntelligenceFilterActivity extends TKActivity {
    public static final String CATEGORIES_USE_GRID_TO_SHOW = "行业|题材|风格|地域";
    public static final String CATEGORIES_SINGLE_CHOICE = "周期";
    public static final String KEY_RESULT_DURATIONS = "DURATIONS";
    public static final String KEY_RESULT_FRACTIONS = "FRACTIONS";
    public static final String KEY_RESULT_INDUSTRIES = "INDUSTRIES";
    public static final String KEY_RESULT_LONG_SHORT_POSITIONS = "LONG_SHORT_POSITIONS";
    public static final String KEY_RESULT_THEMES = "THEMES";
    public static final String KEY_RESULT_INVEST_STYLES = "STYLES";
    public static final String KEY_RESULT_AREAS = "AREAS";

    public int lastSelectedItemPosition = 0;
    public List<SelectableEntity> fatherCategories;
    public List<List<SelectableEntity>> childCategories;        //所有子分类源数组
    //当前选中的子分类，做缓存用，切换的时候数据会被清空已存放选中的子分类列表
    public List<SelectableEntity> childCategoryForList, childCategoryForGrid;
    public FatherIntelFilterCategoriesAdapter fatherCategoriesAdapter;
    public SearchFilterAdapter childCategoriesListAdapter, childCategoriesGridAdapter;
    public ListView lvFatherCategory;
    public ListView lvChildCategory;
    public GridView gvChildCategory;

    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnReset, mBtnConfirm;

    private IntelligenceFilterController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intel_filter);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        lvFatherCategory = (ListView) findViewById(R.id.lv_father_filter_category);
        lvChildCategory = (ListView) findViewById(R.id.lv_child_filter_category);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnReset = (Button) findViewById(R.id.btn_reset);
        gvChildCategory = (GridView) findViewById(R.id.gv_child_filter_category);
    }

    @Override
    protected void setListeners() {
        mController = new IntelligenceFilterController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnConfirm, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnReset, mController);
        registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, lvChildCategory, mController);
        registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, lvFatherCategory, mController);
        registerListener(
                ListenerControllerAdapter.ON_ITEM_CLICK, gvChildCategory, mController);
    }

    @Override
    protected void initData() {
        String[] mStrArrFatherCategories = new String[]{
                "周期", "派别", "行业", "多空", "题材", "风格", "地域"
        };

        String[] effectiveDurations = getResources().getStringArray(R.array.effective_durations);
        String[] fractions = getResources().getStringArray(R.array.fractions);
        String[] industries = getResources().getStringArray(R.array.industries);
        String[] longShortPositions = getResources().getStringArray(R.array.long_short_positions);
        String[] themes = getResources().getStringArray(R.array.themes);
        String[] investStyle = getResources().getStringArray(R.array.invest_styles);
        String[] areas = getResources().getStringArray(R.array.areas);

        fatherCategories = packCategoriesToEntity(mStrArrFatherCategories);
        childCategories = new ArrayList<>();
        childCategories.add(packCategoriesToEntity(effectiveDurations));
        childCategories.add(packCategoriesToEntity(fractions));
        childCategories.add(packCategoriesToEntity(industries));
        childCategories.add(packCategoriesToEntity(longShortPositions));
        childCategories.add(packCategoriesToEntity(themes));
        childCategories.add(packCategoriesToEntity(investStyle));
        childCategories.add(packCategoriesToEntity(areas));
        setDefaultSelected();

        childCategoryForList = new ArrayList<>();
        childCategoryForList.addAll(childCategories.get(0));
        childCategoriesListAdapter = new SearchFilterAdapter(this, childCategoryForList);
        childCategoryForGrid = new ArrayList<>();
        childCategoriesGridAdapter = new SearchFilterAdapter(this, childCategoryForGrid);
        fatherCategoriesAdapter = new FatherIntelFilterCategoriesAdapter(this, fatherCategories);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("情报筛选");
        lvChildCategory.setAdapter(childCategoriesListAdapter);
        lvFatherCategory.setAdapter(fatherCategoriesAdapter);
        gvChildCategory.setAdapter(childCategoriesGridAdapter);
    }

    /**
     * 设置默认选中
     */
    private void setDefaultSelected() {
        fatherCategories.get(0).setSelected(true);
        for (List<SelectableEntity> item : childCategories) {
            item.get(0).setSelected(true);
        }
    }

    private List<SelectableEntity> packCategoriesToEntity(String[] categories) {
        List<SelectableEntity> entities = new ArrayList<>();
        SelectableEntity entity = null;
        for (String item : categories) {
            entity = new SelectableEntity();
            entity.setData(item);
            entity.setSelected(false);

            entities.add(entity);
        }

        return entities;
    }

    public void resetSelectState() {
        for (List<SelectableEntity> item : childCategories) {
            for (SelectableEntity entity : item) {
                entity.setSelected(false);
            }
        }
        setDefaultSelected();
        childCategoriesGridAdapter.notifyDataSetChanged();
        childCategoriesListAdapter.notifyDataSetChanged();
    }

    public void confirmFiltersSelected() {
        Intent intent = new Intent();
        String[] keyArray = new String[]{
                KEY_RESULT_DURATIONS, KEY_RESULT_FRACTIONS,
                KEY_RESULT_INDUSTRIES, KEY_RESULT_LONG_SHORT_POSITIONS,
                KEY_RESULT_THEMES, KEY_RESULT_INVEST_STYLES, KEY_RESULT_AREAS,
        };
        for (int i = 0; i < childCategories.size(); i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < childCategories.get(i).size(); j++) {
                SelectableEntity entity = childCategories.get(i).get(j);
                if (j > 0 && sb.length() > 0 && entity.isSelected()) {
                    sb.append(",");
                }
                if (entity.isSelected()) {
                    if (0 == j) {
                        sb.append("");
                        break;
                    } else {
                        if (0 == i) {
                            sb.append(entity.getData().replace("天", ""));
                        } else {
                            sb.append(entity.getData());
                        }
                    }
                }
            }
            intent.putExtra(keyArray[i], sb.toString());
        }
        setResult(1, intent);
        finish();
    }
}

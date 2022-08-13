package com.jdd.android.jdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.adapters.SearchFilterAdapter;
import com.jdd.android.jdd.controllers.ExperienceFilterController;
import com.jdd.android.jdd.entities.SelectableEntity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：智文筛选
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class ExperienceFilterActivity extends TKActivity {
    public static final String KEY_INDUSTRY_RESULT = "INDUSTRY";
    public static final String KEY_CATEGORY_RESULT = "CATEGORY";

    public List<SelectableEntity> industryEntities;
    public SearchFilterAdapter adapter;

    private ImageButton mIBtnBack;
    private TextView mTVTopTitle;
    private RadioButton mRBProfessionExperience, mRBStartUpThought, mRBNoLimit;
    private CheckBox mCBSelectAll;
    private GridView mGVIndustries;
    private Button mBtnConfirm;

    private ExperienceFilterController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_filter);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTVTopTitle = (TextView) findViewById(R.id.tv_activity_title);
        mRBProfessionExperience = (RadioButton) findViewById(R.id.rb_profession_experience);
        mRBStartUpThought = (RadioButton) findViewById(R.id.rb_start_up_thought);
        mRBNoLimit = (RadioButton) findViewById(R.id.rb_no_limit);
        mCBSelectAll = (CheckBox) findViewById(R.id.cb_select_all);
        mGVIndustries = (GridView) findViewById(R.id.gv_industries);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    protected void setListeners() {
        mController = new ExperienceFilterController(this);
        registerListener(ListenerControllerAdapter.ON_CHECK, mCBSelectAll, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnConfirm, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mGVIndustries, mController);
    }

    @Override
    protected void initData() {
        String[] industries = getResources().getStringArray(R.array.industries);
        if (null == industries) {
            return;
        }
        industryEntities = new ArrayList<>();
        SelectableEntity entity = null;
        for (int i = 0; i < industries.length; i++) {
            entity = new SelectableEntity();
            entity.setSelected(false);
            entity.setData(industries[i]);

            industryEntities.add(entity);
        }
        //第一个为不限，默认选择
        industryEntities.get(0).setSelected(true);
        adapter = new SearchFilterAdapter(this, industryEntities);
    }

    @Override
    protected void initViews() {
        mTVTopTitle.setText("智文筛选");
        if (null != adapter) {
            mGVIndustries.setAdapter(adapter);
        }
    }

    public void optAll(boolean isSelected) {
        for (SelectableEntity entity : industryEntities) {
            entity.setSelected(isSelected);
        }
        adapter.notifyDataSetChanged();
    }

    public void confirmFiltersSelected() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < industryEntities.size(); i++) {
            SelectableEntity entity = industryEntities.get(i);
            if (i > 0 && sb.length() > 0 && entity.isSelected()) {
                sb.append(",");
            }
            if (entity.isSelected()) {
                if (0 == i) {
                    sb.append("");
                    break;
                } else {
                    sb.append(entity.getData());
                }
            }
        }
        String category = "";
        if (mRBProfessionExperience.isChecked()) {
            category = mRBProfessionExperience.getText().toString();
        } else if (mRBStartUpThought.isChecked()) {
            category = mRBStartUpThought.getText().toString();
        } else {
            category = "";
        }
        Intent intent = new Intent();
        intent.putExtra(KEY_INDUSTRY_RESULT, sb.toString());
        intent.putExtra(KEY_CATEGORY_RESULT, category);
        setResult(1, intent);
        finish();
    }
}

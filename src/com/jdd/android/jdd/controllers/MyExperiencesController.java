package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.ExperienceDetailActivity;
import com.jdd.android.jdd.ui.MyExperiencesActivity;
import com.jdd.android.jdd.ui.SearchActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：我发布的智文控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class MyExperiencesController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    public static final int ON_PULL_REFRESH = 88888;
    private MyExperiencesActivity mActivity;

    public MyExperiencesController(MyExperiencesActivity activity) {
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
            case ON_PULL_REFRESH:
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
                mActivity.searchKey = mActivity.etSearchKey.getText().toString();
                mActivity.currentPage = 1;
                mActivity.queryMyExperiences(true);

                break;
            case R.id.btn_wealthy_thoughts:
                mActivity.btnWealthyThoughts.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineWealthyThoughts.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnProfessionExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineProfessionExperiences.setBackgroundResource(R.color.transparent);

                break;
            case R.id.btn_profession_experiences:
                mActivity.btnWealthyThoughts.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineWealthyThoughts.setBackgroundResource(R.color.transparent);
                mActivity.btnProfessionExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineProfessionExperiences.setBackgroundResource(R.color.menu_text_orange);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mActivity, ExperienceDetailActivity.class);
        intent.putExtra(
                ExperienceDetailActivity.KEY_INTENT, mActivity.myExperienceEntities.get(position - 1));
        mActivity.startActivity(intent);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mActivity.currentPage += 1;
        mActivity.queryMyExperiences(false);
    }
}

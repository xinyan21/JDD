package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.RewardActivity;
import com.jdd.android.jdd.ui.UserIndexActivity;
import com.jdd.android.jdd.ui.UserProfileActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：用户主页控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-03
 * @last_edit 2016-01-03
 * @since 1.0
 */
public class UserIndexController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    private UserIndexActivity mActivity;

    public UserIndexController(UserIndexActivity activity) {
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
            case ThinkTankController.ON_PULL_REFRESH:
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
            case R.id.btn_follow:
                if (mActivity.isCurrentAnalystFollowed) {
                    mActivity.cancelFollow(-1);
                } else {
                    mActivity.follow(-1);
                }

                break;
            case R.id.btn_reward:
                Intent rewardIntent = new Intent(mActivity, RewardActivity.class);
                rewardIntent.putExtra(
                        RewardActivity.KEY_USER_ID, mActivity.userEntity.getUserId()
                );
                mActivity.startActivity(rewardIntent);

                break;
            case R.id.btn_more_user_info:
                Intent profileIntent = new Intent(mActivity, UserProfileActivity.class);
                profileIntent.putExtra(UserProfileActivity.KEY_USER, mActivity.userEntity);
                mActivity.startActivity(profileIntent);

                break;
            case R.id.btn_follows:
                mActivity.btnExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineExperience.setBackgroundResource(R.color.transparent);
                mActivity.btnIntelligences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineIntelligence.setBackgroundResource(R.color.transparent);
                mActivity.btnFollows.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineFollows.setBackgroundResource(R.color.menu_text_orange);

                mActivity.setSelectedState(UserIndexActivity.LIST_FOLLOW);

                break;
            case R.id.btn_experiences_study:
                mActivity.btnExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineExperience.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnIntelligences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineIntelligence.setBackgroundResource(R.color.transparent);
                mActivity.btnFollows.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineFollows.setBackgroundResource(R.color.transparent);

                mActivity.setSelectedState(UserIndexActivity.LIST_EXP);

                break;
            case R.id.btn_intelligences:
                mActivity.btnExperiences.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineExperience.setBackgroundResource(R.color.transparent);
                mActivity.btnIntelligences.setTextColor(
                        mActivity.getResources().getColor(R.color.text_black));
                mActivity.boldLineIntelligence.setBackgroundResource(R.color.menu_text_orange);
                mActivity.btnFollows.setTextColor(
                        mActivity.getResources().getColor(R.color.menu_text_gray));
                mActivity.boldLineFollows.setBackgroundResource(R.color.transparent);

                mActivity.setSelectedState(UserIndexActivity.LIST_INTEL);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mActivity.onItemClick(position);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mActivity.loadMore();
    }
}

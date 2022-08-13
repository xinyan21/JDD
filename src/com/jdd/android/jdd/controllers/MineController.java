package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.*;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：我控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class MineController extends ListenerControllerAdapter implements View.OnClickListener {
    private MineActivity mActivity;

    public MineController(MineActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));

                break;
            case R.id.ll_my_experiences:
            case R.id.rl_my_experiences:
                startActivity(MyExperiencesActivity.class);

                break;
            case R.id.ll_my_fans:


                break;
            case R.id.ll_my_intels:
            case R.id.rl_my_intels:
                startActivity(MyIntelligencesActivity.class);

                break;
            case R.id.rl_my_follows:
                startActivity(MyFollowsActivity.class);

                break;
            case R.id.rl_edit_login_pwd:
                startActivity(ResetLoginPwdActivity.class);

                break;
            case R.id.rl_edit_profile:
                Intent intent = new Intent(mActivity, UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.KEY_USER, mActivity.userEntity);
                mActivity.startActivity(intent);

                break;
            case R.id.rl_bought_courses:
                startActivity(BoughtCoursesActivity.class);

                break;
            case R.id.rl_send_coin_to_friend:
                startActivity(SendCoinToFriendActivity.class);

                break;
            case R.id.rl_feed_back:
                startActivity(FeedBackActivity.class);

                break;
            case R.id.btn_acc_security:
                startActivity(AccountSecurityActivity.class);

                break;
            case R.id.btn_recharge:
                startActivity(RechargeActivity.class);

                break;
            case R.id.ibtn_search:
                startActivity(SearchActivity.class);

                break;
            case R.id.rl_favorites:
                startActivity(FavoritesActivity.class);

                break;
            case R.id.btn_logout:
                mActivity.logout();

                break;
            case R.id.btn_buy_three_month:
                mActivity.buyVIP("2880");

                break;
            case R.id.btn_buy_six_month:
                mActivity.buyVIP("5180");

                break;
            case R.id.btn_buy_twelve_month:
                mActivity.buyVIP("9880");

                break;
            case R.id.tv_out_of_date_hint:
                mActivity.showOrHideBuyVIPWindow();

                break;
        }
    }

    private void startActivity(Class cls) {
        mActivity.startActivity(new Intent(mActivity, cls));
    }
}

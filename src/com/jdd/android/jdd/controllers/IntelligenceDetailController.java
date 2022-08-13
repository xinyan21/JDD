package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.CommentArticleFunction;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.*;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：情报详情界面监听器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-10-01
 * @since 1.0
 */
public class IntelligenceDetailController extends ListenerControllerAdapter
        implements View.OnClickListener {
    private IntelligenceDetailActivity mActivity;

    public IntelligenceDetailController(IntelligenceDetailActivity activity) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                mActivity.finish();

                break;
            case R.id.tv_about_author:
                Intent aboutIntent = new Intent(mActivity, UserIndexActivity.class);
                aboutIntent.putExtra(
                        UserIndexActivity.KEY_IN_PARAM, mActivity.articleEntity.getAuthorId()
                );
                mActivity.startActivity(aboutIntent);

                break;
            case R.id.ibtn_reward:
                Intent rewardIntent = new Intent(mActivity, RewardActivity.class);
                rewardIntent.putExtra(
                        RewardActivity.KEY_USER_ID, mActivity.articleEntity.getAuthorId()
                );
                mActivity.startActivity(rewardIntent);

                break;
            case R.id.btn_buy:
                mActivity.buyIntel();

                break;
            case R.id.ibtn_collect:
                mActivity.collectIntel();

                break;
            case R.id.ibtn_report:
                Intent reportIntent = new Intent(mActivity, FeedBackActivity.class);
                mActivity.startActivity(reportIntent);

                break;
            case R.id.btn_check_price:
                if (null == mActivity.stockEntity) {
                    PopUpComponentUtil.showShortToast(mActivity, "很抱歉，未查询到该股票");
                    return;
                }
                Intent intent = new Intent(mActivity, StockDetailsActivity.class);
                intent.putExtra(StockDetailsActivity.KEY_IN_PARAM, mActivity.stockEntity);
                mActivity.startActivity(intent);

                break;
            case R.id.ll_great:
                mActivity.commentIntel(CommentArticleFunction.COMMENT_TYPE_GREAT);

                break;
            case R.id.ll_good:
                mActivity.commentIntel(CommentArticleFunction.COMMENT_TYPE_GOOD);

                break;
            case R.id.ll_ordinary:
                mActivity.commentIntel(CommentArticleFunction.COMMENT_TYPE_ORDINARY);

                break;
            case R.id.ll_bad:
                mActivity.commentIntel(CommentArticleFunction.COMMENT_TYPE_BAD);

                break;
            case R.id.ll_terrible:
                mActivity.commentIntel(CommentArticleFunction.COMMENT_TYPE_TERRIBLE);

                break;
        }
    }
}

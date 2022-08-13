package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.CommentArticleFunction;
import com.jdd.android.jdd.ui.FeedBackActivity;
import com.jdd.android.jdd.ui.UserIndexActivity;
import com.jdd.android.jdd.ui.ExperienceDetailActivity;
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
public class ExperienceDetailController extends ListenerControllerAdapter
        implements View.OnClickListener {
    private ExperienceDetailActivity mActivity;

    public ExperienceDetailController(ExperienceDetailActivity activity) {
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
            case R.id.btn_about_author:
                Intent aboutIntent = new Intent(mActivity, UserIndexActivity.class);
                aboutIntent.putExtra(
                        UserIndexActivity.KEY_IN_PARAM, mActivity.articleEntity.getAuthorId()
                );
                mActivity.startActivity(aboutIntent);

                break;
            case R.id.ibtn_collect:
                mActivity.collectIntel();

                break;
            case R.id.ibtn_report:
                Intent reportIntent = new Intent(mActivity, FeedBackActivity.class);
                mActivity.startActivity(reportIntent);

                break;
            case R.id.ll_great:
                mActivity.commentArticle(CommentArticleFunction.COMMENT_TYPE_GREAT);

                break;
            case R.id.ll_good:
                mActivity.commentArticle(CommentArticleFunction.COMMENT_TYPE_GOOD);

                break;
            case R.id.ll_ordinary:
                mActivity.commentArticle(CommentArticleFunction.COMMENT_TYPE_ORDINARY);
        }
    }
}

package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.sidebar.SideBar;
import com.jdd.android.jdd.ui.MyFollowsActivity;
import com.jdd.android.jdd.ui.UserIndexActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：我关注的人控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class MyFollowsController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener,
        SideBar.OnTouchingLetterChangedListener, PullToRefreshBase.OnRefreshListener {
    private MyFollowsActivity mActivity;

    public MyFollowsController(MyFollowsActivity activity) {
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
            case R.id.ibtn_search:


                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mActivity, UserIndexActivity.class);
        intent.putExtra(
                UserIndexActivity.KEY_IN_PARAM,
                mActivity.followsEntities.get(position - 1).getUserId()
        );
        mActivity.startActivity(intent);
    }

    @Override
    public void onTouchingLetterChanged(String s) {

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        mActivity.currentFollowsPage += 1;
        mActivity.queryFollows();
    }
}

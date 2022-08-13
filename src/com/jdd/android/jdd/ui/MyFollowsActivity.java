package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.MyFollowsAdapter;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.QueryFollowsFunction;
import com.jdd.android.jdd.controllers.MyFollowsController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.QueryFollowsRequest;
import com.jdd.android.jdd.sidebar.SideBar;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：我关注的人列表
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-05
 * @last_edit 2016-01-05
 * @since 1.0
 */
public class MyFollowsActivity extends TKActivity implements IQueryServer {
    public List<UserEntity> followsEntities;
    public short currentFollowsPage = 1;
    public EditText etSearchKey;
    private ImageButton mIBtnSearch;
    private TextView mTVTitle;
    private ImageButton mIBtnBack;
    private PullToRefreshListView mLVMyFollows;
    private SideBar mSBIndex;
    private TextView mTVPopWindowForSelectedCharacter;
    private Dialog mDialogLoading, mDialogCannotCancel;;

    private MyFollowsController mController;
    private MyFollowsAdapter mFollowsAdapter;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private int mCurrentFollowPositionProcessing = 0;     //当前正在处理中的关注人列表索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follows);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryFollows();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        etSearchKey = (EditText) findViewById(R.id.et_search_key);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mLVMyFollows = (PullToRefreshListView) findViewById(R.id.lv_my_follows);
        mSBIndex = (SideBar) findViewById(R.id.sb_name_index);
        mTVPopWindowForSelectedCharacter
                = (TextView) findViewById(R.id.tv_pop_window_for_selected_character);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(this, "加载中，请稍候...", true, true);
        mDialogCannotCancel
                = PopUpComponentUtil.createLoadingProgressDialog(this, "处理中，请稍候...", false, true);
    }

    @Override
    protected void setListeners() {
        mController = new MyFollowsController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVMyFollows, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVMyFollows, mController);
    }

    @Override
    protected void initData() {
        followsEntities = new ArrayList<>();
        mFollowsAdapter = new MyFollowsAdapter(this, followsEntities);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("我关注的人");
        //设置只支持上拉加载更多
        mLVMyFollows.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLVMyFollows.setAdapter(mFollowsAdapter);
    }

    public void queryFollows() {
        Parameter followParam = new Parameter();
        followParam.addParameter(
                QueryFollowsFunction.IN_PAGE, String.valueOf(currentFollowsPage)
        );
        followParam.addParameter(QueryFollowsFunction.IN_USER_ID, "");
        startTask(new QueryFollowsRequest(
                TaskId.TASK_ID_FIRST, followParam, new QueryAction(this)
        ));
        mDialogLoading.show();
    }

    public void follow(int position) {
        mCurrentFollowPositionProcessing = position;
        Parameter param = new Parameter();
        param.addParameter("cId", String.valueOf(followsEntities.get(position).getUserId()));
        startTask(new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_FOLLOW"),
                TaskId.TASK_ID_FOURTH, param, new QueryAction(this)
        ));
        mDialogCannotCancel.show();
    }

    public void cancelFollow(int position) {
        mCurrentFollowPositionProcessing = position;
        Parameter param = new Parameter();
        param.addParameter("cId", String.valueOf(followsEntities.get(position).getUserId()));
        startTask(new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_CANCEL_FOLLOW"),
                TaskId.TASK_ID_FIFTH, param, new QueryAction(this)
        ));
        mDialogCannotCancel.show();
    }

    private void onLoadingComplete() {
        if (mDialogCannotCancel.isShowing()) {
            mDialogCannotCancel.dismiss();
        }
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
        mLVMyFollows.onRefreshComplete();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        onLoadingComplete();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                final ArrayList<UserEntity> users = (ArrayList<UserEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == users || users.size() < 1) {
                    if (1 != currentFollowsPage) {
                        Toast.makeText(this, "没有更多关注啦", Toast.LENGTH_SHORT).show();
                    } else {
                        followsEntities.clear();
                    }
                    return;
                }
                followsEntities.addAll(users);
                users.clear();
                mFollowsAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_FOURTH:
                followsEntities.get(mCurrentFollowPositionProcessing).setFollowed(true);
                mFollowsAdapter.notifyDataSetChanged();

                break;
            case TaskId.TASK_ID_FIFTH:
                followsEntities.get(mCurrentFollowPositionProcessing).setFollowed(false);
                mFollowsAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        onLoadingComplete();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        onLoadingComplete();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        onLoadingComplete();
    }
}

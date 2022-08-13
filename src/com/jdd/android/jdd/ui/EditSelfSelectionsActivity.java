package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.EditSelfSelectionAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.controllers.EditSelfSelectionsController;
import com.jdd.android.jdd.db.SelfSelectionStocksManager;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.DeleteSelfSelectionRequest;
import com.jdd.android.jdd.requests.SaveSelfSelectionEditRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.mobeta.android.dslv.DragSortListView;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.List;

/**
 * 描述：
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-16
 * @last_edit 2016-03-16
 * @since 1.0
 */
public class EditSelfSelectionsActivity extends TKActivity implements IQueryServer {
    public List<StockEntity> stockEntities;
    public EditSelfSelectionAdapter adapter;
    public Dialog dialogSaving;

    private ImageButton mIBtnBack;
    private TextView mTVTopTitle;
    private DragSortListView mDragSortListView;
    private ImageButton mIBtnDelete;
    private View mDeleteBg;

    private EditSelfSelectionsController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_self_selection);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTVTopTitle = (TextView) findViewById(R.id.tv_activity_title);
        mDragSortListView = (DragSortListView) findViewById(R.id.dslv_self_selections);
        mIBtnDelete = (ImageButton) findViewById(R.id.ibtn_delete);
        dialogSaving = PopUpComponentUtil.createLoadingProgressDialog(
                this, "处理中，请稍候...", false, true
        );
        mDeleteBg = findViewById(R.id.ll_delete_stocks);
    }

    @Override
    protected void setListeners() {
        mController = new EditSelfSelectionsController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnDelete, mController);
        registerListener(EditSelfSelectionsController.ON_DROP, mDragSortListView, mController);
    }

    @Override
    protected void initData() {
        UserEntity loginUser = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
        long userId = SelfSelectionStocksFragment.sDefaultUserID;
        if (null != loginUser) {
            userId = loginUser.getUserId();
        }
        final long finalUserId = userId;
        startTask(new CallBack.SchedulerCallBack() {
            @Override
            public void handler(MessageAction messageAction) {
                stockEntities = SelfSelectionStocksManager.getInstance(
                        EditSelfSelectionsActivity.this
                ).queryUserStocks(finalUserId);
                if (null != stockEntities) {
                    adapter = new EditSelfSelectionAdapter(
                            EditSelfSelectionsActivity.this, stockEntities, mDeleteBg
                    );
                }
                messageAction.transferAction(RESULT_OK, null, new CallBack.MessageCallBack() {
                    @Override
                    public void handler(Context context, int i, Bundle bundle) {
                        if (null != adapter) {
                            mDragSortListView.setAdapter(adapter);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initViews() {
        mTVTopTitle.setText("自选股编辑");
    }

    public void saveEdit() {
        SaveSelfSelectionEditRequest request = new SaveSelfSelectionEditRequest(
                this, TaskId.TASK_ID_FIRST, stockEntities, new QueryAction(this)
        );
        startTask(request);
        dialogSaving.show();
    }

    public void delete(List<StockEntity> entities) {
        DeleteSelfSelectionRequest request = new DeleteSelfSelectionRequest(
                this, TaskId.TASK_ID_SECOND, entities, new QueryAction(this)
        );
        startTask(request);
        dialogSaving.show();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                dialogSaving.dismiss();
                finish();

                break;
            case TaskId.TASK_ID_SECOND:
                dialogSaving.dismiss();
                adapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {

    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {

    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dialogSaving.dismiss();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                PopUpComponentUtil.showShortToast(this, "很抱歉，保存编辑失败，请您联系客服");
                finish();

                break;
            case TaskId.TASK_ID_SECOND:
                PopUpComponentUtil.showShortToast(this, "很抱歉，删除自选失败，请您联系客服");

                break;
        }
    }
}

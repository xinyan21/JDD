package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.adapters.NewMyArticlesAdapter;
import com.jdd.android.jdd.adapters.UserIndexExperiencesAdapter;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.ArticleFunction;
import com.jdd.android.jdd.constants.function.QueryMyIntelsFunction;
import com.jdd.android.jdd.controllers.MyExperiencesController;
import com.jdd.android.jdd.controllers.ThinkTankController;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryMyExperiencesRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：我发布的智文
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class MyExperiencesActivity extends TKActivity implements IQueryServer {
    public int currentPage = 1;
    public String searchKey = "";
    public List<ArticleEntity> myExperienceEntities;
    public EditText etSearchKey;
    public Button btnWealthyThoughts, btnProfessionExperiences;
    public View boldLineWealthyThoughts, boldLineProfessionExperiences;
    private ImageButton mIBtnSearch;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private PullToRefreshListView mLVExperiences;
    private Dialog mLoadingDialog;

    private MyExperiencesController mController;
    private NewMyArticlesAdapter mAdapter;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_experiences);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etSearchKey = (EditText) findViewById(R.id.et_search_key);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        boldLineWealthyThoughts = findViewById(R.id.v_bold_line_wealthy_thoughts);
        btnWealthyThoughts = (Button) findViewById(R.id.btn_wealthy_thoughts);
        btnProfessionExperiences = (Button) findViewById(R.id.btn_profession_experiences);
        boldLineProfessionExperiences = findViewById(R.id.v_bold_line_profession_experiences);
        mLVExperiences = (PullToRefreshListView) findViewById(R.id.lv_my_experiences);
        mLoadingDialog = PopUpComponentUtil.createLoadingProgressDialog(
                this, "加载情报中，请稍候...", true, true
        );
    }

    @Override
    protected void setListeners() {
        mController = new MyExperiencesController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnWealthyThoughts, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnProfessionExperiences, mController);
        registerListener(ListenerControllerAdapter.ON_ITEM_CLICK, mLVExperiences, mController);
        registerListener(ThinkTankController.ON_PULL_REFRESH, mLVExperiences, mController);
    }

    @Override
    protected void initData() {
        myExperienceEntities = new ArrayList<>();
        mAdapter = new NewMyArticlesAdapter(this, myExperienceEntities);
        queryMyExperiences(false);
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("我的智文");
        mLVExperiences.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLVExperiences.setAdapter(mAdapter);
    }

    public void queryMyExperiences(boolean isSearch) {
        Parameter param = new Parameter();
        param.addParameter(ArticleFunction.IN_PAGE, String.valueOf(currentPage));
        param.addParameter(QueryMyIntelsFunction.IN_SEARCH_KEY, searchKey);

        int taskId = TaskId.TASK_ID_FIRST;
        if (isSearch) {
            taskId = TaskId.TASK_ID_SECOND;
        }
        startTask(new QueryMyExperiencesRequest(
                taskId, CacheKey.KEY_MY_PUBLISHED_EXP_PRO_EXP, param, new QueryAction(this)));
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLVExperiences.onRefreshComplete();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissLoading();
        final ArrayList<ExperienceEntity> exps = (ArrayList<ExperienceEntity>)
                mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
        if (null == exps || exps.size() < 1) {
            Toast.makeText(this, "您的智文已全部加载", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                if (1 == currentPage && myExperienceEntities.size() > 0) {
                    myExperienceEntities.clear();
                }

                break;
            case TaskId.TASK_ID_SECOND:
                myExperienceEntities.clear();

                break;
        }
        myExperienceEntities.addAll(exps);
        exps.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissLoading();
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissLoading();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissLoading();
    }
}

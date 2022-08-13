package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ViewFlipper;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.controllers.PriceIndexController;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.constants.msg.EngineMsgList;
import com.thinkive.android.app_engine.engine.TKFragmentActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IAppControl;
import com.thinkive.android.app_engine.interfaces.IID;
import com.thinkive.android.app_engine.interfaces.IModule;

/**
 * 描述：行情首页
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-12-26
 * @last_edit
 * @since 1.0
 */
public class PriceIndexActivity extends TKFragmentActivity implements IModule {
    public IAppControl appControl;
    public int currentFragment;
    public ViewFlipper viewFlipper;
    public View selfSelectionStocksBoldLine;    //自选按钮下面的红粗线
    public View roseDropListBoldLine;   //涨跌排名按钮下面的红粗线
    public Button btnSelfSelectionStocks;
    public Button btnRoseDropList;
    public Button btnEditSelfSelection;
    public RoseDropRankListFragment roseDropRankFragment;
    public SelfSelectionStocksFragment selfSelectionFragment;

    private ImageButton mIBtnSearch;
    private FrameLayout mFLSelfSelectionContainer, mFLRoseDropListContainer;

    private PriceIndexController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_index);

        findViews();
        setListeners();
        initViews();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果会员已经过期，跳转到我界面
        if ("true".equals(mCache.getStringCacheItem(CacheKey.KEY_IS_VIP_EXPIRED))) {
            appControl.sendMessage(
                    new AppMsg("mine", String.valueOf(EngineMsgList.OPEN_MODULE), null));
        }
    }

    @Override
    protected void findViews() {
        viewFlipper = (ViewFlipper) findViewById(R.id.vf_container);
        btnRoseDropList = (Button) findViewById(R.id.btn_rose_drop_list);
        btnSelfSelectionStocks = (Button) findViewById(R.id.btn_self_selection_stocks);
        selfSelectionStocksBoldLine = findViewById(R.id.v_horizontal_bold_line_self_selection);
        roseDropListBoldLine = findViewById(R.id.v_horizontal_bold_line_rose_drop_list);
        mIBtnSearch = (ImageButton) findViewById(R.id.ibtn_search);
        mFLRoseDropListContainer = (FrameLayout) findViewById(R.id.fl_rose_drop_list_container);
        mFLSelfSelectionContainer = (FrameLayout) findViewById(R.id.fl_self_selection_container);
        btnEditSelfSelection = (Button) findViewById(R.id.btn_edit_self_selection);
    }

    @Override
    protected void setListeners() {
        mController = new PriceIndexController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnRoseDropList, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnSelfSelectionStocks, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnSearch, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnEditSelfSelection, mController);
    }

    @Override
    protected void initData() {
        currentFragment = R.id.btn_self_selection_stocks;
    }

    @Override
    protected void initViews() {
        roseDropRankFragment = new RoseDropRankListFragment();
        selfSelectionFragment = new SelfSelectionStocksFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_rose_drop_list_container, roseDropRankFragment);
        transaction.add(R.id.fl_self_selection_container, selfSelectionFragment);
        transaction.commit();
    }

    @Override
    public boolean init(IAppContext iAppContext) {
       if (null != iAppContext) {
            appControl = (IAppControl) iAppContext.queryInterface(this, IID.IID_IAppControl);
        }
        return true;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onMessage(AppMsg appMsg) {

    }

    @Override
    public String onCallMessage(AppMsg appMsg) {
        return null;
    }

    @Override
    public void onUnload() {

    }
}

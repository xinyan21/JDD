package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.controllers.PriceController;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.engine.TKFragmentActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IModule;

/**
 * 描述：行情模块
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
@Deprecated
public class PriceActivity extends TKFragmentActivity implements IModule {
    public static final String TAG_INDEX_FRAGMENT = "index";
    public static final String TAG_RISE_LIST_FRAGMENT = "rise";
    public static final String TAG_DROP_LIST_FRAGMENT = "drop";
    public static final String TAG_USER_FRAGMENT = "user";
    public FragmentManager mFragmentManager;
    public IndexFragment mIndexFragment;
    public DropListFragment mDropListFragment;
    public RiseListFragment mRiseListFragment;
    public SelfSelectionStocksFragment mSelfSelectionStocksFragment;
    private RadioGroup mMenu;
    private PriceController mController;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        mController = new PriceController(this);
        mFragmentManager = getSupportFragmentManager();
        mIndexFragment = new IndexFragment();
        mDropListFragment = new DropListFragment();
        mRiseListFragment = new RiseListFragment();
        mSelfSelectionStocksFragment = new SelfSelectionStocksFragment();

        findViews();
        setListeners();
        initViews();
        initData();
    }

    @Override
    protected void findViews() {
        mMenu = (RadioGroup) findViewById(R.id.rg_menu);
    }

    @Override
    protected void setListeners() {
        registerListener(ListenerControllerAdapter.ON_CHECK, mMenu, mController);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
    }

    @Override
    public boolean init(IAppContext iAppContext) {
        return false;
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

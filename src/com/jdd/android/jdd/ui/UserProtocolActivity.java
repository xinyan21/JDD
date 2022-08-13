package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.controllers.UserProtocolController;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：用户协议
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-29
 * @last_edit 2016-02-29
 * @since 1.0
 */
public class UserProtocolActivity extends TKActivity {
    private WebView mWVProtocol;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;

    private UserProtocolController mController = new UserProtocolController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_protocol);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mWVProtocol = (WebView) findViewById(R.id.wv_user_protocol);
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
    }

    @Override
    protected void setListeners() {
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTVTitle.setText("用户协议");
//        mWVProtocol.loadData(getResources().getString(R.string.user_protocol), "html/text", "utf-8");
        mWVProtocol.loadUrl("http://112.74.29.151/reginfo");
    }
}

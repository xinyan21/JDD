package com.jdd.android.jdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.jdd.android.jdd.R;
import com.thinkive.android.app_engine.beans.AppMsg;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.interfaces.IAppContext;
import com.thinkive.android.app_engine.interfaces.IModule;

/**
 * 描述：众筹
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-10
 * @last_edit 2016-03-10
 * @since 1.0
 */
public class CrowdFundingActivity extends TKActivity implements IModule{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crowd_funding);
        findViewById(R.id.iv_project_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardToDetail();
            }
        });
        findViewById(R.id.iv_project_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardToDetail();
            }
        });
        findViewById(R.id.iv_project_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardToDetail();
            }
        });
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    private void forwardToDetail() {
        Intent intent = new Intent(this, CrowdFundingDetailActivity.class);
        startActivity(intent);
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

package com.jdd.android.jdd.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-10
 * @last_edit 2016-03-10
 * @since 1.0
 */
public class CrowdFundingDetailActivity extends TKActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_funding_detail);
        ((TextView) findViewById(R.id.tv_activity_title)).setText("查阅项目");
        findViewById(R.id.ibtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrowdFundingDetailActivity.this.finish();
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
}

package com.jdd.android.jdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jdd.android.jdd.R;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-06-26
 * @since 1.0
 */
public class SplashActivity extends TKActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView img = new ImageView(this);
        img.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        img.setImageResource(R.drawable.splash);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(img);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
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

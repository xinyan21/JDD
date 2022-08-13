package com.jdd.android.jdd.controllers;

import android.content.Intent;
import android.view.View;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.ui.RealNameAuthActivity;
import com.jdd.android.jdd.ui.UserProtocolActivity;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

/**
 * 描述：注册第二步控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class SignInStep2Controller extends ListenerControllerAdapter implements View.OnClickListener {
    private RealNameAuthActivity mActivity;

    public SignInStep2Controller(RealNameAuthActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

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
            case R.id.btn_select_photo:
                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                openAlbumIntent.setType("image/*");
                mActivity.startActivityForResult(openAlbumIntent, 1);

                break;
            case R.id.btn_user_protocol:
                mActivity.startActivity(new Intent(mActivity, UserProtocolActivity.class));

                break;
            case R.id.btn_submit:
                mActivity.submit();

                break;
        }
    }
}

package com.jdd.android.jdd.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mIWXAPI;
    private TextView mTVHint;
    private Button mBtnConfirm;
    private Button mBtnCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIWXAPI = WXAPIFactory.createWXAPI(this, "wx95f33ad02b903e5d");
        mIWXAPI.handleIntent(getIntent(), this);
        Log.i("pay", "onCreate");
        setContentView(R.layout.dialog_confirm);
        mTVHint = (TextView) findViewById(R.id.tv_dialog_text);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mTVHint.setText("恭喜，您已充值成功");
        mBtnConfirm.setText("支付成功");

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("pay", "onResp");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (0 == resp.errCode) {

            } else if (-1 == resp.errCode) {
                Toast.makeText(this, resp.errStr, Toast.LENGTH_LONG).show();
            }
        }
    }
}
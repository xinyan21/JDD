package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.RegisterFunction;
import com.jdd.android.jdd.controllers.SignInStep2Controller;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.UploadFileRequest;
import com.jdd.android.jdd.utils.CheckIdCard;
import com.jdd.android.jdd.utils.ImageCompress;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.adf.log.Logger;
import com.thinkive.android.app_engine.engine.AppEngine;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.FileUtils;
import com.thinkive.android.app_engine.utils.ImageUtils;
import com.thinkive.android.app_engine.utils.StringUtils;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 描述：实名认证
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-05
 * @last_edit 2016-01-05
 * @since 1.0
 */
public class RealNameAuthActivity extends TKActivity implements IQueryServer {
    public EditText etName, etIDNum;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnSubmit;
    private Button mBtnSelectPhoto;
    private Button mBtnUserProtocol;
    private Dialog mDialogLoading;
    private CheckBox mCBAgreeServiceProtocol;

    private SignInStep2Controller mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private boolean mIsIDIdentified = false;    //实名认证是否成功
    private boolean mIsIDPhotoUploaded = false;     //身份证照片是否上传成功
    private FileBody mIDFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_auth);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etIDNum = (EditText) findViewById(R.id.et_id_num);
        etName = (EditText) findViewById(R.id.et_name);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnSelectPhoto = (Button) findViewById(R.id.btn_select_photo);
        mBtnUserProtocol = (Button) findViewById(R.id.btn_user_protocol);
        mCBAgreeServiceProtocol = (CheckBox) findViewById(R.id.cb_agree_service_protocol);
        mDialogLoading = PopUpComponentUtil.createLoadingProgressDialog(this, "实名认证中，请稍候...", true, true);
    }

    @Override
    protected void setListeners() {
        mController = new SignInStep2Controller(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmit, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSelectPhoto, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnUserProtocol, mController);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mTVTitle.setText("实名认证");
    }

    private void dismissDialog() {
        if (mDialogLoading.isShowing()) {
            mDialogLoading.dismiss();
        }
    }

    public void submit() {
        String name = etName.getText().toString();
        String idNo = etIDNum.getText().toString();
        if (StringUtils.isEmpty(name)) {
            toast("姓名不能为空，请输入您的姓名");
            return;
        }
        if (StringUtils.isEmpty(idNo)) {
            toast("身份证号不能为空，请输入您的身份证号");
            return;
        }
        if (!new CheckIdCard(idNo).validate()) {
            toast("身份证号输入错误，请重新输入您的身份证号");
            return;
        }
        if (null == mIDFile) {
            PopUpComponentUtil.showShortToast(this, "未选择证件照片");
            return;
        }
        if (!mCBAgreeServiceProtocol.isChecked()) {
            toast("您未阅读并同意 服务协议，不能进行实名认证！");
            return;
        }
        uploadIDFile();
        mDialogLoading.show();
    }

    private void uploadIDFile() {
        Parameter param = new Parameter();
        param.addParameter("file", mIDFile);
        startTask(new UploadFileRequest(TaskId.TASK_ID_SECOND, param, new QueryAction(this)));
    }

    private void submitRealName() {
        String name = etName.getText().toString();
        String idNo = etIDNum.getText().toString();

        Parameter param = new Parameter();
        param.addParameter(RegisterFunction.IN_USER_NAME, name);
        param.addParameter(RegisterFunction.IN_ID_NO, idNo);
        PushRequest request = new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_CHECK_REAL_NAME"),
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        );
        HashMap<String, String> returnKey = new HashMap<>();
        returnKey.put(BaseServerFunction.ERR_MSG, null);
        request.setReturnKeys(returnKey);
        startTask(request);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                PopUpComponentUtil.showShortToast(this, "实名认证已提交，我们将尽快为您审核");
                finish();

                break;
            case TaskId.TASK_ID_SECOND:
                submitRealName();

                break;
        }
    }


    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                HashMap returnData = (HashMap) bundle.getSerializable(String.valueOf(taskId));
                if (returnData.containsKey(BaseServerFunction.ERR_MSG)) {
                    PopUpComponentUtil.showShortToast(
                            this, returnData.get(BaseServerFunction.ERR_MSG).toString()
                    );
                }

                break;
            case TaskId.TASK_ID_SECOND:
                PopUpComponentUtil.showShortToast(this, "照片上传失败");

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        Toast.makeText(this, R.string.error_net, Toast.LENGTH_SHORT).show();
        dismissDialog();
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        dismissDialog();
        Toast.makeText(this, R.string.error_internal, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (1 == requestCode && null != data) {
            final Uri uri = data.getData();
            startTask(new CallBack.SchedulerCallBack() {
                @Override
                public void handler(MessageAction messageAction) {
                    ImageCompress compress = new ImageCompress();
                    ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                    options.uri = uri;
                    options.maxHeight = 600;
                    options.maxWidth = 600;
                    final Bitmap temp = compress.compressFromUri(RealNameAuthActivity.this, options);
                    saveBitmap(temp);
                    messageAction.transferAction(1, null, new CallBack.MessageCallBack() {
                        @Override
                        public void handler(Context context, int i, Bundle bundle) {
                            if (null != temp) {
                                mBtnSelectPhoto.setBackgroundDrawable(ImageUtils.bitmapToDrawable(temp));
                                mBtnSelectPhoto.setText("");
                            }
                        }
                    });
                }
            });
        } else {
            PopUpComponentUtil.showShortToast(this, "选取图片失败");
        }
    }

    public void saveBitmap(Bitmap bm) {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "temp.png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Logger.info("图片保存成功");
            mIDFile = new FileBody(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

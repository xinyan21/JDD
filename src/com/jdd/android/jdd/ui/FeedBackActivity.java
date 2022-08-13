package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.FeedbackFunction;
import com.jdd.android.jdd.controllers.FeedBackController;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.HashMap;

/**
 * 描述：用户反馈（建议、举报）
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-04
 * @last_edit 2016-01-04
 * @since 1.0
 */
public class FeedBackActivity extends TKActivity implements IQueryServer {
    private static HashMap<String, String> sCategoryToParam = new HashMap<>();
    public Spinner spinFatherCategory, spinChildCategory;
    public EditText etProblemDescription, etContact, etAddress;
    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private Button mBtnSubmit;

    private FeedBackController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        spinChildCategory = (Spinner) findViewById(R.id.sp_problem_category_child);
        spinFatherCategory = (Spinner) findViewById(R.id.sp_problem_category_father);
        etAddress = (EditText) findViewById(R.id.et_city);
        etContact = (EditText) findViewById(R.id.et_contact_info);
        etProblemDescription = (EditText) findViewById(R.id.et_problem_description);
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void setListeners() {
        mController = new FeedBackController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnSubmit, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
    }

    @Override
    protected void initData() {
        sCategoryToParam.put("投诉", "d");
        sCategoryToParam.put("建议", "e");
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("用户反馈");
    }

    public void submitFeedback() {
        String type = sCategoryToParam.get(spinFatherCategory.getSelectedItem().toString());
        String content = etProblemDescription.getText().toString();
        String contact = etContact.getText().toString();
        String address = etAddress.getText().toString();
        if (StringUtils.isEmpty(content)) {
            PopUpComponentUtil.showShortToast(this, "反馈内容不能为空");
            return;
        }
        Parameter param = new Parameter();
        param.addParameter(FeedbackFunction.CONTACT, contact);
        param.addParameter(FeedbackFunction.CONTENT, content);
        param.addParameter(FeedbackFunction.LOCATION, address);
        param.addParameter(FeedbackFunction.TYPE, type);
        startTask(new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_FEEDBACK"),
                TaskId.TASK_ID_FIRST, param, new QueryAction(this)
        ));
        mBtnSubmit.setText("提交中...");
        mBtnSubmit.setEnabled(false);
    }

    private void resetViews() {
        spinFatherCategory.setSelection(0);
        etContact.setText("");
        etAddress.setText("");
        etProblemDescription.setText("");
        mBtnSubmit.setText("提交");
        mBtnSubmit.setEnabled(true);
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        resetViews();
        PopUpComponentUtil.showLongToast(this,"您的反馈已经提交成功，感谢您的支持！");
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        PopUpComponentUtil.showShortToast(this, "很抱歉，您的反馈提交失败，请直接联系我们的客服！");
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        ProjectUtil.defaultNetErrorHandler(this);
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {

    }
}

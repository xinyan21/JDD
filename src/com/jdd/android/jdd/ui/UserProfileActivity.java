package com.jdd.android.jdd.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryAction;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.EditProfileFunction;
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.controllers.UserProfileController;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.PushRequest;
import com.jdd.android.jdd.requests.QueryRegisterInfoRequest;
import com.jdd.android.jdd.utils.PopUpComponentUtil;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

/**
 * 描述：用户资料
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-06
 * @last_edit 2016-01-06
 * @since 1.0
 */
public class UserProfileActivity extends TKActivity implements IQueryServer {
    public static final String KEY_USER = "USER";

    public EditText etNickName, etRank, etFansNum;
    public EditText etIntelsNum, etExperiencesNum, etCommentedByOthersNum;
    public EditText etCommentOthersNum, etRewardedNum;
    public EditText etCity, etBriefIntroduction, etProfession, etSign;
    public Button btnEditProfile;

    private ImageButton mIBtnBack;
    private TextView mTVTitle;
    private ImageView mIVRank;
    private RadioButton mRBtnMale, mRBtnFemale;
    private Dialog mDialogProcessing;

    private UserProfileController mController;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private UserEntity mUserEntity;
    private boolean mIsLoginUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findViews();
        setListeners();
        initData();
        initViews();
    }

    @Override
    protected void findViews() {
        mTVTitle = (TextView) findViewById(R.id.tv_activity_title);
        mIBtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etCity = (EditText) findViewById(R.id.et_city);
        etBriefIntroduction = (EditText) findViewById(R.id.et_brief_introduction);
        etCommentedByOthersNum = (EditText) findViewById(R.id.et_comment_by_others_num);
        etCommentOthersNum = (EditText) findViewById(R.id.et_comment_others_num);
        etExperiencesNum = (EditText) findViewById(R.id.et_experience_num);
        etFansNum = (EditText) findViewById(R.id.et_fans_num);
        etIntelsNum = (EditText) findViewById(R.id.et_intel_num);
        etNickName = (EditText) findViewById(R.id.et_nick_name);
        etRank = (EditText) findViewById(R.id.et_rank);
        etSign = (EditText) findViewById(R.id.et_sign);
        etProfession = (EditText) findViewById(R.id.et_profession);
        etRewardedNum = (EditText) findViewById(R.id.et_rewarded_num);
        mIVRank = (ImageView) findViewById(R.id.iv_rank_icon);
        mRBtnFemale = (RadioButton) findViewById(R.id.rbtn_female);
        mRBtnMale = (RadioButton) findViewById(R.id.rbtn_male);
        btnEditProfile = (Button) findViewById(R.id.btn_edit);
        mDialogProcessing = PopUpComponentUtil.createLoadingProgressDialog(
                this, "处理中...请稍后", true, true
        );
    }

    @Override
    protected void setListeners() {
        mController = new UserProfileController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, btnEditProfile, mController);
    }

    @Override
    protected void initData() {
        UserEntity loginUser = (UserEntity) mCache.getCacheItem(CacheKey.KEY_CURRENT_LOGIN_USER_INFO);
        mUserEntity = (UserEntity) getIntent().getSerializableExtra(KEY_USER);
        Parameter param = new Parameter();
        //检查是否是当前登录用户
        if (mUserEntity.getUserId() == loginUser.getUserId()) {
            mIsLoginUser = true;
            param.addParameter(QueryUserInfoFunction.IN_USER_ID, "");
            mUserEntity = loginUser;
        } else {
            mIsLoginUser = false;
            param.addParameter(
                    QueryUserInfoFunction.IN_USER_ID, String.valueOf(mUserEntity.getUserId())
            );
        }
        startTask(new QueryRegisterInfoRequest(
                TaskId.TASK_ID_FIRST, param, mUserEntity, new QueryAction(this)
        ));
    }

    @Override
    protected void initViews() {
        mTVTitle.setText("个人资料");
        if (null == mUserEntity) {
            return;
        }
        if (!StringUtils.isEmpty(mUserEntity.getNickName())) {
            etNickName.setText(mUserEntity.getNickName());
        }
        if (!StringUtils.isEmpty(mUserEntity.getGender())) {
            if ("wom".equals(mUserEntity.getGender())) {
                mRBtnFemale.setChecked(true);
            } else if ("man".equals(mUserEntity.getGender())) {
                mRBtnMale.setChecked(true);
            }
        }
        etFansNum.setText(String.valueOf(mUserEntity.getFansCount()));
        etIntelsNum.setText(String.valueOf(mUserEntity.getIntelligenceCount()));
        etExperiencesNum.setText(String.valueOf(mUserEntity.getExperienceCount()));
        etCommentedByOthersNum.setText(String.valueOf(mUserEntity.getCommentCountByOthers()));
        etCommentOthersNum.setText(String.valueOf(mUserEntity.getCommentOthersCount()));
        etRewardedNum.setText(String.valueOf(mUserEntity.getRewardedCountByOthers()));
        etCity.setText(mUserEntity.getCity());
        etProfession.setText(mUserEntity.getProfession());
        etBriefIntroduction.setText(mUserEntity.getBriefIntroduction());
        etSign.setText(mUserEntity.getSignature());
        if (mIsLoginUser) {
            btnEditProfile.setVisibility(View.VISIBLE);
        } else {
            btnEditProfile.setVisibility(View.GONE);
        }
    }

    public void enableEdit() {
        btnEditProfile.setText("保存");
        etNickName.setEnabled(true);
        etBriefIntroduction.setEnabled(true);
        etProfession.setEnabled(true);
        etCity.setEnabled(true);
        etSign.setEnabled(true);
        mRBtnFemale.setEnabled(true);
        mRBtnMale.setEnabled(true);

        etNickName.setBackgroundResource(R.drawable.shape_input_rounded_corner);
        etBriefIntroduction.setBackgroundResource(R.drawable.shape_input_rounded_corner);
        etProfession.setBackgroundResource(R.drawable.shape_input_rounded_corner);
        etCity.setBackgroundResource(R.drawable.shape_input_rounded_corner);
        etSign.setBackgroundResource(R.drawable.shape_input_rounded_corner);
    }

    public void disableEdit() {
        btnEditProfile.setText("编辑");
        etNickName.setEnabled(false);
        etBriefIntroduction.setEnabled(false);
        etProfession.setEnabled(false);
        etCity.setEnabled(false);
        etSign.setEnabled(false);
        mRBtnFemale.setEnabled(false);
        mRBtnMale.setEnabled(false);

        etNickName.setBackgroundResource(R.color.transparent);
        etBriefIntroduction.setBackgroundResource(R.color.transparent);
        etProfession.setBackgroundResource(R.color.transparent);
        etCity.setBackgroundResource(R.color.transparent);
        etSign.setBackgroundResource(R.color.transparent);
    }

    public void saveEdit() {
        mUserEntity.setNickName(etNickName.getText().toString());
        mUserEntity.setProfession(etProfession.getText().toString());
        mUserEntity.setBriefIntroduction(etBriefIntroduction.getText().toString());
        mUserEntity.setCity(etCity.getText().toString());
        mUserEntity.setBriefIntroduction(etBriefIntroduction.getText().toString());
        mUserEntity.setSignature(etSign.getText().toString());
        if (mRBtnMale.isChecked()) {
            mUserEntity.setGender("man");
        } else if (mRBtnFemale.isChecked()) {
            mUserEntity.setGender("wom");
        }

        Parameter param = new Parameter();
        param.addParameter(EditProfileFunction.IN_NICK_NAME, mUserEntity.getNickName());
        try {
            param.addParameter(EditProfileFunction.IN_BIRTHDAY,
                    DateFormat.format("yyyy-MM-dd",
                            Integer.valueOf(mUserEntity.getBirthday())).toString()
            );
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        param.addParameter(EditProfileFunction.IN_CITY, mUserEntity.getCity());
        param.addParameter(EditProfileFunction.IN_GENDER, mUserEntity.getGender());
        param.addParameter(EditProfileFunction.IN_HEAD_PORTRAIT, mUserEntity.getAvatarUrl());
        param.addParameter(EditProfileFunction.IN_PROFESSION, mUserEntity.getProfession());
        param.addParameter(EditProfileFunction.IN_PROVINCE, mUserEntity.getProvince());
        param.addParameter(EditProfileFunction.IN_SIGN, mUserEntity.getSignature());
        param.addParameter(EditProfileFunction.IN_INTRODUCTION, mUserEntity.getBriefIntroduction());
        startTask(new PushRequest(
                ProjectUtil.getFullMainServerUrl("URL_EDIT_PROFILE"),
                TaskId.TASK_ID_SECOND, param, new QueryAction(this)
        ));
        mDialogProcessing.show();
    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:
                initViews();
                disableEdit();

                break;
            case TaskId.TASK_ID_SECOND:
                PopUpComponentUtil.showShortToast(this, "您的资料已修改成功");
                disableEdit();

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {
        dismissDialog();
        switch (taskId) {
            case TaskId.TASK_ID_FIRST:

                break;
            case TaskId.TASK_ID_SECOND:
                PopUpComponentUtil.showShortToast(this, "很抱歉，资料修改失败，请您重试或者登录网站进行修改");

                break;
        }
    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {
        dismissDialog();
        PopUpComponentUtil.showShortToast(this, "网络错误");
    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {
        onServerError(taskId, bundle);
    }

    private void dismissDialog() {
        if (mDialogProcessing.isShowing()) {
            mDialogProcessing.dismiss();
        }
    }
}

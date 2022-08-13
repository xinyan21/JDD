package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.QueryUserInfoFunction;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.HttpRequest;
import com.thinkive.adf.invocation.http.ResponseAction;
import com.thinkive.adf.tools.ConfigStore;
import com.thinkive.android.app_engine.utils.StringUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 描述：查询用户注册信息请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-08
 * @since 1.0
 */
public class QueryRegisterInfoRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;
    private UserEntity mUserEntity;

    public QueryRegisterInfoRequest(int taskId, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        ProjectUtil.addPlatformFlag(parameter);
    }

    public QueryRegisterInfoRequest(int taskId, Parameter parameter, UserEntity user, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        mUserEntity = user;
        ProjectUtil.addPlatformFlag(parameter);
    }


    @Override
    public void handler(MessageAction messageAction) {
        String url = ProjectUtil.getFullMainServerUrl("URL_QUERY_REGISTER_INFO");

        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = request.get(url, mParameter);
        if (null == bytes) {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
            return;
        }
        String strResult = null;
        try {
            strResult = new String(bytes, ConfigStore.getConfigValue("system", "CHARSET"));
        } catch (UnsupportedEncodingException e) {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
        if (StringUtils.isEmpty(strResult)) {
            messageAction.transferAction(ResponseAction.RESULT_NET_ERROR, bundle, mAction);
            return;
        }
        try {
            JSONObject jsonResult = new JSONObject(strResult);
            String returnFlag = ProjectUtil.getReturnFlag(jsonResult);
            JSONObject item = jsonResult.optJSONObject("rInfo");
            if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                if (null == mUserEntity) {
                    mUserEntity = new UserEntity();
                }
                if (null != item) {
                    mUserEntity.setUserId(item.optLong("customerId"));
                    mUserEntity.setAddress(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.ADDRESS)
                    ));
                    mUserEntity.setBirthday(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.BIRTHDAY)
                    ));
                    mUserEntity.setCity(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.CITY)
                    ));
                    mUserEntity.setProvince(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.PROVINCE)
                    ));
                    mUserEntity.setName(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.REAL_NAME)
                    ));
                    mUserEntity.setBriefIntroduction(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.INTRODUCTION)
                    ));
                    mUserEntity.setProfession(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.PROFESSION)
                    ));
                    mUserEntity.setGender(ProjectUtil.replaceNullStringAsEmpty(
                            item.optString(QueryUserInfoFunction.GENDER)
                    ));
                    mUserEntity.setRealNameVerifyStatus(
                            item.optString(QueryUserInfoFunction.REAL_NAME_VERIFY_STATUS));
                }

                mCacheKey = CacheKey.KEY_USER_REGISTER_INFO;
                mCache.addCacheItem(mCacheKey, mUserEntity);
                bundle.putString(String.valueOf(mTaskId), mCacheKey);
                messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
            } else {
                messageAction.transferAction(ResponseAction.RESULT_SERVER_ERROR, bundle, mAction);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }
}

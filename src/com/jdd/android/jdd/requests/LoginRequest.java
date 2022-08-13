package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.LoginFunction;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.utils.HttpRequest;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.ResponseAction;
import com.thinkive.adf.tools.ConfigStore;
import com.thinkive.android.app_engine.utils.StringUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 描述：登录接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-28
 * @since 1.0
 */
public class LoginRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;

    public LoginRequest(Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        ProjectUtil.addPlatformFlag(parameter);

        try {
            mTaskId = Integer.valueOf(parameter.getString("taskId"));
        } catch (NumberFormatException e) {
            mTaskId = (int) parameter.getObject(TaskId.TASK_ID);
        }
        mParameter.removeParameter("taskId");
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url;
        url = ProjectUtil.getFullMainServerUrl("URL_LOGIN");

        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = request.post(url, mParameter);
        if (null != bytes) {
            String strResult = null;
            try {
                strResult = new String(bytes, ConfigStore.getConfigValue("system", "CHARSET"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (StringUtils.isEmpty(strResult)) {
                messageAction.transferAction(ResponseAction.RESULT_NET_ERROR, bundle, mAction);
                return;
            }
            try {
                JSONObject jsonResult = new JSONObject(strResult);
                if (ProjectUtil.handleFrameworkReturn(jsonResult, messageAction, mAction)) {
                    return;
                }
                String returnFlag = ProjectUtil.getReturnFlag(jsonResult);
                bundle.putString(String.valueOf(mTaskId), CacheKey.KEY_DATA + mTaskId);
                if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                    UserEntity user = new UserEntity();
                    user.setPhoneNo(Long.valueOf(mParameter.getString(LoginFunction.PHONE_NO)));
                    mCache.addCacheItem(CacheKey.KEY_USER_INFO, user);
                    messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
                } else {
                    bundle.putString(LoginFunction.CONTENT,
                            jsonResult.optString(LoginFunction.CONTENT));
                    messageAction.transferAction(ResponseAction.RESULT_SERVER_ERROR, bundle, mAction);
                }
            } catch (Exception e) {
                e.printStackTrace();
                messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
            }
        } else {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }
}

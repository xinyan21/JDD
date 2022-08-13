package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 描述：编辑用户资料接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-28
 * @since 1.0
 */
public class EditUserProfileRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;

    public EditUserProfileRequest(Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        ProjectUtil.addPlatformFlag(parameter);

        try {
            mTaskId = Integer.valueOf(parameter.getString("taskId"));
            mParameter.removeParameter("taskId");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url;
            url = ConfigStore.getConfigValue("system", "MAIN_SERVER_URL")
                    + ConfigStore.getConfigValue("system", "URL_EDIT_USER_PROFILE");

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
                if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(ProjectUtil.getReturnFlag(jsonResult))) {
                    //标准做法是每个组件都需要有自己的Key，每个组件定义一个就行，通过TaskId来区分不同的请求
                    //这样组件之间就不会冲突
//                    cache.addCacheItem(CacheKey.KEY_DATA + mTaskId, data);
                    bundle.putString(String.valueOf(mTaskId), CacheKey.KEY_DATA + mTaskId);
                    messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
            }
        } else {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }
}

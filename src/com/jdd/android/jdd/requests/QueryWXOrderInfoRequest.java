package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
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
 * 描述：微信支付请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-04-27
 * @since 1.0
 */
public class QueryWXOrderInfoRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;

    public QueryWXOrderInfoRequest(int taskId, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url = ProjectUtil.getFullMainServerUrl("URL_QUERY_WX_PAY_INFO");

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
            String returnFlag = jsonResult.optString("result_code").toLowerCase();
            if ("success".equals(returnFlag)) {
                mCacheKey = CacheKey.KEY_WX_PAY_INFO + mTaskId;
                mCache.addCacheItem(mCacheKey, jsonResult);
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

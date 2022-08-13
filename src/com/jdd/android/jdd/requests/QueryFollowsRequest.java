package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.function.QueryFollowsFunction;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.entities.IntelligenceEntity;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：查询关注的人请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-02
 * @since 1.0
 */
public class QueryFollowsRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;

    public QueryFollowsRequest(int taskId, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url = ProjectUtil.getFullMainServerUrl("URL_QUERY_FOLLOWS");

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
            List<UserEntity> userEntities = new ArrayList<>();
            UserEntity user = null;
            JSONObject jsonResult = new JSONObject(strResult);
            String returnFlag = ProjectUtil.getReturnFlag(jsonResult);
            if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                JSONArray array = jsonResult.optJSONArray(QueryFollowsFunction.DATA_LIST);
                if (null != array && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        user = new UserEntity();
                        user.setUserId(item.optLong(QueryFollowsFunction.USER_ID));
                        user.setAvatarUrl(item.optString(QueryFollowsFunction.HEAD_PORTRAIT_URL));
                        user.setNickName(item.optString(QueryFollowsFunction.NICK_NAME));
                        IntelligenceEntity intel = new IntelligenceEntity();
                        intel.setArticleId(item.optLong(QueryFollowsFunction.LATEST_INTEL_ID));
                        intel.setTitle(item.optString(QueryFollowsFunction.LATEST_INTEL_TITLE));
                        user.setLatestIntel(intel);

                        userEntities.add(user);
                    }
                }

                mCacheKey = CacheKey.KEY_FOLLOWS + mTaskId;
                mCache.addCacheItem(mCacheKey, userEntities);
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

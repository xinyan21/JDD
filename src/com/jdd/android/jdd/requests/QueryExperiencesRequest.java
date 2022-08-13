package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.entities.ExperienceEntity;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：查询智文列表请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-17
 * @since 1.0
 */
public class QueryExperiencesRequest implements CallBack.SchedulerCallBack {
    protected String mUrl;
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;

    public QueryExperiencesRequest(int taskId, String cacheKey, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        mCacheKey = cacheKey;
        mUrl = ProjectUtil.getFullMainServerUrl("URL_QUERY_EXPERIENCES");
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = request.post(mUrl, mParameter);
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
                String returnFlag = ProjectUtil.getReturnFlag(jsonResult);
                if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                    List<ExperienceEntity> experienceEntities = null;
                    JSONArray array = jsonResult.getJSONArray(QueryExperiencesFunction.DATA_LIST);
                    if (null != array && array.length() > 0) {
                        ExperienceEntity entity;
                        experienceEntities = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            entity = new ExperienceEntity();
                            entity.setGoodNum(item.optInt(QueryExperiencesFunction.GOOD));
                            entity.setGreatNum(item.optInt(QueryExperiencesFunction.TOP));
                            entity.setNormalNum(item.optInt(QueryExperiencesFunction.NORMAL));
                            entity.setHeadPortraitUrl(
                                    item.optString(QueryExperiencesFunction.HEAD_PHOTO_URL));
                            entity.setArticleId(item.optLong(QueryExperiencesFunction.ID));
                            entity.setAuthorId(item.optLong(QueryExperiencesFunction.CUSTOMER_ID));
                            entity.setAuthorName(ProjectUtil.replaceNullStringAsEmpty(
                                    item.optString(QueryExperiencesFunction.NICKNAME))
                            );
                            entity.setCategory(item.optString(QueryExperiencesFunction.TYPE));
                            entity.setContent(ProjectUtil.replaceNullStringAsEmpty(
                                    item.optString(QueryExperiencesFunction.CONTENT))
                            );
                            entity.setUpdateDate(
                                    item.optLong(QueryExperiencesFunction.UPDATE_DATE));
                            entity.setUpdateBy(item.optString(QueryExperiencesFunction.UPDATE_BY));
                            entity.setTitle(ProjectUtil.replaceNullStringAsEmpty(
                                    item.optString(QueryExperiencesFunction.TITLE))
                            );
                            entity.setRule(item.optString(QueryExperiencesFunction.RULE));
                            entity.setSummary(ProjectUtil.replaceNullStringAsEmpty(
                                    item.optString(QueryExperiencesFunction.SUMMARY))
                            );
                            entity.setTags(item.optString(QueryExperiencesFunction.TAG));
                            entity.setCreateDate(item.optLong(QueryExperiencesFunction.CREATE_DATE));
                            entity.setIndustry(item.optString(QueryIntelligencesFunction.INDUSTRY));

                            experienceEntities.add(entity);
                        }
                    }

                    mCache.addCacheItem(mCacheKey + mTaskId, experienceEntities);
                    bundle.putString(String.valueOf(mTaskId), mCacheKey + mTaskId);
                    bundle.putInt(
                            QueryExperiencesFunction.PRE_TOTAL,
                            jsonResult.optInt(QueryExperiencesFunction.PRE_TOTAL));
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

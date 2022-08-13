package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.constants.function.QueryExperiencesFunction;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.entities.IntelligenceEntity;
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
 * 描述：查询情报列表请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-17
 * @since 1.0
 */
public class QueryIntelligencesRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;
    protected String mUrl;

    public QueryIntelligencesRequest(int taskId, String cacheKey, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        mCacheKey = cacheKey;
        mUrl = ProjectUtil.getFullMainServerUrl("URL_QUERY_INTELLIGENCES");
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = request.get(mUrl, mParameter);
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
                    List<IntelligenceEntity> intelligenceEntities = null;
                    String listKey = "";
                    if (jsonResult.has(QueryIntelligencesFunction.DATA_LIST)) {
                        listKey = QueryIntelligencesFunction.DATA_LIST;
                    } else if (jsonResult.has(QueryIntelligencesFunction.DATA_LIST2)) {
                        listKey = QueryIntelligencesFunction.DATA_LIST2;
                    }
                    JSONArray array = jsonResult.getJSONArray(listKey);
                    if (null != array && array.length() > 0) {
                        IntelligenceEntity entity;
                        intelligenceEntities = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            entity = new IntelligenceEntity();
                            entity.setGoodNum(item.optInt(QueryIntelligencesFunction.GOOD));
                            entity.setGreatNum(item.optInt(QueryIntelligencesFunction.TOP));
                            entity.setNormalNum(item.optInt(QueryIntelligencesFunction.NORMAL));
                            entity.setArticleId(item.optLong(QueryIntelligencesFunction.ID));
                            entity.setAuthorId(item.optLong(QueryIntelligencesFunction.CUSTOMER_ID));
                            entity.setAuthorName(item.optString(QueryIntelligencesFunction.NICKNAME));
                            entity.setCategory(item.optString(QueryIntelligencesFunction.TYPE));
                            entity.setUpdateDate(
                                    item.optLong(QueryIntelligencesFunction.UPDATE_DATE)
                            );
                            entity.setUpdateBy(item.optString(QueryIntelligencesFunction.UPDATE_BY));
                            entity.setTitle(item.optString(QueryIntelligencesFunction.TITLE));
                            entity.setSummary(item.optString(QueryIntelligencesFunction.SUMMARY));
                            entity.setTags(item.optString(QueryIntelligencesFunction.TAG));
                            entity.setCreateDate(item.optLong(QueryIntelligencesFunction.CREATE_DATE));
                            entity.setBadNum(item.optInt(QueryIntelligencesFunction.BAD));
                            entity.setTerribleNum(item.optInt(QueryIntelligencesFunction.TERRIBLE));
                            entity.setFundamentals(
                                    item.optString(QueryIntelligencesFunction.FUNDAMENTALS)
                            );
                            entity.setFuture(item.optString(QueryIntelligencesFunction.FUTURE));
                            entity.setRisk(item.optString(QueryIntelligencesFunction.RISK));
                            entity.setRecommendReason(
                                    item.optString(QueryIntelligencesFunction.COMMEND_REASON)
                            );
                            entity.setLongShortPosition(
                                    item.optString(QueryIntelligencesFunction.LONG_SHORT_POSITION)
                            );
                            entity.setPrice((float) item.optDouble(QueryIntelligencesFunction.PRICE));
                            entity.setHeadPortraitUrl(
                                    item.optString(QueryExperiencesFunction.HEAD_PHOTO_URL));

                            intelligenceEntities.add(entity);
                        }
                    }

                    mCache.addCacheItem(mCacheKey + mTaskId, intelligenceEntities);
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

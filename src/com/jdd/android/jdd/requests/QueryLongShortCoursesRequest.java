package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.function.ArticleFunction;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.entities.CourseEntity;
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
 * 描述：查询多空课程列表请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-04
 * @since 1.0
 */
public class QueryLongShortCoursesRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;
    protected String mUrl;

    public QueryLongShortCoursesRequest(int taskId, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        ProjectUtil.addPlatformFlag(parameter);
        mUrl = ProjectUtil.getFullMainServerUrl("URL_QUERY_LONG_SHORT_COURSES");
    }

    @Override
    public void handler(MessageAction messageAction) {
        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = request.get(mUrl, mParameter);
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
            if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                List<CourseEntity> courseEntities = new ArrayList<>();
                CourseEntity entity;
                JSONArray array = jsonResult.optJSONArray("result");
                if (null != array && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        entity = new CourseEntity();
                        entity.setAbout(item.optString(QueryCoursesFunction.ABOUT));
                        entity.setCategory(item.optString(QueryCoursesFunction.CATEGORY));
                        entity.setCourseName(item.optString(QueryCoursesFunction.COURSE_NAME));
                        entity.setLearnedCount(item.optInt(QueryCoursesFunction.LEARNED_COUNT));
                        entity.setPrice((float) item.optDouble(QueryCoursesFunction.PRICE));
                        entity.setPptCount(item.optInt(QueryCoursesFunction.PPT_COUNT));
                        entity.setPptUrl(item.optString(QueryCoursesFunction.PPT_BASE_URL));
                        entity.setTeachingMode(item.optString(QueryCoursesFunction.TEACHING_MODE));
                        entity.setVideoTime((float) item.optDouble(QueryCoursesFunction.VIDEO_TIME));
                        entity.setVideoUrl(item.optString(QueryCoursesFunction.VIDEO_URL));
                        entity.setId(item.optLong(ArticleFunction.ID));
                        entity.setCreateDate(item.optLong(ArticleFunction.CREATE_DATE));
                        entity.setUpdateDate(item.optLong(ArticleFunction.UPDATE_DATE));

                        courseEntities.add(entity);
                    }
                }
                mCacheKey = CacheKey.KEY_LONG_SHORT_COURSES + mTaskId;
                mCache.addCacheItem(mCacheKey, courseEntities);
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

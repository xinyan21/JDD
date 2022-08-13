package com.jdd.android.jdd.requests;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
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
 * 描述：查询用户信息请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-09
 * @since 1.0
 */
public class QueryUserInfoRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;

    public QueryUserInfoRequest(
            int taskId, String cacheKey, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        mCacheKey = cacheKey;
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url = ProjectUtil.getFullMainServerUrl("URL_QUERY_USER_INFO");

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
            if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                JSONObject item = jsonResult.getJSONObject(QueryUserInfoFunction.DATA_KEY);
                UserEntity user = new UserEntity();
                user.setAvatarUrl(item.optString(QueryUserInfoFunction.HEAD_PORTRAIT_URL));
                user.setGender(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.GENDER)
                ));
                user.setProfession(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.PROFESSION)
                ));
                user.setUserId(item.optLong(QueryUserInfoFunction.USER_ID));
                user.setBriefIntroduction(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.INTRODUCTION)
                ));
                user.setAddress(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.ADDRESS)
                ));
                user.setBirthday(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.BIRTHDAY)
                ));
                user.setCity(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.CITY)
                ));
                user.setCommentCountByOthers(item.optLong(QueryUserInfoFunction.COMMENTED_COUNT));
                user.setCommentOthersCount(item.optLong(QueryUserInfoFunction.COMMENT_COUNT));
                user.setExperienceCount(item.optLong(QueryUserInfoFunction.EXPERIENCE_COUNT));
                user.setFansCount(item.optLong(QueryUserInfoFunction.FANS_COUNT));
                user.setIntelligenceCount(item.optLong(QueryUserInfoFunction.INTEL_COUNT));
                user.setName(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.REAL_NAME)
                ));
                user.setNickName(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.NICK_NAME)
                ));
                user.setProvince(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.PROVINCE)
                ));
                user.setPhoneNo(item.optInt(QueryUserInfoFunction.MOBILE_PHONE));
                user.setMoralCoins(item.optDouble(QueryUserInfoFunction.MORAL_VALUE));
                user.setCoins(item.optDouble(QueryUserInfoFunction.COINS));
                user.setSignature(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryUserInfoFunction.SIGN)
                ));
                user.setTrialExpireTime(item.optLong(QueryUserInfoFunction.TRIAL_EXPIRE_TIME));
                user.setVipExpireTime(item.optLong(QueryUserInfoFunction.VIP_EXPIRE_TIME));
                user.setLastLoginTime(item.optLong(QueryUserInfoFunction.LAST_LOGIN_TIME));
                //serverTime放在最外层
                user.setServerTime(jsonResult.optLong(QueryUserInfoFunction.SERVER_TIME));

                if (user.getIntelligenceCount() < 0) {
                    user.setIntelligenceCount(0);
                }
                if (user.getExperienceCount() < 0) {
                    user.setExperienceCount(0);
                }

                boolean isVIPExpired = false;
                boolean isTrialExpired = false;
                if (user.getVipExpireTime() == 0 ||
                            user.getVipExpireTime() < user.getServerTime()) {
                    isVIPExpired = true;
                } else if (user.getTrialExpireTime() == 0 ||
                        (user.getTrialExpireTime() < user.getServerTime()
                                && user.getTrialExpireTime() >= user.getVipExpireTime())) {
                    isTrialExpired = true;
                }
                if (isVIPExpired && isTrialExpired) {
                    mCache.addCacheItem(CacheKey.KEY_IS_VIP_EXPIRED, "false");
                }

                mCache.addCacheItem(mCacheKey, user);
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

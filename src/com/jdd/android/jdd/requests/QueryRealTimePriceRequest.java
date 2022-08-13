package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.entities.MinutePrice;
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
 * 描述：查询分时图数据
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-07-06
 * @since 1.0
 */
public class QueryRealTimePriceRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;

    public QueryRealTimePriceRequest(Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;

        try {
            mTaskId = Integer.valueOf(parameter.getString("taskId"));
            mParameter.removeParameter("taskId");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url = ProjectUtil.getFullPriceUrl("URL_QUERY_REAL_TIME");

        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = request.get(url, mParameter);
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
                if (1 == jsonResult.optInt("code")) {
                    JSONArray arrayResult = jsonResult.getJSONArray("result");
                    List<MinutePrice> data = new ArrayList<MinutePrice>();
                    if (null == arrayResult || arrayResult.length() < 1) {
                        messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);

                        return;
                    }
                    JSONObject item = null;
                    MinutePrice entity = null;
                    int dataLength = arrayResult.length();
                    double count = 0; //价格总和，用来计算均价
                    double volCount = 0;      //成交量总计
                    for (int i = 0; i < dataLength; i++) {
                        item = arrayResult.getJSONObject(i);
                        entity = new MinutePrice();

                        if (null == item) {
                            continue;
                        }

                        entity.setNow(item.getDouble("index"));
                        if (i > 0) {
                            entity.setVolume(item.getDouble("vol") - volCount);
                        } else {
                            entity.setVolume(item.getDouble("vol"));
                        }

                        volCount += entity.getVolume();
                        count += entity.getNow();
                        entity.setAvgPrice(count / (i + 1));

                        data.add(entity);
                    }
                    mCache.addCacheItem(CacheKey.KEY_STOCK_REAL_TIME_DATA + mTaskId, data);
                    bundle.putString(
                            String.valueOf(mTaskId), CacheKey.KEY_STOCK_REAL_TIME_DATA + mTaskId
                    );
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

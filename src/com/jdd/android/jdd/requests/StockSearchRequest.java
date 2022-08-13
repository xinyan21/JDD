package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.entities.StockEntity;
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
 * 描述：股票搜索接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-18
 * @since 1.0
 */
public class StockSearchRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;

    public StockSearchRequest(Parameter parameter, ResponseAction action) {
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
        String url = "";
        url = ConfigStore.getConfigValue("system", "PRICE_SERVER_URL")
                + ConfigStore.getConfigValue("system", "URL_QUERY_SEARCH");

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
                if (1 == jsonResult.optInt("code")) {
                    JSONArray arrayResult = jsonResult.getJSONArray("result");
                    List<StockEntity> data = new ArrayList<StockEntity>();
                    if (null == arrayResult || arrayResult.length() < 1) {
                        messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);

                        return;
                    }
                    JSONObject item = null;
                    StockEntity entity = null;
                    for (int i = 0; i < arrayResult.length(); i++) {
                        item = arrayResult.getJSONObject(i);
                        entity = new StockEntity();

                        if (null == item) {
                            continue;
                        }
                        entity.setCode(item.optString("code"));
                        entity.setMarket(item.optString("market"));
                        entity.setName(item.optString("name"));

                        data.add(entity);
                    }
                    mCache.addCacheItem(CacheKey.KEY_STOCK_SEARCH_RESULT + mTaskId, data);
                    bundle.putString(String.valueOf(mTaskId), CacheKey.KEY_STOCK_SEARCH_RESULT + mTaskId);
                    messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
                }
            } catch (JSONException e) {
                messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
            }
        } else {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }
}

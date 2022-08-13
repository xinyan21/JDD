package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.entities.StockEntity;
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
 * 描述：查询相关联股票列表接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-18
 * @since 1.0
 */
public class QueryAssociatedStocksRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;

    public QueryAssociatedStocksRequest(Parameter parameter, ResponseAction action) {
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
        String url = ProjectUtil.getFullPriceUrl("URL_QUERY_ASSOCIATED");

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

                        //调用接口查询股票详细，因为接口原因，所以只能这样
                        entity = QueryStockDetailsRequest.queryStockDetails(entity);

                        data.add(entity);
                    }
                    mCache.addCacheItem(CacheKey.KEY_DATA + mTaskId, data);
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

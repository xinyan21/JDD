package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.entities.FifthOrderEntity;
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
import java.util.Collections;
import java.util.List;

/**
 * 描述：查询股票详细请求
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-18
 * @since 1.0
 */
public class QueryStockDetailsRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;

    public QueryStockDetailsRequest(Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        try {
            mTaskId = Integer.valueOf(parameter.getString("taskId"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url = ConfigStore.getConfigValue("system", "PRICE_SERVER_URL")
                + ConfigStore.getConfigValue("system", "URL_QUERY_STOCK");
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
                    StockEntity entity = new StockEntity();
                    jsonResult = jsonResult.getJSONObject("result").getJSONObject("base");
                    entity.setMarket(mParameter.getString("market"));
                    entity.setCode(mParameter.getString("code"));
                    entity.setName(jsonResult.optString("name"));
                    entity.setChangePercent((float) jsonResult.optDouble("defr"));
                    entity.setNegotiableMarketCapitalization(
                            Double.isNaN(jsonResult.optDouble("mvc")) ? 0 : jsonResult.optDouble("mvc")
                    );
                    entity.setNow(jsonResult.optDouble("last"));
                    entity.setHandOver((float) jsonResult.optDouble("tr"));

                    entity.setOpen(jsonResult.optDouble("open"));
                    entity.setVolume(jsonResult.optLong("vol"));
                    entity.setTurnover(jsonResult.optDouble("turnover"));
                    entity.setHigh(jsonResult.optDouble("high"));
                    entity.setLow(jsonResult.optDouble("low"));
                    entity.setChangeValue(jsonResult.optDouble("def"));
                    entity.setInVol(jsonResult.optInt("in"));
                    entity.setOutVol(jsonResult.optInt("out"));
                    entity.setPreClose(jsonResult.optDouble("preclose"));
                    entity.setLimitUp(1.1 * entity.getPreClose());
                    entity.setLimitDown(0.9 * entity.getPreClose());
                    float pb, pe;
                    pb = (float) jsonResult.optDouble("rate");
                    pe = (float) jsonResult.optDouble("er");
                    if (Float.isNaN(pb)) {
                        pb = 0;
                    }
                    if (Float.isNaN(pe)) {
                        pe = 0;
                    }
                    entity.setPb(pb);
                    entity.setPe(pe);
                    entity.setMarketValue(jsonResult.optDouble("tmv"));

                    //五档买卖盘
                    List<FifthOrderEntity> buyFifthOrder = new ArrayList<>(5);
                    List<FifthOrderEntity> sellFifthOrder = new ArrayList<>(5);
                    JSONArray buyFifthOrderData = jsonResult.getJSONArray("buy");
                    JSONArray sellFifthOrderData = jsonResult.getJSONArray("sell");
                    FifthOrderEntity tempOrder = null;
                    String orderName = null;
                    for (int i = 0; i < 5; i++) {
                        //买五档
                        tempOrder = new FifthOrderEntity();
                        orderName = getFifthOrderName(0, i);

                        try {
                            JSONArray orderData = buyFifthOrderData.getJSONArray(i);
                            tempOrder.setOrderName(orderName);
                            tempOrder.setCount(orderData.getInt(1));
                            tempOrder.setPrice(orderData.getDouble(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        buyFifthOrder.add(tempOrder);

                        //卖五档
                        tempOrder = new FifthOrderEntity();
                        orderName = getFifthOrderName(1, i);

                        try {
                            JSONArray orderData = sellFifthOrderData.getJSONArray(i);
                            tempOrder.setOrderName(orderName);
                            tempOrder.setCount(orderData.getInt(1));
                            tempOrder.setPrice(orderData.getDouble(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sellFifthOrder.add(tempOrder);
                    }
                    entity.setBuyFifthOrder(buyFifthOrder);
                    Collections.reverse(sellFifthOrder);
                    entity.setSellFifthOrder(sellFifthOrder);

                    //保存数据到任务号对应的缓存key里面
                    mCache.addCacheItem(CacheKey.KEY_STOCK_DETAILS + mTaskId, entity);
                    //根据key去取缓存key
                    bundle.putString(String.valueOf(mTaskId), CacheKey.KEY_STOCK_DETAILS + mTaskId);
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

    public static StockEntity queryStockDetails(StockEntity entity) {
        String url = ConfigStore.getConfigValue("system", "PRICE_SERVER_URL")
                + ConfigStore.getConfigValue("system", "URL_QUERY_STOCK");
        HttpRequest request = new HttpRequest();
        Parameter parameter = new Parameter();
        parameter.addParameter("market", entity.getMarket());
        parameter.addParameter("code", entity.getCode());
        byte[] bytes = request.get(url, parameter);
        if (null != bytes) {
            String strResult = null;
            try {
                strResult = new String(bytes, ConfigStore.getConfigValue("system", "CHARSET"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (StringUtils.isEmpty(strResult)) {
                return entity;
            }
            try {
                JSONObject jsonResult = new JSONObject(strResult);
                if (1 == jsonResult.optInt("code")) {
                    jsonResult = jsonResult.getJSONObject("result").getJSONObject("base");

                    entity.setName(jsonResult.optString("name"));
                    entity.setChangePercent((float) jsonResult.optDouble("defr"));
                    entity.setNegotiableMarketCapitalization(jsonResult.optDouble("mvc"));
                    entity.setNow(jsonResult.optDouble("last"));
                    entity.setHandOver((float) jsonResult.optDouble("tr"));

                    entity.setOpen(jsonResult.optDouble("open"));
                    entity.setVolume(jsonResult.optLong("vol"));
                    entity.setTurnover(jsonResult.optDouble("turnover"));
                    entity.setHigh(jsonResult.optDouble("high"));
                    entity.setLow(jsonResult.optDouble("low"));
                    entity.setChangeValue(jsonResult.optDouble("def"));
                    entity.setInVol(jsonResult.optInt("in"));
                    entity.setOutVol(jsonResult.optInt("out"));
                    entity.setPreClose(jsonResult.optDouble("preclose"));

                    //五档买卖盘
                    List<FifthOrderEntity> buyFifthOrder = new ArrayList<>(5);
                    List<FifthOrderEntity> sellFifthOrder = new ArrayList<>(5);
                    JSONArray buyFifthOrderData = jsonResult.getJSONArray("buy");
                    JSONArray sellFifthOrderData = jsonResult.getJSONArray("sell");
                    FifthOrderEntity tempOrder = null;
                    String orderName = null;
                    for (int i = 0; i < 5; i++) {
                        //买五档
                        tempOrder = new FifthOrderEntity();
                        orderName = getFifthOrderName(0, i);

                        try {
                            JSONArray orderData = buyFifthOrderData.getJSONArray(i);
                            tempOrder.setOrderName(orderName);
                            tempOrder.setCount(orderData.getInt(1));
                            tempOrder.setPrice(orderData.getDouble(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        buyFifthOrder.add(tempOrder);

                        //卖五档
                        tempOrder = new FifthOrderEntity();
                        orderName = getFifthOrderName(1, i);

                        try {
                            JSONArray orderData = sellFifthOrderData.getJSONArray(i);
                            tempOrder.setOrderName(orderName);
                            tempOrder.setCount(orderData.getInt(1));
                            tempOrder.setPrice(orderData.getDouble(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sellFifthOrder.add(tempOrder);
                    }

                    entity.setSellFifthOrder(sellFifthOrder);
                    Collections.reverse(sellFifthOrder);
                    entity.setBuyFifthOrder(buyFifthOrder);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    /**
     * 获取五档买卖盘的档数名称，如买一，卖二
     *
     * @param type  买档位0，卖档为1
     * @param order 档数
     * @return 档数名称
     */
    private static String getFifthOrderName(int type, int order) {
        StringBuffer orderName = new StringBuffer();
        if (0 == type) {
            orderName.append("买");
        } else if (1 == type) {
            orderName.append("卖");
        }
        switch (order) {
            case 0:
                orderName.append("一");

                break;
            case 1:
                orderName.append("二");

                break;
            case 2:
                orderName.append("三");

                break;
            case 3:
                orderName.append("四");

                break;
            case 4:
                orderName.append("五");

                break;
        }
        return orderName.toString();
    }
}

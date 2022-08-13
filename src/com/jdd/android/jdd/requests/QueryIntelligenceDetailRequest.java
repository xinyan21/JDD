package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.constants.function.QueryIntelligencesFunction;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.entities.StockPlanEntity;
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
 * 描述：查询情报详情请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-24
 * @since 1.0
 */
public class QueryIntelligenceDetailRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;
    private String mUrl;

    public QueryIntelligenceDetailRequest(int taskId, String url, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        mUrl = url;
        mCacheKey = CacheKey.KEY_EXPERIENCE_DETAIL;
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
//                if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                JSONObject item = jsonResult.getJSONObject(QueryIntelligencesFunction.DATA);
                IntelligenceEntity entity;
                entity = new IntelligenceEntity();
                entity.setPayed("y".equals(jsonResult.optString(QueryIntelligencesFunction.IS_PAYED)));
                entity.setGoodNum(item.optInt(QueryIntelligencesFunction.GOOD));
                entity.setGreatNum(item.optInt(QueryIntelligencesFunction.TOP));
                entity.setNormalNum(item.optInt(QueryIntelligencesFunction.NORMAL));
                entity.setArticleId(item.optLong(QueryIntelligencesFunction.ID));
                entity.setAuthorId(item.optLong(QueryIntelligencesFunction.CUSTOMER_ID));
                entity.setAuthorName(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.NICKNAME))
                );
                entity.setCategory(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.TYPE))
                );
                entity.setUpdateDate(
                        item.optLong(QueryIntelligencesFunction.UPDATE_DATE)
                );
                entity.setUpdateBy(item.optString(QueryIntelligencesFunction.UPDATE_BY));
                entity.setTitle(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.TITLE))
                );
                entity.setSummary(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.SUMMARY))
                );
                entity.setTags(item.optString(QueryIntelligencesFunction.TAG));
                entity.setCreateDate(item.optLong(QueryIntelligencesFunction.CREATE_DATE));
                entity.setBadNum(item.optInt(QueryIntelligencesFunction.BAD));
                entity.setTerribleNum(item.optInt(QueryIntelligencesFunction.TERRIBLE));
                entity.setFundamentals(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.FUNDAMENTALS))
                );
                entity.setFuture(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.FUTURE))
                );
                entity.setRisk(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.RISK))
                );
                entity.setRecommendReason(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.COMMEND_REASON))
                );
                entity.setLongShortPosition(
                        item.optString(QueryIntelligencesFunction.LONG_SHORT_POSITION)
                );
                entity.setPrice((float) item.optDouble(QueryIntelligencesFunction.PRICE));
                entity.setIndustry(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.INDUSTRY))
                );
                entity.setArea(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.AREA))
                );
                entity.setLongShortPosition(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.LONG_SHORT_POSITION))
                );
                entity.setInvestStyle(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.STYLE))
                );
                entity.setTheme(ProjectUtil.replaceNullStringAsEmpty(
                        item.optString(QueryIntelligencesFunction.THEME))
                );

                StockPlanEntity stockPlanEntity = new StockPlanEntity();
                stockPlanEntity.setBuyPrice(item.optDouble(QueryIntelligencesFunction.BUY_PRICE));
                stockPlanEntity.setHoldingDay(item.optInt(QueryIntelligencesFunction.HOLDING_DAY));
                stockPlanEntity.setPositions(
                        (float) item.optDouble(QueryIntelligencesFunction.POSITIONS)
                );
                stockPlanEntity.setSellPrice(
                        item.optDouble(QueryIntelligencesFunction.SELL_PRICE)
                );
                stockPlanEntity.setStockName(
                        item.optString(QueryIntelligencesFunction.STOCK_NAME)
                );
                stockPlanEntity.setStockCode(
                        item.optString(QueryIntelligencesFunction.STOCK_CODE)
                );
                stockPlanEntity.setStopProfit(
                        (float) item.optDouble(QueryIntelligencesFunction.STOP_PROFIT)
                );
                stockPlanEntity.setStopLoss(
                        (float) item.optDouble(QueryIntelligencesFunction.STOP_LOSS)
                );
                entity.setStockPlanEntity(stockPlanEntity);

                mCache.addCacheItem(mCacheKey + mTaskId, entity);
                bundle.putString(String.valueOf(mTaskId), mCacheKey + mTaskId);
                bundle.putInt(
                        QueryIntelligencesFunction.PRE_TOTAL,
                        jsonResult.optInt(QueryIntelligencesFunction.PRE_TOTAL));
                messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
            }
        } else {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }
}

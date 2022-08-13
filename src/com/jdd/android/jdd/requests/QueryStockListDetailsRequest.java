package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.entities.StockEntity;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.ResponseAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：查询涨幅榜接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-18
 * @since 1.0
 */
public class QueryStockListDetailsRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private ResponseAction mAction;
    private int mTaskId;
    private List<StockEntity> mStockEntities;
    private String mCacheKey;

    public QueryStockListDetailsRequest(List<StockEntity> stockList, int taskId, ResponseAction action) {
        mAction = action;
        mStockEntities = stockList;
        mTaskId = taskId;
        mCacheKey = CacheKey.KEY_DATA + mTaskId;
    }

    public QueryStockListDetailsRequest(
            List<StockEntity> stockList, int taskId, String cacheKey, ResponseAction action) {
        mAction = action;
        mStockEntities = stockList;
        mTaskId = taskId;
        mCacheKey = cacheKey + mTaskId;
    }

    @Override
    public void handler(MessageAction messageAction) {
        List<StockEntity> mResultEntities = new ArrayList<>();
        if (null == mStockEntities) {
            return;
        }
        for (StockEntity entity : mStockEntities) {
            mResultEntities.add(QueryStockDetailsRequest.queryStockDetails(entity));
        }

        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);
        //保存数据到任务号对应的缓存key里面
        mCache.addCacheItem(mCacheKey, mResultEntities);
        //根据key去取缓存key
        bundle.putString(String.valueOf(mTaskId), mCacheKey);
        messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
    }
}

package com.jdd.android.jdd.requests;

import android.content.Context;
import android.os.Bundle;
import com.jdd.android.jdd.db.SelfSelectionStocksManager;
import com.jdd.android.jdd.entities.StockEntity;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.ResponseAction;

import java.util.List;

/**
 * 描述：删除自选请求
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-16
 * @since 1.0
 */
public class DeleteSelfSelectionRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private ResponseAction mAction;
    private int mTaskId;
    private List<StockEntity> mStockEntities;
    private Context mContext;

    public DeleteSelfSelectionRequest(
            Context context, int taskId, List<StockEntity> entities, ResponseAction action) {
        mContext = context;
        mAction = action;
        mTaskId = taskId;
        mStockEntities = entities;
    }

    @Override
    public void handler(MessageAction messageAction) {
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);
        try {
            for (StockEntity entity : mStockEntities) {
                SelfSelectionStocksManager.getInstance(mContext).delete(entity);
            }
            messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
        } catch (Exception e) {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }
}

package com.jdd.android.jdd.actions;

import android.content.Context;
import android.os.Bundle;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.ResponseAction;

/**
 * 描述：查询涨幅榜列表处理
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-18
 * @since 1.0
 */
public class QueryRoseListAction extends ResponseAction {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private IQueryServer mIQuery;

    public QueryRoseListAction(IQueryServer mIQuery) {
        this.mIQuery = mIQuery;
    }

    @Override
    protected void onOK(Context context, Bundle bundle) {
            mIQuery.onQuerySuccess(bundle.getInt("taskId"), bundle);
    }

    @Override
    protected void onServerError(Context context, Bundle bundle) {
        mIQuery.onServerError(bundle.getInt("taskId"), bundle);
    }

    @Override
    protected void onNetError(Context context, Bundle bundle) {
        mIQuery.onNetError(bundle.getInt("taskId"), bundle);
    }

    @Override
     protected void onInternalError(Context context, Bundle bundle) {
        mIQuery.onInternalError(bundle.getInt("taskId"), bundle);
    }
}

package com.jdd.android.jdd.actions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.jdd.android.jdd.constants.TaskId;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.ResponseAction;

/**
 * 描述：查询接口响应
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-18
 * @since 1.0
 */
public class QueryAction extends ResponseAction {
    private IQueryServer mIQuery;

    public QueryAction(IQueryServer mIQuery) {
        this.mIQuery = mIQuery;
    }

    @Override
    protected void onOK(Context context, final Bundle bundle) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIQuery.onQuerySuccess(bundle.getInt(TaskId.TASK_ID), bundle);
                }
            });
        } else {
            mIQuery.onQuerySuccess(bundle.getInt(TaskId.TASK_ID), bundle);
        }
    }

    @Override
    protected void onServerError(Context context, final Bundle bundle) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIQuery.onServerError(bundle.getInt(TaskId.TASK_ID), bundle);
                }
            });
        } else {
            mIQuery.onServerError(bundle.getInt(TaskId.TASK_ID), bundle);
        }
    }

    @Override
    protected void onNetError(Context context, final Bundle bundle) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIQuery.onNetError(bundle.getInt(TaskId.TASK_ID), bundle);
                }
            });
        } else {
            mIQuery.onNetError(bundle.getInt(TaskId.TASK_ID), bundle);
        }
    }

    @Override
    protected void onInternalError(Context context, final Bundle bundle) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIQuery.onInternalError(bundle.getInt(TaskId.TASK_ID), bundle);
                }
            });
        } else {
            mIQuery.onInternalError(bundle.getInt(TaskId.TASK_ID), bundle);
        }
    }
}

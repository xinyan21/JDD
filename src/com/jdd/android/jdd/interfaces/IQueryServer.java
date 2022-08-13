package com.jdd.android.jdd.interfaces;

import android.os.Bundle;

/**
 * 描述：查询数据接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation 今德道
 * @date 2015-06-24
 * @since 1.0
 */
public interface IQueryServer {
    /**
     * 查询接口成功
     * @param taskId    请求ID，用来区别接口
     * @param bundle    可以用来传数据
     */
    public void onQuerySuccess(int taskId, Bundle bundle);

    /**
     * 查询接口失败
     * @param taskId    请求ID，用来区别接口
     * @param bundle    可以用来传数据
     */
    public void onServerError(int taskId, Bundle bundle);

    /**
     * 网络异常
     * @param taskId    请求ID，用来区别接口
     * @param bundle    可以用来传数据
     */
    public void onNetError(int taskId, Bundle bundle);

    /**
     * 内部异常
     * @param taskId    请求ID，用来区别接口
     * @param bundle    可以用来传数据
     */
    public void onInternalError(int taskId, Bundle bundle);
}

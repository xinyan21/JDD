package com.jdd.android.jdd.requests;

import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.invocation.http.ResponseAction;

/**
 * 描述：查询我的情报列表请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-25
 * @since 1.0
 */
public class QueryMyIntelsRequest extends QueryIntelligencesRequest {

    public QueryMyIntelsRequest(int taskId, String cacheKey, Parameter parameter, ResponseAction action) {
        super(taskId, cacheKey, parameter, action);
        mUrl = ProjectUtil.getFullMainServerUrl("URL_QUERY_FAVORITE_INTELS");
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        super.handler(messageAction);
    }
}

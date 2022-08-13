package com.jdd.android.jdd.requests;

import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.invocation.http.ResponseAction;
import com.thinkive.adf.tools.ConfigStore;

/**
 * 描述：查询指定用户的情报列表请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-17
 * @since 1.0
 */
public class QuerySpecifiedUserIntelsRequest extends QueryIntelligencesRequest {

    public QuerySpecifiedUserIntelsRequest(int taskId, String cacheKey, Parameter parameter, ResponseAction action) {
        super(taskId, cacheKey, parameter, action);
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        mUrl = ConfigStore.getConfigValue("system", "MAIN_SERVER_URL")
                + ConfigStore.getConfigValue("system", "URL_SPECIFIED_USER_INTELS");
        super.handler(messageAction);
    }
}

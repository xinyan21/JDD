package com.jdd.android.jdd.requests;

import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.invocation.http.ResponseAction;

/**
 * 描述：删除收藏的情报
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-25
 * @last_edit 2016-02-25
 * @since 1.0
 */
public class DeleteFavoriteIntelRequest extends PushRequest {
    public DeleteFavoriteIntelRequest(int taskId, Parameter parameter, ResponseAction action) {
        super(taskId, parameter, action);
        ProjectUtil.addPlatformFlag(parameter);
        mUrl = ProjectUtil.getFullMainServerUrl("URL_DELETE_FAVORITE_INTELS");
    }

    @Override
    public void handler(MessageAction messageAction) {
        super.handler(messageAction);
    }
}

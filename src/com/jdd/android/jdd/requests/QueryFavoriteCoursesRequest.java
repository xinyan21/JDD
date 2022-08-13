package com.jdd.android.jdd.requests;

import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.invocation.http.ResponseAction;

/**
 * 描述：查询收藏的课程列表请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-07
 * @since 1.0
 */
public class QueryFavoriteCoursesRequest extends QueryLongShortCoursesRequest {

    public QueryFavoriteCoursesRequest(int taskId, Parameter parameter, ResponseAction action) {
        super(taskId, parameter, action);
        ProjectUtil.addPlatformFlag(parameter);
        mUrl = ProjectUtil.getFullMainServerUrl("URL_QUERY_MY_COURSES");
    }

    @Override
    public void handler(MessageAction messageAction) {
        super.handler(messageAction);
    }
}

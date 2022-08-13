package com.jdd.android.jdd.requests;

import android.os.Bundle;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.jdd.android.jdd.utils.HttpRequest;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.invocation.http.ResponseAction;
import com.thinkive.adf.tools.ConfigStore;
import com.thinkive.android.app_engine.utils.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 描述：此请求用来出来大部分提交数据事务，默认不返回值。
 * 需要返回值则手动把key的HashMap对象设置到ReturnKeys里面（通过setReturnKeys方法），各位为<key, null>
 * 如果返回结果里有值则自动会把null替换为对应值
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-25
 * @since 1.0
 */
public class PushRequest implements CallBack.SchedulerCallBack {
    protected String mUrl;
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    /**
     * 如果需要返回值，就把
     */
    private HashMap mReturnKeys;

    public PushRequest(int taskId, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        ProjectUtil.addPlatformFlag(parameter);
    }

    public PushRequest(
            String url, int taskId, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        mUrl = url;
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = request.post(mUrl, mParameter);
        if (null == bytes) {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
            return;
        }
        String strResult = null;
        try {
            strResult = new String(bytes, ConfigStore.getConfigValue("system", "CHARSET"));
        } catch (Exception e) {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
        if (StringUtils.isEmpty(strResult)) {
            messageAction.transferAction(ResponseAction.RESULT_NET_ERROR, bundle, mAction);
            return;
        }
        try {
            JSONObject jsonResult = new JSONObject(strResult);
            String returnFlag = ProjectUtil.getReturnFlag(jsonResult);
            bundle.putSerializable(String.valueOf(mTaskId), parseReturnValues(jsonResult));
            if (BaseServerFunction.RETURN_FLAG_SUCCESS.equals(returnFlag)) {
                messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
            } else if(BaseServerFunction.RETURN_FLAG_FAILED.equals(returnFlag)){
                messageAction.transferAction(ResponseAction.RESULT_SERVER_ERROR, bundle, mAction);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }

    private HashMap parseReturnValues(JSONObject returnValue) {
        if (null == mReturnKeys) {
            return null;
        }
        Iterator<String> iterator = mReturnKeys.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (returnValue.has(key)) {
                mReturnKeys.put(key, returnValue.opt(key));
            }
        }
        return mReturnKeys;
    }

    public void setReturnKeys(HashMap returnKeys) {
        mReturnKeys = returnKeys;
    }
}

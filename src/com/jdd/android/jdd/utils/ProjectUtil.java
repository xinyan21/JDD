package com.jdd.android.jdd.utils;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.BaseServerFunction;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.invocation.http.ResponseAction;
import com.thinkive.adf.tools.ConfigStore;
import org.json.JSONObject;

/**
 * 描述：项目工具类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-07-06
 * @since 1.0
 */
public class ProjectUtil {

    /**
     * 默认的网络异常处理
     *
     * @param context 上下文
     */
    public static void defaultNetErrorHandler(Context context) {
        PopUpComponentUtil.showShortToast(context, context.getString(R.string.error_net));
    }

    /**
     * 处理框架返回的数据，比如参数异常、网络异常、解析服务器返回结果异常
     * {\"error_info\":\"连接被重置,数据无法返回（SocketException）\",\"error_no\":\"-110\"}
     * {\"error_info\":\"请求数据超时,请检查网络或服务是否运行正常.\",\"error_no\":\"-111\"}
     * {\"error_info\":\"框架内部错误!.\",\"error_no\":\"-119\"}
     *
     * @param jsonResult    返回结果
     * @param messageAction 消息传输对象
     * @param action        处理请求的实现类对象
     */
    public static boolean handleFrameworkReturn(
            JSONObject jsonResult, MessageAction messageAction, ResponseAction action) {
        if (null == jsonResult || jsonResult.isNull("error_no")) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString(BaseServerFunction.ERR_MSG, jsonResult.optString("error_info"));
        switch (jsonResult.optInt("error_no")) {
            case -110:
                messageAction.transferAction(ResponseAction.RESULT_NET_ERROR, bundle, action);

                return true;
            case -111:
                messageAction.transferAction(ResponseAction.RESULT_NET_ERROR, bundle, action);

                return true;
            case -119:
                messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, action);

                return true;
        }
        return false;
    }

    /**
     * 替换null字符串为空字符串
     *
     * @param data 源字符串
     * @return
     */
    public static String replaceNullStringAsEmpty(String data) {
        if ("null".equals(data)) {
            return "";
        } else {
            return data;
        }
    }

    public static String getDefaultCourseIcon() {
        return ConfigStore.getConfigValue("system", "MAIN_SERVER_URL") +
                "/photo/default/sketch.JPG";
    }

    /**
     * 获取ppt图片的全路径地址
     *
     * @param baseUrl 相对路径
     * @param index   从1算起
     * @return 图片地址
     */
    public static String getPPTUrl(String baseUrl, int index) {
        StringBuffer sb = new StringBuffer();
        sb.append(ConfigStore.getConfigValue("system", "MAIN_SERVER_URL"));
        sb.append(baseUrl.replaceFirst("/", ""));
        sb.append(index);
        sb.append(".JPG");

        return sb.toString();
    }

    /**
     * 获取课程图标的全路径地址
     *
     * @param baseUrl 相对路径
     * @return 图片地址
     */
    public static String getCourseIconUrl(String baseUrl) {
        StringBuffer sb = new StringBuffer();
        sb.append(ConfigStore.getConfigValue("system", "MAIN_SERVER_URL"));
        sb.append(baseUrl.replaceFirst("/", ""));
        sb.append("sketch.JPG");

        return sb.toString();
    }

    /**
     * 增加平台标志参数
     *
     * @param param 参数
     * @return 参数
     */
    public static void addPlatformFlag(Parameter param) {
        param.addParameter("platform", "a");
    }

    /**
     * 相对路径加上域名，得到全路径地址
     *
     * @param relativeAddr 相对路径
     * @return 全路径
     */
    public static String getFullUrl(String relativeAddr) {
        relativeAddr = relativeAddr.replaceFirst("/", "");
        return ConfigStore.getConfigValue("system", "MAIN_SERVER_URL") + relativeAddr;
    }

    public static String getVideoFullUrl(String relativeAddr) {
        relativeAddr = relativeAddr.replaceFirst("/", "");
        return ConfigStore.getConfigValue("system", "VIDEO_SERVER_URL") + relativeAddr;
    }

    public static String getFullPriceUrl(String functionName) {
        return ConfigStore.getConfigValue("system", "PRICE_SERVER_URL")
                + ConfigStore.getConfigValue("system", functionName);
    }

    public static String getFullMainServerUrl(String functionName) {
        return ConfigStore.getConfigValue("system", "MAIN_SERVER_URL")
                + ConfigStore.getConfigValue("system", functionName);
    }

    public static String getReturnFlag(JSONObject data) {
        String returnFlag = BaseServerFunction.RETURN_FLAG_FAILED;
        if (!data.isNull("state")) {
            returnFlag = data.optString("state");
        }
        return returnFlag;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}

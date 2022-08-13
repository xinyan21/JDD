package com.jdd.android.jdd.constants.function;

/**
 * 描述：服务器接口基类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-27
 * @last_edit 2016-01-27
 * @since 1.0
 */
public class BaseServerFunction {
    /**
     * 行情使用数字进行标识调用结果
     */
    public static final int RETURN_CODE_SUCCESS = 1;
    public static final int RETURN_CODE_FAILED = -1;
    /**
     * 主网站使用字符串标识调用结果
     */
    public static final String RETURN_FLAG_SUCCESS = "suc";
    public static final String RETURN_FLAG_FAILED = "err";
    public static final String ERR_CONTENT = "content";
    public static final String ERR_MSG = "message";
}

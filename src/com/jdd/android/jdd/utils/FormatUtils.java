package com.jdd.android.jdd.utils;

import java.text.DecimalFormat;

/**
 * 描述：数据格式化工具类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class FormatUtils {
    public static String format2PointTwo(double value) {
        DecimalFormat df = new DecimalFormat("######0.00");

        return df.format(value);
    }

    public static String format2PointThree(double value) {
        DecimalFormat df = new DecimalFormat("######0.000");

        return df.format(value);
    }

    /**
     * 格式化金额，返回带中文单位的数字，保留2位小数点
     *
     * @param money 多少金额
     * @return 带中文单位的数字
     */
    public static String formatMoney(double money) {
        int unitYi = 100000000;
        int unitWan = 10000;

        if (money - unitYi >= 0) {
            return format2PointTwo(money / unitYi) + "亿";
        } else if (money - unitWan >= 0) {
            return format2PointTwo(money / unitWan) + "万";
        } else {
            return format2PointTwo(money);
        }
    }
}

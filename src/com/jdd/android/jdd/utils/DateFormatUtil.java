package com.jdd.android.jdd.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 文件名: DateFormatUtil
 * 作者:  熊杰
 * 日期: 2014/4/28
 * 时间: 15:56
 * 开发工具: IntelliJ IDEA 13.1.1
 * 开发语言: Java,Android
 * 开发框架:
 * 版本: v0.1
 * <strong>该类主要用于自定义数据的输出格式，如保留两位小数输出字符串，还可以定义自己的输出格式</strong>
 * <p></p>
 */

public class DateFormatUtil {

    /**
     * 返回保留两位小数后的数值
     *
     * @param d
     * @return
     */
    public static String twoPointForDouble(double d) {
        DecimalFormat df = new DecimalFormat("#####0.00");
        return df.format(d);
    }

    /**
     * 小数向上取整
     */
    public static double getCeil(double d, int n) {
        BigDecimal b = new BigDecimal(String.valueOf(d));
        b = b.divide(BigDecimal.ONE, n, BigDecimal.ROUND_CEILING);
        return b.doubleValue();
    }

    /**
     * 小数向下取整
     */
    public static double getFloor(double d, int n) {
        BigDecimal b = new BigDecimal(String.valueOf(d));
        b = b.divide(BigDecimal.ONE, n, BigDecimal.ROUND_UP);
        return b.doubleValue();
    }
}

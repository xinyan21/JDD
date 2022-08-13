package com.jdd.android.jdd.utils;

import android.database.Cursor;
import com.thinkive.adf.log.Logger;
import com.thinkive.android.app_engine.utils.StringUtils;

/**
 * 描述：数据库操作工具类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-12-14
 * @last_edit
 * @since 1.0
 */
public class DbUtil {
    /**
     * 通过字段名获取对应值
     *
     * @param cursor 游标对象
     * @param filed  字段名
     * @return 对应值
     */
    public static double getDoubleByName(Cursor cursor, String filed) {
        double value = 0;

        if (null != cursor && !StringUtils.isEmpty(filed)) {
            try {
                value = cursor.getDouble(cursor.getColumnIndex(filed));
            } catch (Exception e) {
                Logger.e(DbUtil.class, e.getMessage());
                return value;
            }
        }

        return value;
    }

    /**
     * 通过字段名获取对应值
     *
     * @param cursor 游标对象
     * @param filed  字段名
     * @return 对应值
     */
    public static String getStringByName(Cursor cursor, String filed) {
        String value = "";

        if (null != cursor && !StringUtils.isEmpty(filed)) {
            try {
                value = cursor.getString(cursor.getColumnIndex(filed));
            } catch (Exception e) {
                Logger.e(DbUtil.class, e.getMessage());
                return value;
            }
        }

        return value;
    }

    public static int getIntByName(Cursor cursor, String filed) {
        int value = 0;

        if (null != cursor && !StringUtils.isEmpty(filed)) {
            try {
                value = cursor.getInt(cursor.getColumnIndex(filed));
            } catch (Exception e) {
                Logger.e(DbUtil.class, e.getMessage());
                return value;
            }
        }

        return value;
    }

    public static long getLongByName(Cursor cursor, String filed) {
        long value = 0;

        if (null != cursor && !StringUtils.isEmpty(filed)) {
            try {
                value = cursor.getLong(cursor.getColumnIndex(filed));
            } catch (Exception e) {
                Logger.e(DbUtil.class, e.getMessage());
                return value;
            }
        }

        return value;
    }
}

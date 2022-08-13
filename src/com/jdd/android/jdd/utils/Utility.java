package com.jdd.android.jdd.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 描述：辅助类
 * </p>
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2014-07-05
 */
public class Utility {

    public static boolean checkPhoneNo(String phoneNo) {
        if (StringUtils.isEmpty(phoneNo) || phoneNo.length() != 11 || !phoneNo.startsWith("1")) {
            return false;
        }

        return true;
    }

    public static boolean checkPwd(String pwd) {
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
            return false;
        }
        return true;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * -将数字格式化为精确到小数点后2位
     *
     * @param value
     * @return
     */
    public static String formatDouble2PointTwo(double value) {
        DecimalFormat df;
        df = new DecimalFormat("######0.00");
        return df.format(value);
    }

    public static String formatDouble2(double value) {
        DecimalFormat df;
        df = new DecimalFormat("######0.00");
        // DecimalFormat df = new DecimalFormat("#.00");
        double value1 = Double.valueOf(df.format(value));
        int value2 = (int) value1;
        if (0 == (value1 - (double) value2)) {
            return df.format(value2);
        } else {
            return df.format(value1);
        }
    }

    public static String formatVolume1WithoutUnit(double volume) {
        int yi = 10000 * 10000;
        if (volume > yi) {
            if (formatDouble2PointTwo(volume / yi).length() > 5) {
                int i = (int) Math.round(Double
                        .valueOf(formatDouble2PointTwo(volume / yi)));
                return new StringBuffer().append(i).toString();
            } else {
                return new StringBuffer()
                        .append(formatDouble2PointTwo(volume / yi)).toString();
            }
        } else if (volume > 10000) {
            if (formatDouble2PointTwo(volume / 10000).length() > 5) {
                int i = (int) Math.round(Double
                        .valueOf(formatDouble2PointTwo(volume / 10000)));
                return new StringBuffer().append(i).toString();
            } else {
                return new StringBuffer()
                        .append(formatDouble2PointTwo(volume / 10000)).toString();
            }
        } else {
            return new StringBuffer().append(formatDouble2PointTwo(volume)).toString();
        }
    }

    /**
     * DecimalFormat转换最简便
     *
     * @param type
     * @param input
     * @return
     */
    public static String format(String type, double input) {
        DecimalFormat df;
        if ("0".equals(type) || "1".equals(type) || "2".equals(type)
                || "14".equals(type) || "9".equals(type) || "7".equals(type)
                || "15".equals(type) || "18".equals(type)) {
            df = new DecimalFormat("######0.00");
        } else {
            df = new DecimalFormat("######0.000");
        }
        return df.format(input);
    }


    /**
     * DecimalFormat转换最简便
     */
    public static String format(double input) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(input);
    }

    public static int compare(BigDecimal val1, BigDecimal val2) {
        int result = 0;
        if (val1.compareTo(val2) < 0) {
            result = -1;
        }
        if (val1.compareTo(val2) == 0) {
            result = 0;
        }
        if (val1.compareTo(val2) > 0) {
            result = 1;
        }
        return result;
    }

    /**
     * 获取通知栏高度
     *
     * @return
     */
    public static int getBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;// 默认为38，

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}

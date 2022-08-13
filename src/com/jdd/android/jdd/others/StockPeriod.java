package com.jdd.android.jdd.others;

import java.io.Serializable;

/**
 * 描述：股票周期分类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-04-11
 * @last_edit 2016-04-11
 * @since 1.0
 */
public enum StockPeriod implements Serializable{
    MIN_5("m5"), MIN_15("m15"), MIN_30("m30"),
    MIN_60("m60"), DAY("daily"), WEEK("week"), MONTH("month");
    private String period;

    StockPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return period;
    }
}

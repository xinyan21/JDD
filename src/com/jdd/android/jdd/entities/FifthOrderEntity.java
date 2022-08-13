package com.jdd.android.jdd.entities;

import java.io.Serializable;

/**
 * 描述：五档买卖盘实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-30
 * @since 1.0
 */
public class FifthOrderEntity implements Serializable {
    private String orderName;   //档数名称
    private double price;       //价格
    private long count;     //手数

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

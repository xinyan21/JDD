package com.jdd.android.jdd.entities;

public class PointInfo {

    //这个点距左边的距离
    private float startX;

    //当前这个点在Y轴上的高度值
    private float valueY;

    //当前这一分钟的其他信息
    private MinutePrice mMinutePrice;

    public PointInfo(float startX, float valueY, MinutePrice minutePrice) {
        this.startX = startX;
        this.valueY = valueY;
        this.mMinutePrice = minutePrice;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getValueY() {
        return valueY;
    }

    public void setValueY(float valueY) {
        this.valueY = valueY;
    }

    public MinutePrice getMinutePrice() {
        return mMinutePrice;
    }

    public void setMinutePrice(MinutePrice minutePrice) {
        this.mMinutePrice = minutePrice;
    }
}

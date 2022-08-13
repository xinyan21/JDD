package com.jdd.android.jdd.entities;

import android.os.Parcel;
import android.os.Parcelable;


public class MinutePrice implements Parcelable {

    //日期
    private String date;

    //分钟数
    private int minute;

    //现价
    private double now;

    //均价
    private double avgPrice;

    //成交量
    private double volume;

    public MinutePrice() {

    }

    public MinutePrice(String date, int minute, double now, double avgPrice, double volume) {
        this.date = date;
        this.minute = minute;
        this.now = now;
        this.avgPrice = avgPrice;
        this.volume = volume;
    }

    public MinutePrice(Parcel source) {
        this.date = source.readString();
        this.minute = source.readInt();
        this.now = source.readDouble();
        this.avgPrice = source.readDouble();
        this.volume = source.readDouble();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public double getNow() {
        return now;
    }

    public void setNow(double now) {
        this.now = now;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(minute);
        dest.writeDouble(now);
        dest.writeDouble(avgPrice);
        dest.writeDouble(volume);
    }

    public static final Creator<MinutePrice> CREATOR = new Creator<MinutePrice>() {
        @Override
        public MinutePrice createFromParcel(Parcel source) {
            return new MinutePrice(source);
        }

        @Override
        public MinutePrice[] newArray(int i) {
            return new MinutePrice[i];
        }
    };

}

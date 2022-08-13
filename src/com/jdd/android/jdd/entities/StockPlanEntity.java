package com.jdd.android.jdd.entities;

/**
 * 描述：股票操作计划信息实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-01
 * @last_edit
 * @since 1.0
 */
public class StockPlanEntity {
    private String stockName;   //股票名称
    private String stockCode;   //股票代码
    private int holdingDay;   //持仓天数
    private double buyPrice;   //买入价
    private double sellPrice;   //卖出价
    private float positions;   //仓位
    private float stopProfit;   //止盈
    private float stopLoss;   //止损

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public int getHoldingDay() {
        return holdingDay;
    }

    public void setHoldingDay(int holdingDay) {
        this.holdingDay = holdingDay;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public float getPositions() {
        return positions;
    }

    public void setPositions(float positions) {
        this.positions = positions;
    }

    public float getStopProfit() {
        return stopProfit;
    }

    public void setStopProfit(float stopProfit) {
        this.stopProfit = stopProfit;
    }

    public float getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(float stopLoss) {
        this.stopLoss = stopLoss;
    }
}

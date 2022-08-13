package com.jdd.android.jdd.entities;

import android.text.SpannableStringBuilder;
import com.jdd.android.jdd.ui.SelfSelectionStocksFragment;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：股票实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class StockEntity implements Serializable {
    private String name;        //股票名称
    private String code;        //股票代码
    private String market;        //股票市场：上海、深圳
    private float changePercent;        //涨跌幅
    private double changeValue;        //涨跌额
    private double negotiableMarketCapitalization;        //流通市值
    private double preClose;        //昨收
    private double now;        //现价
    private double low;        //最低价
    private double high;        //最高价
    private double open;        //开盘价
    private double limitUp;        //涨停
    private double limitDown;        //跌停
    private double volume;        //成交量
    private double turnover;        //成交额
    private float handOver;        //换手
    private int inVol;        //内盘
    private int outVol;        //外盘
    private long circulatingShares;        //流通股
    private float profitPerShare;        //每股收益
    private long totalShares;        //总股
    private float riseSpeed;        //涨速
    private short category;        //股票分类，见类StockCategory
    private float quantityRelative;        //量比
    private double marketValue;     //市值
    private float pb;       //市净率
    private float pe;       //市盈率
    private List<FifthOrderEntity> buyFifthOrder;    //买五档
    private List<FifthOrderEntity> sellFifthOrder;    //卖五档
    private SpannableStringBuilder candleExplanation;       //K线兵解
    private SpannableStringBuilder volExplanation;      //成交量兵解

    //自选股属性
    private int order;   //排列顺序
    private boolean isSelectedToDelete = false;
    private long userId = SelfSelectionStocksFragment.sDefaultUserID;    //用户id

    public SpannableStringBuilder getCandleExplanation() {
        return candleExplanation;
    }

    public void setCandleExplanation(SpannableStringBuilder candleExplanation) {
        this.candleExplanation = candleExplanation;
    }

    public SpannableStringBuilder getVolExplanation() {
        return volExplanation;
    }

    public void setVolExplanation(SpannableStringBuilder volExplanation) {
        this.volExplanation = volExplanation;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public float getPb() {
        return pb;
    }

    public void setPb(float pb) {
        this.pb = pb;
    }

    public float getPe() {
        return pe;
    }

    public void setPe(float pe) {
        this.pe = pe;
    }

    public boolean isSelectedToDelete() {
        return isSelectedToDelete;
    }

    public void setSelectedToDelete(boolean selectedToDelete) {
        isSelectedToDelete = selectedToDelete;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getPreClose() {
        return preClose;
    }

    public void setPreClose(double preClose) {
        this.preClose = preClose;
    }

    public List<FifthOrderEntity> getSellFifthOrder() {
        return sellFifthOrder;
    }

    public void setSellFifthOrder(List<FifthOrderEntity> sellFifthOrder) {
        this.sellFifthOrder = sellFifthOrder;
    }

    public List<FifthOrderEntity> getBuyFifthOrder() {
        return buyFifthOrder;
    }

    public void setBuyFifthOrder(List<FifthOrderEntity> buyFifthOrder) {
        this.buyFifthOrder = buyFifthOrder;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public float getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(float changePercent) {
        this.changePercent = changePercent;
    }

    public double getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(double changeValue) {
        this.changeValue = changeValue;
    }

    public double getNegotiableMarketCapitalization() {
        return negotiableMarketCapitalization;
    }

    public void setNegotiableMarketCapitalization(double negotiableMarketCapitalization) {
        this.negotiableMarketCapitalization = negotiableMarketCapitalization;
    }

    public double getNow() {
        return now;
    }

    public void setNow(double now) {
        this.now = now;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getLimitUp() {
        return limitUp;
    }

    public void setLimitUp(double limitUp) {
        this.limitUp = limitUp;
    }

    public double getLimitDown() {
        return limitDown;
    }

    public void setLimitDown(double limitDown) {
        this.limitDown = limitDown;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public float getHandOver() {
        return handOver;
    }

    public void setHandOver(float handOver) {
        this.handOver = handOver;
    }

    public float getQuantityRelative() {
        return quantityRelative;
    }

    public void setQuantityRelative(float quantityRelative) {
        this.quantityRelative = quantityRelative;
    }

    public int getInVol() {
        return inVol;
    }

    public void setInVol(int inVol) {
        this.inVol = inVol;
    }

    public int getOutVol() {
        return outVol;
    }

    public void setOutVol(int outVol) {
        this.outVol = outVol;
    }

    public long getCirculatingShares() {
        return circulatingShares;
    }

    public void setCirculatingShares(long circulatingShares) {
        this.circulatingShares = circulatingShares;
    }

    public float getProfitPerShare() {
        return profitPerShare;
    }

    public void setProfitPerShare(float profitPerShare) {
        this.profitPerShare = profitPerShare;
    }

    public long getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(long totalShares) {
        this.totalShares = totalShares;
    }

    public float getRiseSpeed() {
        return riseSpeed;
    }

    public void setRiseSpeed(float riseSpeed) {
        this.riseSpeed = riseSpeed;
    }

    public short getCategory() {
        return category;
    }

    public void setCategory(short category) {
        this.category = category;
    }
}

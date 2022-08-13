package com.jdd.android.jdd.entities;

/**
 * 描述：情报实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-04
 * @since 1.0
 */
public class IntelligenceEntity extends ArticleEntity {
    private boolean isPayed;    //是否已购买
    private float price;      //价格，为0表示免费，否则为收费
    private StockPlanEntity stockPlanEntity;    //股票操作计划
    private int greatNum;       //牛评数
    private int goodNum;       //好评数
    private int normalNum;       //行评数
    private int badNum;     //差评数
    private int terribleNum;    //烂评数
    private String fundamentals;    //基本面
    private String future;    //未来展望
    private String risk;    //风险
    private String recommendReason;
    private String industry;    //行业
    private String longShortPosition;    //多空位置
    private String investStyle;     //风格
    private String theme;     //题材
    private String area;     //地域
    private String headPortraitUrl;       //头像地址

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public String getInvestStyle() {
        return investStyle;
    }

    public void setInvestStyle(String investStyle) {
        this.investStyle = investStyle;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
    }

    public StockPlanEntity getStockPlanEntity() {
        return stockPlanEntity;
    }

    public void setStockPlanEntity(StockPlanEntity stockPlanEntity) {
        this.stockPlanEntity = stockPlanEntity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getGreatNum() {
        return greatNum;
    }

    public void setGreatNum(int greatNum) {
        this.greatNum = greatNum;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getNormalNum() {
        return normalNum;
    }

    public void setNormalNum(int normalNum) {
        this.normalNum = normalNum;
    }

    public int getBadNum() {
        return badNum;
    }

    public void setBadNum(int badNum) {
        this.badNum = badNum;
    }

    public int getTerribleNum() {
        return terribleNum;
    }

    public void setTerribleNum(int terribleNum) {
        this.terribleNum = terribleNum;
    }

    public String getFundamentals() {
        return fundamentals;
    }

    public void setFundamentals(String fundamentals) {
        this.fundamentals = fundamentals;
    }

    public String getFuture() {
        return future;
    }

    public void setFuture(String future) {
        this.future = future;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLongShortPosition() {
        return longShortPosition;
    }

    public void setLongShortPosition(String longShortPosition) {
        this.longShortPosition = longShortPosition;
    }
}

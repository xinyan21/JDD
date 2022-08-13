package com.jdd.android.jdd.entities;

/**
 * 描述：智文实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-16
 * @since 1.0
 */
public class ExperienceEntity extends ArticleEntity{
    private int greatNum;       //牛数
    private int goodNum;       //好数
    private int normalNum;       //行数
    private String headPortraitUrl;       //头像地址
    private String rule;        //权限
    private String industry;    //行业

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
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

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }
}

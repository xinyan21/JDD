package com.jdd.android.jdd.entities;

/**
 * 描述：课程分类实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-08-25
 * @since 1.0
 */
public class CourseCategoryEntity {
    private String iconResName;     //图标资源名称
    private String categoryName;      //课程分类名称
    private String detailCategory;      //详细分类：必修、选修、技能培训
    private double totalTime;      //总课时
    private String about;      //课程简介

    public String getIconResName() {
        return iconResName;
    }

    public void setIconResName(String iconResName) {
        this.iconResName = iconResName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDetailCategory() {
        return detailCategory;
    }

    public void setDetailCategory(String detailCategory) {
        this.detailCategory = detailCategory;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}

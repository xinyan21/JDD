package com.jdd.android.jdd.entities;

import java.io.Serializable;

/**
 * 描述：课程实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-08-26
 * @since 1.0
 */
public class CourseEntity implements Serializable{
    private String courseName;       //课程名称
    private String about;       //简介
    private String teachingMode;    //授课方式
    private long learnedCount;     //已学习人数
    private String pptUrl;
    private int pptCount;
    private String videoUrl;
    private String category;
    private float videoTime;
    private float price;
    private long id;
    private long createDate;      //创建日期
    private long updateDate;      //更新日期

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getPptUrl() {
        return pptUrl;
    }

    public void setPptUrl(String pptUrl) {
        this.pptUrl = pptUrl;
    }

    public int getPptCount() {
        return pptCount;
    }

    public void setPptCount(int pptCount) {
        this.pptCount = pptCount;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(float videoTime) {
        this.videoTime = videoTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTeachingMode() {
        return teachingMode;
    }

    public void setTeachingMode(String teachingMode) {
        this.teachingMode = teachingMode;
    }

    public long getLearnedCount() {
        return learnedCount;
    }

    public void setLearnedCount(long learnedCount) {
        this.learnedCount = learnedCount;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}

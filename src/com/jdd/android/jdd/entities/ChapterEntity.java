package com.jdd.android.jdd.entities;

/**
 * 描述：课程章节实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-08-26
 * @since 1.0
 */
public class ChapterEntity {
    private String about;       //简介
    private String content;       //内容
    private String url;       //网络内容
    private String contentType;           //内容类型，视频/ppt/图文

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

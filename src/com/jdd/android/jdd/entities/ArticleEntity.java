package com.jdd.android.jdd.entities;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 描述：智文（普通文章）实体类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit
 * @since 1.0
 */
public class ArticleEntity implements Serializable{
    private long articleId;     //文章id
    private long authorId;      //作者的用户id
    private String authorName;      //作者名称
    private long createDate;      //创建日期
    private long updateDate;      //更新日期
    private String updateBy;        //更新人
    private String content;      //内容
    private String title;      //标题
    private boolean isFavorites;      //是否已收藏
    private String category;     //文章分类：创富感悟、职业心得
    private String tags;      //标签
    private String summary;     //简介
    private String type;        //类型

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public boolean isFavorites() {
        return isFavorites;
    }

    public void setFavorites(boolean favorites) {
        isFavorites = favorites;
    }

    private HashMap<String, Long> comments;      //点评

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Long> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, Long> comments) {
        this.comments = comments;
    }
}

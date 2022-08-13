package com.jdd.android.jdd.constants.function;

/**
 * 描述：文章相关接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-17
 * @last_edit 2016-02-17
 * @since 1.0
 */
public class ArticleFunction {
    public static final String IN_PAGE = "page";
    //分析师主页文章列表查询参数：0为智文，1为情报
    public static final String IN_ARTICLE_TYPE = "idx";
    public static final String IN_USER_ID = "cId";

    public static final String ID = "id";
    public static final String CREATE_DATE = "createdDate";
    public static final String UPDATE_DATE = "updatedDate";
    public static final String UPDATE_BY = "updateBy";
    public static final String PRE_TOTAL = "preTotal";      //从当前页算起，预读4页的数据条数
}

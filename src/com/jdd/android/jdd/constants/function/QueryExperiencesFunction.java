package com.jdd.android.jdd.constants.function;

/**
 * 描述：查询智文列表接口和查询智文详情接口合并在一起
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-16
 * @since 1.0
 */
public class QueryExperiencesFunction extends ArticleFunction {
    //入参
    public static final String IN_SEARCH_KEY = "searchKey";
    public static final String IN_INDUSTRY = "industry";
    public static final String IN_TYPE_ = "type";
    public static final String IN_ORDER_NAME = "orderName";
    public static final String IN_ORDER_VALUE = "orderValue";
    public static final String IN_ARTICLE_ID = "sId";
    //入参字典
    public static final String ORDER_NAME_TIME = "timeOrder";
    public static final String ORDER_NAME_RECOMMEND = "recommendOrder";
    //出参
    public static final String DATA = "sentiment";
    public static final String DATA_LIST = "sentimentList";
    public static final String CONTENT = "content";
    public static final String CUSTOMER_ID = "customerId";
    public static final String GOOD = "good";
    public static final String HEAD_PHOTO_URL = "headPhotoUrl";
    public static final String INDUSTRY = "industry";
    public static final String NICKNAME = "nickName";
    public static final String NORMAL = "normal";
    public static final String RULE = "rule";       //权限(pu:公开,fs:粉丝,pv:私人)
    public static final String STATUS = "status";       //状态（I1:发布，I3:草稿)
    public static final String SUMMARY = "summary";
    public static final String TAG = "tag";
    public static final String TITLE = "title";
    public static final String TOP = "top";     //牛
    public static final String TYPE = "type";
}

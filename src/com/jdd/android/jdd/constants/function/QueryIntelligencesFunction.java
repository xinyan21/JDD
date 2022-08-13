package com.jdd.android.jdd.constants.function;

/**
 * 描述：查询情报列表接口和查询情报详情接口合并在一起
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-16
 * @since 1.0
 */
public class QueryIntelligencesFunction extends ArticleFunction {
    //入参
    public static final String IN_SEARCH_KEY = "searchKey";
    public static final String IN_INDUSTRY = "industry";
    public static final String IN_TYPE_ = "type";
    public static final String IN_ORDER_NAME = "orderName";
    public static final String IN_ORDER_VALUE = "orderValue";
    public static final String IN_ARTICLE_ID = "id";
    public static final String IN_THEME = "theme";
    public static final String IN_STYLE = "style";
    public static final String IN_AREA = "area";
    public static final String IN_NEED_PAY = "worth";
    public static final String IN_DURATION = "time";
    //入参字典
    public static final String ORDER_NAME_TIME = "timeOrder";
    public static final String ORDER_NAME_HOT = "hotOrder";
    public static final String ORDER_NAME_TOP = "topOrder";
    public static final String ORDER_VALUE_UP = "up";
    public static final String ORDER_VALUE_DOWN = "down";
    //出参
    public static final String DATA = "intelligence";
    public static final String DATA_LIST = "intelligenceList";
    public static final String DATA_LIST2 = "intellignenceList";
    public static final String CUSTOMER_ID = "customerId";
    public static final String GOOD = "good";
    public static final String NICKNAME = "nickName";
    public static final String NORMAL = "normal";
    public static final String SUMMARY = "summary";
    public static final String TAG = "tag";
    public static final String TITLE = "title";
    public static final String TOP = "top";     //牛
    public static final String TYPE = "type";
    public static final String BAD = "bad";
    public static final String TERRIBLE = "poor";
    public static final String FUNDAMENTALS = "contentBase";
    public static final String FUTURE = "contentFuture";
    public static final String RISK = "contentRisk";
    public static final String COMMEND_REASON = "contentReason";
    public static final String INDUSTRY = "industry";
    public static final String LONG_SHORT_POSITION = "tagBBI";
    public static final String THEME = "tagTheme";
    public static final String STYLE = "tagStyle";
    public static final String AREA = "tagArea";
    public static final String PRICE = "cost";
    public static final String IS_PAYED = "isPay";
    //股票操作计划参数
    public static final String BUY_PRICE = "inPrice";
    public static final String STOCK_NAME = "stockName";
    public static final String STOCK_CODE = "stockCode";
    public static final String HOLDING_DAY = "stockSpace";
    public static final String SELL_PRICE = "outPrice";
    public static final String POSITIONS = "stockSpace";
    public static final String STOP_PROFIT = "surplusRatio";
    public static final String STOP_LOSS = "stopRatio";
}

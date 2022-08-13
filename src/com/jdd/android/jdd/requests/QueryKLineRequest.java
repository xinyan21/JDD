package com.jdd.android.jdd.requests;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import cn.limc.androidcharts.entity.*;
import com.jdd.android.jdd.others.StockPeriod;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.HttpRequest;
import com.thinkive.adf.invocation.http.ResponseAction;
import com.thinkive.adf.tools.ConfigStore;
import com.thinkive.android.app_engine.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述：查询K线数据
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-07-08
 * @since 1.0
 */
public class QueryKLineRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private StockPeriod mStockPeriod;

    public QueryKLineRequest(
            Parameter parameter, int taskId, StockPeriod period, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        mStockPeriod = period;
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        String url = ProjectUtil.getFullPriceUrl("URL_QUERY_KLINE");

        HttpRequest request = new HttpRequest();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        byte[] bytes = null;
        try {
            bytes = request.get(url, mParameter);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (null != bytes) {
            String strResult = null;
            try {
                strResult = new String(bytes, ConfigStore.getConfigValue("system", "CHARSET"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (StringUtils.isEmpty(strResult)) {
                messageAction.transferAction(ResponseAction.RESULT_NET_ERROR, bundle, mAction);
                return;
            }
            try {
                JSONObject jsonResult = new JSONObject(strResult);
                if (1 == jsonResult.optInt("code")) {
                    jsonResult = jsonResult.getJSONObject("result");
                    JSONArray kLineData = jsonResult.getJSONArray("stock");
                    if (null == kLineData || kLineData.length() < 1) {
                        messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);

                        return;
                    }
                    ArrayList<IStickEntity> ohlcs = null; // 高低价
                    ArrayList<IStickEntity> vols = null; // 成交量
                    ohlcs = new ArrayList<IStickEntity>();
                    vols = new ArrayList<IStickEntity>();
                    int index = 0;
                    IStickEntity ohlc = null;
                    IStickEntity vol = null;
                    JSONObject item = null;
                    int color = 0;
                    int dataLength = kLineData.length();

                    //K线
                    for (index = 0; index < dataLength; index++) {
                        item = kLineData.getJSONObject(index);
                        double open, close;
                        open = item.optDouble("open");
                        close = item.optDouble("close");
                        String strDate = DateFormat.format("yyyyMMdd", item.optLong("time")).toString();
                        int nDate = 0;
                        try {
                            nDate = Integer.valueOf(strDate);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        ohlc = new OHLCEntity(
                                open, item.optDouble("high"), item.optDouble("low"), close, nDate
                        );
                        if (open > close) {
                            color = Color.parseColor("#00CFCF");
                        } else if (open <= close) {
                            color = Color.RED;
                        }
//                        else {
//                            color = Color.GRAY;
//                        }
                        vol = new ColoredStickEntity(item.optDouble("vol"), 0, item.optInt("date"), color);

                        ohlcs.add(ohlc);
                        vols.add(vol);
                    }

                    String baseCacheKey = mParameter.getString("market")
                            + mParameter.getString("code") + mStockPeriod.toString();
                    mCache.addCacheItem(baseCacheKey + "ohlc", ohlcs);
                    mCache.addCacheItem(baseCacheKey + "vol", vols);
                    mCache.addCacheItem(
                            baseCacheKey + "direction", parseBattleDirection(jsonResult.getJSONObject("canvas")));
                    mCache.addCacheItem(
                            baseCacheKey + "morale", parseBattleMorale(jsonResult.getJSONObject("canvas")));
                    mCache.addCacheItem(
                            baseCacheKey + "force", parseBattleForce(jsonResult.getJSONObject("canvas")));
                    mCache.addCacheItem(
                            baseCacheKey + "explanation",
                            parseLongShortExplanation(jsonResult.getJSONObject("text"),
                                    baseCacheKey));

                    messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
            }
        } else {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }

    /**
     * 解析多空方向
     *
     * @param source
     * @return
     */
    private List<LineEntity<DateValueEntity>> parseBattleDirection(JSONObject source) {
        List<LineEntity<DateValueEntity>> maLines = new ArrayList<>();
        try {
            LineEntity<DateValueEntity> soliderLine = new LineEntity<>();
            LineEntity<DateValueEntity> commanderLine = new LineEntity<>();
            LineEntity<DateValueEntity> commanderInChiefLine = new LineEntity<>();
            LineEntity<DateValueEntity> peopleLine = new LineEntity<>();
            LineEntity<DateValueEntity> foodLine = new LineEntity<>();
            LineEntity<DateValueEntity> shuai = new LineEntity<>();

            JSONObject item;
            item = source.getJSONObject("兵");
            soliderLine.setTitle("兵");
            soliderLine.setLineData(parseLineData(item.optJSONArray("position")));
            soliderLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            item = source.getJSONObject("方向");
            commanderLine.setTitle("方向");
            commanderLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            commanderLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("将");
            commanderInChiefLine.setTitle("将");
            commanderInChiefLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            commanderInChiefLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("压力");
            peopleLine.setTitle("压力");
            peopleLine.setLineColor(Color.LTGRAY);
            peopleLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("离五日线");
            foodLine.setTitle("离兵线");
            foodLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            foodLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("帅");
            shuai.setTitle("帅");
            shuai.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            shuai.setLineData(parseLineData(item.optJSONArray("position")));

            maLines.add(soliderLine);
            maLines.add(commanderInChiefLine);
            maLines.add(shuai);
            maLines.add(commanderLine);
            maLines.add(peopleLine);
            maLines.add(foodLine);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return maLines;
    }

    /**
     * 解析多空力量
     *
     * @param source 数据源
     * @return 多空力量
     */
    private List<LineEntity<DateValueEntity>> parseBattleForce(JSONObject source) {
        List<LineEntity<DateValueEntity>> maLines = new ArrayList<>();
        try {
            LineEntity<DateValueEntity> publicFeelingLine = new LineEntity<>();     //人心
            LineEntity<DateValueEntity> longLine = new LineEntity<>();      //多头
            LineEntity<DateValueEntity> shortLine = new LineEntity<>();     //空头
            LineEntity<DateValueEntity> strengthLine = new LineEntity<>();     //空头

            JSONObject item;
            item = source.getJSONObject("人心");
            publicFeelingLine.setTitle("人心");
            publicFeelingLine.setLineData(parseLineData(item.optJSONArray("position")));
            publicFeelingLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            item = source.getJSONObject("多头");
            longLine.setTitle("多头");
            longLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            longLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("空头");
            shortLine.setTitle("空头");
            shortLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            shortLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("力");
            strengthLine.setTitle("力");
            strengthLine.setLineColor(Color.GRAY);
            strengthLine.setLineData(parseLineData(item.optJSONArray("position")));

            maLines.add(longLine);
            maLines.add(shortLine);
            maLines.add(publicFeelingLine);
            maLines.add(strengthLine);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return maLines;
    }

    /**
     * 解析多空士气，绘制彩带的四条线
     *
     * @param source
     * @return
     */
    private List<LineEntity<DateValueEntity>> parseBattleMorale(JSONObject source) {
        List<LineEntity<DateValueEntity>> maLines = new ArrayList<>();
        try {
            //多前锋
            LineEntity<DateValueEntity> longForwardLine = new LineEntity<>();
            //空前锋
            LineEntity<DateValueEntity> shortForwardLine = new LineEntity<>();
            //主力
            LineEntity<DateValueEntity> mainForceLine = new LineEntity<>();
            //趋势
            LineEntity<DateValueEntity> trendLine = new LineEntity<>();

            JSONObject item = null;
            item = source.getJSONObject("多方");
            longForwardLine.setTitle("多方");
            longForwardLine.setLineData(parseLineData(item.optJSONArray("position")));
            longForwardLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            item = source.getJSONObject("空方");
            shortForwardLine.setTitle("空方");
            shortForwardLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            shortForwardLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("主力");
            mainForceLine.setTitle("主力");
            mainForceLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            mainForceLine.setLineData(parseLineData(item.optJSONArray("position")));
            item = source.getJSONObject("士气");
            trendLine.setTitle("士气");
            trendLine.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            trendLine.setLineData(parseLineData(item.optJSONArray("position")));

            maLines.add(longForwardLine);
            maLines.add(shortForwardLine);
            maLines.add(mainForceLine);
            maLines.add(trendLine);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return maLines;
    }

    private List<LineEntity<DateValueEntity>> parseLongShortExplanation(JSONObject source, String baseCacheKey) {
        List<LineEntity<DateValueEntity>> explanations = new ArrayList<>();
        try {
            LineEntity<DateValueEntity> lightIn = new LineEntity<>();
            LineEntity<DateValueEntity> clearHold = new LineEntity<>();
            LineEntity<DateValueEntity> riskRange = new LineEntity<>();
            LineEntity<DateValueEntity> carefulHold = new LineEntity<>();
            LineEntity<DateValueEntity> hitPressure = new LineEntity<>();
            LineEntity<DateValueEntity> accidentRisk = new LineEntity<>();
            LineEntity<DateValueEntity> risk = new LineEntity<>();
            LineEntity<DateValueEntity> bigRisk = new LineEntity<>();
            LineEntity<DateValueEntity> hold = new LineEntity<>();

            JSONObject item = null;
            String lineName = "轻仓介入";
            HashMap<String, String> hmTips = new HashMap<>();
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            lightIn.setTitle(lineName);
            lightIn.setLineData(parseLineData(item.optJSONArray("position")));
            lightIn.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            lineName = "空仓军令";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            clearHold.setTitle(lineName);
            clearHold.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            clearHold.setLineData(parseLineData(item.optJSONArray("position")));
            lineName = "风险波段";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            riskRange.setTitle(lineName);
            riskRange.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            riskRange.setLineData(parseLineData(item.optJSONArray("position")));
            lineName = "小心持有";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            carefulHold.setTitle(lineName);
            carefulHold.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            carefulHold.setLineData(parseLineData(item.optJSONArray("position")));
            lineName = "冲击压力线";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            hitPressure.setTitle(lineName);
            hitPressure.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            hitPressure.setLineData(parseLineData(item.optJSONArray("position")));
            lineName = "突发风险";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            accidentRisk.setTitle(lineName);
            accidentRisk.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            accidentRisk.setLineData(parseLineData(item.optJSONArray("position")));
            lineName = "风险";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            risk.setTitle(lineName);
            risk.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            risk.setLineData(parseLineData(item.optJSONArray("position")));
            lineName = "大风险";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            bigRisk.setTitle(lineName);
            bigRisk.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            bigRisk.setLineData(parseLineData(item.optJSONArray("position")));
            lineName = "持有仓位";
            item = source.getJSONObject(lineName);
            hmTips.put(lineName, item.optString("tip"));
            hold.setTitle(lineName);
            hold.setLineColor(parseColor(item.getJSONObject("opts").optString("color")));
            hold.setLineData(parseLineData(item.optJSONArray("position")));

            explanations.add(lightIn);
            explanations.add(clearHold);
            explanations.add(riskRange);
            explanations.add(carefulHold);
            explanations.add(hitPressure);
            explanations.add(accidentRisk);
            explanations.add(risk);
            explanations.add(bigRisk);
            explanations.add(hold);

            mCache.addCacheItem(baseCacheKey + "tips", hmTips);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return explanations;
    }

    private List<DateValueEntity> parseLineData(JSONArray source) {
        List<DateValueEntity> maLineData = new ArrayList<>();
        DateValueEntity entity = null;
        if (null == source) {
            return null;
        }
        for (int i = 0; i < source.length(); i++) {
            Double item = source.optDouble(i);
            if (item.isNaN()) {
                item = 0.0;
            }
            entity = new DateValueEntity(item, 0);
            maLineData.add(entity);
        }

        return maLineData;
    }

    private int parseColor(String color) {
        if (color.startsWith("#") && color.length() == 7) {
            return Color.parseColor(color);
        } else if (color.equals("blue")) {
            return Color.BLUE;
        } else if (color.equals("yellow")) {
            return Color.YELLOW;
        } else if (color.equals("green")) {
            return Color.GREEN;
        } else if (color.equals("red")) {
            return Color.RED;
        } else if (color.equals("blue")) {
            return Color.BLUE;
        } else if (color.equals("white")) {
            return Color.WHITE;
        } else if (color.equals("#fff")) {
            return Color.WHITE;
        } else {
            return 0;
        }
    }
}

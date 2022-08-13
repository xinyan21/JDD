package com.jdd.android.jdd.utils;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import cn.limc.androidcharts.entity.*;
import com.jdd.android.jdd.entities.StockEntity;
import com.thinkive.android.app_engine.utils.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述：多空兵法算法
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-07-08
 * @since 1.0
 */
public class LongShortArtOfWarUtil {

    /**
     * 分析多空兵解提示语，并返回需要显示的兵解提示
     *
     * @param data   包含说有提示的数组
     * @param index  指定需要提取提示的指定位置c
     * @param tipMap 兵解对应的提示map
     * @return 需要显示的兵解提示包括文字和颜色，格式为JSONObject，文字的key为explanation，颜色为color
     */
    public static SpannableStringBuilder analyzeLongShortExplanationTip(
            List<LineEntity<DateValueEntity>> data, int index, HashMap<String, String> tipMap) {
        if (null == data) {
            return null;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < data.size(); i++) {
            LineEntity<DateValueEntity> item = data.get(i);
            if (null != item && item.getLineData().size() > index) {
                DateValueEntity entity = item.getLineData().get(index);
                if (1 == entity.getValue()) {
                    if (ssb.length() > 0) {
                        ssb.append("\n");
                    }
                    String text = tipMap.get(item.getTitle());
                    try {
                        ssb.append(text);
                        ForegroundColorSpan span = new ForegroundColorSpan(item.getLineColor());
                        ssb.setSpan(span, ssb.length() - text.length(), ssb.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return ssb;
    }

    /**
     * 分析多空兵解提示语，并返回需要显示的兵解提示
     *
     * @param data        包含说有提示的数组
     * @param index       指定需要提取提示的指定位置
     * @param stockEntity 将解析的兵解直接放到里面的字段
     * @param tipMap      兵解对应的提示map
     */
    public static void analyzeLongShortExplanation(
            List<LineEntity<DateValueEntity>> data, int index,
            StockEntity stockEntity, HashMap<String, String> tipMap) {
        if (null == data) {
            return;
        }
        String volType = "冲击压力线|大风险|持有仓位|突发风险|风险";
        String candleType = "风险波段|轻仓介入|空仓军令|小心持有";
        SpannableStringBuilder ssbCandle = new SpannableStringBuilder();
        SpannableStringBuilder ssbVol = new SpannableStringBuilder();
        for (int i = 0; i < data.size(); i++) {
            LineEntity<DateValueEntity> item = data.get(i);
            if (null != item && item.getLineData().size() > index) {
                DateValueEntity entity = item.getLineData().get(index);
                if (1 == entity.getValue()) {
                    String text = tipMap.get(item.getTitle());
                    if (volType.contains(item.getTitle())) {
                        if (ssbCandle.length() > 0) {
                            ssbCandle.append("\n");
                        }
                        try {
                            ssbCandle.append(text);
                            ForegroundColorSpan span = new ForegroundColorSpan(item.getLineColor());
                            ssbCandle.setSpan(span, ssbCandle.length() - text.length(), ssbCandle.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (candleType.contains(item.getTitle())) {
                        if (ssbVol.length() > 0) {
                            ssbVol.append("\n");
                        }
                        try {
                            ssbVol.append(text);
                            ForegroundColorSpan span = new ForegroundColorSpan(item.getLineColor());
                            ssbVol.setSpan(span, ssbVol.length() - text.length(), ssbVol.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }//end else
                }//end if
            }//end if
        }//end for
        stockEntity.setCandleExplanation(ssbCandle);
        stockEntity.setVolExplanation(ssbVol);
    }

    public static List<LineEntity<DateValueEntity>> calcBattleForce(
            IChartData<IStickEntity> ohlcs,
            IChartData<IStickEntity> vols) {
        List<LineEntity<DateValueEntity>> battleForceLines = new ArrayList<>();

        if (null == ohlcs || null == vols) {
            return null;
        }

        int length = ohlcs.size();
        float[] buys = new float[length];     //买盘
        float[] sells = new float[length];        //卖盘
        for (int i = 0; i < length; i++) {
            OHLCEntity ohlc = (OHLCEntity) ohlcs.get(i);
            ColoredStickEntity vol = (ColoredStickEntity) vols.get(i);
            double var1 = vol.getHigh() /
                    ((ohlc.getHigh() - ohlc.getLow()) * 2 - Math.abs(ohlc.getClose() - ohlc.getOpen()));
            double buy = 0, sell = 0;
            if (ohlc.getClose() > ohlc.getOpen()) {
                buy = Math.abs(var1 * (ohlc.getHigh() - ohlc.getLow()));
                sell = Math.abs(var1 * (ohlc.getOpen() - ohlc.getLow() + ohlc.getHigh() - ohlc.getClose()));
            } else if (ohlc.getClose() < ohlc.getOpen()) {
                buy = var1 * (ohlc.getHigh() - ohlc.getOpen() + ohlc.getClose() - ohlc.getLow());
                sell = var1 * (ohlc.getHigh() - ohlc.getLow());
            } else {
                buy = vol.getHigh() / 2;
                sell = buy;
            }
            buys[i] = (float) buy;
            sells[i] = (float) sell;
        }
        float[] buyEMA2 = StockUtil.calcEMA(buys, 2);
        float[] buyEMA3 = StockUtil.calcEMA(buys, 3);
        float[] sellEMA2 = StockUtil.calcEMA(sells, 2);
        float[] sellEMA3 = StockUtil.calcEMA(sells, 3);

        float[] buyEMA = new float[length];
        float[] sellEMA = new float[length];
        for (int i = 0; i < length; i++) {
            buyEMA[i] = buyEMA2[i] + buyEMA3[i];
            sellEMA[i] = sellEMA2[i] + sellEMA3[i];
        }
        //多头
        float[] longs = StockUtil.calcMA(buyEMA, 3);
        //空头
        float[] shorts = StockUtil.calcMA(sellEMA, 3);

        float[] volMA7 = StockUtil.calcVolumeMA(vols, 7);
        float[] volMA14 = StockUtil.calcVolumeMA(vols, 14);
        float[] heartOfPeople = new float[length];
        for (int i = 0; i < length; i++) {
            heartOfPeople[i] = (volMA14[i] + volMA7[i]) / 2;
        }
        LineEntity<DateValueEntity> longLine = new LineEntity<>();
        LineEntity<DateValueEntity> shortLine = new LineEntity<>();
        LineEntity<DateValueEntity> heartOfPeopleLine = new LineEntity<>();
        List<DateValueEntity> longLineData = new ArrayList<>();
        List<DateValueEntity> shortLineData = new ArrayList<>();
        List<DateValueEntity> heartOfPeopleLineData = new ArrayList<>();
        DateValueEntity item = null;
        for (int i = 0; i < length; i++) {
            int date = ohlcs.get(i).getDate();
            item = new DateValueEntity(longs[i], date);
            longLineData.add(item);
            item = new DateValueEntity(shorts[i], date);
            shortLineData.add(item);
            item = new DateValueEntity(heartOfPeople[i], date);
            heartOfPeopleLineData.add(item);
        }
        longLine.setLineColor(Color.RED);
        longLine.setLineData(longLineData);
        shortLine.setLineData(shortLineData);
        shortLine.setLineColor(Color.GREEN);
        heartOfPeopleLine.setLineColor(Color.WHITE);
        heartOfPeopleLine.setLineData(heartOfPeopleLineData);

        battleForceLines.add(longLine);
        battleForceLines.add(shortLine);
        battleForceLines.add(heartOfPeopleLine);

        return battleForceLines;
    }

}

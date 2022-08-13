package com.jdd.android.jdd.utils;

import android.content.Context;
import android.graphics.Color;
import cn.limc.androidcharts.entity.*;
import com.jdd.android.jdd.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 描述：股票相关辅助类
 * </p>
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2014-07-05
 */
public class StockUtil {

    /**
     * 是否是交易时间：9:15-3:00
     *
     * @return true是交易时间，否则不是
     */
    public static boolean isTradingTime() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (Calendar.SATURDAY == dayOfWeek || Calendar.SUNDAY == dayOfWeek) {
            return false;
        }
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay <= 9 || hourOfDay > 15) {
            return false;
        }
        int minuteOfHour = calendar.get(Calendar.MINUTE);
        if ((minuteOfHour >= 15 && hourOfDay == 9) || hourOfDay > 9) {
            return true;
        }

        return false;
    }

    /**
     * 是否是初始化时间：9:00-9:15，此时所有数据显示- -
     *
     * @return true是初始化时间，否则不是
     */
    public static boolean isInitializationTime() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (Calendar.SATURDAY == dayOfWeek || Calendar.SUNDAY == dayOfWeek) {
            return false;
        }
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay <= 9 || hourOfDay > 15) {
            return false;
        }
        int minuteOfHour = calendar.get(Calendar.MINUTE);
        if ((minuteOfHour >= 15 && hourOfDay == 9)) {
            return true;
        }
        return false;
    }


    /**
     * 通过值获取显示的颜色，按照中国风红涨跌绿
     *
     * @param context 上下文
     * @param value   值
     * @return 颜色值
     */
    public static int getColorByValue(Context context, double value) {
        if (value < 0) {
            return context.getResources().getColor(R.color.stock_green);
        } else if (value > 0) {
            return context.getResources().getColor(R.color.stock_red);
        }
        return context.getResources().getColor(R.color.text_gray);
    }

    /**
     * 通过值获取显示的颜色，按照中国风红涨跌绿（持平为白色，与getColorByValue的唯一不同之处）
     *
     * @param context 上下文
     * @param value   值
     * @return 颜色值
     */
    public static int getColorByValue2(Context context, double value) {
        if (value < 0) {
            return context.getResources().getColor(R.color.stock_green);
        } else if (value > 0) {
            return context.getResources().getColor(R.color.stock_red);
        }
        return context.getResources().getColor(R.color.white);
    }

    /**
     * -计算MA线所需数据
     *
     * @param ohlcs -开高低关
     * @param days  -天数
     * @return
     */
    public static List<DateValueEntity> calcMA(
            ArrayList<IStickEntity> ohlcs,
            int days) {

        if (days < 2) {
            return null;
        }

        List<DateValueEntity> maValues = new ArrayList<DateValueEntity>();

        float sum = 0;
        float avg = 0;
        for (int i = 0; i < ohlcs.size(); i++) {
            float close = (float) ((OHLCEntity) ohlcs.get(i)).getClose();
            if (i < days) {
                sum = sum + close;
                avg = sum / (i + 1f);
            } else {
                sum = sum + close
                        - (float) ((OHLCEntity) ohlcs.get(i - days)).getClose();
                avg = sum / days;
            }
            maValues.add(new DateValueEntity(avg, ohlcs.get(i).getDate()));
        }

        return maValues;
    }

    public static float[] calcMA(float[] values, int days) {

        if (days < 2) {
            return null;
        }

        float[] maValues = new float[values.length];

        float sum = 0;
        float avg = 0;
        for (int i = 0; i < values.length; i++) {
            float close = values[i];
            if (i < days) {
                sum = sum + close;
                avg = sum / (i + 1f);
            } else {
                sum = sum + close
                        - values[i - days];
                avg = sum / days;
            }
            maValues[i] = avg;
        }

        return maValues;
    }

    public static float[] calcVolumeMA(IChartData<IStickEntity> vols, int days) {

        if (days < 2) {
            return null;
        }

        float[] maValues = new float[vols.size()];

        float sum = 0;
        float avg = 0;
        for (int i = 0; i < vols.size(); i++) {
            float close = (float) ((ColoredStickEntity) vols.get(i)).getHigh();
            if (i < days) {
                sum = sum + close;
                avg = sum / (i + 1f);
            } else {
                sum = sum + close
                        - (float) ((ColoredStickEntity) vols.get(i - days)).getHigh();
                avg = sum / days;
            }
            maValues[i] = avg;
        }

        return maValues;
    }

    /**
     * 获得参数 p 的指数平滑异同移动平均数 EXMA 计算公式： 2 n - 1 X = ———— * C + ———— * Xp n + 1 n +
     * 1 C = 收盘价 Xp = 前一天指数平均数
     * <p/>
     * 可用于计算 EXPMA (Exponential Price Moving Average) 一般快慢参数为 param(12 or 50)
     *
     * @param p 原始值
     * @param n 周期
     * @return ema[x]
     */
    public static float[] calcEMA(float[] p, int n) {
        float[] values = new float[p.length];

        values[0] = p[0];

        for (int i = 1; i < p.length; i++) {
            values[i] = (float) (values[i - 1] * (1 - 2 / (n + 1.0)) + p[i] * 2
                    / (n + 1.0));
        }

        return values;
    }

    /**
     * 获得参数 p 的差离值
     * <p/>
     * DIF = 快速EMA - 慢速EMA
     *
     * @param p    原始值
     * @param fast 快速 EMA 参数，一般为 6
     * @param slow 慢速 EMA 参数，一般为 12
     * @return dif[]
     */
    public static float[] calcDIF(float[] p, int fast, int slow) {
        float[] values = new float[p.length];

        float[] fastEMA = calcEMA(p, fast);
        float[] slowEMA = calcEMA(p, slow);

        for (int i = 0; i < p.length; i++) {
            values[i] = fastEMA[i] - slowEMA[i];
        }

        return values;
    }

    /**
     * 计算 MACD 技术曲线数据
     * <p/>
     * 1．MACD由正负差（DIF）和异同平均数（DEA）两部分组成，当然，正负差是核心， DEA是辅助。先介绍DIF的计算方法。
     * DIF是快速平滑移动平均线与慢速平滑移动平均线的差，DIF的正负差的名称由此而来。
     * 快速和慢速的区别是进行指数平滑时采用的参数大小不同，快速是短期的，慢速是长期的。 以现在常用的参数12和26为例，对DIF的计算过程进行介绍。
     * （1）快速平滑移动线（EMA）是12日的，计算公式为： 今日EMA（12）=2/（12+1）×今日收盘价+11/（12+1）×昨日EMA（12）
     * （2）慢速平滑移动平均线（EMA）是26日的，计算公式为：
     * 今日EMA（26）=2/（26+1）×今日收盘价+25/（26+1）×昨日EMA（26）
     * 以上两个公式是指数平滑的公式，平滑因子分别为2/13和2/27。 如果选别的系数，则可照此法办理。 DIF=EMA（12）-EMA（26）
     * 有了DIF之后，MACD的核心就有了。单独的DIF也能进行行情预测，但为了使信号更可靠， 我们引入了另一个指标DEA。
     * 2．DEA是DIF的移动平均，也就是连续数日的DIF的算术平均。这样，DEA自己又有了个 参数，那就是作算术平均的DIF的个数，即天数。
     * 对DIF作移动平均就像对收盘价作移动平均一样，是为了消除偶然因素的影响，使结论更可靠。 3．此外，在分析软件上还有一个指标叫柱状线（BAR）：
     * BAR=2×（DIF-DEA）
     *
     * @param close 昨收
     * @param fast  快速移动参数
     * @param slow  慢速以动参数
     * @param n     周期
     * @return macd[2][x]
     */
    public static float[][] calcMACD(float[] close, int fast, int slow, int n) {
        float[][] values = new float[2][];

        values[0] = calcDIF(close, fast, slow); // 求 DIF
        values[1] = calcEMA(values[0], n); // 对DIF做一次异同移动平均得到 DEA

        return values;
    }

    /**
     * 计算MACD数据
     *
     * @param kLine K线数据
     * @param fast  快速移动参数
     * @param slow  慢速以动参数
     * @param n     周期
     * @return
     */
    public static List<IStickEntity> calcMACD(IChartData<IStickEntity> kLine,
                                              int fast, int slow, int n) {
        if (null == kLine) {
            return null;
        }
        List<IStickEntity> macdEntities = new ArrayList<IStickEntity>();
        float[] close = new float[kLine.size()];
        OHLCEntity entity = null;
        for (int i = 0; i < kLine.size(); i++) {
            entity = (OHLCEntity) kLine.get(i);
            close[i] = (float) entity.getClose();
        }

        float[][] macds = null;
        MACDEntity macdEntity = null;
        macds = calcMACD(close, fast, slow, n);
        for (int i = 0; i < macds[0].length; i++) {
            macdEntity = new MACDEntity();
            macdEntity.setDiff(macds[0][i]);
            macdEntity.setDea(macds[1][i]);
            macdEntity
                    .setMacd(2 * (macdEntity.getDiff() - macdEntity.getDea()));

            macdEntities.add(macdEntity);
        }

        return macdEntities;
    }

    /**
     * 求 数组 p 在 最近 n 跨距（前面n个元素）内的最小值
     *
     * @param p     参数数组
     * @param index 当前的点
     * @param n     n个元素
     * @return
     */
    public static float min(float[] p, int index, int n) {
        float theMin = 0;
        if (0 == index) {
            index = p.length - 1;
        }
        if (n == 0) {
            n = p.length;
        }
        if (null == p || p.length == 0) {
            return Integer.MAX_VALUE;
        }
        int startIndex = index - n + 1;
        if (startIndex < 0) {
            startIndex = 0;
        }
        theMin = p[startIndex];
        while (Float.isNaN(theMin)) {
            if (startIndex < p.length - 1) {
                theMin = p[++startIndex];
            } else {
                return Integer.MAX_VALUE;
            }
        }
        for (int i = startIndex + 1; i <= index; i++) {
            if (p[i] < theMin) {
                theMin = p[i];
            }
        }

        return theMin;
    }

    /**
     * 求 数组 p 在 最近 n 跨距（前面n个元素）内的最大值
     *
     * @param p     参数数组
     * @param index 当前的点
     * @param n     n个元素
     * @return
     */
    public static float max(float[] p, int index, int n) {
        float theMin = 0;
        if (0 == index) {
            index = p.length - 1;
        }
        if (n == 0) {
            n = p.length;
        }
        if (null == p || p.length == 0) {
            return Integer.MAX_VALUE;
        }
        int startIndex = index - n + 1;
        if (startIndex < 0) {
            startIndex = 0;
        }
        theMin = p[startIndex];
        while (Float.isNaN(theMin)) {
            if (startIndex < p.length - 1) {
                theMin = p[++startIndex];
            } else {
                return Integer.MIN_VALUE;
            }
        }
        for (int i = startIndex + 1; i <= index; i++) {
            if (p[i] > theMin) {
                theMin = p[i];
            }
        }

        return theMin;
    }

    /**
     * 计算KDJ技术曲线数据
     * <p/>
     * 1．产生KD以前，先产生未成熟随机值RSV。其计算公式为： N日RSV=[(Ct-Ln)/(Hn-Ln)] ×100
     * 2．对RSV进行指数平滑，就得到如下K值： 今日K值=2/3×昨日K值+1/3×今日RSV
     * 式中，1/3是平滑因子，是可以人为选择的，不过目前已经约定俗成，固定为1/3了。 3．对K值进行指数平滑，就得到如下D值：
     * 今日D值=2/3×昨日D值+1/3×今日K值 式中，1/3为平滑因子，可以改成别的数字，同样已成约定，1/3也已经固定。
     * 4．在介绍KD时，往往还附带一个J指标，计算公式为： J=3D-2K=D+2(D-K) 可见J是D加上一个修正值。J的实质是反映D和D
     * 与K的差值。此外，有的书中J指标的计算公式为：J=3K-2D
     *
     * @param close 昨收
     * @param high  最高
     * @param low   最低
     * @param n     周期
     * @return kdj[3][x]
     */
    public static float[][] calcKDJ(float[] close, float[] high, float[] low, int n) {
        float[][] kdj = new float[3][];
        int len = high.length;

        float[] k = new float[len];
        float[] d = new float[len];
        float[] j = new float[len];

        int startIndex = 0;
        int i;
        for (i = 0; i < len; i++) {
            if (close[i] <= 0.0) {
                continue;
            } else {
                startIndex = i;
                break;
            }
        }
        int t;
        for (i = startIndex, t = 0; i < len; i++, t++) {
            float ax = close[i] - min(low, i, n - 1);
            float bx = max(high, i, n - 1) - min(low, i, n - 1);
            float rsv = (float) (ax / bx * 100.0);

            if (0 == t) {
                k[t] = rsv;
                d[t] = rsv;
                j[t] = rsv;
            } else {
                k[t] = (float) (2 * k[t - 1] / 3.0 + 1 * rsv / 3.0);
                d[t] = (float) (2 * d[t - 1] / 3.0 + 1 * k[t] / 3.0);
                j[t] = 3 * k[t] - 2 * d[t];
            }
        }
        kdj[0] = k;
        kdj[1] = d;
        kdj[2] = j;

        return kdj;
    }

    /**
     * -计算KDJ数据，并封装成图表可以直接用的数据
     *
     * @param ohlcs -开高低价列表
     * @param n     -周期
     * @return
     */
    public static List<LineEntity<DateValueEntity>> calcKDJ(IChartData<IStickEntity> ohlcs, int n) {
        if (null == ohlcs || ohlcs.size() < 1) {
            return null;
        }

        List<LineEntity<DateValueEntity>> data = new ArrayList<LineEntity<DateValueEntity>>(3);
        int length = ohlcs.size();
        float[] close = new float[length];
        float[] high = new float[length];
        float[] low = new float[length];
        int[] date = new int[length];
        for (int i = 0; i < length; i++) {
            OHLCEntity ohlc = (OHLCEntity) ohlcs.get(i);

            close[i] = (float) ohlc.getClose();
            high[i] = (float) ohlc.getHigh();
            low[i] = (float) ohlc.getLow();
            date[i] = ohlc.getDate();
        }

        // 得到基本数据类型的kdj
        float[][] kdj = calcKDJ(close, high, low, n);

        if (null == kdj) {
            return null;
        }

        // 开始封装
        LineEntity<DateValueEntity> kEntity = new LineEntity<DateValueEntity>();
        kEntity.setTitle("K");
        kEntity.setLineColor(Color.WHITE);
        kEntity.setLineData(packValue(kdj[0], date));

        LineEntity<DateValueEntity> dEntity = new LineEntity<DateValueEntity>();
        dEntity.setTitle("D");
        dEntity.setLineColor(0xfff4be04);// 黄
        dEntity.setLineData(packValue(kdj[1], date));

        LineEntity<DateValueEntity> jEntity = new LineEntity<DateValueEntity>();
        jEntity.setTitle("J");
        jEntity.setLineColor(0xff9932CD);
        jEntity.setLineData(packValue(kdj[2], date));

        data.add(kEntity);
        data.add(dEntity);
        data.add(jEntity);

        return data;
    }

    public static List<DateValueEntity> packValue(float[] value, int[] date) {
        List<DateValueEntity> entities = new ArrayList<DateValueEntity>();

        if (null == value || null == date) {
            return entities;
        }
        for (int i = 0; i < value.length; i++) {
            entities.add(new DateValueEntity(value[i], date[i]));
        }

        return entities;
    }

    public static List<LineEntity<DateValueEntity>> calcBoll(IChartData<IStickEntity> ohlcs, int step, int p) {
        if (null == ohlcs || ohlcs.size() < 1) {
            return null;
        }

        List<LineEntity<DateValueEntity>> data = new ArrayList<LineEntity<DateValueEntity>>(3);
        int length = ohlcs.size();
        float[] close = new float[length];
        int[] date = new int[length];
        for (int i = 0; i < length; i++) {
            OHLCEntity ohlc = (OHLCEntity) ohlcs.get(i);

            close[i] = (float) ohlc.getClose();
            date[i] = ohlc.getDate();
        }
        float sum = Float.NaN;
        float mid[] = calcMA(close, step);
        float std = 0;
        float up[] = new float[length];
        float down[] = new float[length];
        for (int i = step - 1; i < length; i++) {
            if (Float.isNaN(sum)) {
                if (i - step + 1 >= 0 && i - step + 1 < length) {
                    sum = 0;
                    for (int j = i - step + 1; j < i; j++) {
                        sum += Math.pow(close[j] - mid[j], 2);
                    }
                } else {
                    continue;
                }
            }
            sum += Math.pow(close[i] - mid[i], 2);
            std = (float) Math.sqrt(sum / step);
            up[i] = mid[i] + p * std;
            down[i] = mid[i] - p * std;
            sum -= Math.pow(close[i - step + 1] - mid[i - step + 1], 2);
        }
        // 开始封装
        LineEntity<DateValueEntity> upperEntity = new LineEntity<DateValueEntity>();
        upperEntity.setTitle("UPPER");
        upperEntity.setLineColor(Color.WHITE);
        upperEntity.setLineData(packValue(up, date));

        LineEntity<DateValueEntity> midEntity = new LineEntity<DateValueEntity>();
        midEntity.setTitle("MID");
        midEntity.setLineColor(Color.BLUE);// 黄
        midEntity.setLineData(packValue(mid, date));

        LineEntity<DateValueEntity> lowerEntity = new LineEntity<DateValueEntity>();
        lowerEntity.setTitle("LOWER");
        lowerEntity.setLineColor(Color.GREEN);
        lowerEntity.setLineData(packValue(down, date));

        data.add(upperEntity);
        data.add(midEntity);
        data.add(lowerEntity);
        return data;
    }

    public static List<LineEntity<DateValueEntity>> calcDMI(IChartData<IStickEntity> ohlcs, int step, int step2) {
        if (null == ohlcs || ohlcs.size() < 1) {
            return null;
        }

        List<LineEntity<DateValueEntity>> data = new ArrayList<LineEntity<DateValueEntity>>(3);
        int length = ohlcs.size();
        float[] close = new float[length];
        float[] high = new float[length];
        float[] low = new float[length];
        int[] date = new int[length];
        for (int i = 0; i < length; i++) {
            OHLCEntity ohlc = (OHLCEntity) ohlcs.get(i);

            close[i] = (float) ohlc.getClose();
            high[i] = (float) ohlc.getHigh();
            low[i] = (float) ohlc.getLow();
            date[i] = ohlc.getDate();
        }
        float nSum = 0, pSum = 0, trSum = 0, hd, ld;
        float pDi[] = new float[length], nDi[] = new float[length],
                dx[] = new float[length], adx[] = new float[length], adxr[] = new float[length];
        if (length > step) {
            for (int i = 1; i < step; i++) {
                trSum += Math.max(
                        Math.max(high[i] - low[i], Math.abs(high[i] - close[i - 1])),
                        Math.abs(close[i - 1] - low[i])
                );
                hd = high[i] - high[i - 1];
                ld = low[i - 1] - low[i];
                if (hd > 0 && hd > ld) {
                    pSum += hd;
                }
                if (ld > 0 && ld > hd) {
                    nSum += ld;
                }
            }
        }
        for (int i = step; i < length; i++) {
            trSum += Math.max(
                    Math.max(high[i] - low[i], Math.abs(high[i] - close[i - 1])),
                    Math.abs(close[i - 1] - low[i])
            );
            hd = high[i] - high[i - 1];
            ld = low[i - 1] - low[i];
            if (hd > 0 && hd > ld) {
                pSum += hd;
            }
            if (ld > 0 && ld > hd) {
                nSum += ld;
            }

            pDi[i] = 100 * pSum / trSum;
            nDi[i] = 100 * nSum / trSum;
            dx[i] = 100 * Math.abs(pDi[i] - nDi[i]) / (pDi[i] + nDi[i]);

            int preI = i - step + 1;
            trSum += Math.max(
                    Math.max(high[preI] - low[preI], Math.abs(high[preI] - close[preI - 1])),
                    Math.abs(close[preI - 1] - low[preI])
            );
            hd = high[preI] - high[preI - 1];
            ld = low[preI - 1] - low[preI];
            if (hd > 0 && hd > ld) {
                pSum -= hd;
            }
            if (ld > 0 && ld > hd) {
                nSum -= ld;
            }
        }
        adx = calcMA(dx, step2);
        for (int i = 0; i < adx.length; i++) {
            if (i - step2 + 1 >= 0) {
                adxr[i] = (adx[i] + adx[i - step2 + 1]) / 2;
            }
        }
        // 开始封装
        LineEntity<DateValueEntity> di1Entity = new LineEntity<DateValueEntity>();
        di1Entity.setTitle("DI1");
        di1Entity.setLineColor(Color.YELLOW);
        di1Entity.setLineData(packValue(pDi, date));

        LineEntity<DateValueEntity> di2Entity = new LineEntity<DateValueEntity>();
        di2Entity.setTitle("DI2");
        di2Entity.setLineColor(Color.CYAN);// 黄
        di2Entity.setLineData(packValue(nDi, date));

        LineEntity<DateValueEntity> adxEntity = new LineEntity<DateValueEntity>();
        adxEntity.setTitle("ADX");
        adxEntity.setLineColor(Color.GREEN);
        adxEntity.setLineData(packValue(adx, date));

        LineEntity<DateValueEntity> adxrEntity = new LineEntity<DateValueEntity>();
        adxrEntity.setTitle("ADXR");
        adxrEntity.setLineColor(Color.BLUE);
        adxrEntity.setLineData(packValue(adxr, date));

        data.add(di1Entity);
        data.add(di2Entity);
        data.add(adxEntity);
        data.add(adxrEntity);
        return data;
    }

    public static List<LineEntity<DateValueEntity>> calcRSI(
            IChartData<IStickEntity> ohlcs, int step1, int step2, int step3) {
        if (null == ohlcs || ohlcs.size() < 1) {
            return null;
        }

        List<LineEntity<DateValueEntity>> data = new ArrayList<LineEntity<DateValueEntity>>(3);
        int length = ohlcs.size();
        float[] close = new float[length];
        int[] date = new int[length];
        for (int i = 0; i < length; i++) {
            OHLCEntity ohlc = (OHLCEntity) ohlcs.get(i);

            close[i] = (float) ohlc.getClose();
        }
        // 开始封装
        LineEntity<DateValueEntity> rsi1Entity = new LineEntity<DateValueEntity>();
        rsi1Entity.setTitle("RSI" + step1);
        rsi1Entity.setLineColor(Color.YELLOW);
        rsi1Entity.setLineData(packValue(calcRSI(close, step1), date));

        LineEntity<DateValueEntity> rsi2Entity = new LineEntity<DateValueEntity>();
        rsi2Entity.setTitle("RSI" + step2);
        rsi2Entity.setLineColor(Color.GREEN);
        rsi2Entity.setLineData(packValue(calcRSI(close, step2), date));

        LineEntity<DateValueEntity> rsi3Entity = new LineEntity<DateValueEntity>();
        rsi3Entity.setTitle("RSI" + step3);
        rsi3Entity.setLineColor(Color.BLUE);
        rsi3Entity.setLineData(packValue(calcRSI(close, step3), date));

        data.add(rsi1Entity);
        data.add(rsi2Entity);
        data.add(rsi3Entity);
        return data;
    }

    public static float[] calcRSI(float close[], int step) {
        if (null == close) {
            return null;
        }
        int length = close.length;
        float nSum = 0, pSum = 0, cOffset;
        float rsi[] = new float[length];
        if (length > step) {
            for (int i = 1; i < step; i++) {
                cOffset = close[i] - close[i - 1];
                if (cOffset < 0) {
                    nSum += cOffset;
                } else {
                    nSum += cOffset;
                }
            }
        }
        for (int i = step; i < length; i++) {
            cOffset = close[i] - close[i - 1];
            if (cOffset < 0) {
                nSum += cOffset;
            } else {
                nSum += cOffset;
            }
            if (pSum == 0 || nSum == 0) {
                rsi[i] = 0;
            } else {
                rsi[i] = pSum * 100 / (pSum + (-nSum));
            }
            cOffset = close[i - step + 1] - close[i - step];
            if (cOffset < 0) {
                nSum -= cOffset;
            } else {
                pSum -= cOffset;
            }
        }
        return rsi;
    }
}

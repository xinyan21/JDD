
package com.jdd.android.jdd.entities;

public class PriceInfo {

    // 涨幅 1 Y
    private double uppercent;
    // 现价 2 Y
    private double now;
    // 涨跌 3 Y
    private double up;
    // 买入价 4 Y
    private double buy;
    // 卖出价 5 Y
    private double sell;
    // 成交量 6 Y
    private String volume;
    // 涨速 7 Y
    private double minchange;
    // 换手 8 Y
    private double hsl;
    // 今开 9 Y
    private double open;
    // 最高 10 Y
    private double high;
    // 最低 11 Y
    private double low;
    // 昨收 12 Y
    private double yesterday;
    // 市盈(动) 13 Y
    private double prg;
    // 成交额 14 Y
    private String amount;
    // 量比 15 Y
    private double volrate;
    // 振幅 16 Y
    private double flux;
    // 均价 17 Y
    private double average;
    // 内盘 18 Y
    private String inside;
    // 外盘 19 Y
    private double outside;
    // 量比基量 20 N
    private double volbase;
    // 股票类型 21 N
    private double stktype;
    // 名称 22 N
    private String name;
    // 市场代码 23 N
    private String market;
    // 股票代码 24 Y
    private String code;
    // 现手 25 N
    private double thedeal;
    // 市净率 26 Y
    private double scl;
    // 流通市值 27 N
    private String ltsz;
    // 拼音简称 28 Y
    private String pyname;
    // 委差 29 N
    private double wc;
    // 委比 30 N
    private double wb;
    // 总市值 31 N
    private String zsz;
    // 每股净资 32 N
    private double mgjz;
    // 笔升跌 33 N
    private double thechange;
    // 34 N
    private String fristName;
    // 35 N
    private double fristUppercent;
    // 36 N
    private double fristUp;
    // z自选模块
    private boolean checkBoxBooean;

    private int id; // 主键
    private int sort;// 排序字段
    private String type;// 股票类别
    private String industry; // 所属行业

    private String mbuyprice1;// 买一
    private String mbuyprice2;// 买二
    private String mBuyprice3;// 买三
    private String mBuyprice4;// 买四
    private String mBuyprice5;// 买五
    private String mBuyvol1;// 买盘量一
    private String mBuyvol2;// 买盘量二
    private String mBuyvol3;// 买盘量三
    private String mBuyvol4;// 买盘量四
    private String mBuyvol5;// 买盘量五
    private String mSellprice1;// 卖一
    private String mSellprice2;// 卖二
    private String mSellprice3;// 卖三
    private String mSellprice4;// 卖四
    private String mSellprice5;// 卖五
    private String mSellvol1;// 卖盘量一
    private String mSellvol2;// 卖盘量二
    private String mSellvol3;// 卖盘量三
    private String mSellvol4;// 卖盘量四
    private String mSellvol5;// 卖盘量五

    public double getUppercent() {
        return uppercent;
    }

    public void setUppercent(double uppercent) {
        this.uppercent = uppercent;
    }

    public double getNow() {
        return now;
    }

    public void setNow(double now) {
        this.now = now;
    }

    public double getUp() {
        return up;
    }

    public void setUp(double up) {
        this.up = up;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public double getMinchange() {
        return minchange;
    }

    public void setMinchange(double minchange) {
        this.minchange = minchange;
    }

    public double getHsl() {
        return hsl;
    }

    public void setHsl(double hsl) {
        this.hsl = hsl;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getYesterday() {
        return yesterday;
    }

    public void setYesterday(double yesterday) {
        this.yesterday = yesterday;
    }

    public double getPrg() {
        return prg;
    }

    public void setPrg(double prg) {
        this.prg = prg;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getVolrate() {
        return volrate;
    }

    public void setVolrate(double volrate) {
        this.volrate = volrate;
    }

    public double getFlux() {
        return flux;
    }

    public void setFlux(double flux) {
        this.flux = flux;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getInside() {
        return inside;
    }

    public void setInside(String inside) {
        this.inside = inside;
    }

    public double getOutside() {
        return outside;
    }

    public void setOutside(double outside) {
        this.outside = outside;
    }

    public double getVolbase() {
        return volbase;
    }

    public void setVolbase(double volbase) {
        this.volbase = volbase;
    }

    public double getStktype() {
        return stktype;
    }

    public void setStktype(double stktype) {
        this.stktype = stktype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getThedeal() {
        return thedeal;
    }

    public void setThedeal(double thedeal) {
        this.thedeal = thedeal;
    }

    public double getScl() {
        return scl;
    }

    public void setScl(double scl) {
        this.scl = scl;
    }

    public String getLtsz() {
        return ltsz;
    }

    public void setLtsz(String ltsz) {
        this.ltsz = ltsz;
    }

    public String getPyname() {
        return pyname;
    }

    public void setPyname(String pyname) {
        this.pyname = pyname;
    }

    public double getWc() {
        return wc;
    }

    public void setWc(double wc) {
        this.wc = wc;
    }

    public double getWb() {
        return wb;
    }

    public void setWb(double wb) {
        this.wb = wb;
    }

    public String getZsz() {
        return zsz;
    }

    public void setZsz(String zsz) {
        this.zsz = zsz;
    }

    public double getMgjz() {
        return mgjz;
    }

    public void setMgjz(double mgjz) {
        this.mgjz = mgjz;
    }

    public double getThechange() {
        return thechange;
    }

    public void setThechange(double thechange) {
        this.thechange = thechange;
    }

    public String getFristName() {
        return fristName;
    }

    public void setFristName(String fristName) {
        this.fristName = fristName;
    }

    public double getFristUppercent() {
        return fristUppercent;
    }

    public void setFristUppercent(double fristUppercent) {
        this.fristUppercent = fristUppercent;
    }

    public double getFristUp() {
        return fristUp;
    }

    public void setFristUp(double fristUp) {
        this.fristUp = fristUp;
    }

    public boolean isCheckBoxBooean() {
        return checkBoxBooean;
    }

    public void setCheckBoxBooean(boolean checkBoxBooean) {
        this.checkBoxBooean = checkBoxBooean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getMbuyprice1() {
        return mbuyprice1;
    }

    public void setMbuyprice1(String mbuyprice1) {
        this.mbuyprice1 = mbuyprice1;
    }

    public String getMbuyprice2() {
        return mbuyprice2;
    }

    public void setMbuyprice2(String mbuyprice2) {
        this.mbuyprice2 = mbuyprice2;
    }

    public String getmBuyprice3() {
        return mBuyprice3;
    }

    public void setmBuyprice3(String mBuyprice3) {
        this.mBuyprice3 = mBuyprice3;
    }

    public String getmBuyprice4() {
        return mBuyprice4;
    }

    public void setmBuyprice4(String mBuyprice4) {
        this.mBuyprice4 = mBuyprice4;
    }

    public String getmBuyprice5() {
        return mBuyprice5;
    }

    public void setmBuyprice5(String mBuyprice5) {
        this.mBuyprice5 = mBuyprice5;
    }

    public String getmBuyvol1() {
        return mBuyvol1;
    }

    public void setmBuyvol1(String mBuyvol1) {
        this.mBuyvol1 = mBuyvol1;
    }

    public String getmBuyvol2() {
        return mBuyvol2;
    }

    public void setmBuyvol2(String mBuyvol2) {
        this.mBuyvol2 = mBuyvol2;
    }

    public String getmBuyvol3() {
        return mBuyvol3;
    }

    public void setmBuyvol3(String mBuyvol3) {
        this.mBuyvol3 = mBuyvol3;
    }

    public String getmBuyvol4() {
        return mBuyvol4;
    }

    public void setmBuyvol4(String mBuyvol4) {
        this.mBuyvol4 = mBuyvol4;
    }

    public String getmBuyvol5() {
        return mBuyvol5;
    }

    public void setmBuyvol5(String mBuyvol5) {
        this.mBuyvol5 = mBuyvol5;
    }

    public String getmSellprice1() {
        return mSellprice1;
    }

    public void setmSellprice1(String mSellprice1) {
        this.mSellprice1 = mSellprice1;
    }

    public String getmSellprice2() {
        return mSellprice2;
    }

    public void setmSellprice2(String mSellprice2) {
        this.mSellprice2 = mSellprice2;
    }

    public String getmSellprice3() {
        return mSellprice3;
    }

    public void setmSellprice3(String mSellprice3) {
        this.mSellprice3 = mSellprice3;
    }

    public String getmSellprice4() {
        return mSellprice4;
    }

    public void setmSellprice4(String mSellprice4) {
        this.mSellprice4 = mSellprice4;
    }

    public String getmSellprice5() {
        return mSellprice5;
    }

    public void setmSellprice5(String mSellprice5) {
        this.mSellprice5 = mSellprice5;
    }

    public String getmSellvol1() {
        return mSellvol1;
    }

    public void setmSellvol1(String mSellvol1) {
        this.mSellvol1 = mSellvol1;
    }

    public String getmSellvol2() {
        return mSellvol2;
    }

    public void setmSellvol2(String mSellvol2) {
        this.mSellvol2 = mSellvol2;
    }

    public String getmSellvol3() {
        return mSellvol3;
    }

    public void setmSellvol3(String mSellvol3) {
        this.mSellvol3 = mSellvol3;
    }

    public String getmSellvol4() {
        return mSellvol4;
    }

    public void setmSellvol4(String mSellvol4) {
        this.mSellvol4 = mSellvol4;
    }

    public String getmSellvol5() {
        return mSellvol5;
    }

    public void setmSellvol5(String mSellvol5) {
        this.mSellvol5 = mSellvol5;
    }
}

package cn.limc.androidcharts.entity;//package cn.limc.cn.limc.androidcharts.entity;
//
///**
// * <p>描述：KDJ指数实体类</p>
// *
//// * @author 徐鑫炎
// * @version 1.0
// * @corporation
// * @date 2014-07-05
// */
//public class KDJEntity implements IStickEntity{
//    private int date;
//    /**
//     * @return the date
//     */
//    public int getDate() {
//        return date;
//    }
//
//    /**
//     * @param date
//     *            the date to set
//     */
//    public void setDate(int date) {
//        this.date = date;
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @return
//     *
//     * @see cn.limc.cn.limc.androidcharts.entity.IMeasurable#getHigh()
//     */
//    public double getHigh() {
//        return Math.max(Math.max(getDea(), getDiff()), getMacd());
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @return
//     *
//     * @see cn.limc.cn.limc.androidcharts.entity.IMeasurable#getLow()
//     */
//    public double getLow() {
//        return Math.min(Math.min(getDea(), getDiff()), getMacd());
//    }
//}

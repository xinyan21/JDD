package com.jdd.android.jdd.entities;

/**
 * ������ָ��ʵ����
 *
 * @author ������
 * @version 1.0
 * @corporation ��µ�
 * @date 2015-06-16
 * @since 1.0
 */
@Deprecated
public class IndexEntity {
    private String name;        //��Ʊ����
    private String code;        //��Ʊ����
    private float changePercent;        //�ǵ���
    private double changeValue;        //�ǵ���
    private double volume;        //�ɽ���
    private int riseStockNum;       //���ǹ�Ʊ��
    private int steadyStockNum;     //ƽ�̹�Ʊ��
    private int dropStockNum;       //�µ���Ʊ��

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(float changePercent) {
        this.changePercent = changePercent;
    }

    public double getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(double changeValue) {
        this.changeValue = changeValue;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getRiseStockNum() {
        return riseStockNum;
    }

    public void setRiseStockNum(int riseStockNum) {
        this.riseStockNum = riseStockNum;
    }

    public int getSteadyStockNum() {
        return steadyStockNum;
    }

    public void setSteadyStockNum(int steadyStockNum) {
        this.steadyStockNum = steadyStockNum;
    }

    public int getDropStockNum() {
        return dropStockNum;
    }

    public void setDropStockNum(int dropStockNum) {
        this.dropStockNum = dropStockNum;
    }
}

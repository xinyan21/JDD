package com.jdd.android.jdd.entities;

/**
 * 描述：可选择的实体类，主要用来封装搜索条件
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-22
 * @last_edit 2016-02-22
 * @since 1.0
 */
public class SelectableEntity {
    private String data;
    private boolean isSelected;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

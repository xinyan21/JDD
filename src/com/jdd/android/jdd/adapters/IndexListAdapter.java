package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.IndexEntity;
import com.jdd.android.jdd.utils.FormatUtils;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：指数列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
@Deprecated
public class IndexListAdapter extends BaseAdapter {

    private List<IndexEntity> mEntities;
    private Context mContext;

    public IndexListAdapter(Context context, List<IndexEntity> data) {
        mEntities = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mEntities || mEntities.size() < 1) {
            return null;
        }
        IndexEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_index_list, null);

            holder.changePercent = (TextView) convertView.findViewById(R.id.tv_change_percent);
            holder.name = (TextView) convertView.findViewById(R.id.tv_change_percent);
            holder.changeValue = (TextView) convertView.findViewById(R.id.tv_change_value);
            holder.dropStockNum = (TextView) convertView.findViewById(R.id.tv_drop_stock_num);
            holder.steadyStockNum = (TextView) convertView.findViewById(R.id.tv_steady_stock_num);
            holder.riseStockNum = (TextView) convertView.findViewById(R.id.tv_rise_stock_num);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getName())) {
            holder.name.setText(entity.getName());
        }
        holder.changePercent.setText(FormatUtils.format2PointTwo(entity.getChangePercent()));
        holder.dropStockNum.setText(FormatUtils.format2PointTwo(entity.getDropStockNum()));
        holder.riseStockNum.setText(FormatUtils.format2PointTwo(entity.getRiseStockNum()));
        holder.steadyStockNum.setText(FormatUtils.format2PointTwo(entity.getSteadyStockNum()));
        holder.changeValue.setText(FormatUtils.format2PointTwo(entity.getChangeValue()));
        holder.volume.setText(FormatUtils.format2PointTwo(entity.getVolume()));


        return convertView;
    }

    class ViewHolder {
        TextView name;      //股票名称
        TextView changePercent;      //涨跌幅
        TextView changeValue;        //涨跌额
        TextView volume;        //成交量
        TextView riseStockNum;       //上涨股票数
        TextView steadyStockNum;     //平盘股票数
        TextView dropStockNum;    //下跌股票数
    }
}

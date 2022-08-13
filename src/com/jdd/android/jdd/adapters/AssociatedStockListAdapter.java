package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.utils.FormatUtils;

import java.util.List;

/**
 * 描述：相关联股票列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class AssociatedStockListAdapter extends BaseAdapter {

    private List<StockEntity> mEntities;
    private Context mContext;

    public AssociatedStockListAdapter(Context context, List<StockEntity> data) {
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
        StockEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_associated_stocks, null);

            holder.changePercent = (TextView) convertView.findViewById(R.id.tv_change_percent);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.now = (TextView) convertView.findViewById(R.id.tv_now);
            holder.quantityRelative = (TextView) convertView.findViewById(R.id.tv_quantity_relative);
            holder.ltsz = (TextView) convertView.findViewById(R.id.tv_negotiable_market_capitalization);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(entity.getName());
        holder.quantityRelative.setText(FormatUtils.format2PointTwo(entity.getQuantityRelative()));
        holder.changePercent.setText(FormatUtils.format2PointTwo(entity.getChangePercent()));
        holder.now.setText(FormatUtils.format2PointTwo(entity.getNow()));
        holder.ltsz.setText(FormatUtils.format2PointTwo(entity.getNegotiableMarketCapitalization()));

        return convertView;
    }

    class ViewHolder {
        TextView quantityRelative;      //量比
        TextView name;      //股票名称
        TextView changePercent;      //涨跌幅
        TextView now;       //现价
        TextView ltsz;  //流通市值
    }
}

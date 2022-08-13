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
import com.jdd.android.jdd.views.HVListView;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：涨幅榜列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-17
 * @since 1.0
 */
@Deprecated
public class RiseListAdapter extends BaseAdapter {
    private Context mContext;
    private HVListView mListView;
    private List<StockEntity> mEntities;

    public RiseListAdapter(Context context, HVListView listView, List<StockEntity> entities) {
        mContext = context;
        mListView = listView;
        mEntities = entities;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public StockEntity getItem(int position) {
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_rise_list, null);

            holder.changePercent = (TextView) convertView.findViewById(R.id.tv_change_percent);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.handOver = (TextView) convertView.findViewById(R.id.tv_handover);
            holder.code = (TextView) convertView.findViewById(R.id.tv_code);
            holder.negotiableMarketCapitalization
                    = (TextView) convertView.findViewById(R.id.tv_negotiable_market_capitalization);
            holder.now = (TextView) convertView.findViewById(R.id.tv_now);
            holder.quantityRelative = (TextView) convertView.findViewById(R.id.tv_quantity_relative);
            holder.riseSpeed = (TextView) convertView.findViewById(R.id.tv_rise_speed);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.now.setText(FormatUtils.format2PointTwo(entity.getNow()));
        holder.riseSpeed.setText(FormatUtils.format2PointTwo(entity.getRiseSpeed()));
        holder.negotiableMarketCapitalization.setText(entity.getNegotiableMarketCapitalization() + "亿");
        holder.quantityRelative.setText(FormatUtils.format2PointTwo(entity.getQuantityRelative()));
        if (!StringUtils.isEmpty(entity.getCode())) {
            holder.code.setText(entity.getCode());
        }
        holder.changePercent.setText(FormatUtils.format2PointTwo(entity.getChangePercent()));
        holder.handOver.setText(FormatUtils.format2PointTwo(entity.getHandOver()));
        if (!StringUtils.isEmpty(entity.getName())) {
            holder.name.setText(entity.getName());
        }

        //校正（处理同时上下和左右滚动出现错位情况）
        View child = ((ViewGroup) convertView).getChildAt(1);
        int head = mListView.getHeadScrollX();
        if (child.getScrollX() != head) {
            child.scrollTo(mListView.getHeadScrollX(), 0);
        }

        return convertView;
    }

    class ViewHolder {
        TextView code;
        TextView name;      //股票名称
        TextView changePercent;      //涨跌幅
        TextView negotiableMarketCapitalization;
        TextView handOver;
        TextView now;
        TextView riseSpeed;
        TextView quantityRelative;
    }
}

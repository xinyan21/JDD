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
import com.jdd.android.jdd.utils.StockUtil;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：涨跌排名列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-12-26
 * @since 1.0
 */
public class RoseDropRankAdapter extends BaseAdapter {
    private List<StockEntity> mEntities;
    private Context mContext;

    public RoseDropRankAdapter(Context context, List<StockEntity> entities) {
        mEntities = entities;
        mContext = context;
    }

    @Override
    public int getCount() {
        return null == mEntities ? 0 : mEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return null == mEntities ? null : mEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_stocks, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_stock_name);
            holder.code = (TextView) convertView.findViewById(R.id.tv_stock_code);
            holder.changePercent = (TextView) convertView.findViewById(R.id.tv_change_percent);
            holder.now = (TextView) convertView.findViewById(R.id.tv_now);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtils.isEmpty(entity.getName())) {
            holder.name.setText(entity.getName());
        }
        if (!StringUtils.isEmpty(entity.getCode())) {
            holder.code.setText(entity.getCode());
        }
        holder.changePercent.setText(FormatUtils.format2PointTwo(entity.getChangePercent()) + "%");
        holder.changePercent.setTextColor(
                StockUtil.getColorByValue(mContext, entity.getChangePercent()));
        holder.now.setText(FormatUtils.format2PointTwo(entity.getNow()));
        holder.now.setTextColor(StockUtil.getColorByValue(mContext, entity.getNow() - entity.getPreClose()));

        return convertView;
    }

    class ViewHolder {
        TextView name;      //股票名称
        TextView code;      //股票代码
        TextView now;       //现价
        TextView changePercent;       //可变化值，值范围为涨跌值、涨跌幅
    }
}

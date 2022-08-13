package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.FifthOrderEntity;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.utils.FormatUtils;
import com.jdd.android.jdd.utils.StockUtil;

import java.util.List;

/**
 * 描述：五档买卖盘适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
public class FiveOrderAdapter extends BaseAdapter {

    private List<FifthOrderEntity> mEntities;
    private Context mContext;
    private StockEntity mStockEntity;

    public FiveOrderAdapter(Context context, List<FifthOrderEntity> data, StockEntity stockEntity) {
        mEntities = data;
        mContext = context;
        mStockEntity = stockEntity;
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
        FifthOrderEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_fifth_order, null);

            holder.orderName = (TextView) convertView.findViewById(R.id.tv_order_name);
            holder.price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.count = (TextView) convertView.findViewById(R.id.tv_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.orderName.setText(entity.getOrderName());
        holder.count.setText(String.valueOf(entity.getCount()));
        holder.price.setText(FormatUtils.format2PointTwo(entity.getPrice()));
        if (StockUtil.isInitializationTime() || entity.getPrice() == 0) {
            holder.count.setText("- -");
            holder.price.setText("- -");
        }
        int color;
        if (entity.getPrice() > 0) {
            color = StockUtil.getColorByValue2(mContext, entity.getPrice() - mStockEntity.getPreClose());
        } else {
            color = StockUtil.getColorByValue2(mContext, 0);
        }
        holder.count.setTextColor(color);
        holder.price.setTextColor(color);

        return convertView;
    }

    class ViewHolder {
        TextView orderName;      //档数名称
        TextView price;      //价格
        TextView count;      //手数
    }

    public void setStockEntity(StockEntity stockEntity) {
        mStockEntity = stockEntity;
    }
}

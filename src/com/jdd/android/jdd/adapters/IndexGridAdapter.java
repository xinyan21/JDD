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
 * 描述：指数网格列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-16
 * @since 1.0
 */
public class IndexGridAdapter extends BaseAdapter {
    private List<StockEntity> mEntities;
    private Context mContext;
    private boolean mIsNewLayout = false;

    public IndexGridAdapter(Context context, List<StockEntity> entities) {
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

            if (!mIsNewLayout) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_item_index, null);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_item_index_new, null);
            }
            holder.now = (TextView) convertView.findViewById(R.id.tv_stock_code);
            holder.stockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            holder.changeValue = (TextView) convertView.findViewById(R.id.tv_change_value);
            holder.changePercent = (TextView) convertView.findViewById(R.id.tv_change_percent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getName())) {
            holder.stockName.setText(entity.getName());
        }
        holder.changeValue.setText(FormatUtils.format2PointTwo(entity.getChangeValue()));
        holder.changePercent.setText(FormatUtils.format2PointTwo(entity.getChangePercent()) + "%");
        holder.changeValue.setTextColor(
                StockUtil.getColorByValue(mContext, entity.getNow() - entity.getPreClose())
        );
        holder.changePercent.setTextColor(
                StockUtil.getColorByValue(mContext, entity.getNow() - entity.getPreClose())
        );
        holder.now.setText(FormatUtils.format2PointTwo(entity.getNow()));
        holder.now.setTextColor(StockUtil.getColorByValue(mContext, entity.getNow() - entity.getPreClose()));

        return convertView;
    }

    public void setNewLayout(boolean newLayout) {
        mIsNewLayout = newLayout;
    }

    class ViewHolder {
        TextView stockName;
        TextView now;
        TextView changeValue;
        TextView changePercent;
    }
}

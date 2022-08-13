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
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：空方跌幅榜列表适配器，自选股票池列表格式跟跌幅榜一样，所以先共用一个
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
@Deprecated
public class DropListAdapter extends BaseAdapter {

    private List<StockEntity> mEntities;
    private Context mContext;

    public DropListAdapter(Context context, List<StockEntity> data) {
        mEntities = data;
        mContext = context;
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_drop_list, null);

            holder.changePercent = (TextView) convertView.findViewById(R.id.tv_change_percent);
            holder.code = (TextView) convertView.findViewById(R.id.tv_code);
//            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.now = (TextView) convertView.findViewById(R.id.tv_now);
            holder.handOver = (TextView) convertView.findViewById(R.id.tv_handover);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getCode())) {
            holder.code.setText(entity.getCode()+"  "+entity.getName());
        }
        holder.changePercent.setText(FormatUtils.format2PointTwo(entity.getChangePercent()));
        holder.now.setText(FormatUtils.format2PointTwo(entity.getNow()));
        holder.handOver.setText(FormatUtils.format2PointTwo(entity.getHandOver()));

        return convertView;
    }

    class ViewHolder {
        TextView code;      //股票代码
        TextView name;      //股票名称
        TextView changePercent;      //涨跌幅
        TextView now;
        TextView handOver;
    }
}

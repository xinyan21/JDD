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
 * 描述：自选股列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-12-26
 * @since 1.0
 */
public class SelfSelectionStocksAdapter extends BaseAdapter {
    private static final short CHANGEABLE_STATE_AS_CHANGE_VALUE = 1; //可变化值的状态为涨跌值
    private static final short CHANGEABLE_STATE_AS_CHANGE_PERCENT = 2; //可变化值的状态为涨跌幅
    private List<StockEntity> mEntities;
    private Context mContext;
    //默认为涨跌幅
    private short mCurrentChangeableState = CHANGEABLE_STATE_AS_CHANGE_PERCENT;

    public SelfSelectionStocksAdapter(Context context, List<StockEntity> entities) {
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_self_selection_stocks, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_stock_name);
            holder.code = (TextView) convertView.findViewById(R.id.tv_stock_code);
            holder.changeableValue = (TextView) convertView.findViewById(R.id.tv_changeable_value);
            holder.now = (TextView) convertView.findViewById(R.id.tv_now);

            holder.changeableValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentChangeableState = (short) (mCurrentChangeableState % 2 + 1);

                    notifyDataSetChanged();
                }
            });

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
        if (CHANGEABLE_STATE_AS_CHANGE_PERCENT == mCurrentChangeableState) {
            holder.changeableValue.setText(FormatUtils.format2PointTwo(entity.getChangePercent()) + "%");
        } else if (CHANGEABLE_STATE_AS_CHANGE_VALUE == mCurrentChangeableState) {
            holder.changeableValue.setText(FormatUtils.format2PointTwo(entity.getChangeValue()));
        }
        holder.changeableValue.setBackgroundColor(
                StockUtil.getColorByValue(mContext, entity.getChangeValue()));
        holder.now.setText(FormatUtils.format2PointTwo(entity.getNow()));
        holder.now.setTextColor(StockUtil.getColorByValue(mContext, entity.getNow() - entity.getPreClose()));

        return convertView;
    }

    class ViewHolder {
        TextView name;      //股票名称
        TextView code;      //股票代码
        TextView now;       //现价
        TextView changeableValue;       //可变化值，值范围为涨跌值、涨跌幅
    }
}

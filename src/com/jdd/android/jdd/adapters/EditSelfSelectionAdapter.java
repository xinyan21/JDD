package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.StockEntity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：编辑自选股列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-16
 * @last_edit 2016-03-16
 * @since 1.0
 */
public class EditSelfSelectionAdapter extends BaseAdapter {
    private List<StockEntity> mEntities;
    private Context mContext;
    private View mDeleteBg;     //删除按钮的背景

    public EditSelfSelectionAdapter(Context context, List<StockEntity> entities, View deleteBg) {
        mEntities = entities;
        mContext = context;
        mDeleteBg = deleteBg;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (null == mEntities || mEntities.size() < 1) {
            return null;
        }
        final StockEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.dslv_item_edit_self_selections, null);
            holder.checkArea = (RelativeLayout) convertView.findViewById(R.id.rl_select_to_delete);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.cb_delete_select);
            holder.dragHandle = (ImageView) convertView.findViewById(R.id.ibtn_drag_handle);
            holder.moveTop = (ImageButton) convertView.findViewById(R.id.ibtn_move_top);
            holder.stockCode = (TextView) convertView.findViewById(R.id.tv_stock_code);
            holder.stockName = (TextView) convertView.findViewById(R.id.tv_stock_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtils.isEmpty(entity.getName())) {
            holder.stockName.setText(entity.getName());
        }
        if (!StringUtils.isEmpty(entity.getCode())) {
            holder.stockCode.setText(entity.getCode());
        }
        if (entity.isSelectedToDelete()) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }

        holder.checkArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.isSelectedToDelete()) {
                    entity.setSelectedToDelete(false);
                } else {
                    entity.setSelectedToDelete(true);
                }
                notifyDataSetChanged();
                setDeleteBg();
            }
        });
        holder.moveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEntities.remove(position);
                mEntities.add(0, entity);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        RelativeLayout checkArea;
        CheckBox checkbox;
        TextView stockName;
        TextView stockCode;
        ImageButton moveTop;
        ImageView dragHandle;
    }

    /**
     * 设置删除按钮的背景色
     *
     * @return
     */
    public void setDeleteBg() {
        for (StockEntity entity : mEntities) {
            if (entity.isSelectedToDelete()) {
                mDeleteBg.setBackgroundResource(R.color.stock_red);
                return;
            }
        }
        mDeleteBg.setBackgroundResource(R.color.text_gray);
    }
}

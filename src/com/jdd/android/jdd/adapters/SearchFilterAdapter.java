package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.SelectableEntity;
import com.thinkive.adf.log.Logger;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：搜索条件表格适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-22
 * @last_edit 2016-02-22
 * @since 1.0
 */
public class SearchFilterAdapter extends BaseAdapter {
    private Context mContext;
    private List<SelectableEntity> mEntities;

    public SearchFilterAdapter(Context context, List<SelectableEntity> entities) {
        mContext = context;
        mEntities = entities;
    }

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
        final SelectableEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gv_item_search_filter, null);
            holder.value = (TextView) convertView.findViewById(R.id.tv_filter_value);
            //Bug: 当数据量超过100的时候，这里点击的position不对，所以放到Item的点击事件里面处理
//            holder.value.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Logger.d("Grid item click, position" + position);
//                    if (!entity.isSelected()) {
//                        entity.setSelected(true);
//                        //如果是单选，那么就要取消其它的选中状态
//                        if (!mIsMultiChoice) {
//                            for (int i = 0; i < mEntities.size(); i++) {
//                                if (i != position) {
//                                    mEntities.get(i).setSelected(false);
//                                }
//                            }
//                        }
//                    } else {
//                        entity.setSelected(false);
//                    }
//                    //第一个值为不限，如果选择了不限，那么后面的选中全部清除
//                    if (0 == position) {
//                        for (int i = 1; i < mEntities.size(); i++) {
//                            mEntities.get(i).setSelected(false);
//                        }
//                    } else {
//                        mEntities.get(0).setSelected(false);
//                    }
//                    notifyDataSetChanged();
//                }
//            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity.isSelected()) {
            holder.value.setTextColor(Color.WHITE);
            holder.value.setBackgroundResource(R.color.menu_text_orange);
        } else {
            holder.value.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            holder.value.setBackgroundResource(R.color.white);
        }
        if (!StringUtils.isEmpty(entity.getData())) {
            holder.value.setText(entity.getData());
        }

        return convertView;
    }

    class ViewHolder {
        TextView value;
    }
}

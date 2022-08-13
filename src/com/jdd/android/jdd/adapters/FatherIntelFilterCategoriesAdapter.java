package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.entities.SelectableEntity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：情报筛选条件父列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-02-23
 * @since 1.0
 */
public class FatherIntelFilterCategoriesAdapter extends BaseAdapter {
    private List<SelectableEntity> mEntities;
    private Context mContext;

    public FatherIntelFilterCategoriesAdapter(Context context, List<SelectableEntity> entities) {
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
        SelectableEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_item_father_intel_filter,
                    null);
            holder.categoryName = (TextView)
                    convertView.findViewById(R.id.tv_father_filter_category_name);
            holder.selectedFlag = (ImageView) convertView.findViewById(R.id.iv_selected_flag);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getData())) {
            holder.categoryName.setText(entity.getData());
        }
        if (entity.isSelected()) {
            holder.selectedFlag.setVisibility(View.VISIBLE);
        } else {
            holder.selectedFlag.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView categoryName;
        ImageView selectedFlag;
    }
}

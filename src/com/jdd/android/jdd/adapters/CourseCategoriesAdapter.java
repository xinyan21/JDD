package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.CourseCategoryEntity;
import com.thinkive.android.app_engine.utils.StringUtils;
import com.thinkive.android.app_engine.utils.SystemUtils;

import java.util.List;

/**
 * 描述：多空学堂课程大分类适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-08-25
 * @since 1.0
 */
public class CourseCategoriesAdapter extends BaseAdapter {
    private List<CourseCategoryEntity> mEntities;
    private Context mContext;

    public CourseCategoriesAdapter(Context context,List<CourseCategoryEntity> entities) {
        mEntities = entities;
        mContext=context;
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
        CourseCategoryEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_course_category, null);
            holder.about = (TextView) convertView.findViewById(R.id.tv_about);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_course_icon);
            holder.courseName = (TextView) convertView.findViewById(R.id.tv_course_name);
            holder.detailCategory = (TextView) convertView.findViewById(R.id.tv_course_detail_category);
            holder.totalTime = (TextView) convertView.findViewById(R.id.tv_total_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageResource(
                SystemUtils.getDrawableResId(mContext,entity.getIconResName()));
        if (!StringUtils.isEmpty(entity.getCategoryName())) {
            holder.courseName.setText(entity.getCategoryName());
        }
        if (!StringUtils.isEmpty(entity.getDetailCategory())) {
            holder.detailCategory.setText(entity.getDetailCategory());
        }
        if (!StringUtils.isEmpty(entity.getAbout())) {
            holder.about.setText(entity.getAbout());
        }
        holder.totalTime.setText(entity.getTotalTime()+"");

        return convertView;
    }

    class ViewHolder{
        ImageView icon;
        TextView courseName;
        TextView detailCategory;
        TextView totalTime;
        TextView about;
    }
}

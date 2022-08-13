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
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：推荐课程列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-08
 * @since 1.0
 */
public class RecommendedCoursesAdapter extends BaseAdapter {
    private List<CourseEntity> mEntities;
    private Context mContext;

    public RecommendedCoursesAdapter(Context context, List<CourseEntity> entities) {
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
        CourseEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_item_recommended_courses, null);
            holder.courseName = (TextView) convertView.findViewById(R.id.tv_course_name);
            holder.courseLogo = (ImageView) convertView.findViewById(R.id.iv_course_logo);
            holder.category = (TextView) convertView.findViewById(R.id.tv_course_detail_category);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getCourseName())) {
            holder.courseName.setText(entity.getCourseName());
        }

        return convertView;
    }

    class ViewHolder{
        TextView courseName;
        ImageView courseLogo;
        TextView category;  //课程分类、选修、必修
    }
}

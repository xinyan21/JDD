package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.CourseEntity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：首页最新课程列表列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-4-2
 * @since 1.0
 */
public class IndexLatestCoursesAdapter extends BaseAdapter {
    private List<CourseEntity> mEntities;
    private Context mContext;

    public IndexLatestCoursesAdapter(Context context, List<CourseEntity> entities) {
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
        CourseEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_latest_articles, null);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtils.isEmpty(entity.getCourseName())) {
            holder.title.setText(entity.getCourseName());
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;      //文章标题
    }
}

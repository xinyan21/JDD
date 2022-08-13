package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：智库首页列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-04
 * @since 1.0
 */
public class ExperiencesIndexAdapter extends BaseAdapter {
    private List<ExperienceEntity> mEntities;
    private Context mContext;

    public ExperiencesIndexAdapter(Context context, List<ExperienceEntity> entities) {
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
        ArticleEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_experience_index, null);

            holder.experienceType = (TextView) convertView.findViewById(R.id.tv_experience_type);
            holder.authorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
//            holder.tags = (GridView) convertView.findViewById(R.id.gv_intelligence_tags);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtils.isEmpty(entity.getTitle())) {
            holder.title.setText(entity.getTitle());
        }
        if (!StringUtils.isEmpty(entity.getCategory())) {
            holder.experienceType.setText(entity.getCategory());
        }
        if (!StringUtils.isEmpty(entity.getAuthorName())) {
            holder.authorName.setText(entity.getAuthorName());
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView authorName;
        TextView experienceType;
    }
}

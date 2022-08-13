package com.jdd.android.jdd.adapters;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.ui.FavoritesActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：用户主页的智文列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-3-1
 * @since 1.0
 */
public class UserIndexExperiencesAdapter extends BaseAdapter {
    private List<ExperienceEntity> mEntities;
    private Activity mActivity;

    public UserIndexExperiencesAdapter(Activity context, List<ExperienceEntity> entities) {
        mEntities = entities;
        mActivity = context;
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
        final ArticleEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.lv_item_user_index_experiences, null
            );
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.category = (TextView) convertView.findViewById(R.id.tv_experience_type);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getTitle())) {
            holder.title.setText(entity.getTitle());
        }
        if (entity.getCreateDate() > 0) {
            String datetime = DateFormat.format(
                    MyArticlesAdapter.DATE_FORMAT, entity.getCreateDate()).toString();
            holder.date.setText(datetime);
        }
        if (!StringUtils.isEmpty(entity.getCategory())) {
            holder.category.setText(entity.getCategory());
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;      //文章标题
        TextView date;        //日期
        TextView category;      //智文类型
    }
}

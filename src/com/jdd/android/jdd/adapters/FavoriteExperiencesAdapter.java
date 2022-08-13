package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.ui.FavoritesActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：收藏的智文列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-1-3
 * @since 1.0
 */
public class FavoriteExperiencesAdapter extends BaseAdapter {
    private List<ArticleEntity> mEntities;
    private FavoritesActivity mActivity;

    public FavoriteExperiencesAdapter(FavoritesActivity context, List<ArticleEntity> entities) {
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
                    R.layout.lv_item_favorite_experience, null);
            holder.authorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.delete = (ImageButton) convertView.findViewById(R.id.ibtn_delete);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.category = (TextView) convertView.findViewById(R.id.tv_experience_type);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.deleteFavorite(position);
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getTitle())) {
            holder.title.setText(entity.getTitle());
        }
        if (!StringUtils.isEmpty(entity.getAuthorName())) {
            holder.authorName.setText(entity.getAuthorName());
        }

        return convertView;
    }

    class ViewHolder {
        ImageButton delete;       //删除收藏
        TextView title;      //文章标题
        TextView authorName;        //作者姓名
        TextView category;      //智文类型
    }
}

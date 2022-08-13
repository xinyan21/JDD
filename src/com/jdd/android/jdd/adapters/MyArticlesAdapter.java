package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：我的情报列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-26
 * @since 1.0
 */
@Deprecated
public class MyArticlesAdapter extends BaseAdapter {
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm";
    private List<ArticleEntity> mEntities;
    private Context mContext;
    private boolean mShowAuthorName=true;

    public MyArticlesAdapter(Context context, List<ArticleEntity> entities) {
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_my_intels, null);

            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.authorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.title = (TextView) convertView.findViewById(R.id.tv_intelligence_title);
            holder.authorNameLayout = (LinearLayout) convertView.findViewById(
                    R.id.ll_author_name
            );

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
        if (entity.getCreateDate() > 0) {
            String datetime = DateFormat.format(DATE_FORMAT, entity.getCreateDate()).toString();
            holder.date.setText(datetime);
        } else {
            holder.date.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView authorName;
        TextView date;
        LinearLayout authorNameLayout;
    }

    public void setShowAuthorName(boolean showAuthorName) {
        mShowAuthorName = showAuthorName;
    }
}

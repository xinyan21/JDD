package com.jdd.android.jdd.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.ui.FavoritesActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：用户首页的情报列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-3-1
 * @since 1.0
 */
public class UserIndexIntelsAdapter extends BaseAdapter {
    private List<IntelligenceEntity> mEntities;
    private Activity mActivity;

    public UserIndexIntelsAdapter(Activity activity, List<IntelligenceEntity> entities) {
        mEntities = entities;
        mActivity = activity;
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
        final IntelligenceEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.lv_item_user_index_intels, null);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.price = (TextView) convertView.findViewById(R.id.tv_price_value);

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
        if (entity.getPrice() > 0) {
            holder.price.setText(String.valueOf(entity.getPrice()));
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;      //文章标题
        TextView date;        //作者姓名
        TextView price;
    }
}

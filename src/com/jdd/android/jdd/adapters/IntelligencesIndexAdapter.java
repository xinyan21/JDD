package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：情报首页列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-04
 * @since 1.0
 */
public class IntelligencesIndexAdapter extends BaseAdapter {
    private List<IntelligenceEntity> mEntities;
    private Context mContext;

    public IntelligencesIndexAdapter(Context context, List<IntelligenceEntity> entities) {
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
        IntelligenceEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_intelligence_index, null);

            holder.price = (TextView) convertView.findViewById(R.id.tv_price_value);
            holder.authorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtils.isEmpty(entity.getTitle())) {
            if (!StringUtils.isEmpty(entity.getIndustry())) {
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                String industry = entity.getIndustry();
                ssb.append("[");
                ssb.append(industry);
                ssb.append("]");
                ForegroundColorSpan span = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.text_red)
                );
                ssb.setSpan(span, 0, industry.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.text_gray)
                );
                ssb.append(entity.getTitle());
                ssb.setSpan(
                        span, industry.length() + 3, ssb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                holder.title.setText(ssb);
            } else {
                holder.title.setText(entity.getTitle());
            }
        }
        if (!StringUtils.isEmpty(entity.getAuthorName())) {
            holder.authorName.setText(entity.getAuthorName());
        }
        if (entity.getPrice() > 0) {
            holder.price.setText(String.valueOf(entity.getPrice()));
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView authorName;
        TextView price;
    }
}

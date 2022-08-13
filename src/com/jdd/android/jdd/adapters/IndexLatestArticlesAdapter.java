package com.jdd.android.jdd.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.ArticleEntity;
import com.jdd.android.jdd.entities.ExperienceEntity;
import com.jdd.android.jdd.entities.IntelligenceEntity;
import com.jdd.android.jdd.ui.FavoritesActivity;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：首页最新列表列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-6-14
 * @since 1.0
 */
public class IndexLatestArticlesAdapter extends BaseAdapter {
    private List<ArticleEntity> mEntities;
    private Context mContext;
    private DisplayImageOptions mDisplayImageOptions;

    public IndexLatestArticlesAdapter(Context context, List<ArticleEntity> entities) {
        mEntities = entities;
        mContext = context;
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.postProcessor(new BitmapProcessor() {
            @Override
            public Bitmap process(Bitmap bitmap) {
                return ProjectUtil.toRoundBitmap(bitmap);
            }
        });
        mDisplayImageOptions = builder.build();
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
        ArticleEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_latest_articles, null);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.about = (TextView) convertView.findViewById(R.id.tv_about);
            holder.authorName = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.goodCount = (TextView) convertView.findViewById(R.id.tv_cmt_good_count);
            holder.greatCount = (TextView) convertView.findViewById(R.id.tv_cmt_great_count);
            holder.headPortrait = (ImageView) convertView.findViewById(R.id.rci_head_portrait);
            holder.industry = (TextView) convertView.findViewById(R.id.tv_industry_name);
            holder.price = (TextView) convertView.findViewById(R.id.tv_price_value);
            holder.articleType = (TextView) convertView.findViewById(R.id.tv_article_type);
            holder.delete = (ImageButton) convertView.findViewById(R.id.ibtn_delete);
            if (mContext instanceof FavoritesActivity) {
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((FavoritesActivity) mContext).deleteFavorite(position);
                    }
                });
            }

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
        if (entity instanceof IntelligenceEntity) {
            holder.price.setVisibility(View.VISIBLE);
            holder.about.setVisibility(View.VISIBLE);
            IntelligenceEntity intel = (IntelligenceEntity) entity;
            holder.goodCount.setText(String.valueOf(intel.getGoodNum()));
            holder.greatCount.setText(String.valueOf(intel.getGreatNum()));
            holder.price.setText(intel.getPrice() + "今币");
            holder.articleType.setText("(股票情报)");
            holder.industry.setText(intel.getIndustry());
            if (!StringUtils.isEmpty(intel.getSummary()) && !"null".equals(intel.getSummary())) {
                holder.about.setText(intel.getSummary());
            } else {
                holder.about.setVisibility(View.GONE);
            }
            ImageLoader.getInstance().displayImage(
                    ProjectUtil.getFullUrl(intel.getHeadPortraitUrl()), holder.headPortrait, mDisplayImageOptions
            );
        } else if (entity instanceof ExperienceEntity) {
            holder.price.setVisibility(View.GONE);
            holder.about.setVisibility(View.GONE);
            ExperienceEntity exp = (ExperienceEntity) entity;
            holder.goodCount.setText(String.valueOf(exp.getGoodNum()));
            holder.greatCount.setText(String.valueOf(exp.getGreatNum()));
            holder.articleType.setText("(智文)");
            holder.industry.setText(exp.getIndustry());
            ImageLoader.getInstance().displayImage(
                    ProjectUtil.getFullUrl(exp.getHeadPortraitUrl()), holder.headPortrait, mDisplayImageOptions
            );
        }
        if (mContext instanceof FavoritesActivity) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;      //文章标题
        TextView industry;
        TextView authorName;
        TextView about;
        TextView greatCount;
        TextView goodCount;
        TextView price;
        TextView articleType;
        ImageView headPortrait;
        ImageButton delete;
    }
}

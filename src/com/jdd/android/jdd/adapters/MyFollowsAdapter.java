package com.jdd.android.jdd.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.others.AnimateFirstDisplayListener;
import com.jdd.android.jdd.ui.MyFollowsActivity;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：我的关注人列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-3-3
 * @since 1.0
 */
public class MyFollowsAdapter extends BaseAdapter {
    private List<UserEntity> mEntities;
    private MyFollowsActivity mActivity;
    private ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions mDisplayImageOptions;

    public MyFollowsAdapter(MyFollowsActivity activity, List<UserEntity> entities) {
        mEntities = entities;
        mActivity = activity;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_portrait)
                .showImageForEmptyUri(R.drawable.ic_default_portrait)
                .showImageOnFail(R.drawable.ic_default_portrait)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();
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
        final UserEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.lv_item_user_index_follows, null
            );
//            holder.intelsNum = (TextView) convertView.findViewById(R.id.tv_intels_num);
//            holder.experiencesNum = (TextView) convertView.findViewById(R.id.tv_experience_num);
//            holder.followsNum = (TextView) convertView.findViewById(R.id.tv_fans_num);
            holder.userName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.follow = (ImageButton) convertView.findViewById(R.id.ibtn_follow);
            holder.gender = (ImageView) convertView.findViewById(R.id.iv_gender);
            holder.isVerified = (ImageView) convertView.findViewById(R.id.iv_is_verified);
            holder.portrait = (ImageView) convertView.findViewById(R.id.iv_user_portrait);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);

            holder.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.isFollowed()) {
                        mActivity.cancelFollow(position);
                    } else {
                        mActivity.follow(position);
                    }
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (entity.isVerified()) {
            holder.isVerified.setVisibility(View.VISIBLE);
        } else {
            holder.isVerified.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(entity.getGender())) {
            if ("男".equals(entity.getGender())) {
                holder.gender.setImageResource(R.drawable.male);
            } else if ("女".equals(entity.getGender())) {
                holder.gender.setImageResource(R.drawable.female);
            } else {
                holder.gender.setVisibility(View.GONE);
            }
        }
        if (!StringUtils.isEmpty(entity.getNickName())) {
            holder.userName.setText(entity.getNickName());
        }
        if (null != entity.getLatestIntel()) {
            if (!StringUtils.isEmpty(entity.getLatestIntel().getTitle())) {
                holder.title.setText(entity.getLatestIntel().getTitle());
            }
        }
        if (entity.isFollowed()) {
            holder.follow.setImageResource(R.drawable.btn_has_followed);
        } else {
            holder.follow.setImageResource(R.drawable.btn_follow);
        }
        ImageLoader.getInstance().displayImage(
                ProjectUtil.getFullUrl(entity.getAvatarUrl()),
                holder.portrait, mDisplayImageOptions, mAnimateFirstListener
        );
//        holder.followsNum.setText(String.valueOf(entity.getFansCount()));
//        holder.intelsNum.setText(String.valueOf(entity.getIntelligenceCount()));
//        holder.experiencesNum.setText(String.valueOf(entity.getExperienceCount()));

        return convertView;
    }

    class ViewHolder {
        ImageView portrait;
        ImageView gender;
        ImageView isVerified;
        ImageView rank;
        ImageButton follow;
        TextView userName;
        TextView title;     //article title
//        TextView intelsNum;
//        TextView followsNum;
//        TextView experiencesNum;
    }
}

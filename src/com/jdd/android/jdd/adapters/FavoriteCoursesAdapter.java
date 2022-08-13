package com.jdd.android.jdd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.ui.FavoritesActivity;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：收藏的课程列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-1-3
 * @since 1.0
 */
public class FavoriteCoursesAdapter extends BaseAdapter {
    private List<CourseEntity> mEntities;
    private FavoritesActivity mActivity;

    public FavoriteCoursesAdapter(FavoritesActivity activity, List<CourseEntity> entities) {
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
        CourseEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.lv_item_favorite_course, null);
            holder.courseName = (TextView) convertView.findViewById(R.id.tv_course_name);
            holder.about = (TextView) convertView.findViewById(R.id.tv_about);
            holder.courseIcon = (ImageView) convertView.findViewById(R.id.iv_course_icon);
            holder.delete = (ImageButton) convertView.findViewById(R.id.ibtn_delete);
            holder.teachingModePic
                    = (ImageView) convertView.findViewById(R.id.iv_teaching_mode_pic);
            holder.teachingModePPT
                    = (ImageView) convertView.findViewById(R.id.iv_teaching_mode_ppt);
            holder.teachingModeVideo
                    = (ImageView) convertView.findViewById(R.id.iv_teaching_mode_video);
            holder.modeConnectFirst = (TextView) convertView.findViewById(R.id.tv_first_plus);
            holder.modeConnectSecond = (TextView) convertView.findViewById(R.id.tv_second_plus);

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

        if (!StringUtils.isEmpty(entity.getCourseName())) {
            holder.courseName.setText(entity.getCourseName());
        }
        if (!StringUtils.isEmpty(entity.getAbout())) {
            holder.about.setText(entity.getAbout());
        }
        if (QueryCoursesFunction.TEACHING_MODE_PIC_TEXT.equals(entity.getTeachingMode())) {
            holder.teachingModePic.setVisibility(View.VISIBLE);
            holder.modeConnectFirst.setVisibility(View.GONE);
            holder.teachingModeVideo.setVisibility(View.GONE);
        } else if (QueryCoursesFunction.TEACHING_MODE_VIDEO.equals(entity.getTeachingMode())) {
            holder.teachingModePic.setVisibility(View.GONE);
            holder.modeConnectFirst.setVisibility(View.GONE);
            holder.teachingModeVideo.setVisibility(View.VISIBLE);
        } else if (QueryCoursesFunction.TEACHING_MODE_VIDEO_PIC_TEXT.equals(entity.getTeachingMode())) {
            holder.teachingModePic.setVisibility(View.VISIBLE);
            holder.modeConnectFirst.setVisibility(View.VISIBLE);
            holder.teachingModeVideo.setVisibility(View.VISIBLE);
        }
        String iconUrl = ProjectUtil.getDefaultCourseIcon();
        if (!StringUtils.isEmpty(entity.getPptUrl()) && !"null".equals(entity.getPptUrl())) {
            iconUrl = ProjectUtil.getCourseIconUrl(entity.getPptUrl());
        }
        ImageLoader.getInstance().displayImage(iconUrl, holder.courseIcon);

        return convertView;
    }

    class ViewHolder {
        ImageView courseIcon;       //课程图标
        ImageButton delete;       //删除收藏
        TextView courseName;      //文章标题
        ImageView teachingModePic;       //图文授课
        ImageView teachingModePPT;       //ppt授课
        ImageView teachingModeVideo;       //视频授课
        TextView about;     //简介
        TextView modeConnectFirst, modeConnectSecond;
    }
}

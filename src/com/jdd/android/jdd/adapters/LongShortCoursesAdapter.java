package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.function.QueryCoursesFunction;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：多空学堂课程列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-01
 * @since 1.0
 */
public class LongShortCoursesAdapter extends BaseAdapter {
    private List<CourseEntity> mEntities;
    private Context mContext;

    public LongShortCoursesAdapter(Context context, List<CourseEntity> entities) {
        mEntities = entities;
        mContext = context;
    }

    @Override
    public int getCount() {
        return null == mEntities ? 0 : mEntities.size();
    }

    @Override
    public CourseEntity getItem(int i) {
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

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_item_long_short_course, null
            );
            holder.about = (TextView) convertView.findViewById(R.id.tv_course_about_value);
            holder.courseIcon = (ImageView) convertView.findViewById(R.id.iv_course_logo);
            holder.courseName = (TextView) convertView.findViewById(R.id.tv_course_name);
            holder.learnedCount = (TextView) convertView.findViewById(R.id.tv_learned_number);
            holder.buy = (Button) convertView.findViewById(R.id.btn_buy_course);
            holder.teachingModePic =
                    (ImageView) convertView.findViewById(R.id.iv_teaching_mode_pic);
            holder.teachingModePPT =
                    (ImageView) convertView.findViewById(R.id.iv_teaching_mode_ppt);
            holder.teachingModeVideo =
                    (ImageView) convertView.findViewById(R.id.iv_teaching_mode_video);
            holder.price = (TextView) convertView.findViewById(R.id.tv_course_price);
            holder.modeConnectFirst = (TextView) convertView.findViewById(R.id.tv_first_plus);
            holder.modeConnectSecond = (TextView) convertView.findViewById(R.id.tv_second_plus);
            holder.freeFlag = (TextView) convertView.findViewById(R.id.tv_free_flag);
            holder.priceUnit = (TextView) convertView.findViewById(R.id.tv_course_price_item);

            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity.getAbout())) {
            holder.about.setText(entity.getAbout());
        }
        if (!StringUtils.isEmpty(entity.getCourseName())) {
            holder.courseName.setText(entity.getCourseName());
        }
        holder.learnedCount.setText(String.valueOf(entity.getLearnedCount()));
        if (entity.getPrice() == 0) {
            holder.freeFlag.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.GONE);
            holder.priceUnit.setVisibility(View.GONE);
            holder.buy.setVisibility(View.GONE);
        } else {
            holder.freeFlag.setVisibility(View.GONE);
            holder.price.setVisibility(View.VISIBLE);
            holder.priceUnit.setVisibility(View.VISIBLE);
            holder.buy.setVisibility(View.VISIBLE);
            holder.price.setText(String.valueOf(entity.getPrice()));
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
        ImageView courseIcon;
        TextView courseName;
        TextView learnedCount;
        TextView about;
        Button buy;
        TextView price, priceUnit;
        ImageView teachingModeVideo, teachingModePic, teachingModePPT;
        TextView modeConnectFirst, modeConnectSecond;
        TextView freeFlag;
    }
}

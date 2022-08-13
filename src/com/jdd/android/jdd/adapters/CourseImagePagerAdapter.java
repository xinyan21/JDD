package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.CourseEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：模板列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-08-25
 * @since 1.0
 */
public class CourseImagePagerAdapter extends BaseAdapter {
    private List<String> mEntities;
    private Context mContext;

    public CourseImagePagerAdapter(Context context, List<String> entities) {
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
        String entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.vp_item_course_images, null);
            holder.img = (ImageView) convertView.findViewById(R.id.iv_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(entity)) {
            ImageLoader.getInstance().displayImage(entity, holder.img);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView img;
    }
}

package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.thinkive.android.app_engine.utils.StringUtils;

/**
 * 描述：情报标签列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-04
 * @since 1.0
 */
@Deprecated
public class IntelligenceTagsAdapter extends BaseAdapter {
    private String[] mTags;
    private Context mContext;

    public IntelligenceTagsAdapter(Context context, String[] tags) {
        mTags = tags;
        mContext=context;
    }

    @Override
    public int getCount() {
        return null == mTags ? 0 : mTags.length;
    }

    @Override
    public Object getItem(int i) {
        return null == mTags ? null : mTags[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TextView view = new TextView(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        view.setTextSize(16);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        if (!StringUtils.isEmpty(mTags[position])) {
            view.setText(mTags[position]);
        }

        return view;
    }
}

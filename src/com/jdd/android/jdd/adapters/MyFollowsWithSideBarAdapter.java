package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.sidebar.GroupMemberBean;
import com.thinkive.android.app_engine.engine.TKActivity;

import java.util.List;

/**
 * 描述：我的关注人列表（非用户首页的那个列表，而是带侧边栏索引的）
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-05
 * @last_edit 2016-01-05
 * @since 1.0
 */
public class MyFollowsWithSideBarAdapter extends BaseAdapter {
    private TKActivity mContext;
    private List<UserEntity> mEntities;
    private List<GroupMemberBean> mGroupMemberBeans;

    public MyFollowsWithSideBarAdapter(
            TKActivity activity, List<UserEntity> entities, List<GroupMemberBean> groupMemberBeans) {
        mContext = activity;
        mEntities = entities;
        mGroupMemberBeans = groupMemberBeans;
    }

    @Override
    public int getCount() {
        return null != mEntities ? mEntities.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null != mEntities ? mEntities.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mEntities || mEntities.size() < 1) {
            return null;
        }
        UserEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_my_follows, null);
            holder.catalog = (TextView) convertView.findViewById(R.id.tv_catalog);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.catalog.setVisibility(View.VISIBLE);
            if (null != mGroupMemberBeans && null != mGroupMemberBeans.get(position)) {
                holder.catalog.setText(mGroupMemberBeans.get(position).getSortLetters());
            }
        } else {
            holder.catalog.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mGroupMemberBeans.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mGroupMemberBeans.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    class ViewHolder {
        TextView catalog;        //用户名首字母
        ImageView avatar;        //头像
        ImageView gender;        //性别
        TextView userName;        //用户名
        ImageView isVerified;        //是否已认证
        ImageView rankIcon;        //军衔图标
        TextView latestArticleTitle;        //最新文章标题
        ImageButton followState;        //关注状态：已关注、相互关注
    }
}

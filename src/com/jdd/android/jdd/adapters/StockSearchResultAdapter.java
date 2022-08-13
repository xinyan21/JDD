package com.jdd.android.jdd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.db.SelfSelectionStocksManager;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.entities.UserEntity;
import com.jdd.android.jdd.ui.SelfSelectionStocksFragment;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.util.List;

/**
 * 描述：股票搜索结果列表适配器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-1-14
 * @since 1.0
 */
public class StockSearchResultAdapter extends BaseAdapter {
    private List<StockEntity> mEntities;
    private TKActivity mContext;

    public StockSearchResultAdapter(TKActivity context, List<StockEntity> entities) {
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
        final StockEntity entity = mEntities.get(position);
        if (null == entity) {
            return null;
        }

        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_item_stock_search_result, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.code = (TextView) convertView.findViewById(R.id.tv_code);
            holder.addSelfSelection
                    = (ImageButton) convertView.findViewById(R.id.ibtn_add_self_selection);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtils.isEmpty(entity.getName())) {
            holder.name.setText(entity.getName());
        }
        if (!StringUtils.isEmpty(entity.getCode())) {
            holder.code.setText(entity.getCode());
        }
        if (null != SelfSelectionStocksManager.getInstance(mContext).query(entity)) {
            holder.addSelfSelection.setImageResource(R.drawable.ibtn_delete_self_selection);
        } else {
            holder.addSelfSelection.setImageResource(R.drawable.ibtn_add_to_self_selection);
        }

        holder.addSelfSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEntity loginUser =
                        (UserEntity) DataCache.getInstance().getCache().getCacheItem(
                                CacheKey.KEY_CURRENT_LOGIN_USER_INFO
                        );
                long userId = SelfSelectionStocksFragment.sDefaultUserID;
                if (null != loginUser) {
                    userId = loginUser.getUserId();
                }
                entity.setUserId(userId);
                SelfSelectionStocksManager.getInstance(mContext).insert(entity);
                Toast.makeText(mContext, "已成功添加" + entity.getName(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView name;      //股票名称
        TextView code;      //股票代码
        ImageButton addSelfSelection;
    }
}

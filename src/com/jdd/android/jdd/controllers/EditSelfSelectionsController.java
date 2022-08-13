package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.ui.EditSelfSelectionsActivity;
import com.mobeta.android.dslv.DragSortListView;
import com.thinkive.adf.listeners.ListenerControllerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：编辑自选股控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-16
 * @last_edit 2016-03-16
 * @since 1.0
 */
public class EditSelfSelectionsController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener, DragSortListView.DropListener {
    public static final int ON_DROP = 888888;
    private EditSelfSelectionsActivity mActivity;

    public EditSelfSelectionsController(EditSelfSelectionsActivity activity) {
        mActivity = activity;
    }

    @Override
    public void register(int i, View view) {
        switch (i) {
            case ON_CLICK:
                view.setOnClickListener(this);

                break;
            case ON_ITEM_CLICK:
                if (view instanceof ListView) {
                    ((ListView) view).setOnItemClickListener(this);
                }

                break;
            case ON_DROP:
                if (view instanceof DragSortListView) {
                    ((DragSortListView) view).setDropListener(this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                if (null == mActivity.stockEntities) {
                    mActivity.finish();
                } else {
                    mActivity.saveEdit();
                }

                break;
            case R.id.ibtn_delete:
                deleteStocks();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void deleteStocks() {
        if (null == mActivity.stockEntities) {
            return;
        }
        List<StockEntity> temp = new ArrayList<>();
        for (StockEntity item : mActivity.stockEntities) {
            if (item.isSelectedToDelete()) {
                temp.add(item);
            }
        }
        mActivity.stockEntities.removeAll(temp);
        if (temp.size() > 0) {
            mActivity.delete(temp);
        }
        mActivity.adapter.notifyDataSetChanged();
        mActivity.adapter.setDeleteBg();
    }

    @Override
    public void drop(int from, int to) {
        StockEntity entity = mActivity.stockEntities.get(from);
        mActivity.stockEntities.remove(from);
        mActivity.stockEntities.add(to, entity);
        mActivity.adapter.notifyDataSetChanged();
    }
}

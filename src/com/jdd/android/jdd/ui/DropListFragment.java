package com.jdd.android.jdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryRoseListAction;
import com.jdd.android.jdd.adapters.DropListAdapter;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryRoseListRequest;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.android.app_engine.engine.TKFragment;

import java.util.List;

/**
 * 描述：空头跌幅榜
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
@Deprecated
public class DropListFragment extends TKFragment implements IQueryServer {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private View mLayout;
    private ListView mListView;
    private DropListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_drop_list, container, false);

        findViews();
        setListeners();
        initViews();
        initData();

        return mLayout;
    }

    @Override
    protected void findViews() {
        mListView = (ListView) mLayout.findViewById(R.id.lv_drop_list);
    }

    @Override
    protected void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockEntity entity = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("stock_entity",entity);
                Intent intent = new Intent(getActivity(), StockDetailsActivity.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        Parameter parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(200));
        startTask(new QueryRoseListRequest(parameter, new QueryRoseListAction(this)));
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        switch (taskId) {
            case 200:
                final List<StockEntity> data = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == data || data.size() < 1) {
                    return;
                }
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = null;
                        mAdapter = new DropListAdapter(getActivity(), data);
                        mListView.setAdapter(mAdapter);
                    }
                });

                break;
        }
    }

    @Override
    public void onServerError(int taskId, Bundle bundle) {

    }

    @Override
    public void onNetError(int taskId, Bundle bundle) {

    }

    @Override
    public void onInternalError(int taskId, Bundle bundle) {

    }

    private void bindData() {

    }
}

package com.jdd.android.jdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.actions.QueryRoseListAction;
import com.jdd.android.jdd.adapters.RiseListAdapter;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.interfaces.IQueryServer;
import com.jdd.android.jdd.requests.QueryRoseListRequest;
import com.jdd.android.jdd.views.HVListView;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.android.app_engine.engine.TKFragment;

import java.util.List;

/**
 * 描述：多头涨幅榜
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-16
 * @since 1.0
 */
@Deprecated
public class RiseListFragment extends TKFragment implements IQueryServer {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private HVListView mListView;       //涨幅榜列表
    //---------------表头字段，点击时切换升降序--------------------
    private TextView mHeaderNegotiableMarketCapitalization;
    private TextView mHeaderNow;
    private TextView mHeaderRiseSpeed;
    private TextView mHeaderQuantityRelative;
    private TextView mHandOver;
    private View mLayout;

    private RiseListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_rise_list, container, false);

        findViews();
        setListeners();
        initViews();
        initData();

        return mLayout;
    }

    @Override
    protected void findViews() {
        mListView = (HVListView) mLayout.findViewById(R.id.hv_rise_list);
        mListView.mListHead = (LinearLayout) mLayout.findViewById(R.id.layout_header);

        mHeaderNegotiableMarketCapitalization = (TextView)
                mLayout.findViewById(R.id.tv_header_negotiable_market_capitalization);
        mHandOver = (TextView) mLayout.findViewById(R.id.tv_header_handover);
        mHeaderNow = (TextView) mLayout.findViewById(R.id.tv_header_now);
        mHeaderRiseSpeed = (TextView) mLayout.findViewById(R.id.tv_header_rise_speed);
        mHeaderQuantityRelative = (TextView) mLayout.findViewById(R.id.tv_header_quantity_relative);
    }

    @Override
    protected void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockEntity entity = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("stock_entity", entity);
                Intent intent = new Intent(getActivity(), StockDetailsActivity.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        Parameter parameter = new Parameter();
        parameter.addParameter("taskId", String.valueOf(100));
        parameter.addParameter("sortFlag", "up");
        startTask(new QueryRoseListRequest(parameter, new QueryRoseListAction(this)));
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onQuerySuccess(int taskId, Bundle bundle) {
        switch (taskId) {
            case 100:
                final List<StockEntity> data = (List<StockEntity>)
                        mCache.getCacheItem(bundle.getString(String.valueOf(taskId)));
                if (null == data || data.size() < 1) {
                    return;
                }
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = null;
                        mAdapter = new RiseListAdapter(getActivity(), mListView, data);
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
}

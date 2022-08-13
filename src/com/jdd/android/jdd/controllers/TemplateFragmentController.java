package com.jdd.android.jdd.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdd.android.jdd.R;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKFragment;

/**
 * 描述：Fragment模板控制器
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-01-02
 * @last_edit 2016-01-02
 * @since 1.0
 */
public class TemplateFragmentController extends ListenerControllerAdapter implements
        View.OnClickListener, AdapterView.OnItemClickListener {
    private TKFragment mFragment;

    public TemplateFragmentController(TKFragment fragment) {
        mFragment = fragment;
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
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}

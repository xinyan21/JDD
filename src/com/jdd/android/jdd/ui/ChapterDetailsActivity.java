package com.jdd.android.jdd.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.thinkive.android.app_engine.engine.TKActivity;

/**
 * 描述：课程章节
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-07
 * @since 1.0
 */
@Deprecated
public class ChapterDetailsActivity extends TKActivity {
    private TextView mTitle;
    private ImageButton mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail_video_ppt);
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }
}

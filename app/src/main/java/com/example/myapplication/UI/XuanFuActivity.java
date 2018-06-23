package com.example.myapplication.UI;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.Adapter.BaseAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Temp.SectionDecoration;
import com.gavin.com.library.listener.PowerGroupListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class XuanFuActivity extends BaseTitleActivity {
    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mTitleTv;
    private TextView mHeaderTv;
    private RecyclerView mRlv;
    private FloatingActionButton mFab;
    private ArrayList<TempBean> dataList;

    @Override
    public void initViews() {
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mHeaderTv = (TextView) findViewById(R.id.header_tv);
        mRlv = (RecyclerView) findViewById(R.id.rlv);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        MyAdapter adater = new MyAdapter(R.layout.base_item_recycler);
        mRlv.setLayoutManager(new LinearLayoutManager(this));
        mRlv.setAdapter(adater);
        dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(new TempBean("测试---->", R.drawable.ic_launcher_background));
        }
        adater.setNewData(dataList);
        initDecoration(mRlv, dataList);
    }

    @Override
    public void initListeners() {

    }

    @NotNull
    @Override
    public String setTitle() {
        return "悬浮按钮";
    }

    @Override
    public int setLayoutResource() {
        return R.layout.activity_xuan_fu;
    }

    /**
     * 添加悬浮布局
     *
     * @param rlv
     * @param dataList
     */
    private void initDecoration(RecyclerView rlv, final ArrayList<TempBean> dataList) {
        SectionDecoration decoration = SectionDecoration.Builder
                .init(new PowerGroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //获取组名，用于判断是否是同一组
                        if (dataList.size() > position) {
                            return dataList.get(position).getName();
                        }
                        return null;
                    }

                    @Override
                    public View getGroupView(int position) {
                        //获取自定定义的组View
                        if (dataList.size() > position) {
                            View view = getLayoutInflater().inflate(R.layout.base_item_recycler, null, false);
                            ((TextView) view.findViewById(R.id.tv_name)).setText(dataList.get(position).getName());
                            ((ImageView) view.findViewById(R.id.iv_left)).setImageResource(dataList.get(position).getIcon());
                            return view;
                        } else {
                            return null;
                        }
                    }
                }).build();
        rlv.addItemDecoration(decoration);
    }

    private class MyAdapter extends BaseAdapter<TempBean, BaseViewHolder> {
        public MyAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, TempBean item) {

        }
    }

    private class TempBean {
        private String name;
        private int icon;

        public TempBean(String name, int icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }
}
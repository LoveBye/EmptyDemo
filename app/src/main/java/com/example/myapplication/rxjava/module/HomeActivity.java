package com.example.myapplication.rxjava.module;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.rxjava.base.BaseActivity;
import com.example.myapplication.rxjava.base.BaseViewPagerAdapter;
import com.example.myapplication.rxjava.constant.GlobalConfig;
import com.example.myapplication.rxjava.module.rxjava2.operators.OperatorsFragment;
import com.example.myapplication.rxjava.module.rxjava2.usecases.UseCasesFragment;
import com.example.myapplication.rxjava.module.web.WebViewActivity;
import com.example.myapplication.rxjava.util.ScreenUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.home_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.home_appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.home_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.fab)
    FloatingActionButton mFab;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        StatusBarUtil.setTranslucent(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4 以上版本
            // 设置 Toolbar 高度为 80dp，适配状态栏
            ViewGroup.LayoutParams layoutParams = mToolbarTitle.getLayoutParams();
//            layoutParams.height = ScreenUtil.dip2px(this,ScreenUtil.getStatusBarHeight(this));
            layoutParams.height = ScreenUtil.dip2px(this, 80);
            mToolbarTitle.setLayoutParams(layoutParams);
        }

        initToolBar(mToolbar, false, "");
        String[] titles = {
                GlobalConfig.CATEGORY_NAME_OPERATORS,
                GlobalConfig.CATEGORY_NAME_EXAMPLES
        };

        BaseViewPagerAdapter pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), titles);
        pagerAdapter.addFragment(new OperatorsFragment());
        pagerAdapter.addFragment(new UseCasesFragment());

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        }
    }


    @OnClick(R.id.fab)
    public void onViewClicked() {
        WebViewActivity.start(this, "https://github.com/nanchen2251", "我的GitHub,欢迎Star");
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }
}

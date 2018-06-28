package com.example.app.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;

import org.jetbrains.annotations.NotNull;

public class RxJavaActivity extends BaseTitleActivity {
    private TextView mTvRxJava;

    @Override
    public void initViews() {
        mTvRxJava = findViewById(R.id.tv_rxjava);
    }

    @Override
    public void initListeners() {
        mTvRxJava.setOnClickListener(this);
    }

    @NonNull
    @Override
    public String setTitle() {
        return "RxJavaActivity";
    }

    @Override
    public int setLayoutResource() {
        return R.layout.activity_rx_java;
    }

    @Override
    public void onClick(@NotNull View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_rxjava:
                mTvRxJava.append("我的天呢\n");
                break;
        }
    }
}
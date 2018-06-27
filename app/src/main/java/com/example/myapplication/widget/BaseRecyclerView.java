package com.example.myapplication.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class BaseRecyclerView extends RecyclerView {
    public BaseRecyclerView(Context context) {
        super(context);
        initRecyclerView();
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRecyclerView();
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRecyclerView();
    }

    void initRecyclerView() {
        addItemDecoration(new SpaceItemDecoration(2));
        setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
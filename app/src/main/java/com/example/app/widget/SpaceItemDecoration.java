package com.example.app.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
//    private int spanCount;
    private int space;
    private boolean includeEdge;

    public SpaceItemDecoration(int space) {
//        this.spanCount = spanCount;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        outRect.left = space;
        outRect.right = space;
        if (position != 0 && position != 1) {
            outRect.top = 2 * space;
        } else {
            outRect.top = space;
        }
    }
}
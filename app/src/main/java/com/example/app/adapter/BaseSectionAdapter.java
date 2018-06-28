package com.example.app.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

public abstract class BaseSectionAdapter<T extends SectionEntity, K extends BaseViewHolder> extends BaseSectionQuickAdapter<T, K> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public BaseSectionAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, sectionHeadResId, data);
    }

//    public BaseSectionAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
//        super(layoutResId, sectionHeadResId, data);
//    }
}
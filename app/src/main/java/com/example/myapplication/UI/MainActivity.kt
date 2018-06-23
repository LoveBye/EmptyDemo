package com.example.myapplication.UI

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.example.myapplication.Adapter.BaseSectionAdapter
import com.example.myapplication.Moudle.MySection
import com.example.myapplication.R
import com.example.myapplication.Utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseTitleActivity() {

    override fun initListeners() {

    }

    override fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        recycler_catalog.layoutManager = layoutManager
        var list = ArrayList<MySection>()
        for (i in 0..40) {
            if (i % 4 == 0) {
                list.add(MySection(true, "我的Title" + i, "我的Content"))
            } else {
                list.add(MySection(false, "我的Title" + i, "我的Content"))
            }
        }
        val mAdapter = MyAdapter(R.layout.base_item_recycler, R.layout.base_header_recycler, list)
        recycler_catalog.setAdapter(mAdapter)

//        设置动画和拖拽，缺少相关方法？？还是不支持kotlin？？
//        val callback = SimpleItemTouchHelperCallback(mAdapter)
//        val mItemTouchHelper = ItemTouchHelper(callback)
//        mAdapter.setItemTouchHelper(mItemTouchHelper)
//        mAdapter.setDragViewId(R.id.tv_name)

//        mItemTouchHelper.attachToRecyclerView(recycler_catalog)
        var mCurrentPosition = 0
        val linearLayoutManager: LinearLayoutManager = recycler_catalog.layoutManager as LinearLayoutManager
        var mSuspensionHeight = 0
        recycler_catalog.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mSuspensionHeight = linear_header.getHeight()
            }

            
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onClickListener = View.OnClickListener { ToastUtils.showToast(this, "点击了右边按钮") }
        setRightButton(R.drawable.share_black, onClickListener)
    }

    override fun setTitle(): String {
        return "小辉是第一个activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_main
    }

    inner class MyAdapter : BaseSectionAdapter<MySection, BaseViewHolder> {
        constructor(layoutResId: Int, sectionHeadResId: Int, data: List<MySection>) : super(layoutResId, sectionHeadResId, data)

        override fun convertHead(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.title)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener({
                ToastUtils.showToast(mContext, "" + helper.getLayoutPosition())
            })
        }

        override fun convert(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.content)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener({
                ToastUtils.showToast(mContext, "" + helper.getLayoutPosition())
            })
        }
    }
}
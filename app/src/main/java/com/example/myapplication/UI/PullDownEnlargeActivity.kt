package com.example.myapplication.UI

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.example.myapplication.Adapter.BaseSectionAdapter
import com.example.myapplication.Moudle.MySection
import com.example.myapplication.R
import com.example.myapplication.Utils.ToastUtils
import kotlinx.android.synthetic.main.activity_pull_down_enlarge.*

class PullDownEnlargeActivity : BaseTitleActivity() {

    override fun initListeners() {

    }

    override fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        recycler_catalog.layoutManager = layoutManager
        var list = ArrayList<MySection>()
        for (i in 0..40) {
            list!!.add(MySection(false, R.drawable.share_black, "Title" + i, "Content" + i))
        }
        val mAdapter = MyAdapter(R.layout.base_item_recycler, R.layout.base_header_recycler, list)
        recycler_catalog.setAdapter(mAdapter)
        val img = ImageView(this)
        img.setImageResource(R.drawable.default_loading_pic)
        mAdapter.setHeaderView(img)
        recycler_catalog.setZoomView(img)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onClickListener = View.OnClickListener { ToastUtils.showToast(this, "点击了右边按钮") }
        setRightButton(R.drawable.share_black, onClickListener)
    }

    override fun setTitle(): String {
        return "下拉放大Activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_pull_down_enlarge
    }

    inner class MyAdapter : BaseSectionAdapter<MySection, BaseViewHolder> {
        constructor(layoutResId: Int, sectionHeadResId: Int, data: List<MySection>) : super(layoutResId, sectionHeadResId, data)

        override fun convertHead(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.title)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener({
                ToastUtils.showToast(mContext, "" + data.indexOf(item))
            })
        }

        override fun convert(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.content)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener({
                ToastUtils.showToast(mContext, "" + data.indexOf(item))
            })
        }
    }
}
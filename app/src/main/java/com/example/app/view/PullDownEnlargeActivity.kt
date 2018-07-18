package com.example.app.view

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.adapter.BaseSectionAdapter
import com.example.app.bean.MySection
import com.example.app.R
import com.example.app.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_pull_down_enlarge.*

class PullDownEnlargeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_down_enlarge)
        val layoutManager = LinearLayoutManager(this)
        recycler_catalog.layoutManager = layoutManager
        val list = ArrayList<MySection>()
        for (i in 0..40) {
            list.add(MySection(i, R.drawable.share_black, "Title" + i, "Content" + i))
        }
        val mAdapter = MyAdapter(R.layout.base_item_recycler, R.layout.base_header_recycler, list)
        recycler_catalog.setAdapter(mAdapter)
        val img = ImageView(this)
        img.setImageResource(R.drawable.temp_0)
        mAdapter.setHeaderView(img)
        recycler_catalog.setZoomView(img)
    }

    inner class MyAdapter(layoutResId: Int, sectionHeadResId: Int, data: List<MySection>) :
            BaseSectionAdapter<MySection, BaseViewHolder>(layoutResId, sectionHeadResId, data) {
        override fun convertHead(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.title)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener {
                ToastUtils.showToast(mContext, "" + data.indexOf(item))
            }
        }

        override fun convert(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.content)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener {
                ToastUtils.showToast(mContext, "" + data.indexOf(item))
            }
        }
    }
}
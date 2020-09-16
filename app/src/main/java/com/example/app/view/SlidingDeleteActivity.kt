package com.example.app.view

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.R
import com.example.app.adapter.BaseAdapter
import com.example.app.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_sliding_delete.*
import java.util.*

class SlidingDeleteActivity : BaseTitleActivity() {
    private val adapter: MyAdapter = MyAdapter(R.layout.item_recycler_sliding_delete)
    private val dataList: ArrayList<String> = ArrayList<String>()

    override fun initViews() {
        for (i in 0..9) {
            dataList.add("1234567890-->$i")
        }
        adapter.setNewData(dataList)
        recycler_sliding!!.layoutManager = LinearLayoutManager(this)
        recycler_sliding!!.adapter = adapter
    }

    override fun initListeners() {
    }

    override fun setTitle(): String {
        return "侧滑删除Activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_sliding_delete
    }

    private inner class MyAdapter(layoutResId: Int) : BaseAdapter<String, BaseViewHolder>(layoutResId) {
        override fun convert(helper: BaseViewHolder, item: String) {
            val position = data.indexOf(item)
            helper.setText(R.id.item_content, item)
            helper.getView<View>(R.id.sliding_delete).setOnClickListener {
                data.remove(item)
                notifyDataSetChanged()
                recycler_sliding.slidingFinish()
            }
            helper.getView<View>(R.id.sliding_top).setOnClickListener {
                data.add(0, item)
                data.removeAt(position + 1)
                notifyDataSetChanged()
                recycler_sliding.slidingFinish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ToastUtils.showToast(mActivity, "侧滑删除的返回键")
    }
}
package com.example.app.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.adapter.BaseSectionAdapter
import com.example.app.bean.MySection
import com.example.app.R
import com.example.app.widget.SectionDecoration
import com.example.app.utils.ToastUtils
import com.gavin.com.library.listener.PowerGroupListener
import kotlinx.android.synthetic.main.activity_group_title.*

class GroupTitleActivity : BaseTitleActivity() {

    override fun initListeners() {

    }

    override fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        recycler_catalog.layoutManager = layoutManager
        val list = ArrayList<MySection>()
        for (i in 0..40) {
            if (i % 6 == 0) {
                list.add(MySection(false, R.drawable.share_black, "分组" + i, "分组Activity"+ i))
            } else {
                list.add(MySection(false, R.drawable.share_black, "", "分组Activity" + i))
            }
        }
        val mAdapter = MyAdapter(R.layout.base_item_recycler, R.layout.base_header_recycler, list)
        recycler_catalog.setAdapter(mAdapter)
//        val img = ImageView(this)
//        img.setImageResource(R.drawable.default_loading_pic)
//        mAdapter.setHeaderView(img)
//        recycler_catalog.setZoomView(img)
        initDecoration(recycler_catalog, list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onClickListener = View.OnClickListener { ToastUtils.showToast(this, "点击了右边按钮") }
        setRightButton(R.drawable.share_black, onClickListener)
    }

    override fun setTitle(): String {
        return "分组Activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_group_title
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

    /**
     * 添加悬浮布局
     *
     * @param rlv
     * @param dataList
     */
    private fun initDecoration(rlv: RecyclerView, dataList: java.util.ArrayList<MySection>) {
        val decoration = SectionDecoration.Builder
                .init(object : PowerGroupListener {
                    override fun getGroupName(position: Int): String? {
                        //获取组名，用于判断是否是同一组
                        return if (dataList.size > position) {
                            dataList[position].title
                        } else null
                    }

                    override fun getGroupView(position: Int): View? {
                        //获取自定定义的组View
                        if (dataList.size > position) {
                            val view = layoutInflater.inflate(R.layout.base_header_recycler, null, false)
                            view.findViewById<TextView>(R.id.tv_name).setText(dataList[position].title)
//                            view.findViewById<ImageView>(R.id.iv_left).setImageResource(dataList[position].icon)
                            return view
                        } else {
                            return null
                        }
                    }
                })
                .setGroupHeight(100)
                .build()
        rlv.addItemDecoration(decoration)
    }
}
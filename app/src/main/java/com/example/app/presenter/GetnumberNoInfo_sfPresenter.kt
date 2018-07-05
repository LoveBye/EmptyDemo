package com.example.app.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.R
import com.example.app.adapter.BaseAdapter
import com.example.app.bean.GetChanneOnel
import com.example.app.bean.GetnumberNoInfo_sfBean
import com.example.app.model.GetnumberNoInfo_sfModel
import com.example.app.utils.LogUtils
import com.example.app.utils.ToastUtils
import com.library.flowlayout.FlowLayoutManager
import io.reactivex.functions.Consumer

class GetnumberNoInfo_sfPresenter(val context: Context) : BasePresenter(), Consumer<GetnumberNoInfo_sfBean> {
    val mModel = GetnumberNoInfo_sfModel(context)
    lateinit var recycler: RecyclerView
    val adapter = MyAdapter(R.layout.item_recycler_flow)

    fun getnumberNoInfo_sf(recycler: RecyclerView) {
        showProgressDialog(context, "加载中，请稍候")
        this.recycler = recycler
        mModel.getnumberNoInfo_sf(this)
    }

    override fun accept(data: GetnumberNoInfo_sfBean) {
        try {
            Log.e("accept", "成功:" + data.toString() + "\n")
            dismissProgressDialog()
            val listener = View.OnClickListener {
                mAlertDialog!!.dismiss()
                recycler.layoutManager = FlowLayoutManager()
                recycler.adapter = adapter
                adapter.mItemTouchHelper.attachToRecyclerView(recycler)//设置可拖动
                adapter.setNewData(data.data[0].GetChanneOnelList)
            }
            showAlertDialog(context, "我自定义的提示信息", listener)
        } catch (e: Exception) {
            ToastUtils.showToast(context, e.toString())
        }
    }

    inner class MyAdapter(layoutResId: Int) : BaseAdapter<GetChanneOnel, BaseViewHolder>(layoutResId) {
        override fun convert(helper: BaseViewHolder, item: GetChanneOnel) {
            mCanSwipe = true//设置可滑动删除
            helper.setText(R.id.tv_name, item.Channel_Name)
        }

        override fun remove(position: Int) {
            //删除某项之前，对该项进行操作，否则remove super以后，该项就不在集合中，按此位置拿不到删掉的数据
            LogUtils.showLog(context, "删除成功了" + data[position].Channel_Name)
            super.remove(position)
        }
    }
}
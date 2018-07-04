package com.example.app.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.app.bean.GetnumberNoInfo_sfBean
import com.example.app.model.GetnumberNoInfo_sfModel
import io.reactivex.functions.Consumer

class GetnumberNoInfo_sfPresenter(val context: Context) : BasePresenter(), Consumer<GetnumberNoInfo_sfBean> {
    val mModel = GetnumberNoInfo_sfModel(context)
    lateinit var recycler: RecyclerView
    fun getnumberNoInfo_sf(recycler: RecyclerView) {
        showProgressDialog(context, "加载中0，请稍候")
        showProgressDialog(context, "加载中1，请稍候")
        showProgressDialog(context, "加载中2，请稍候")
        this.recycler = recycler
        mModel.getnumberNoInfo_sf(this)
    }

    override fun accept(data: GetnumberNoInfo_sfBean) {
        Log.e("accept", "成功:" + data.toString() + "\n")
        dismissProgressDialog()
        dismissProgressDialog()
        showProgressDialog(context, "又一次加载中，请稍候")
        var listener = View.OnClickListener {
            mAlertDialog!!.dismiss()
            showToast(context, "点击了确定")
        }
        showAlertDialog(context, "我自定义的提示信息", listener)
    }
}
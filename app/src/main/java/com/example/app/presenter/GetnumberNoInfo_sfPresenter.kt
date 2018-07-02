package com.example.app.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.app.bean.GetnumberNoInfo_sfBean
import com.example.app.model.GetnumberNoInfo_sfModel
import io.reactivex.functions.Consumer

class GetnumberNoInfo_sfPresenter(val context: Context) : BasePresenter(), Consumer<GetnumberNoInfo_sfBean> {
    val mModel = GetnumberNoInfo_sfModel(context)
    lateinit var recycler: RecyclerView
    fun getnumberNoInfo_sf(recycler: RecyclerView) {
        this.recycler = recycler
        mModel.getnumberNoInfo_sf(this)
    }

    override fun accept(data: GetnumberNoInfo_sfBean) {
        Log.e("accept", "成功:" + data.toString() + "\n")
    }
}
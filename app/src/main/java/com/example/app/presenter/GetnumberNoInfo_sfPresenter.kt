package com.example.app.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.example.app.bean.GetnumberNoInfo_sfBean
import com.example.app.model.GetnumberNoInfo_sfModel
import com.example.app.utils.LogUtils
import io.reactivex.functions.Consumer

class GetnumberNoInfo_sfPresenter(val context: Context) : Consumer<GetnumberNoInfo_sfBean> {
    val mModel = GetnumberNoInfo_sfModel(context)

    fun getnumberNoInfo_sf(recycler: RecyclerView) {
        mModel.getnumberNoInfo_sf(this)
    }

    override fun accept(t: GetnumberNoInfo_sfBean) {
        LogUtils.showLog(context, "accept")
    }
}
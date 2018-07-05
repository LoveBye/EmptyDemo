package com.example.app.model

import android.content.Context
import com.example.app.bean.GetnumberNoInfo_sfBean
import com.example.app.utils.LogUtils
import com.example.app.utils.ToastUtils
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class GetnumberNoInfo_sfModel(val context: Context) : Observable<Response>() {
    override fun subscribeActual(observer: Observer<in Response>?) {

    }

    val TAG = "TAG"
    fun getnumberNoInfo_sf(consumer: Consumer<GetnumberNoInfo_sfBean>) {
        //post方式提交的数据
        val formBody = FormBody.Builder()
                .add("op", "GetnumberNoInfo_sf")
                .add("user_Group_ID", "327")
                .add("Account_ID", "1970")
                .build()
        Observable.create(ObservableOnSubscribe<Response> { e ->
            val builder = Request.Builder()
                    .url("http://api.taiyasaifu.com/ajax/xzHandler.ashx")
                    .post(formBody)
                    .build()
            val response = OkHttpClient()
                    .newCall(builder)
                    .execute()
            e.onNext(response)
        })
                .map(Function<Response, GetnumberNoInfo_sfBean> {
                    try {
                        if (it.isSuccessful()) {
                            if (it.body() != null) {
                                val fromJson = Gson().fromJson(it.body()!!.string(), GetnumberNoInfo_sfBean::class.java)
                                LogUtils.showLog(context, "" + fromJson)
                                return@Function fromJson
                            }
                        }
                    } catch (e: Exception) {
                        ToastUtils.showToast(context, e.toString())
                    }
                    return@Function null
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)
    }
}
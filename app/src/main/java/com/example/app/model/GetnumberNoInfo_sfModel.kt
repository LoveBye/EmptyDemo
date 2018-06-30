package com.example.app.model

import android.content.Context
import android.util.Log
import com.example.app.bean.GetnumberNoInfo_sfBean
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class GetnumberNoInfo_sfModel(val context: Context) {
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
        }).map(Function<Response, GetnumberNoInfo_sfBean> { response ->
            val body = response.body()
            return@Function Gson().fromJson(body!!.string(), GetnumberNoInfo_sfBean::class.java)
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, Consumer { throwable ->
                    Log.e(TAG, "subscribe 线程:" + Thread.currentThread().name + "\n")
                    Log.e(TAG, "失败：" + throwable.message + "\n")
                })
    }
}
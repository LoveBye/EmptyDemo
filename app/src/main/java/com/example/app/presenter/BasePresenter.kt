package com.example.app.presenter

import android.content.Context
import com.example.app.utils.LogUtils

abstract class BasePresenter {
    fun showToast(context: Context, msg: String) {
        LogUtils.showLog(context, msg)
    }
}
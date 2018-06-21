package com.example.myapplication.Utils

import android.content.Context
import android.util.Log

object LogUtils {
    fun showLog(context: Context, msg: String) {
        Log.e(context.javaClass.name, msg)
    }
}
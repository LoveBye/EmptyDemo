package com.example.myapplication

import com.example.myapplication.Utils.LogUtils
import com.example.myapplication.Utils.ToastUtils

class MainActivity : BaseTitleActivity() {
    override fun setTitle(): String {
        return "我的第一个activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun initListeners() {
        ToastUtils.showToast(this, "initListeners方法")
    }

    init {
        LogUtils.showLog(this, "init方法")
    }
}
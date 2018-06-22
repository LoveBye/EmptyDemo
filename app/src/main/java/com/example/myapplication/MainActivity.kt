package com.example.myapplication

import android.os.Bundle
import android.view.View
import com.example.myapplication.Utils.LogUtils
import com.example.myapplication.Utils.ToastUtils

class MainActivity : BaseTitleActivity() {

    override fun initListeners() {

    }

    override fun initViews() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onClickListener = View.OnClickListener { ToastUtils.showToast(this, "点击了右边按钮") }
        setRightButton(R.drawable.share_black, onClickListener)
    }

    override fun setTitle(): String {
        return "我的第一个activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_main
    }

    init {
        LogUtils.showLog(this, "init方法")
    }
}
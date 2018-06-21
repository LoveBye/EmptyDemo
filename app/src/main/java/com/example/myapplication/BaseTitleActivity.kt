package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.myapplication.Utils.StatusBarCompat
import com.example.myapplication.Utils.ToastUtils
import kotlinx.android.synthetic.main.layout_base_title.*

abstract class BaseTitleActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> ToastUtils.showToast(this, "点击了返回")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarCompat.transparencyBar(this)
        setContentView(R.layout.layout_base_title)
        val inflate = LayoutInflater.from(this).inflate(setLayoutResource(), null)
        view_content.addView(inflate)
        img_back.setOnClickListener(this)
        tv_title.setText(setTitle())
    }

    //    接收子类传回的Title
    abstract fun setTitle(): String

    //    接收子类传回的布局文件
    abstract fun setLayoutResource(): Int

    override fun initListeners() {
        ToastUtils.showToast(this, "initListeners方法")
    }
}
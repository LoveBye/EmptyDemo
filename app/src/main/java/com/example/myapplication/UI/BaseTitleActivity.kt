package com.example.myapplication.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.Utils.ToastUtils
import kotlinx.android.synthetic.main.base_layout_title.*

abstract class BaseTitleActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout_title)
        val inflate = LayoutInflater.from(this).inflate(setLayoutResource(), null)
        view_content.addView(inflate)

        initViews()
        initListeners()
        img_back.setOnClickListener(this)
        tv_title.setText(setTitle())
    }

    abstract fun initViews()

    abstract fun initListeners()

    //    接收子类传回的Title
    abstract fun setTitle(): String

    //    接收子类传回的布局文件
    abstract fun setLayoutResource(): Int

    //初始化右边按钮图标和点击事件
    fun setRightButton(drawableResId: Int, onClickListener: View.OnClickListener) {
        img_right?.setImageResource(drawableResId)
        img_right?.setOnClickListener(onClickListener)
    }

    //点击事件
    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> ToastUtils.showToast(this, "点击了返回")
        }
    }
}
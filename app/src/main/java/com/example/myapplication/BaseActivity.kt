package com.example.myapplication

import android.os.Bundle
import android.support.v4.app.FragmentActivity

abstract class BaseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListeners()
    }

    abstract fun initListeners()
}
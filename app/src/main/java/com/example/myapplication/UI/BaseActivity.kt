package com.example.myapplication.UI

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.example.myapplication.Utils.LogUtils
import com.example.myapplication.Utils.StatusBarCompat

abstract class BaseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarCompat.transparencyBar(this)
        LogUtils.showLog(this,"onCreate")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.showLog(this,"onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.showLog(this,"onPause")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.showLog(this,"onStart")
    }

    override fun onRestart() {
        super.onRestart()
        LogUtils.showLog(this,"onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.showLog(this,"onDestroy")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.showLog(this,"onStop")
    }
}
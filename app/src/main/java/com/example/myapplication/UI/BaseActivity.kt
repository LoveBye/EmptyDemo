package com.example.myapplication.UI

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.example.myapplication.Utils.LogUtils
import com.example.myapplication.Utils.StatusBarCompat

abstract class BaseActivity : FragmentActivity() {
    val PERMISSION_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        StatusBarCompat.transparencyBar(this)
        LogUtils.showLog(this, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.showLog(this, "onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.showLog(this, "onPause")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.showLog(this, "onStart")
    }

    override fun onRestart() {
        super.onRestart()
        LogUtils.showLog(this, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.showLog(this, "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.showLog(this, "onStop")
    }
}
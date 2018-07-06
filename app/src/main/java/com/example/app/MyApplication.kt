package com.example.app

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.example.app.utils.DensityHelper


class MyApplication : Application() {
    val DESIGN_WIDTH = 720F
    override fun onCreate() {
        super.onCreate()
        DensityHelper(this, DESIGN_WIDTH).activate()  //DESIGN_WIDTH为设计图宽度，同样不要忘记清单文件配置Application，另 布局中使用pt
        AndroidNetworking.initialize(applicationContext)
    }
}
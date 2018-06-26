package com.example.myapplication.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object ToastUtils {
    //单例吐司
    private var sToast: Toast? = null

    fun showToast(context: Context, msg: String) {
        if (sToast == null) {
            //注意内存泄露，静态的且持有引用。所以context.getApplicationContext()
            sToast = Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT)
        }
        //如果这个Toast已经在显示了，那么这里会立即修改文本
        sToast!!.setText(msg) //吐司可以setText
        sToast!!.show()
        //控制台输出
        Log.e(context.javaClass.name, msg)
    }
}
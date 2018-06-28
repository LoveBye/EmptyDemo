package com.example.app.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
object StatusBarCompat {
    private val INVALID_VAL = -1
    private val COLOR_DEFAULT = Color.parseColor("#20000000")
    private val MIUI = 942664
    private val FLYME = 63498
    private val ANDROID_M = 7482
    val SYS_EMUI = "sys_emui"
    val SYS_MIUI = "sys_miui"
    val SYS_FLYME = "sys_flyme"
    private val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"
    private val KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level"
    private val KEY_EMUI_VERSION = "ro.build.version.emui"
    private val KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion"
    //小米
    //华为
    //魅族
    val system: String
        get() {
            var SYS = ""
            try {
                val prop = Properties()
                prop.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
                if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                        || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                        || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
                    SYS = SYS_MIUI
                } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                        || prop.getProperty(KEY_EMUI_VERSION, null) != null
                        || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                    SYS = SYS_EMUI
                } else if (meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
                    SYS = SYS_FLYME
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return SYS
            }

            return SYS
        }

    val meizuFlymeOSFlag: String
        get() = getSystemProperty("ro.build.display.id", "")

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun compat(activity: Activity, statusColor: Int) {
        //当前手机版本为5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.window.statusBarColor = statusColor
            }
            return
        }
        //当前手机版本为4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            var color = COLOR_DEFAULT
            val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
            if (statusColor != INVALID_VAL) {
                color = statusColor
            }
            val statusBarView = View(activity)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity))
            statusBarView.setBackgroundColor(color)
            contentView.addView(statusBarView, lp)
        }
    }

    fun compat(activity: Activity) {
        compat(activity, INVALID_VAL)
    }
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
    /**
     * 修改状态栏为全透明，并且不影响导航栏颜色，0919亲测好使
     *
     * @param activity
     */
    @TargetApi(19)
    fun transparencyBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.BLACK
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (StatusBarCompat.system == StatusBarCompat.SYS_MIUI) {
            StatusBarCompat.compat(activity, Color.parseColor("#000000"))
        } else if (StatusBarCompat.system == StatusBarCompat.SYS_FLYME) {
            StatusBarCompat.compat(activity, Color.parseColor("#000000"))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断如果是6.0及其以上，才将状态栏字体和图标颜色变为黑色
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            StatusBarCompat.compat(activity, Color.parseColor("#000000"))
        }
    }
    /**
     * 修改状态栏为全透明，并且不影响导航栏颜色，0919亲测好使
     *
     * @param activity
     */
    @TargetApi(19)
    fun transparencyBar(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.BLACK
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (StatusBarCompat.system == StatusBarCompat.SYS_MIUI) {
            StatusBarCompat.compat(activity, Color.parseColor("#000000"))
        } else if (StatusBarCompat.system == StatusBarCompat.SYS_FLYME) {
            StatusBarCompat.compat(activity, Color.parseColor("#000000"))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断如果是6.0及其以上，才将状态栏字体和图标颜色变为黑色
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            StatusBarCompat.compat(activity, Color.parseColor("#000000"))
        }
    }
    private fun getSystemProperty(key: String, defaultValue: String): String {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val get = clz.getMethod("get", String::class.java, String::class.java)
            return get.invoke(clz, key, defaultValue) as String
        } catch (e: Exception) {
        }
        return defaultValue
    }
}
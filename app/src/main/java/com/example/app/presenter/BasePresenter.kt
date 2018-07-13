package com.example.app.presenter

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.widget.TextView
import com.example.app.R
import com.example.app.bean.ImageModel
import com.example.app.utils.ToastUtils

abstract class BasePresenter {
    val PERMISSION_REQUEST_CODE = 15//申请权限
    var mProgressDialog: ProgressDialog? = null
    var mAlertDialog: Dialog? = null

    /**
     * 显示吐司
     */
    fun showToast(context: Context, msg: String) {
        ToastUtils.showToast(context, msg)
    }

    /**
     * 显示加载框
     */
    fun showProgressDialog(context: Context, msg: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(context)
        } else {
            mProgressDialog!!.dismiss()
            mProgressDialog = ProgressDialog(context)
        }
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog!!.setCancelable(true)
        mProgressDialog!!.setCanceledOnTouchOutside(false)
        mProgressDialog!!.setMessage(msg)
        mProgressDialog!!.show()
    }

    /**
     * 加载完毕，取消等待框
     */
    fun dismissProgressDialog() {
        mProgressDialog!!.dismiss()
    }

    /**
     * 显示提示框
     */
    fun showAlertDialog(context: Context, msg: String, listener: View.OnClickListener) {
        mAlertDialog = Dialog(context)
        val window = mAlertDialog!!.window
        mAlertDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)//设置Dialog没有标题。需在setContentView之前设置，在之后设置会报错
        window!!.setContentView(R.layout.base_dialog)
        val tv_hint = window.findViewById<TextView>(R.id.tv_dialog_hint)
        val tvConfirm = window.findViewById<TextView>(R.id.tv_dialog_confirm)
        val tvCancel = window.findViewById<TextView>(R.id.tv_dialog_cancel)
        tv_hint.setText(msg)
        tvCancel.setOnClickListener { mAlertDialog!!.dismiss() }
        tvConfirm.setOnClickListener(listener)
        mAlertDialog!!.show()
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private fun checkPermission(activity: Activity, permission: String) {
        //permission=Manifest.permission.WRITE_EXTERNAL_STORAGE

        val hasWriteContactsPermission = ContextCompat.checkSelfPermission(activity.getApplication(), permission);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限
//            ToastUtils.showToast(activity, "有权限")
        } else {
            //没有权限，申请权限。
            val strings: Array<String> = arrayOf(permission)
            ActivityCompat.requestPermissions(activity, strings, PERMISSION_REQUEST_CODE);
        }
    }
}
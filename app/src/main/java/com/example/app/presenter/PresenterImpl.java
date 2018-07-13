package com.example.app.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import static butterknife.internal.Utils.arrayOf;

public class PresenterImpl {
    private final Activity mActivity;
    private ProgressDialog mProgressDialog;
    private int PERMISSION_REQUEST_CODE = 15;

    public PresenterImpl(Activity activity) {
        mActivity = activity;
    }

    void showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mActivity);//等待转转转;
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    void showProgressDialog(String title) {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mActivity);//等待转转转;
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setTitle(title);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    void dismissProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    void checkPermission(Activity activity, String permission) {
        //permission=Manifest.permission.WRITE_EXTERNAL_STORAGE

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(activity.getApplication(), permission);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限
//            ToastUtils.showToast(activity, "有权限")
        } else {
            //没有权限，申请权限。
            String[] strings = {permission};
            ActivityCompat.requestPermissions(activity, strings, PERMISSION_REQUEST_CODE);
        }
    }
}
package com.example.app.presenter;

import android.app.Activity;
import android.app.ProgressDialog;

public class PresenterImpl {
    private final Activity mActivity;
    private ProgressDialog mProgressDialog;

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
}
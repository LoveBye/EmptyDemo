package com.example.app.view

import com.example.app.R
import com.example.app.presenter.GetnumberNoInfo_sfPresenter
import kotlinx.android.synthetic.main.activity_retrofit.*

class RetrofitActivity : BaseTitleActivity() {
    val mPresenter = GetnumberNoInfo_sfPresenter(this)

    override fun initViews() {
        mPresenter.getnumberNoInfo_sf(recycler_getnumbernoinfo_sf)
    }

    override fun initListeners() {
    }

    override fun setTitle(): String {
        return "RetrofitActivity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_retrofit
    }
}
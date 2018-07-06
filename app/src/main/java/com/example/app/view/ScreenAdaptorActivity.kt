package com.example.app.view

import com.example.app.R

class ScreenAdaptorActivity : BaseTitleActivity() {
    override fun initViews() {

    }

    override fun initListeners() {
    }

    override fun setTitle(): String {
        return "ScreenAdaptorActivity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_screen_adaptor
    }
}
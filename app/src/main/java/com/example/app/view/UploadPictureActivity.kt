package com.example.app.view

import android.content.Intent
import com.example.app.R
import com.example.app.presenter.ChoosePicPresenterImpl
import kotlinx.android.synthetic.main.activity_upload_picture.*

class UploadPictureActivity : BaseTitleActivity() {
    var mChoosePicPresenterImpl: ChoosePicPresenterImpl? = null
    override fun initViews() {
        mChoosePicPresenterImpl = ChoosePicPresenterImpl(item_parent, this, recycler_pic)
    }

    override fun initListeners() {
        img_pic.setOnClickListener {
            mChoosePicPresenterImpl!!.chooseSinglePic(item_parent, img_pic)
        }
    }

    override fun setTitle(): String {
        return "UploadPictureActivity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_upload_picture
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mChoosePicPresenterImpl!!.onActivityResult(requestCode, data)
    }
}
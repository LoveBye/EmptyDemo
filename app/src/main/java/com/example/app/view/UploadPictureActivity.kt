package com.example.app.view

import android.content.Intent
import com.example.app.R
import com.example.app.presenter.ChoosePicPresenterImpl
import kotlinx.android.synthetic.main.activity_upload_picture.*
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.R.attr.bitmap
import android.graphics.Color
import android.graphics.Rect
import java.io.FileNotFoundException
import java.io.FileOutputStream
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import com.example.app.utils.ToastUtils


class UploadPictureActivity : BaseTitleActivity() {
    var mChoosePicPresenterImpl: ChoosePicPresenterImpl? = null
    override fun initViews() {
        mChoosePicPresenterImpl = ChoosePicPresenterImpl(item_parent, this, recycler_pic)
    }

    override fun initListeners() {
        img_pic.setOnClickListener {
            mChoosePicPresenterImpl!!.chooseSinglePic(item_parent, img_pic)
        }
        tv_clip_img.setOnClickListener {
            val view = getWindow().getDecorView()
            view.setDrawingCacheEnabled(true)
            view.buildDrawingCache()
            var bitmap = view.getDrawingCache()
            val frame = Rect()
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame)
            val toHeight = frame.top
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, frame.width(), frame.height())
//            bitmap = Bitmap.createBitmap(bitmap, x, y + 2 * toHeight, width, height).toInt()
            try {
                val fout = FileOutputStream("mnt/sdcard/test.png")
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            view.isDrawingCacheEnabled = false
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
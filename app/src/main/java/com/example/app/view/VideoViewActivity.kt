package com.example.app.view

import com.example.app.R
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_videoview.*

class VideoViewActivity : BaseTitleActivity() {
    override fun initViews() {
        btn.setOnClickListener {
            val path = Environment.getExternalStorageDirectory().path + "/" + et1.text.toString()//获取视频路径
            val uri = Uri.parse(path)//将路径转换成uri
            video.setVideoURI(uri)//为视频播放器设置视频路径
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                video.setMediaController(MediaController(this@VideoViewActivity))
            }//显示控制栏
            video.setOnPreparedListener {
                video.start()//开始播放视频
            }
        }
    }

    override fun initListeners() {
    }

    override fun setTitle(): String {
        return "VideoViewActivity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_videoview
    }
}
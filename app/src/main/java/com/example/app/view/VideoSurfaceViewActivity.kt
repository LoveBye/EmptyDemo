package com.example.app.view

import android.view.View
import com.example.app.R
import android.view.SurfaceHolder
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_video_surfaceview.*
import android.media.MediaPlayer
import android.util.Log
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.util.*
import android.content.pm.ActivityInfo
import android.widget.RelativeLayout

class VideoSurfaceViewActivity : BaseTitleActivity() {
    private var path: String? = null//本地文件路径
    private var holder: SurfaceHolder? = null
    private var player: MediaPlayer? = null//媒体播放器
    private var timer: Timer? = null//定时器
    private var task: TimerTask? = null//定时器任务
    private var position = 0
    override fun initViews() {
        play.isEnabled = false
        holder = sfv!!.holder
        holder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //当进度条停止拖动的时候，把媒体播放器的进度跳转到进度条对应的进度
                if (player != null) {
                    player!!.seekTo(seekBar.progress)
                }
            }
        })
        holder!!.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                //为了避免图像控件还没有创建成功，用户就开始播放视频，造成程序异常，所以在创建成功后才使播放按钮可点击
                Log.e("zhangdi", "surfaceCreated")
                play.isEnabled = true
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                Log.e("zhangdi", "surfaceChanged")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                //当程序没有退出，但不在前台运行时，因为surfaceview很耗费空间，所以会自动销毁，
                // 这样就会出现当你再次点击进程序的时候点击播放按钮，声音继续播放，却没有图像
                //为了避免这种不友好的问题，简单的解决方式就是只要surfaceview销毁，我就把媒体播放器等
                //都销毁掉，这样每次进来都会重新播放，当然更好的做法是在这里再记录一下当前的播放位置，
                //每次点击进来的时候把位置赋给媒体播放器，很简单加个全局变量就行了。
                Log.e("zhangdi", "surfaceDestroyed")
                if (player != null) {
                    position = player!!.currentPosition
                    stop()
                }
            }
        })
    }

    override fun initListeners() {
    }

    override fun setTitle(): String {
        return "VideoSurfaceViewActivity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_video_surfaceview
    }

    //    播放
    fun play(v: View) {
        play()
        path?.let {
            Log.d("zhangdi", it)
        }
    }

    private var isPause: Boolean = false
    fun play() {
        play.isEnabled = false//在播放时不允许再点击播放按钮
        if (isPause) {//如果是暂停状态下播放，直接start
            isPause = false
            player!!.start()
            return
        }
        path = (Environment.getExternalStorageDirectory().path + "/")
        path += et.text.toString()//sdcard的路径加上文件名称是文件全路径
        val file = File(path)
        if (!file.exists()) {//判断需要播放的文件路径是否存在，不存在退出播放流程
            Toast.makeText(this, "文件路径不存在", Toast.LENGTH_LONG).show()
            return
        }
        try {
            player = MediaPlayer()
            player!!.setDataSource(path)
            player!!.setDisplay(holder)//将影像播放控件与媒体播放控件关联起来
            player!!.setOnCompletionListener {
                //视频播放完成后，释放资源
                play.isEnabled = true
                stop()
            }
            player!!.setOnPreparedListener {
                //媒体播放器就绪后，设置进度条总长度，开启计时器不断更新进度条，播放视频
                Log.d("zhangdi", "onPrepared")
                sb.max = player!!.duration
                timer = Timer()
                task = object : TimerTask() {
                    override fun run() {
                        try {
                            val time = player!!.currentPosition
                            sb.progress = time
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                timer!!.schedule(task, 0, 1)
                sb.progress = position
                player!!.seekTo(position)
                player!!.start()
                changeVideoSize()
            }
            player!!.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //    暂停
    fun pause(view: View) {
        pause()
    }

    private fun pause() {
        if (player != null && player!!.isPlaying) {
            player!!.pause()
            isPause = true
            play.isEnabled = true
        }
    }

    //    重播
    fun replay(view: View) {
        replay()
    }

    private fun replay() {
        isPause = false
        if (player != null) {
            stop()
            play()
        }
    }

    fun stop(view: View) {
        stop()
    }

    private fun stop() {
        isPause = false
        if (player != null) {
            sb.progress = 0
            player!!.stop()
            player!!.release()
            player = null
            if (timer != null) {
                timer!!.cancel()
            }
            play.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }


    fun changeVideoSize() {
        var videoWidth = player!!.getVideoWidth()
        var videoHeight = player!!.getVideoHeight()

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        val max: Float
        if (resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max(videoWidth.toFloat() / sfv.width, videoHeight.toFloat() / sfv.height)
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(videoWidth.toFloat() / sfv.height, videoHeight.toFloat() / sfv.width)
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = Math.ceil((videoWidth.toFloat() / max).toDouble()).toInt()
        videoHeight = Math.ceil((videoHeight.toFloat() / max).toDouble()).toInt()

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        sfv.setLayoutParams(RelativeLayout.LayoutParams(videoWidth, videoHeight))
    }

    fun onVideoSizeChanged(mp: MediaPlayer, width: Int, height: Int) {
        changeVideoSize()
    }
}
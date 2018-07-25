package com.example.app.view

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.R
import com.example.app.adapter.BaseSectionAdapter
import com.example.app.bean.MySection
import com.example.app.rxjava.module.RxJavaActivity
import com.example.app.smalllib.VCamera
import com.example.app.utils.DeviceUtils
import com.example.app.utils.ToastUtils
import com.example.app.widget.SectionDecoration
import com.gavin.com.library.listener.PowerGroupListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseTitleActivity() {

    override fun initListeners() {

    }

    override fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        recycler_catalog.layoutManager = layoutManager
        val list = ArrayList<MySection>()
        list.add(MySection(0, R.drawable.share_black, "RecyclerView", "下拉放大Activity"))
        list.add(MySection(1, R.drawable.share_black, "RecyclerView", "分组Activity"))
        list.add(MySection(2, R.drawable.share_black, "RecyclerView", "侧滑删除Activity"))
        list.add(MySection(3, R.drawable.share_black, "RecyclerView", "瀑布流Activity"))
        list.add(MySection(4, R.drawable.share_black, "RxJava", "RxJavaActivity"))
        list.add(MySection(5, R.drawable.share_black, "Retrofit", "RetrofitActivity"))
        list.add(MySection(6, R.drawable.share_black, "Picture", "UploadPictureActivity"))
        list.add(MySection(7, R.drawable.share_black, "TextView", "TextViewActivity"))
        list.add(MySection(8, R.drawable.share_black, "自定义View", "CameraActivity"))
        list.add(MySection(9, R.drawable.share_black, "自定义View", "VideoSurfaceViewActivity"))
        list.add(MySection(10, R.drawable.share_black, "自定义View", "VideoViewActivity"))
        val mAdapter = MyAdapter(R.layout.base_item_recycler, R.layout.base_header_recycler, list)
        recycler_catalog.setAdapter(mAdapter)
        initDecoration(recycler_catalog, list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onClickListener = View.OnClickListener { ToastUtils.showToast(this, "点击了右边按钮") }
        setRightButton(R.drawable.share_black, onClickListener)
    }

    override fun setTitle(): String {
        return "第一个activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_main
    }

    inner class MyAdapter(layoutResId: Int, sectionHeadResId: Int, data: List<MySection>)
        : BaseSectionAdapter<MySection, BaseViewHolder>(layoutResId, sectionHeadResId, data) {

        override fun convertHead(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.title)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener {
                ToastUtils.showToast(this@MainActivity, "" + data.indexOf(item))
            }
        }

        override fun convert(helper: BaseViewHolder, item: MySection) {
            helper.setText(R.id.tv_name, item.content)
            helper.getView<TextView>(R.id.tv_name).setOnClickListener {
                val mIntent = Intent()
                when (item.id) {
                    0 -> {
                        mIntent.setClass(mContext, PullDownEnlargeActivity::class.java)
                        startActivity(mIntent)
                    }
                    1 -> {
                        mIntent.setClass(mContext, GroupTitleActivity::class.java)
                        startActivity(mIntent)
                    }
                    2 -> {
                        mIntent.setClass(mContext, SlidingDeleteActivity::class.java)
                        startActivity(mIntent)
                    }
                    3 -> {
                        mIntent.setClass(mContext, WaterfallActivity::class.java)
                        startActivity(mIntent)
                    }
                    4 -> {
                        mIntent.setClass(mContext, RxJavaActivity::class.java)
                        startActivity(mIntent)
                    }
                    5 -> {
                        mIntent.setClass(mContext, RetrofitActivity::class.java)
                        startActivity(mIntent)
                    }
                    6 -> {
                        mIntent.setClass(mContext, UploadPictureActivity::class.java)
                        startActivity(mIntent)
                    }
                    7 -> {
                        mIntent.setClass(mContext, TextViewActivity::class.java)
                        startActivity(mIntent)
                    }
                    8 -> {
                        initSmallVideo()
                        mIntent.setClass(mContext, CameraActivity::class.java)
                        startActivity(mIntent)
                    }
                    9 -> {
                        initSmallVideo()
                        mIntent.setClass(mContext, VideoSurfaceViewActivity::class.java)
                        startActivity(mIntent)
                    }  10 -> {
                        initSmallVideo()
                        mIntent.setClass(mContext, VideoViewActivity::class.java)
                        startActivity(mIntent)
                    }
                    else -> ToastUtils.showToast(mContext, "" + data.indexOf(item))
                }
            }
        }
    }

    /**
     * 添加悬浮布局
     *
     * @param rlv
     * @param dataList
     */
    private fun initDecoration(rlv: RecyclerView, dataList: java.util.ArrayList<MySection>) {
        val decoration = SectionDecoration.Builder
                .init(object : PowerGroupListener {
                    override fun getGroupName(position: Int): String? {
                        //获取组名，用于判断是否是同一组
                        return if (dataList.size > position) {
                            dataList[position].title
                        } else null
                    }

                    override fun getGroupView(position: Int): View? {
                        //获取自定定义的组View
                        if (dataList.size > position) {
                            val view = layoutInflater.inflate(R.layout.base_header_recycler, null, false)
                            view.findViewById<TextView>(R.id.tv_name).setText(dataList[position].title)
//                            view.findViewById<ImageView>(R.id.iv_left).setImageResource(dataList[position].icon)
                            return view
                        } else {
                            return null
                        }
                    }
                })
                .setGroupHeight(100)
                .build()
        rlv.addItemDecoration(decoration)
    }

    internal fun initSmallVideo() {
        // 设置拍摄视频缓存路径
        val dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim.toString() + "/mabeijianxi/")
            } else {
                VCamera.setVideoCachePath(dcim.path.replace("/sdcard/",
                        "/sdcard-ext/") + "/mabeijianxi/")
            }
        } else {
            VCamera.setVideoCachePath(dcim.toString() + "/mabeijianxi/")
        }
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true)
        // 初始化拍摄SDK，必须
        VCamera.initialize(this)
    }
}
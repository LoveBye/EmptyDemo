package com.example.myapplication.UI

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseViewHolder
import com.example.myapplication.Adapter.BaseAdapter
import com.example.myapplication.Moudle.ImageModel
import com.example.myapplication.R
import java.io.File
import java.util.*

class WaterfallActivity : BaseTitleActivity() {
    private var mRlv: RecyclerView? = null
    private var dataList: ArrayList<TempBean>? = null
    private var thread: Thread? = null
    private var adater: MyAdapter? = null
    private var handler: Handler = object : Handler() {     //此处的object 要加，否则无法重写 handlerMessage
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 0) {
                Toast.makeText(applicationContext, "子线程消息", Toast.LENGTH_LONG).show()
                adater!!.setNewData(dataList)
            }
        }
    }

    override fun initViews() {
        mRlv = findViewById<View>(R.id.rlv) as RecyclerView
        adater = MyAdapter(R.layout.item_recycler_waterfall)
        mRlv!!.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRlv!!.adapter = adater
        dataList = ArrayList()
        dataList!!.add(TempBean("测试---->", R.drawable.temp_0))
        dataList!!.add(TempBean("测试---->\n测试---->测试---->测试---->\n测试---->测试---->测试---->", R.drawable.temp_1))
        dataList!!.add(TempBean("测试---->测试---->测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_2))
        dataList!!.add(TempBean("测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_3))
        dataList!!.add(TempBean("测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_4))
        dataList!!.add(TempBean("测试---->\n测试---->\n测试---->测试---->测试---->测试---->\n测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_5))
        dataList!!.add(TempBean("测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_1))
        dataList!!.add(TempBean("测试---->\n测试---->\n测试---->测试---->\n测试---->测试---->\n测试---->", R.drawable.temp_2))
        dataList!!.add(TempBean("测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_3))
        dataList!!.add(TempBean("测试---->测试---->测试---->", R.drawable.temp_0))
        dataList!!.add(TempBean("测试---->测试---->测试---->测试---->测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_4))
        dataList!!.add(TempBean("测试---->测试---->测试---->测试---->测试---->测试---->测试---->", R.drawable.temp_5))
//        adater!!.setNewData(dataList)
        checkPermissionAndLoadImages()
    }

    override fun initListeners() {

    }

    override fun setTitle(): String {
        return "瀑布流Activity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_waterfall
    }

    private inner class MyAdapter(layoutResId: Int) : BaseAdapter<TempBean, BaseViewHolder>(layoutResId) {

        override fun convert(helper: BaseViewHolder, item: TempBean) {
            helper.setText(R.id.tv_name, item.name)
            if (item.icon == 0) {
                Glide.with(this@WaterfallActivity)
                        .load(File(item.path))
                        .into(helper.getView(R.id.img_waterfall))
            } else {
                helper.setImageResource(R.id.img_waterfall, item.icon)
            }
        }
    }

    private inner class TempBean {
        var name: String? = null
        var icon: Int = 0
        var path: String? = ""

        constructor(name: String, icon: Int) {
            this.name = name
            this.icon = icon
        }

        constructor(name: String, path: String) {
            this.name = name
            this.path = path
        }
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private fun checkPermissionAndLoadImages() {
        var hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，加载图片。
            ImageModel.loadImageForSDCard(this, {
                //folders是图片文件夹的列表，每个文件夹中都有若干张图片。
                folders ->
                for (item in folders) {
                    for (item1 in item.images) {
                        dataList!!.add(TempBean(item1.name, item1.path))
                    }
                }
                handler.sendEmptyMessage(0)
            })
        } else {
            //没有权限，申请权限。
            var strings: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this,
                    strings, PERMISSION_REQUEST_CODE);
        }
    }
}
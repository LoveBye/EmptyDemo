package com.example.app.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.R
import com.example.app.adapter.BaseAdapter
import com.example.app.bean.ImageModel
import com.example.app.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_waterfall.*

class WaterfallActivity : BaseTitleActivity() {
    private var dataList: ArrayList<TempBean> = ArrayList()
    private var thread: Thread? = null
    private var adater: MyAdapter = MyAdapter(R.layout.item_recycler_waterfall)
    private var ITEM_COUNT = 20
    private var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {     //此处的object 要加，否则无法重写 handlerMessage
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 0) {
                if (dataList.size > ITEM_COUNT) {
                    adater.setNewData(dataList.subList(0, ITEM_COUNT))
                    adater.loadMoreComplete()
                } else {
                    adater.loadMoreEnd()
                }
            }
        }
    }

    override fun initViews() {
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rlv.layoutManager = manager
        rlv.adapter = adater
        rlv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                manager.invalidateSpanAssignments()
            }
        })
        checkPermissionAndLoadImages()
    }

    override fun initListeners() {
        adater.setOnLoadMoreListener({
            ITEM_COUNT = ITEM_COUNT + 20
            handler.sendEmptyMessage(0)
        }, rlv)
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
            GlideUtils.loadPic(this@WaterfallActivity,
                    item.path,
                    helper.getView(R.id.img_waterfall))
            helper.getView<View>(R.id.item_parent).setOnClickListener {
                val mListPics = arrayListOf<String>()
                for (pics in data) {
                    mListPics.add(pics.path.toString())
                }
                val intent = Intent(this@WaterfallActivity, ImagePagerActivity::class.java)
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, data.indexOf(item))
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, mListPics)
                startActivity(intent)
            }
        }
    }

    private inner class TempBean {
        var name: String? = ""
        var path: String? = ""

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
            ImageModel.loadImageForSDCard(this) {
                //folders是图片文件夹的列表，每个文件夹中都有若干张图片。
                folders ->
                for (item in folders) {
                    for (item1 in item.images) {
                        dataList.add(TempBean(item1.name, item1.path))
                    }
                }
                handler.sendEmptyMessage(0)
            }
        } else {
            //没有权限，申请权限。
            var strings: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this,
                    strings, PERMISSION_REQUEST_CODE);
        }
    }
}
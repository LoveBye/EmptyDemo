package com.example.app.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.R
import com.example.app.adapter.BaseAdapter
import com.example.app.bean.ImageModel
import kotlinx.android.synthetic.main.activity_waterfall.*
import java.util.*

class WaterfallActivity : BaseTitleActivity() {
    private val dataList: ArrayList<TempBean> = ArrayList<TempBean>()
    private val adater: MyAdapter = MyAdapter(R.layout.item_recycler_waterfall)
    private var ITEM_COUNT = 20
    @SuppressLint("HandlerLeak")
    private val handler: Handler =
            object : Handler() {     //此处的object 要加，否则无法重写 handlerMessage
                override fun handleMessage(msg: Message?) {
                    super.handleMessage(msg)
                    if (msg?.what == 0) {
                        if (dataList.size > ITEM_COUNT) {
                            if (ITEM_COUNT < 20) {
                                adater.setNewData(dataList.subList(0, ITEM_COUNT))
                            } else {
                                val subList = dataList.subList(ITEM_COUNT - 20, ITEM_COUNT)
                                val size = adater.data.size
                                adater.data.addAll(subList)
                                adater.notifyItemInserted(size)
                            }
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
        adater.helper.attachToRecyclerView(rlv)//设置可滚动
        rlv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                manager.invalidateSpanAssignments()
            }
        })
        checkPermissionAndLoadImages()
        initDatas()
    }

    private fun initDatas() {

    }

    override fun initListeners() {
        adater.setOnLoadMoreListener({
            ITEM_COUNT = ITEM_COUNT + 20
            handler.sendEmptyMessage(0)
        }, rlv)
    }

    override fun setTitle(): String {
        return "RetrofitActivity"
    }

    override fun setLayoutResource(): Int {
        return R.layout.activity_waterfall
    }

    private inner class MyAdapter(layoutResId: Int) : BaseAdapter<TempBean, BaseViewHolder>(layoutResId) {
        val map = mutableMapOf<Int, Int>()
        override fun convert(helper: BaseViewHolder, item: TempBean) {
            helper.setText(R.id.tv_name, item.name)
            val imageView = helper.getView<ImageView>(R.id.img_waterfall)
            if (item.picHeight!! > 50)
                imageView.layoutParams.height = item.picHeight!!
            //获取图片显示在ImageView后的宽高
            Glide.with(mContext)
//                    .asBitmap()//强制Glide返回一个Bitmap对象
                    .load(item.path)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if (imageView == null) {
                                return false
                            }
                            if (imageView.getScaleType() !== ImageView.ScaleType.FIT_XY) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY)
                            }
                            val params = imageView.getLayoutParams()
                            val vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight()
                            val scale = vw.toFloat() / resource!!.getIntrinsicWidth()
                            val vh = Math.round(resource.getIntrinsicHeight() * scale)
                            params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom()
                            imageView.setLayoutParams(params)
                            item.picHeight = params.height
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?,
                                                  model: Any?,
                                                  target: com.bumptech.glide.request.target.Target<Drawable>?,
                                                  isFirstResource: Boolean): Boolean {
                            return false
                        }
                    }).into(imageView)

            helper.getView<View>(R.id.item_parent).setOnClickListener {
                val mListPics = kotlin.collections.arrayListOf<kotlin.String>()
                for (pics in data) {
                    mListPics.add(pics.path.toString())
                }
                val intent = android.content.Intent(this@WaterfallActivity, com.example.app.view.ImagePagerActivity::class.java)
                intent.putExtra(com.example.app.view.ImagePagerActivity.EXTRA_IMAGE_INDEX, data.indexOf(item))
                intent.putExtra(com.example.app.view.ImagePagerActivity.EXTRA_IMAGE_URLS, mListPics)
                startActivity(intent)
            }
        }
    }

    private inner class TempBean(name: String, path: String) {
        var name: String? = name
        var path: String? = path
        var picHeight: Int? = 0
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private fun checkPermissionAndLoadImages() {
        val hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            //有权限，加载图片。
            ImageModel.loadImageForSDCard(this) {
                //folders是图片文件夹的列表，每个文件夹中都有若干张图片。
                folders ->
                for (item in folders) {
                    for (item1 in item.images!!) {
                        dataList.add(TempBean(item1.name!!, item1.path!!))
                    }
                }
                handler.sendEmptyMessage(0)
            }
        } else {
            //没有权限，申请权限。
            val strings: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this,
                    strings, PERMISSION_REQUEST_CODE);
        }
    }
}
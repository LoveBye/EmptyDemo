package com.example.app.presenter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.R
import com.example.app.utils.GlideUtils
import com.example.app.utils.LogUtils
import com.example.app.utils.SPUtils
import com.example.app.widget.album.ImageSelectorUtils
import java.io.*
import java.util.*

class ChoosePicPresenterImpl : PresenterImpl, View.OnClickListener {
    private val mListImgviewAddress: ArrayList<String>
    private var mActivity: Activity? = null
    private var mPopWnd: PopupWindow? = null
    private val GET_PHOTO_FROM_ALBUM = 2001
    private val RESULT_CAMERA_ONLY = 2002
    private val RESULT_CAMERA_CROP_PATH_RESULT = 2003
    private var imageUri: Uri? = null
    private var mMapViews: MutableMap<String, View> = mutableMapOf()
    private var mRecyclerPic: RecyclerView? = null
    private var mAdapterPic: PicAdapter? = null
    private var mLinearParent: View? = null
    private var isMulti = false

    constructor(activity: Activity) : super(activity) {
        mActivity = activity
        mMapViews = HashMap()
        mListImgviewAddress = ArrayList()
    }

    //只有单选
    constructor(mLinearParent: LinearLayout, activity: Activity, recyclerPic: RecyclerView) : super(activity) {
        isMulti = true
        mActivity = activity
        mRecyclerPic = recyclerPic
        this.mLinearParent = mLinearParent
        mMapViews = HashMap()
        mListImgviewAddress = ArrayList()
        mAdapterPic = PicAdapter(activity)

        mRecyclerPic!!.layoutManager = GridLayoutManager(mActivity, 3)
        mAdapterPic!!.data.add("")
        mRecyclerPic!!.adapter = mAdapterPic
        mMapViews[mRecyclerPic!!.toString()] = mRecyclerPic as View
        mListImgviewAddress.add(mRecyclerPic!!.toString())

        checkPermission(activity, Manifest.permission.CAMERA)
        checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
    }//包括多选

    fun chooseSinglePic(mLinearParent: View, mImgPerson0: ImageView) {
        isMulti = false
        this.mLinearParent = mLinearParent
        mMapViews[mImgPerson0.toString()] = mImgPerson0
        mListImgviewAddress.add(mImgPerson0.toString())
        val contentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_popupwindow, null)
        val linearViewGroup = contentView.findViewById<View>(R.id.linear_view_group)
        val tvCamera = contentView.findViewById<View>(R.id.tv_pop_camera)
        val tvAlbum = contentView.findViewById<View>(R.id.tv_pop_album)
        val tvCancle = contentView.findViewById<View>(R.id.tv_pop_cancle)
        linearViewGroup.setOnClickListener(this)
        tvCamera.setOnClickListener(this)
        tvAlbum.setOnClickListener(this)
        tvCancle.setOnClickListener(this)
        mPopWnd = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mPopWnd!!.showAtLocation(mLinearParent, Gravity.BOTTOM, 0, 0)
        mPopWnd!!.isOutsideTouchable = true
        mPopWnd!!.isFocusable = false
    }

    fun chooseMultiPic() {
        isMulti = true
        mMapViews[mRecyclerPic!!.toString()] = mRecyclerPic as View
        mListImgviewAddress.add(mRecyclerPic!!.toString())
        val contentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_popupwindow, null)
        val linearViewGroup = contentView.findViewById<View>(R.id.linear_view_group)
        val tvCamera = contentView.findViewById<View>(R.id.tv_pop_camera)
        val tvAlbum = contentView.findViewById<View>(R.id.tv_pop_album)
        val tvCancle = contentView.findViewById<View>(R.id.tv_pop_cancle)
        linearViewGroup.setOnClickListener(this)
        tvCamera.setOnClickListener(this)
        tvAlbum.setOnClickListener(this)
        tvCancle.setOnClickListener(this)
        mPopWnd = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mPopWnd!!.showAtLocation(mLinearParent, Gravity.BOTTOM, 0, 0)
        mPopWnd!!.isOutsideTouchable = true
        mPopWnd!!.isFocusable = false
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_pop_album//打开图库
            -> {
                mPopWnd!!.dismiss()
                if (isMulti) {
                    ImageSelectorUtils.openPhoto(mActivity, GET_PHOTO_FROM_ALBUM, false, 6 - mAdapterPic!!.data.size + 1)
                } else {
                    ImageSelectorUtils.openPhoto(mActivity, GET_PHOTO_FROM_ALBUM, true, 0)
                }
            }
            R.id.tv_pop_camera//打开相机
            -> {
                try {
                    mPopWnd!!.dismiss()
                    val path = getSDCardPath()
                    val tempTime = System.currentTimeMillis().toString() + ""
                    val file = File("$path/$tempTime.jpg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //兼容android7.0 使用共享文件的形式
                        val contentValues = ContentValues(1)
                        contentValues.put(MediaStore.Images.Media.DATA, file.absolutePath)
                        imageUri = mActivity!!.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    } else {
                        imageUri = Uri.fromFile(file)
                    }
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//action is capture
                    intent.putExtra("return-data", false)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                    intent.putExtra("noFaceDetection", true)
                    mActivity!!.startActivityForResult(intent, RESULT_CAMERA_ONLY)
                } catch (e: Exception) {
                    LogUtils.showLog(mActivity!!.applicationContext, e.toString())
                }
            }
            R.id.tv_pop_cancle -> mPopWnd!!.dismiss()
        }
    }

    fun onActivityResult(requestCode: Int, data: Intent?) {
        try {
            when (requestCode) {
                RESULT_CAMERA_ONLY -> try {
                    val bitmap = getBitmapYS(getRealFilePath(mActivity, imageUri))
                    saveBitmapFile(mListImgviewAddress.size - 1, bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                RESULT_CAMERA_CROP_PATH_RESULT -> try {
                    val bitmap = getBitmapYS(getRealFilePath(mActivity, imageUri))
                    saveBitmapFile(mListImgviewAddress.size - 1, bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (requestCode == GET_PHOTO_FROM_ALBUM && data != null) {
                val images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT)
                if (isMulti) {
                    images?.let {
                        for (i in images.indices) {
                            val bitmap = getBitmapYS(images[i])
                            saveBitmapFile(mListImgviewAddress.size - 1, bitmap)
                        }
                    }
                } else {
                    images?.let {
                        val bitmap = getBitmapYS(images[0])
                        saveBitmapFile(mListImgviewAddress.size - 1, bitmap)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 压缩
     *
     * @return
     */
    private fun getBitmapYS(filePath: String?): Bitmap? {
        try {
            // 从选取相册的Activity中返回后
            // 设置参数
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
            BitmapFactory.decodeFile(filePath, options)
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 2 // 默认像素压缩比例，压缩为原图的1/2
            val minLen = Math.min(height, width) // 原图的最小边长
            if (minLen > 600) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                val ratio = minLen.toFloat() / 600.0f // 计算像素压缩比例
                inSampleSize = ratio.toInt()
            }
            options.inJustDecodeBounds = false // 计算好压缩比例后，这次可以去加载原图了
            options.inSampleSize = inSampleSize // 设置为刚才计算的压缩比例
            val bm = BitmapFactory.decodeFile(filePath, options) // 解码文件
            Log.w("TAG", "size: " + bm.byteCount + " width: " + bm.width + " heigth:" + bm.height + " " + minLen) // 输出
            return bm
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    @SuppressLint("SdCardPath")
//将Bitmap转成文件并且上传至服务器，成功后拼成要上传的picArr
    fun saveBitmapFile(position: Int, bitmap: Bitmap?) {
        if (bitmap == null)
            return
        showProgressDialog("图片处理中，请稍候……")
        val tempTime = System.currentTimeMillis().toString() + ""
        val file = File("/mnt/sdcard/$tempTime.jpg")//将要保存图片的路径
        try {
            val bos: BufferedOutputStream
            bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
            val params = HashMap<String, String>()
            params["OP"] = "UpLoadpic"
            params["Member_ID"] = SPUtils.getPrefString(mActivity!!.applicationContext, "USER_ID", "")
            dismissProgressDialog()
            val view = mMapViews.get(mListImgviewAddress.get(position));
            if (view is ImageView) {
                GlideUtils.loadPic(mActivity,
                        bitmap,
                        view)
            } else {
                (view as RecyclerView).setAdapter(mAdapterPic);
                mAdapterPic!!.getData().removeAt(mAdapterPic!!.getData().size - 1)
                mAdapterPic!!.addData("")//临时加个临时图片
                if (mAdapterPic!!.getData().size < 6)
                    mAdapterPic!!.addData("")
                setRecyclerTag(mAdapterPic!!.getData());
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setRecyclerTag(data: List<String>) {
        var temp = ""
        for (i in data.indices) {
            temp = temp + data[i] + "|"
        }
        if (temp.length > 0)
            temp = temp.substring(0, temp.length - 1)
        mRecyclerPic!!.tag = temp
    }

    inner class PicAdapter(context: Context) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_shop_pic), OnItemCallbackListener {
        init {
            this.mContext = context
        }

        override fun convert(helper: BaseViewHolder, item: String?) {
            if (item != null && item == "") {
                helper.setVisible(R.id.del, false)
                helper.getView<View>(R.id.iv).isClickable = true
                GlideUtils.loadPic(mContext, R.drawable.tail_add_pic, helper.getView<View>(R.id.iv) as ImageView)
                helper.getView<View>(R.id.iv).setOnClickListener { chooseMultiPic() }
            } else {
                GlideUtils.loadPic(mContext, item!!, helper.getView<View>(R.id.iv) as ImageView)
                helper.setVisible(R.id.del, true)
                helper.getView<View>(R.id.iv).isClickable = false
            }
            helper.getView<View>(R.id.del).setOnClickListener {
                if (data.size == 6 && data[5] != "")
                    data.add("")
                data.removeAt(data.indexOf(item))
                notifyDataSetChanged()
                setRecyclerTag(data)//删除某一项以后
            }
        }

        //拖动
        override fun onMove(fromPosition: Int, toPosition: Int) {
            /**
             * 在这里进行给原数组数据的移动
             */
            Collections.swap(mData, fromPosition, toPosition)
            /**
             * 通知数据移动
             */
            notifyItemMoved(fromPosition, toPosition)
            setRecyclerTag(data)//拖动排序后
        }

        override fun onSwipe(position: Int) {

        }
    }

    interface OnItemCallbackListener {
        /**
         * @param fromPosition 起始位置
         * @param toPosition   移动的位置
         */
        fun onMove(fromPosition: Int, toPosition: Int)

        fun onSwipe(position: Int)
    }

    companion object {

        fun getRealFilePath(context: Context?, uri: Uri?): String? {
            if (null == uri) return null
            val scheme = uri.scheme
            var data: String? = null
            if (scheme == null)
                data = uri.path
            else if (ContentResolver.SCHEME_FILE == scheme) {
                data = uri.path
            } else if (ContentResolver.SCHEME_CONTENT == scheme) {
                val cursor = context!!.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                        if (index > -1) {
                            data = cursor.getString(index)
                        }
                    }
                    cursor.close()
                }
            }
            return data
        }
    }

    private fun getSDCardPath(): String {
        val cmd = "cat /proc/mounts"
        val run = Runtime.getRuntime()// 返回与当前 Java 应用程序相关的运行时对象
        try {
            val p = run.exec(cmd)// 启动另一个进程来执行命令
            val `in` = BufferedInputStream(p.inputStream)
            val inBr = BufferedReader(InputStreamReader(`in`))
            val lineStr: String = inBr.readLine()
            if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                val strArray = lineStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (strArray.size >= 5) {
                    return strArray[1].replace("/.android_secure", "")
                }
            }
            inBr.close()
            `in`.close()
        } catch (e: Exception) {
            return Environment.getExternalStorageDirectory().path
        }

        return Environment.getExternalStorageDirectory().path
    }
}
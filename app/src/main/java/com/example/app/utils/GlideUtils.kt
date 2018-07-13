package com.example.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.app.R
import java.io.File


/**
 * Created by TAIYA on 2018/4/9.
 */

object GlideUtils {
    private val bitmap: Bitmap? = null

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     */
    fun loadPic(context: Context?, imgUrl: String, imageView: ImageView) {
        val requestOptions = RequestOptions()
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context!!.applicationContext)
                .load(imgUrl)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(imageView)
    }

    /**
     * @param context
     * @param resourceId
     * @param imageView
     */
    fun loadPic(context: Context, resourceId: Int, imageView: ImageView) {
        val requestOptions = RequestOptions()
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context.applicationContext)
                .load(resourceId)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(imageView)
    }

    /**
     * @param context
     * @param headimgurl
     * @param imageView
     */
    fun loadHead(context: Context?, headimgurl: String, imageView: ImageView) {
        val requestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head_icon)
                .error(R.drawable.default_head_icon)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context!!.applicationContext)
                .load(headimgurl)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(imageView)
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param isFitCenter
     */
    fun loadPic(context: Context?, imgUrl: String, imageView: ImageView, isFitCenter: Boolean) {
        val requestOptions: RequestOptions
        if (isFitCenter) {
            requestOptions = RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            requestOptions = RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
        Glide.with(context!!.applicationContext)
                .load(imgUrl)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(imageView)
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param isFitCenter
     * @param resourceId
     */
    fun loadPic(context: Context?, imgUrl: String, imageView: ImageView, isFitCenter: Boolean, resourceId: Int) {
        val requestOptions: RequestOptions
        if (isFitCenter) {
            requestOptions = RequestOptions()
                    .fitCenter()
                    .placeholder(resourceId)
                    .error(resourceId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            requestOptions = RequestOptions()
                    .centerCrop()
                    .placeholder(resourceId)
                    .error(resourceId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
        Glide.with(context!!.applicationContext)
                .load(imgUrl)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(imageView)
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param glideRoundTransform
     */
    fun transform(context: Context?, imgUrl: String, imageView: ImageView, glideRoundTransform: GlideRoundTransform?) {
        val requestOptions: RequestOptions
        if (glideRoundTransform == null) {
            requestOptions = RequestOptions()
                    .centerCrop()
                    .transform(GlideRoundTransform(context, 2))
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            requestOptions = RequestOptions()
                    .centerCrop()
                    .transform(glideRoundTransform)
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
        Glide.with(context!!.applicationContext)
                .load(imgUrl)
                .apply(requestOptions)
                .into(imageView)
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param glideRoundTransform
     */
    fun transform(context: Context?, imgUrl: String, imageView: ImageView, isFitCenter: Boolean, glideRoundTransform: GlideRoundTransform) {
        val requestOptions: RequestOptions
        if (isFitCenter) {
            requestOptions = RequestOptions()
                    .fitCenter()
                    .transform(glideRoundTransform)
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        } else {
            requestOptions = RequestOptions()
                    .centerCrop()
                    .transform(glideRoundTransform)
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
        Glide.with(context!!.applicationContext)
                .load(imgUrl)
                .apply(requestOptions)
                .into(imageView)
    }

    /**
     * @param context
     * @param file
     * @param ivImage
     */
    fun loadFile(context: Context?, file: File, ivImage: ImageView) {
        val requestOptions = RequestOptions()
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        ivImage.tag = null//需要清空tag，否则报错
        Glide.with(context!!.applicationContext)
                .load(file)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(ivImage)
    }

    fun loadPic(context: Context?, bitmap: Bitmap, ivImage: ImageView) {
        val requestOptions = RequestOptions()
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        ivImage.tag = null//需要清空tag，否则报错
        Glide.with(context!!.applicationContext)
                .load(bitmap)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(ivImage)
    }

    /**
     * @param context
     * @param headimgurl
     * @param imageView
     */
    fun loadHead_Clear(context: Context?, headimgurl: String, imageView: ImageView) {
        val requestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head_icon)
                .error(R.drawable.default_head_icon)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        imageView.tag = null//需要清空tag，否则报错
        Glide.with(context!!.applicationContext)
                .load(headimgurl)
                .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                .apply(requestOptions)
                .into(imageView)
    }
}
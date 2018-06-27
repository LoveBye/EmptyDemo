package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

import java.io.File;

/**
 * Created by TAIYA on 2018/4/9.
 */

public class GlideUtils {
    private static Bitmap bitmap;

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     */
    public static void loadPic(Context context, String imgUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (context != null)
            if (context != null) {
                Glide.with(context.getApplicationContext())
                        .load(imgUrl)
                        .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                        .apply(requestOptions)
                        .into(imageView);
            }
    }

    /**
     * @param context
     * @param resourceId
     * @param imageView
     */
    public static void loadPic(Context context, int resourceId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (context != null) {
            Glide.with(context.getApplicationContext())
                    .load(resourceId)
                    .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    /**
     * @param context
     * @param headimgurl
     * @param imageView
     */
    public static void loadHead(Context context, String headimgurl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head_icon)
                .error(R.drawable.default_head_icon)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (context != null) {
            Glide.with(context.getApplicationContext())
                    .load(headimgurl)
                    .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param isFitCenter
     */
    public static void loadPic(Context context, String imgUrl, ImageView imageView, boolean isFitCenter) {
        RequestOptions requestOptions;
        if (isFitCenter) {
            requestOptions = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        }
        if (context != null) {
            Glide.with(context.getApplicationContext())
                    .load(imgUrl)
                    .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param isFitCenter
     * @param resourceId
     */
    public static void loadPic(Context context, String imgUrl, ImageView imageView, boolean isFitCenter, int resourceId) {
        RequestOptions requestOptions;
        if (isFitCenter) {
            requestOptions = new RequestOptions()
                    .fitCenter()
                    .placeholder(resourceId)
                    .error(resourceId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(resourceId)
                    .error(resourceId)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        }
        if (context != null) {
            Glide.with(context.getApplicationContext())
                    .load(imgUrl)
                    .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param glideRoundTransform
     */
    public static void transform(Context context, String imgUrl, ImageView imageView, GlideRoundTransform glideRoundTransform) {
        RequestOptions requestOptions;
        if (glideRoundTransform == null) {
            requestOptions = new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, 2))
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions = new RequestOptions()
                    .centerCrop()
                    .transform(glideRoundTransform)
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        }
        if (context != null) {
            Glide.with(context.getApplicationContext())
                    .load(imgUrl)
//                        .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    /**
     * @param context
     * @param imgUrl
     * @param imageView
     * @param glideRoundTransform
     */
    public static void transform(Context context, String imgUrl, ImageView imageView, boolean isFitCenter, GlideRoundTransform glideRoundTransform) {
        RequestOptions requestOptions;
        if (isFitCenter) {
            requestOptions = new RequestOptions()
                    .fitCenter()
                    .transform(glideRoundTransform)
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        } else {
            requestOptions = new RequestOptions()
                    .centerCrop()
                    .transform(glideRoundTransform)
                    .placeholder(R.drawable.default_loading_pic)
                    .error(R.drawable.default_loading_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        }
        if (context != null) {
            Glide.with(context.getApplicationContext())
                    .load(imgUrl)
//                        .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    /**
     * @param context
     * @param file
     * @param ivImage
     */
    public static void loadFile(Context context, File file, ImageView ivImage) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (context != null) {
            ivImage.setTag(null);//需要清空tag，否则报错
            Glide.with(context.getApplicationContext())
                    .load(file)
                    .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(ivImage);
        }
    }

    public static void loadPic(Context context, Bitmap bitmap, ImageView ivImage) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_loading_pic)
                .error(R.drawable.default_loading_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (context != null) {
            ivImage.setTag(null);//需要清空tag，否则报错
            Glide.with(context.getApplicationContext())
                    .load(bitmap)
                    .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(ivImage);
        }
    }

    /**
     * @param context
     * @param headimgurl
     * @param imageView
     */
    public static void loadHead_Clear(Context context, String headimgurl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_head_icon)
                .error(R.drawable.default_head_icon)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (context != null) {
            imageView.setTag(null);//需要清空tag，否则报错
            Glide.with(context.getApplicationContext())
                    .load(headimgurl)
                    .thumbnail(0.1f)//先加载原图的1/10作为缩略图
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}
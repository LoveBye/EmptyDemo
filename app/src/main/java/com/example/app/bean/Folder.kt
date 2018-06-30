package com.example.app.bean

import com.example.app.utils.StringUtils
import java.util.*

/**
 * 图片文件夹实体类
 */
class Folder {

    var name: String? = null
    var images: ArrayList<Image>? = null

    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, images: ArrayList<Image>) {
        this.name = name
        this.images = images
    }

    fun addImage(image: Image?) {
        if (image != null && StringUtils.isNotEmptyString(image.path)) {
            if (images == null) {
                images = ArrayList()
            }
            images!!.add(image)
        }
    }
}
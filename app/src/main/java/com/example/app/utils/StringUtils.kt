package com.example.app.utils

import android.text.TextPaint
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

object StringUtils {
    fun isNotEmptyString(str: String?): Boolean {
        return str != null && str != ""
    }

    val EMPTY = ""

    private val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
    private val DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss"
    /** 用于生成文件  */
    private val DEFAULT_FILE_PATTERN = "yyyy-MM-dd-HH-mm-ss"
    private val KB = 1024.0
    private val MB = 1048576.0
    private val GB = 1073741824.0
    val DATE_FORMAT_PART = SimpleDateFormat(
            "HH:mm")

    fun currentTimeString(): String {
        return DATE_FORMAT_PART.format(Calendar.getInstance().time)
    }

    fun chatAt(pinyin: String?, index: Int): Char {
        return if (pinyin != null && pinyin.length > 0) pinyin[index] else ' '
    }

    /** 获取字符串宽度  */
    fun GetTextWidth(Sentence: String, Size: Float): Float {
        if (isEmpty(Sentence))
            return 0f
        val FontPaint = TextPaint()
        FontPaint.textSize = Size
        return FontPaint.measureText(Sentence.trim { it <= ' ' }) + (Size * 0.1).toInt() // 留点余地
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    fun formatDate(date: Date, pattern: String): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    fun formatDate(date: Long, pattern: String): String {
        val format = SimpleDateFormat(pattern)
        return format.format(Date(date))
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @return 例如2011-3-24
     */
    fun formatDate(date: Date): String {
        return formatDate(date, DEFAULT_DATE_PATTERN)
    }

    fun formatDate(date: Long): String {
        return formatDate(Date(date), DEFAULT_DATE_PATTERN)
    }

    /**
     * 获取当前时间 格式为yyyy-MM-dd 例如2011-07-08
     *
     * @return
     */
    fun getDate(): String {
        return formatDate(Date(), DEFAULT_DATE_PATTERN)
    }

    /** 生成一个文件名，不含后缀  */
    fun createFileName(): String {
        val date = Date(System.currentTimeMillis())
        val format = SimpleDateFormat(DEFAULT_FILE_PATTERN)
        return format.format(date)
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    fun getDateTime(): String {
        return formatDate(Date(), DEFAULT_DATETIME_PATTERN)
    }

    /**
     * 格式化日期时间字符串
     *
     * @param date
     * @return 例如2011-11-30 16:06:54
     */
    fun formatDateTime(date: Date): String {
        return formatDate(date, DEFAULT_DATETIME_PATTERN)
    }

    fun formatDateTime(date: Long): String {
        return formatDate(Date(date), DEFAULT_DATETIME_PATTERN)
    }

    /**
     * 格林威时间转换
     *
     * @param gmt
     * @return
     */
    fun formatGMTDate(gmt: String): String {
        val timeZoneLondon = TimeZone.getTimeZone(gmt)
        return formatDate(Calendar.getInstance(timeZoneLondon)
                .timeInMillis)
    }

    /**
     * 拼接数组
     *
     * @param array
     * @param separator
     * @return
     */
    fun join(array: ArrayList<String>?,
             separator: String): String {
        val result = StringBuffer()
        if (array != null && array.size > 0) {
            for (str in array) {
                result.append(str)
                result.append(separator)
            }
            result.delete(result.length - 1, result.length)
        }
        return result.toString()
    }

    fun join(iter: Iterator<String>?,
             separator: String): String {
        val result = StringBuffer()
        if (iter != null) {
            while (iter.hasNext()) {
                val key = iter.next()
                result.append(key)
                result.append(separator)
            }
            if (result.length > 0)
                result.delete(result.length - 1, result.length)
        }
        return result.toString()
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || str.length == 0 || str.equals("null", ignoreCase = true)
    }

    fun isNotEmpty(str: String): Boolean {
        return !isEmpty(str)
    }

    /**
     *
     * @param str
     * @return
     */
    fun trim(str: String?): String {
        return str?.trim { it <= ' ' } ?: EMPTY
    }

    /**
     * 转换时间显示
     *
     * @param time
     * 毫秒
     * @return
     */
    fun generateTime(time: Long): String {
        val totalSeconds = (time / 1000).toInt()
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        return if (hours > 0)
            String.format("%02d:%02d:%02d", hours, minutes,
                    seconds)
        else
            String.format("%02d:%02d", minutes, seconds)
    }

    fun isBlank(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    /** 根据秒速获取时间格式  */
    fun gennerTime(totalSeconds: Int): String {
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * 转换文件大小
     *
     * @param size
     * @return
     */
    fun generateFileSize(size: Long): String {
        val fileSize: String
        if (size < KB)
            fileSize = size.toString() + "B"
        else if (size < MB)
            fileSize = String.format("%.1f", size / KB) + "KB"
        else if (size < GB)
            fileSize = String.format("%.1f", size / MB) + "MB"
        else
            fileSize = String.format("%.1f", size / GB) + "GB"

        return fileSize
    }

    /** 查找字符串，找到返回，没找到返回空  */
    fun findString(search: String, start: String, end: String): String {
        val start_len = start.length
        val start_pos = if (StringUtils.isEmpty(start)) 0 else search.indexOf(start)
        if (start_pos > -1) {
            val end_pos = if (StringUtils.isEmpty(end))
                -1
            else
                search.indexOf(end,
                        start_pos + start_len)
            if (end_pos > -1)
                return search.substring(start_pos + start.length, end_pos)
        }
        return ""
    }

    /**
     * 截取字符串
     *
     * @param search
     * 待搜索的字符串
     * @param start
     * 起始字符串 例如：<title>
     * @param end
     * 结束字符串 例如：</title>
     * @param defaultValue
     * @return
     */
    fun substring(search: String, start: String, end: String,
                  defaultValue: String): String {
        val start_len = start.length
        val start_pos = if (StringUtils.isEmpty(start)) 0 else search.indexOf(start)
        if (start_pos > -1) {
            val end_pos = if (StringUtils.isEmpty(end))
                -1
            else
                search.indexOf(end,
                        start_pos + start_len)
            return if (end_pos > -1)
                search.substring(start_pos + start.length, end_pos)
            else
                search.substring(start_pos + start.length)
        }
        return defaultValue
    }

    /**
     * 截取字符串
     *
     * @param search
     * 待搜索的字符串
     * @param start
     * 起始字符串 例如：<title>
     * @param end
     * 结束字符串 例如：</title>
     * @return
     */
    fun substring(search: String, start: String, end: String): String {
        return substring(search, start, end, "")
    }

    /**
     * 拼接字符串
     *
     * @param strs
     * @return
     */
    fun concat(vararg strs: String): String {
        val result = StringBuffer()
        if (strs != null) {
            for (str in strs) {
                if (str != null)
                    result.append(str)
            }
        }
        return result.toString()
    }

    /**
     * Helper function for making null strings safe for comparisons, etc.
     *
     * @return (s == null) ? "" : s;
     */
    fun makeSafe(s: String?): String {
        return s ?: ""
    }
}
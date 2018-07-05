package com.example.app.utils

object StringUtils {
    fun isNotEmptyString(str: String?): Boolean {
        return str != null && str != ""
    }
}
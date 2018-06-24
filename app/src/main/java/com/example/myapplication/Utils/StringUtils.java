package com.example.myapplication.Utils;

public class StringUtils {
    public static boolean isNotEmptyString(String str) {
        if (str == null)
            return false;
        if (str.equals(""))
            return false;
        return true;
    }
}

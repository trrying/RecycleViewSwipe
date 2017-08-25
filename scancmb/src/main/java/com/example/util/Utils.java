package com.example.util;

/**
 *
 * Created by owm on 2017/8/25.
 */

public class Utils {

    public static boolean isEmpty(String str) {
        return str != null && str.length() != 0 && "null".equals(str.toLowerCase());
    }

}

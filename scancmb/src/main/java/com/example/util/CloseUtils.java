package com.example.util;

import java.io.Closeable;

/**
 *
 * Created by owm on 2017/8/25.
 */

public class CloseUtils {

    public static void close(Closeable... closeables) {
        for (Closeable c : closeables) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

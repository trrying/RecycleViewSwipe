package com.owm.clear.util;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by owm on 2017/8/1.
 */

public class FileUtils {

    private static int sBufferSize = 8192;

    /**
     * 获取内置SD卡路径
     * @return
     */
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取外置SD卡路径
     * http://www.jianshu.com/p/1a50c26c6ca9
     */
    public static String[] getExtSDCardPath(Context context) {
        if (context == null) {
            return null;
        }
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            return (String[])invoke;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }


    /**
     * 获取外置SD卡路径
     * http://blog.csdn.net/chadeltu/article/details/43736093
     * @return  应该就一条记录或空
     */
    public static List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard"))
                {
                    String [] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory())
                    {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lResult;
    }

    public static final double[] levelSize = new double[]{
             Math.pow(1024, 1),
             Math.pow(1024, 2),
             Math.pow(1024, 3),
             Math.pow(1024, 4),};
    public static final String[] postfix = new String[]{"bytes", "KB", "MB", "GB"};

    public static String getSize(File file) {
        if (file != null && file.isFile()) {
            return getSize(file.length());
        }
        return "";
    }

    public static String getSize(long size) {
        DecimalFormat format = new DecimalFormat("####.00");
        for (int i = 0; i < levelSize.length; i++) {
            if (size < levelSize[i]) {
                return format.format(size/(levelSize[i]/1024)) + postfix[i];
            }
        }
        return "";
    }

    public static boolean writeFileFromIs(String filePath, InputStream is) {
        return writeFileFromIs(filePath, is, false);
    }

    public static boolean writeFileFromIs(String filePath, InputStream is, boolean append) {
        return writeFileFromIs(getFileByPath(filePath), is, append);
    }

    public static boolean writeFileFromIs(File file, InputStream is, boolean append) {
        if (!createOrExistsDir(file) || is == null) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
            byte[] cache = new byte[sBufferSize];
            int len;
            while ((len = is.read(cache, 0, sBufferSize)) != -1) {
                fileOutputStream.write(cache, 0, len);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close(fileOutputStream);
        }
        return true;
    }


    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    private static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean equals(File file1, File file2) {
        boolean result = false;
        if (file1 != null && file2 != null) {
            result = TextUtils.equals(file1.getPath(), file2.getPath());
        }
        return result;
    }

}

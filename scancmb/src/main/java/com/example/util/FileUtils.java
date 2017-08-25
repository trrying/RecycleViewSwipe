package com.example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * Created by owm on 2017/8/25.
 */

public class FileUtils {

    public static boolean saveFile(String filePath, String content) {
        FileWriter fileWriter = null;
        try {
            mkdirs(filePath);

            fileWriter = new FileWriter(filePath);

            fileWriter.write(content);

            fileWriter.flush();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(fileWriter);
        }
        return false;
    }

    public static String readFile(String filePath) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                stringBuilder.append(buffer);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(bufferedReader);
        }
        return null;
    }

    public static boolean mkdirs(String filePath) {
        if (!Utils.isEmpty(filePath)) {
            int lastIndexOf = filePath.lastIndexOf("\\");
            if (lastIndexOf != -1) {
                String dir = filePath.substring(0, lastIndexOf);
                File file = new File(dir);
                if (!file.exists()) {
                    return file.mkdirs();
                }
            }
            return true;
        }
        return false;
    }

}

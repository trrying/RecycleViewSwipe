package com.example.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * Created by owm on 2017/7/1.
 */

public class HttpUtils {

    public static String get(String urlStr) {
        StringBuilder response = new StringBuilder();
        BufferedReader br = null;
        try {
            URL url = new URL(urlStr);    // 把字符串转换为URL请求地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.connect();// 连接会话
            // 获取输入流
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {// 循环读取流
                response.append(line);
            }
            connection.disconnect();// 断开连接
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败! "+urlStr);
        } finally {
            CloseUtils.close(br);
        }
        return response.toString();
    }

    public static String post(String urlStr, String bodyParam) {
        StringBuilder response = new StringBuilder();

        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlStr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //获得HttpURLConnection对象
            connection.setRequestMethod("POST");
            // 设置请求方法为post
            connection.setUseCaches(false);
            //不使用缓存
            connection.setConnectTimeout(10000);
            //设置超时时间
            connection.setReadTimeout(10000);
            //设置读取超时时间
            connection.setDoInput(true);
            //设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setDoOutput(true);
            //设置为true后才能写入参数
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            outputStream = connection.getOutputStream();
            outputStream.write(bodyParam.getBytes());
            outputStream.flush();
            //写入参数
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            CloseUtils.close(outputStream, inputStream);
        }
        return response.toString();
    }

}

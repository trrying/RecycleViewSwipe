package com.example.util.feifan;

import com.example.bean.feifan.City;
import com.example.bean.feifan.Plaza;
import com.example.bean.feifan.PlazaResult;
import com.example.db.DbUtils;
import com.example.util.FileUtils;
import com.example.util.HttpUtils;
import com.example.util.LogUtils;
import com.example.util.ProduceExcel;
import com.example.util.Utils;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

/**
 *
 * Created by owm on 2017/9/5.
 */

public class PlazaUtils {
    private static final String plazaListUrl = "https://api.ffan.com/ffan/v1/city/locationSeek?cityId=%s&size=%s&page=%s";

    private static final String basePath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\response\\feifan\\plazaList\\";

    public static void main(String[] args) {
//        getPlaza();

        List<Plaza> plazaList = DbUtils.findAll(Plaza.class);

        ProduceExcel.list2Excel(plazaList, "");

    }

    public static void getPlaza() {

        List<City> cityList = CityUtils.getCityList();
        Gson gson = new Gson();

        for (int i = 0; i < cityList.size(); i++) {
            StringBuilder log = new StringBuilder();
            try {
                City city = cityList.get(i);
                log.append("------------------------ ").append(i).append(" cityName : ").append(city.cityName).append(" ------------------------\n");
                String cityId = city.cityId;
//                cityId = "440100";
                int size = 50;
                int page = 1;
                int total = 0;
                do {
                    String url = String.format(Locale.getDefault(), plazaListUrl, cityId, size + "", page + "");
                    log.append(url).append("\n");
                    String response = HttpUtils.get(url);

                    if (Utils.isEmpty(response)) {
                        log.append("response is empty\n");
                        continue;
                    }

                    String filePath = basePath + cityId + "_50_" + page + ".txt";
                    boolean saveFile = FileUtils.saveFile(filePath, response);
                    if (!saveFile) {
                        log.append("save file error filePath : ").append(filePath);
                    }

                    PlazaResult result = gson.fromJson(response, PlazaResult.class);
                    if (result != null && result.data != null && result.data.list != null) {
                        total = result.data.total;
                        int count = DbUtils.save(result.data.list);
                        log.append("save db success list.size : ").append(result.data.list.size()).append("  db size = ").append(count);
                    } else {
                        log.append("PlazaResult is null");
                    }
                } while(total > page++ * size);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtils.println(log.toString());
        }

    }

}

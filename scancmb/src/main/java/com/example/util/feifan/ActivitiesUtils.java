package com.example.util.feifan;

import com.example.bean.feifan.Activities;
import com.example.bean.feifan.ActivitiesResult;
import com.example.bean.feifan.Plaza;
import com.example.db.DbUtils;
import com.example.util.FileUtils;
import com.example.util.HttpUtils;
import com.example.util.LogUtils;
import com.example.util.ProduceExcel;
import com.example.util.Utils;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * Created by owm on 2017/9/5.
 */

public class ActivitiesUtils {

    private static final String listUrl = "https://api.ffan.com/ffan/v1/city/activities?size=%s&offset=%s&plazaId=%s&cityId=%s";

    private static final String basePath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\response\\feifan\\CouponList\\";

    public static void main(String[] args) {
//        getActivities();
        productExcel();
    }

    public static void getActivities() {
        List<Plaza> plazaList = DbUtils.findAll(Plaza.class);

        double threadNum = 10;

        int threadProductCount = (int) Math.ceil(plazaList.size() / threadNum);

        for (int i = 0; i < threadNum; i++) {
            int begin = i * threadProductCount;
            int end = (i+1) * threadProductCount;
            end = end >= plazaList.size() ? plazaList.size() : end;
            if (begin >= end) {
                break;
            }
            List<Plaza> data = plazaList.subList(begin, end);
            new Thread(new ScanThread(data, i)).start();
        }
        LogUtils.println("size = "+plazaList.size());

    }
    public static class ScanThread implements Runnable {

        List<Plaza> plazaList;
        int position;
        float size;
        public ScanThread(List<Plaza> plazaList, int position) {
            this.plazaList = plazaList;
            this.position = position;
            size = plazaList.size();
        }

        @Override
        public void run() {
            Gson gson = new Gson();

            for (int i = 0; i < plazaList.size(); i++) {
                StringBuilder log = new StringBuilder();
                try {
                    Plaza plaza = plazaList.get(i);
                    log.append("------------------------ ").append("position : ").append(position).append(" ").append(i / size * 100).append("% ").append(" plazaName : ").append(plaza.plazaName).append(" ------------------------\n");
                    int size = 50;
                    int offset = 0;
                    int total = 0;
                    do {
                        String url = String.format(Locale.getDefault(), listUrl, size, offset, plaza.plazaId, plaza.cityId);
                        log.append(url).append("\n");
                        String response = HttpUtils.get(url);

                        if (Utils.isEmpty(response)) {
                            log.append("response is empty\n");
                            continue;
                        }

                        String filePath = basePath + plaza.plazaId + "_" + plaza.cityId + "_50_" + offset + ".txt";
                        boolean saveFile = FileUtils.saveFile(filePath, response);
                        if (!saveFile) {
                            log.append("save file error filePath : ").append(filePath);
                        }

                        ActivitiesResult result = gson.fromJson(response, ActivitiesResult.class);
                        if (result != null && result.data != null && result.data.list != null) {

                            for (int j = 0; j < result.data.list.size(); j++) {
                                Activities coupons = result.data.list.get(j);
                                coupons.plazaId = plaza.plazaId;
                                coupons.plazaName = plaza.plazaName;
                            }

                            total = result.data.list.size();
                            int count = DbUtils.save(result.data.list);
                            log.append("save db success list.size : ").append(result.data.list.size()).append("  db size = ").append(count).append("\n");
                        } else {
                            log.append("PlazaResult is null\n");
                        }

                        offset += size;
                    } while (total >= size);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.println(log.toString());
            }
        }

    }
    public static void productExcel() {

        String logo = "https://timg.ffan.com/convert/resize/url_%s/width_284/height_160/tfs/xxx.webp";

        String detailUrl = "https://h5.ffan.com/app/activity?plazaId=%s&cityId=%s&aid=%s";

        Map<String, String> cityMap = CityUtils.getCityMap();

        List<Activities> activitiesList = DbUtils.findAll(Activities.class);

        for (int i = 0; i < activitiesList.size(); i++) {
            Activities activities = activitiesList.get(i);
            if (activities != null && activities.cityId != null) {
                activities.cityName = cityMap.get(activities.cityId);
                activities.logo = String.format(Locale.getDefault(), logo, activities.pic);
                activities.detail_url = String.format(Locale.getDefault(), detailUrl, activities.plazaId, activities.cityId, activities.id);
            }
        }

        ProduceExcel.list2Excel(activitiesList);
    }

}

package com.example.util.feifan;

import com.example.bean.feifan.Coupons;
import com.example.bean.feifan.CouponsResult;
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

public class CouponsUtils {
    private static final String listUrl = "https://api.ffan.com/ffan/v1/city/coupons?size=%s&offset=%s&plazaId=%s&cityId=%s";

    private static final String basePath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\response\\feifan\\CouponList\\";
    
    public static void main(String[] args) {
//        getCoupons();

//        String sql = "insert into Coupons  (cityId,marketPrice,pic,endDate,saleNum,startDate,id,title,price,sortValue,subtitle,oriPrice,plazaId,plazaName,poiLong,plazaLongitude,poiLat,plazaLatitude,soldNum) values  ('310100','1','T12mATBQK_1RCvBVdK','2018.01.31','9','2017.09.05','20170104203523','巴蒂娜部分服装5折起','0','-1','精美服饰，让你美丽一夏','1','310100','','','','','','9')";
//        Statement statement = Db.getSM();
//        int result = -1;
//        try {
//            result = statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println("result : "+result);

        productExcel();
    }
    
    public static void getCouponsTest() {

        List<Plaza> plazaList = DbUtils.findAll(Plaza.class);

        new Thread(new ScanCouponsThread(plazaList.subList(0, 10), 1)).start();
    }

    public static void getCoupons() {

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
            List<Plaza> products = plazaList.subList(begin, end);
            new Thread(new ScanCouponsThread(products, i)).start();
        }
        LogUtils.println("size = "+plazaList.size());
    }

    public static class ScanCouponsThread implements Runnable {

        List<Plaza> plazaList;
        int position;
        float size;
        public ScanCouponsThread(List<Plaza> plazaList, int position) {
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

                        CouponsResult result = gson.fromJson(response, CouponsResult.class);
                        if (result != null && result.data != null && result.data.list != null) {

                            for (int j = 0; j < result.data.list.size(); j++) {
                                Coupons coupons = result.data.list.get(j);
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

        String detailUrl = "http://m.ffan.com/#/detail/coupon/%s?cityId=%s&plazaId=%s";

        Map<String, String> cityMap = CityUtils.getCityMap();

        List<Coupons> couponsList = DbUtils.findAll(Coupons.class);

        for (int i = 0; i < couponsList.size(); i++) {
            Coupons coupons = couponsList.get(i);
            if (coupons != null && coupons.cityId != null) {
                coupons.cityName = cityMap.get(coupons.cityId);
                coupons.logo = String.format(Locale.getDefault(), logo, coupons.pic);
                coupons.detail_url = String.format(Locale.getDefault(), detailUrl, coupons.id, coupons.cityId, coupons.plazaId);
            }
        }

        ProduceExcel.list2Excel(couponsList);
    }

}

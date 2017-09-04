package com.example.util;

import com.example.bean.city.City;
import com.example.bean.product.Product;
import com.example.bean.product.ProductListResult;
import com.example.bean.product.ProductResult;
import com.example.db.DbUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by owm on 2017/8/24.
 */

public class ProductUtils {

    private static final String productListUrl = "https://piao.o2o.cmbchina.com/yummy-portal/JSONServer/execute.do";

    private static final String bodyParams = "body={districtId='', labelId='02', cityNo='%s', longitude='115.31423', parmName='DEFAULT', signOfOrder='0', regionId='', dimension='23.123802', merTypeId2=''}&syshead={chnlUserId='', trans_code='SI_PRD0020', sessionId='null', chnlId='01', pageIndex=1, pageSize=10000000}&p0=a&p1=61&p2=tencent&p3=fe071a668570454da7aa55e9aeffce362&p4=8d661e2fe08849029e4123f2ff016ddf&p5=&p6=530515443&p7=c768d2847b7644beafcf815c82f4b380&p8=129367d83a3543ee9ad944c65a5766e8&p9=null&p10=aa187607300245cc85c4be760d053bdd&groupFlag=";

    public static final String productDetailParam = "body={cityNo='%s', productNo='%s', dimension='23.123802', longitude='113.31423', strNo='null'}&syshead={chnlUserId='', chnlId='01', trans_code='SI_PRD0017', sessionId='null'}&p0=a&p1=61&p2=tencent&p3=fe071a668570454da7aa55e9aeffce362&p4=3c5558fc55f443279758949f90d16f36&p5=&p6=530519289&p7=c768d2847b7644beafcf815c82f4b380&p8=a0d0563f4531428ca07b22986b1b7ef1&p9=null&p10=f776d2c9673a42c99100db629e176de6&groupFlag=";

    private static final String basePath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\getData1\\";


    private static final String detailPath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\detail\\";
    private static final String detailFailPath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\detail\\failList\\";

    public static void getProductList() {
        List<City> cityList = CityUtils.getCityList();

        List<City> retryCityList = new ArrayList<>();

        Gson gson = new Gson();

        City city = null;

        for (int i = 0; i < cityList.size(); i++) {
            try {
                city = cityList.get(i);

                LogUtils.println("");
                LogUtils.println("-------------------------- " + i + "  cityInfo : " + city.cityNo + " " + city.cityName + " --------------------------");

                String bodyParam = String.format(Locale.getDefault(), bodyParams, city.cityNo);

                String response = HttpUtils.post(productListUrl, bodyParam);

                if (Utils.isEmpty(response)) {
                    LogUtils.println("cityNo : " + city.cityNo + "  response is empty");
                    retryCityList.add(city);
                    continue;
                }

                String filePath = basePath + city.cityNo + ".txt";
                boolean saveFile = FileUtils.saveFile(filePath, response);
                if (!saveFile) {
                    LogUtils.println("save file error filePath : " + filePath);
                }

                ProductListResult productListResult = gson.fromJson(response, ProductListResult.class);
                if (!checkProductEmpty(productListResult)) {
                    LogUtils.println("productList.size : " + productListResult.body.rows.size());
                    //添加city信息
                    ProductUtils.addCityInfo(productListResult.body.rows, city);

                    //将列表装换成字符串保存
                    ProductUtils.changeData2DbData(productListResult.body.rows);

                    //保存到数据库
                    DbUtils.save(productListResult.body.rows);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                retryCityList.add(city);
            }
        }

    }

    private static void changeData2DbData(Product product) {
        if (product == null) {
            return;
        }
        Gson gson = new Gson();
        if (product.rows != null && product.rows.size() != 0) {
            try {
                product.rowsJson = gson.toJson(product.rows);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (product.onegoPayDiscountList != null && product.onegoPayDiscountList.size() != 0) {
            try {
                product.onegoPayDiscountListJson = gson.toJson(product.onegoPayDiscountList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void changeData2DbData(List<Product> data) {
        if (data == null) {
            return;
        }
        for (Product product : data) {
            changeData2DbData(product);
        }
    }

    private static void changeDbData2NormalData(List<Product> data) {
        if (checkDataEmpty(data)) {
            return;
        }
        Gson gson = new Gson();
        for (Product product : data) {
            if (product == null) {
                continue;
            }
            if (!Utils.isEmpty(product.rowsJson)) {
                try {
                    product.rows = gson.fromJson(product.rowsJson, new TypeToken<List<Product.RowsBean>>(){}.getType());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!Utils.isEmpty(product.onegoPayDiscountListJson)) {
                try {
                    product.onegoPayDiscountList = gson.fromJson(product.onegoPayDiscountListJson, new TypeToken<List<Product.OnegoPayDiscountListBean>>(){}.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkProductEmpty(ProductListResult productListResult) {
        if (productListResult == null) {
            LogUtils.println("productListResult is null");
            return true;
        }
        if (productListResult.body == null) {
            LogUtils.println("productListResult.body is null");
            return true;
        }
        return checkDataEmpty(productListResult.body.rows);
    }

    private static boolean checkDataEmpty(List<Product> data) {
        if (data == null) {
            LogUtils.println("changeData2Db param data == null");
            return true;
        }
        if (data.size() == 0) {
            LogUtils.println("changeData2Db param data.size() == 0");
            return true;
        }
        return false;
    }

    private static void addCityInfo(List<Product> data, City city) {
        if (checkDataEmpty(data)) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            Product product = data.get(i);
            if (product != null) {
                product.cityNo = city.cityNo;
                product.cityName = city.cityName;
            }
        }
    }

    public static void getProductDetail1() {
        List<Product> productList = DbUtils.query(Product.class, "productNo", "9917063000870");

        new Thread(new GetProductDetail(productList, 1)).start();
    }


    public static void getProductDetail() {

        List<Product> productList = DbUtils.findAll(Product.class);

        int threadNum = 10;

        int threadProductCount = productList.size() / threadNum + 1;
        for (int i = 0; i < threadNum; i++) {
            int begin = i * threadProductCount;
            int end = (i+1) * threadProductCount;
            end = end >= productList.size() ? productList.size() : end;
            if (begin >= end) {
                break;
            }
            List<Product> products = productList.subList(begin, end);
            new Thread(new GetProductDetail(products, i)).start();
        }
        LogUtils.println("size = "+productList.size());
    }

    public static class GetProductDetail implements Runnable {
        public static List<String> failProductNo = new ArrayList();
        private List<Product> data;

        private int position;

        public GetProductDetail(List<Product> data, int position) {
            this.data = data;
            this.position = position;
        }

        @Override
        public void run() {
            Gson gson = new Gson();

            for (int i = 0; i < data.size(); i++) {

                Product product = data.get(i);
                try {
                    StringBuilder builder = new StringBuilder();
                    if (product == null || Utils.isEmpty(product.productNo)) {
                        continue;
                    }
                    builder.append("----------------- " + "position : ").append(position).append(" productNo : ").append(product.productNo).append(" -------------------");
                    String bodyParam = String.format(Locale.getDefault(), productDetailParam, product.cityNo, product.productNo);

                    String response = HttpUtils.post(productListUrl, bodyParam);

                    if (Utils.isEmpty(response)) {
                        builder.append("cityNo : ").append(product.cityNo).append(" productNo : ").append(product.productNo).append("  response is empty");
                        failProductNo.add(product.productNo);
                        continue;
                    }

                    String filePath = detailPath + product.cityNo + "_" + product.productNo + ".txt";
                    boolean saveFile = FileUtils.saveFile(filePath, response);
                    if (!saveFile) {
                        builder.append("save file error filePath : ").append(filePath);
                    }

                    ProductResult productResult = gson.fromJson(response, ProductResult.class);

                    if (checkProductResult(productResult)) {
                        Product body = productResult.body;

                        product.merOnegoType = body.merOnegoType;
                        product.strNo = body.strNo;
                        product.rushBeginTime = body.rushBeginTime;
                        product.recommend = body.recommend;
                        product.signOfRushToday = body.signOfRushToday;
                        product.rushEndTime = body.rushEndTime;
                        product.distance = body.distance;
                        product.stock = body.stock;
                        product.isCanAutoReturn = body.isCanAutoReturn;
                        product.servicePhone = body.servicePhone;
                        product.longitude = body.longitude;
                        product.merNo = body.merNo;
                        product.onegoPayDiscountList = body.onegoPayDiscountList;
                    }
                    changeData2DbData(product);
                    DbUtils.deleteAndSave(product, "productNo", product.productNo);
                    builder.append("\nend");
                    LogUtils.println(builder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (product != null) {
                        failProductNo.add(product.productNo);
                    }
                }

            }

            String failProductNoInfo = "Thread position : " + position + "  failProductNo : " + gson.toJson(failProductNo);
            FileUtils.saveFile(detailFailPath+"failInfo.txt", failProductNoInfo);

        }

    }

    public static boolean checkProductResult(ProductResult productResult) {
        return productResult != null && productResult.body != null;
    }

    public static void createExcel() {

        List<Product> productList = DbUtils.findAll(Product.class);

        changeDbData2NormalData(productList);

        ProduceExcel.product2Excel(productList, "");

    }


}

















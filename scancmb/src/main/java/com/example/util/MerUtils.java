package com.example.util;

import com.example.bean.product.Mer;
import com.example.bean.product.MerResult;
import com.example.bean.product.Product;
import com.example.db.DbUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by owm on 2017/9/4.
 */

public class MerUtils {


    private static final String productListUrl = "https://piao.o2o.cmbchina.com/yummy-portal/JSONServer/execute.do";

    private static final String merPath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\mer\\";
    private static final String merFailPath = "E:\\work\\demo\\RecycleViewSwipe\\scancmb\\mer\\failList\\";

    private static final String productMerParam = "body={dimension='23.123606', longitude='113.314306', cityNo='%s', productNo='%s'}&syshead={chnlUserId='', trans_code='SI_PRD0006', sessionId='null', chnlId='01', pageIndex=1, pageSize=10}&p0=a&p1=61&p2=tencent&p3=fe071a668570454da7aa55e9aeffce362&p4=06c0a0d13ed24832bdb37b9438810cc0&p5=&p6=531464159&p7=c768d2847b7644beafcf815c82f4b380&p8=e3ea956d21744f31803352cf7d7f7fcc&p9=null&p10=6b6459e8babc45cfafc5cb3f1c115fcf&groupFlag=";


    public static void getProductMerTest() {

        List<Product> query = DbUtils.query(Product.class, "productNo", "9917031000253");

        new Thread(new GetProductMer(query, 1)).start();
    }

    public static void getProductMer() {
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
            new Thread(new GetProductMer(products, i)).start();
        }
        LogUtils.println("size = "+productList.size());
    }

    public static class GetProductMer implements Runnable {
        public static List<String> failProductNo = new ArrayList();

        public static String[] columns = {"merName", "totalRecords", "merListInfo"};
        private List<Product> data;
        private float size;
        private int position;

        public GetProductMer(List<Product> data, int position) {
            this.data = data;
            this.position = position;
            this.size = data.size();
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
                    builder.append("----------------- " + "position : ").append(position).append("  ").append(i / size * 100).append("% ").append("  productNo : ").append(product.productNo).append(" -------------------");
                    String bodyParam = String.format(Locale.getDefault(), productMerParam, product.cityNo, product.productNo);

                    String response = HttpUtils.post(productListUrl, bodyParam);

                    if (Utils.isEmpty(response)) {
                        builder.append("cityNo : ").append(product.cityNo).append(" productNo : ").append(product.productNo).append("  response is empty");
                        failProductNo.add(product.productNo);
                        continue;
                    }

                    String filePath = merPath + product.cityNo + "_" + product.productNo + ".txt";
                    boolean saveFile = FileUtils.saveFile(filePath, response);
                    if (!saveFile) {
                        builder.append("save file error filePath : ").append(filePath);
                    }

                    MerResult productResult = gson.fromJson(response, MerResult.class);

                    if (checkMerResult(productResult)) {
                        Mer body = productResult.body;

                        product.merName = body.merName;
                        product.totalRecords = body.totalRecords;
                        if (body.rows != null && body.rows.size() > 0) {
                            StringBuilder info = new StringBuilder();
                            for (int j = 0; j < body.rows.size(); j++) {
                                Mer.RowsBean row = body.rows.get(j);
                                if (row != null) {
                                    info.append(row.strName).append("  ").append(row.address).append("  ").append(row.servicePhone).append("\n");
                                }
                            }
                            product.merListInfo = info.toString();
                        }
                        String[] columnValues = {product.merName, product.totalRecords, product.merListInfo};
                        boolean updateResult = DbUtils.update(Product.class, columns, columnValues, "productNo", product.productNo);
                        builder.append("\nupdate result："+updateResult);
//                        String sql = "update product set merName = '"+product.merName.replace("'","''")+"',totalRecords = '"+product.totalRecords.replace("'","''")+"',merListInfo='"+product.merListInfo.replace("'","''")+"' where productNo = '"+product.productNo+"'";
//                        Db.getSM().executeUpdate(sql);
                    }
//                    changeData2DbData(product);
//                    DbUtils.deleteAndSave(product, "productNo", product.productNo);
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
            FileUtils.saveFile(merFailPath+"failInfo.txt", failProductNoInfo);

        }

    }

    public static boolean checkMerResult(MerResult result) {
        return result != null && result.body != null;
    }

}

package com.owm.depreciate.manager;

import com.google.gson.Gson;
import com.owm.depreciate.bean.FieldTag;
import com.owm.depreciate.bean.Result;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Created by owm on 2017/7/31.
 */

public class TagManager {

    private volatile static TagManager instance;

    private List<FieldTag> fieldTagList;

    private TagManager() {
        fieldTagList = new ArrayList<>();
        initData();
    }

    public static TagManager getInstance() {
        if(instance == null) {
            synchronized (TagManager.class) {
                if(instance == null) {
                    instance = new TagManager();
                }
            }
        }
        return instance;
    }


    /** 网页标题tag **/
    private static final String pageTitleTag = "title";
    /** 价格tag **/
    private static final String priceTag = "span.mui-price-integer";
    /** 价格tag **/
    private static final String priceOriginTag = "del.price-origin";
    /** 商品标题tag **/
    private static final String nameTag = "div.main";
    /** 邮费tag **/
    private static final String postageTag = "div.postage.cell";
    /** 月销量tag **/
    private static final String salesCellTag = "div.sales.cell";
    /** 发货点tag **/
    private static final String deliveryCellTag = "div.delivery.cell";
    /** 所属商店名称tag **/
    private static final String tagShopName = "div.shop-t";
    /** 所属商店log tag **/
    private static final String tagShopLogo = "div.shop-logo.cell";

    private static final String tagShowcase = "section.s-showcase";// img  attr src
    private static final String tagScroller = "div.scroller";// img  attr src

    private void initData() {

//        {"fieldName":"pageTitle","selectTagList":[{"selectList":["section.s-showcase","img"],"attr":["src","data-src"]},{"selectList":["div.scroller","img"],"attr":["src","data-src"]}]}

        String jsonData = "{\"data\":[{\"fieldName\":\"pageTitle\",\"selectTagList\":[{\"selectList\":[\"title\"]}]}," +
                "{\"fieldName\":\"price\",\"selectTagList\":[{\"selectList\":[\"span.mui-price-integer\"]},{\"selectList\":[\"div.price\",\"strong\"]}]}," +
                "{\"fieldName\":\"priceOrigin\",\"selectTagList\":[{\"selectList\":[\"del.price-origin\"]},{\"selectList\":[\"div.oprice\",\"del\"]}]}," +
                "{\"fieldName\":\"commodityName\",\"selectTagList\":[{\"selectList\":[\"title\",\"h1\"]}]}," +
                "{\"fieldName\":\"postage\",\"selectTagList\":[{\"selectList\":[\"div.postage.cell\"]}]}," +
                "{\"fieldName\":\"salesCell\",\"selectTagList\":[{\"selectList\":[\"div.sales.cell\"]}]}," +
                "{\"fieldName\":\"deliveryCell\",\"selectTagList\":[{\"selectList\":[\"div.delivery.cell\"]}]}," +
                "{\"fieldName\":\"shopName\",\"selectTagList\":[{\"selectList\":[\"div.shop-t\"]}]}," +
                "{\"fieldName\":\"shopLogo\",\"selectTagList\":[{\"selectList\":[\"div.shop-logo.cell\",\"img\"],\"attr\":[\"src\",\"data-src\"]}]}," +
                "{\"fieldName\":\"showcase\",\"selectTagList\":[{\"selectList\":[\"#s-showcase\",\"img\"],\"attr\":[\"src\",\"data-src\"]},{\"selectList\":[\"div.scroller\",\"img\"],\"attr\":[\"src\",\"data-src\"]}],\"isList\":\"1\"}" +
                "]}";

        fieldTagList.clear();
        Gson gson = new Gson();
        Result result = gson.fromJson(jsonData, Result.class);
        if (result != null && result.data != null) {
            fieldTagList.addAll(result.data);
        }

    }

    public List<FieldTag> getFieldTagList() {
        return fieldTagList;
    }

    public static void main(String[] args) {
        getInstance();
    }

}

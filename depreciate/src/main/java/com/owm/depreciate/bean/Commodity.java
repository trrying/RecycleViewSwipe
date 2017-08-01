package com.owm.depreciate.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品bean
 * Created by owm on 2017/7/28.
 */

public class Commodity {

    /** 来源：淘宝 **/
    public static final int source_taobao = 0x1;

    /** 来源：天猫 **/
    public static final int source_tmall = 0x2;

    /** 来源：京东 **/
    public static final int source_jd = 0x3;


    public long id;
    /** 来源：淘宝、天猫、京东..... **/
    public int source;
    public String name;
    public String pageTitle;
    public String detail;
    public String url;
    /** 头图 **/
    public List<String> showcase = new ArrayList<>();
    public String currentPrice;
    public String firstPrice;
    /** 原价 **/
    public String originPrice;
//    /** 价格变化列表 **/
//    public List<Price> priceList = new ArrayList<>();

    /** 快递费 **/
    public String postage;
    /** 月销量 **/
    public String salesCell;
    /** 发货点 **/
    public String deliveryCell;

    /** 所属店铺 **/
    public String shopName;
    /** 所属店铺logo **/
    public String shopLogo;


}

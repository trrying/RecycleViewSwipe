package com.example.util.feifan;

import com.example.bean.feifan.Activities;
import com.example.bean.feifan.Coupons;
import com.example.bean.feifan.Plaza;
import com.example.db.DbUtils;

/**
 * 
 * Created by owm on 2017/9/5.
 */

public class FeiFanMain {
    
    public static void main(String[] args) {
//        CityUtils.getCityList();

        DbUtils.createTable(Activities.class);
        DbUtils.createTable(Coupons.class);
        DbUtils.createTable(Plaza.class);
    }
    
}

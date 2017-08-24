package com.example.util;

import com.example.bean.city.City;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by owm on 2017/8/24.
 */

public class ProductUtils {

    public static void getData() {
        List<City> cityList = CityUtils.getCityList();

        ArrayList retryCityList = new ArrayList();

        for (int i = 0; i < cityList.size(); i++) {

        }

    }

}

package com.example.util;

import com.example.bean.cmb.city.City;
import com.example.bean.cmb.city.CityList;
import com.example.bean.cmb.city.CityResult;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by owm on 2017/8/24.
 */

public class CityUtils {

    public static List<City> getCityList() {
        String cityJson = "";

        ArrayList result = new ArrayList();

        Gson gson = new Gson();

        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("cityJson.txt"));
            String buffer;
            while((buffer = reader.readLine()) != null) {
                builder.append(buffer);
            }
            cityJson = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        CityResult cityResult = gson.fromJson(cityJson, CityResult.class);

        if (cityResult == null) {
            System.out.println("city result == null");
            return result;
        }
        if (cityResult.cityList == null) {
            System.out.println("city list == null");
            return result;
        }
        for (int i = 1; i < cityResult.cityList.size(); i++) {
            CityList cityList = cityResult.cityList.get(i);
            if (cityList != null && cityList.cities != null) {
                result.addAll(cityList.cities);
            }
        }
        return result;
    }


}

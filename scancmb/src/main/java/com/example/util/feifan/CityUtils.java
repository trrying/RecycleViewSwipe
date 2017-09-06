package com.example.util.feifan;

import com.example.bean.feifan.City;
import com.example.bean.feifan.CityResult;
import com.example.util.FileUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by owm on 2017/9/5.
 */

public class CityUtils {

    public static List<City> getCityList() {
        List<City> cityList = new ArrayList<>();
        String cityJson = FileUtils.readFile("feifanCityJson.txt");
        Gson gson = new Gson();
        CityResult cityResult = gson.fromJson(cityJson, CityResult.class);
        if (cityResult != null && cityResult.cityList != null) {
            cityList.addAll(cityResult.cityList);
        }
        return cityList;
    }

    public static Map<String, String> getCityMap() {
        List<City> cityList = getCityList();
        Map<String, String> cityMap = new HashMap<>();
        for (int i = 0; i < cityList.size(); i++) {
            cityMap.put(cityList.get(i).cityId, cityList.get(i).cityName);
        }
        return cityMap;
    }

    public static void main(String[] args) {
//        getCityList();
        getCityMap();
    }




























}

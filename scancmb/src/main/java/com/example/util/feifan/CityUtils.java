package com.example.util.feifan;

import com.example.bean.feifan.City;
import com.example.bean.feifan.CityResult;
import com.example.util.FileUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
        getCityList();
    }




























}

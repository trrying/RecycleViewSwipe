package com.example.bean.city;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by owm on 2017/8/24.
 */

public class CityList implements Serializable {
    /**
     * cities : [{"lon":"116.408198","cityName":"北京","cityNo":"10","lat":"39.904667000000003"},{"lon":"113.26442299999999","cityName":"广州","cityNo":"20","lat":"23.129075"},{"lon":"121.472916","cityName":"上海","cityNo":"21","lat":"31.230708"},{"lon":"114.057818","cityName":"深圳","cityNo":"755","lat":"22.543447"}]
     * section : 热门城市
     */

    public String section;
    public List<City> cities;
}
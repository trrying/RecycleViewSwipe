package com.example.bean.cmb.city;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by owm on 2017/8/24.
 */

public class CityResult implements Serializable{

    /**
     * cityList : [{"cities":[{"lon":"116.408198","cityName":"北京","cityNo":"10","lat":"39.904667000000003"},{"lon":"113.26442299999999","cityName":"广州","cityNo":"20","lat":"23.129075"}]
     * respCode : 1000
     * respMsg : 操作成功
     */

    public String respCode;
    public String respMsg;
    public List<CityList> cityList;
}

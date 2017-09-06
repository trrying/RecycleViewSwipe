package com.example.bean.feifan;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by owm on 2017/9/5.
 */

public class ActivitiesResult implements Serializable {

    public String status;
    public String message;
    public DataBean data;
    public String msg;

    public static class DataBean {
        public InfoBean info;
        public List<Activities> list;
        public List<ScatsBean> scats;
        public List<PlazasBean> plazas;

        public static class InfoBean {
            /**
             * more : 0
             * current : 10
             * size : 10
             * cityId : 440100
             */

            public String more;
            public String current;
            public String size;
            public String cityId;
        }

        public static class ScatsBean {
            /**
             * categoryId : 1002
             * categoryName : 百货
             * categoryLevel : 1
             */

            public String categoryId;
            public String categoryName;
            public String categoryLevel;
        }

        public static class PlazasBean {
            /**
             * id : 1000280
             * name : 广州白云万达广场
             * address : 广州市白云区云城东路501号
             * poiLong : 113.266379
             * plazaLongitude : 113.266379
             * poiLat : 23.172775
             * plazaLatitude : 23.172775
             * pic : h006308a0b47891b0a4b17431df7f52f228
             * supportWifi : 1
             * supportFindCar : 1
             * supportSmartQueue : 1
             * supportBeacon : 1
             * beaconDeployer : 0
             * plazaType : 1
             * type : plaza
             * distance :
             * plazaId : 1000280
             * cityId : 440100
             * plazaName : 广州白云万达广场
             */

            public String id;
            public String name;
            public String address;
            public String poiLong;
            public String plazaLongitude;
            public String poiLat;
            public String plazaLatitude;
            public String pic;
            public String supportWifi;
            public String supportFindCar;
            public String supportSmartQueue;
            public String supportBeacon;
            public String beaconDeployer;
            public String plazaType;
            public String type;
            public String distance;
            public String plazaId;
            public String cityId;
            public String plazaName;
        }
    }
}

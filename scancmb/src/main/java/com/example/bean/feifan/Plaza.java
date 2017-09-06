package com.example.bean.feifan;

/**
 *
 * Created by owm on 2017/9/5.
 */

public class Plaza {
    /**
     * cityId : 440100
     * cityName : 广州市
     * distance : 2.0km
     * distanceOri : 1.9508125621643
     * id : 1100163
     * pic : T1t5KvBsYX1RCvBVdK
     * plazaAddress : 天河区 体育西路191号中石化大厦
     * plazaId : 1100163
     * plazaName : 广州佳兆业广场
     * plazaType : 1
     * type : plaza
     * icon : [{"title":"购物中心","type":"shopping"},{"title":"免费WiFi","type":"wifi"}]
     */

    public String cityId;
    public String cityName;
    public String distance;
    public String distanceOri;
    public String id;
    public String pic;
    public String plazaAddress;
    public String plazaId;
    public String plazaName;
    public String plazaType;
    public String type;

    public static class IconBean {
        /**
         * title : 购物中心
         * type : shopping
         */

        public String title;
        public String type;
    }
}

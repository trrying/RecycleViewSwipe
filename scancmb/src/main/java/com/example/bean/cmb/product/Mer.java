package com.example.bean.cmb.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by owm on 2017/9/4.
 */

public class Mer  implements Serializable {
    /**
     * merName : 卡朋西餐厅（广州）
     * totalRecords : 11
     * merLogo : https://piao-cdn.o2o.cmbchina.com/images/20170614-171429_200_150_002000069.jpg
     * merNo : 002000069
     * rows : [{"dimension":"23.124059","strName":"卡朋（289艺术园店）","distance":"0.05688660247164543","address":"广州市越秀区大道289号289艺术园","servicePhone":"020-34042630","longitude":"113.313749","strNo":"0020000690016"},{"dimension":"23.123461","strName":"卡朋（较场西路店）","distance":"3.041599268090662","address":"广州市越秀区较场西路16号","servicePhone":"020-83813235","longitude":"113.284489","strNo":"0020000690006"},{"dimension":"23.123451","strName":"卡朋（周沫北京路广百店）","distance":"4.703071868592041","address":"广州越秀区西湖路18号广百百货新翼9楼卡朋","servicePhone":"020-36660419","longitude":"113.268241","strNo":"0020000690017"},{"dimension":"23.092766","strName":"卡朋（江南大道店）","distance":"5.203672206731941","address":"江南大道中255号","servicePhone":"020-34042630","longitude":"113.276149","strNo":"0020000690010"},{"dimension":"23.094982","strName":"卡朋（江南新地店）","distance":"5.463318093570782","address":"广州市海珠区江南西路46号江南新地商业街A20B铺","servicePhone":"020-84243529","longitude":"113.270966","strNo":"0020000690015"},{"dimension":"23.072471","strName":"卡朋（东晓南店）","distance":"6.1314715892609035","address":"东晓南路1290号新都荟3楼","servicePhone":"020-34411848","longitude":"113.292332","strNo":"0020000690011"},{"dimension":"23.129317","strName":"卡朋（越富店）","distance":"6.143443158457222","address":"广州越秀区人民北路829-831号越富二期创兴广场4楼","servicePhone":"020-29172386","longitude":"113.254453","strNo":"0020000690012"},{"dimension":"23.124486","strName":"卡朋（新光城市广场店）","distance":"6.784845429674735","address":"广州市荔湾区康王中路新光城市广场5楼","servicePhone":"020-81274363","longitude":"113.247886","strNo":"0020000690013"},{"dimension":"23.117632","strName":"卡朋（恒宝店）","distance":"7.449775508074782","address":"广州市荔湾区宝华路133号恒宝广场3楼","servicePhone":"020-81178087","longitude":"113.241691","strNo":"0020000690008"},{"dimension":"23.121448","strName":"卡朋（东圃店）","distance":"9.148252725756134","address":"广州天河区东圃大马路1之1号","servicePhone":"020-32038806","longitude":"113.403652","strNo":"0020000690014"}]
     */

    public String merName;
    public String totalRecords;
    public String merLogo;
    public String merNo;
    public List<RowsBean> rows = new ArrayList<>();

    public static class RowsBean  implements Serializable{
        /**
         * dimension : 23.124059
         * strName : 卡朋（289艺术园店）
         * distance : 0.05688660247164543
         * address : 广州市越秀区大道289号289艺术园
         * servicePhone : 020-34042630
         * longitude : 113.313749
         * strNo : 0020000690016
         */

        public String dimension;
        public String strName;
        public String distance;
        public String address;
        public String servicePhone;
        public String longitude;
        public String strNo;
    }
}

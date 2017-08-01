package com.owm.depreciate.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by owm on 2017/7/31.
 */

public class FieldTag implements Serializable{
    /** 属性名 **/
    public String fieldName;
    /** 该属性是不是列表 **/
    public int isList;
    /** 改属性匹配 html tag标签组 **/
    public List<SelectTag> selectTagList = new ArrayList<>();

    public static class SelectTag implements Serializable {
        /**
         * 属性匹配的html tag 列表
         * 第一个获取不到，再尝试第二个，以此类推
         */
        public List<String> selectList = new ArrayList<>();
        /**
         * html tag 属性组；
         * 用来获取标签的属性值；
         * 如果为空，则获取该标签的text();
         */
        public List<String> attr = new ArrayList<>();
    }

}

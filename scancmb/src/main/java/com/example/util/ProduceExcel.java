package com.example.util;


import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 其他类型数据转化为Excel表格
 * Created by owm on 2017/7/2.
 */

public class ProduceExcel {
    /**
     * 一个sheet最大行数
     */
    public static final int MAX_ROW = 50 * 1000;

    public static <T> void list2Excel(List<T> list, String excelFilePath) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (excelFilePath == null || excelFilePath.isEmpty()) {
            excelFilePath = "default_"+System.currentTimeMillis()+".xls";
        }
        try {

            File excelFile = new File(excelFilePath);
            WritableWorkbook workbook = Workbook.createWorkbook(excelFile);


            Field[] fields = list.get(0).getClass().getDeclaredFields();

            //一个sheet行数不能超过65536，超过要分sheet插入
            if (list.size() > MAX_ROW) {
                int sheetSize = (int) Math.ceil((float)list.size() / MAX_ROW);
                for (int i = 0; i < sheetSize; i++) {
                    WritableSheet sheet = workbook.createSheet("sheet"+(i+1), i);
                    addCell(list.subList(i * MAX_ROW, Math.min((i + 1) * MAX_ROW, list.size())), sheet, fields);
                }
            } else {
                WritableSheet sheet = workbook.createSheet("sheet1", 0);
                addCell(list, sheet, fields);
            }

            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static <T> void addCell(List<T> list, WritableSheet sheet, Field[] fields) throws IllegalAccessException, WriteException {
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            sheet.addCell(new Label(i, 0, fields[i].getName()));
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < fields.length; j++) {
                String value = (String) fields[j].get(list.get(i));
                sheet.addCell(new Label(j, i+1, value));
            }
        }
    }

}

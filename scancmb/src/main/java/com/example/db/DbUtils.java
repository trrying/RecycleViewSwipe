package com.example.db;

import com.example.util.LogUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库工具类
 * Created by owm on 2017/7/1.
 */

public class DbUtils {

    public static boolean createTable(Class clazz) {
        try {

            Class.forName("org.sqlite.JDBC");
            Field[] fields = clazz.getDeclaredFields();

            Connection connection = DriverManager.getConnection("jdbc:sqlite:simple.db");

            Statement statement = connection.createStatement();

            statement.executeUpdate("drop table if exists "+clazz.getSimpleName());
//            statement.executeUpdate("create table person (id integer, name string)");

            StringBuilder builder = new StringBuilder();
            builder.append("create table ").append(clazz.getSimpleName()).append(" (");
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                builder.append(fields[i].getName()).append(" string");
                if (i != fields.length - 1) {
                    builder.append(", ");
                } else {
                    builder.append(")");
                }
            }
            String sql = builder.toString();
            LogUtils.println("createTable sql = " + sql);
            statement.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static <T> void save(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (T t : list) {
            save(t);
        }
    }

    private static <T> Field[] getFields(T t) {
        if (t == null) {
            return null;
        }
        Class clazz = t.getClass();
        return clazz.getDeclaredFields();
    }

    public static <T> void save(T t) {
        if (t == null) {
            return;
        }

        StringBuilder insertSql = new StringBuilder();

        try {
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();

            Statement statement = Db.getSM();

            StringBuilder names = new StringBuilder();
            names.append(" (");

            StringBuilder values = new StringBuilder();
            values.append(" (");
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(t);
                if (value != null && !value.equals("null") && !(value instanceof Iterable) && !(value instanceof Map)) {
                    names.append(fields[i].getName());
                    values.append("'").append(value.toString().replace("'","''")).append("'");
                    if (i != fields.length - 1) {
                        names.append(",");
                        values.append(",");
                    }
                }
            }

            names.append(")");
            values.append(")");

            insertSql.append("insert into ").append(clazz.getSimpleName()).append(" ").append(names.toString()).append(" values ").append(values.toString());

            statement.executeUpdate(insertSql.toString());

        } catch (Exception e) {
            LogUtils.println("sql : " + insertSql.toString());
            e.printStackTrace();
        }
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            String selectSql = "select * from "+clazz.getSimpleName();
            ResultSet resultSet = Db.getSM().executeQuery(selectSql);

            while (resultSet.next()) {
                T bean = clazz.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    String value = resultSet.getString(fields[i].getName());
                    fields[i].set(bean, value);
                }
                list.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

//    public static int count(TongTongBean bean) {
//        findAll(TongTongBean.class);
//        int count = 0;
//        try {
//            //select * from TongTongBean where coordinateX == '116.359467' and coordinateY == '39.914751'
//            StringBuilder countSql = new StringBuilder();
//            countSql.append("select count(*) from ").append(bean.getClass().getSimpleName())
//                    .append(" where coordinateX = '").append(bean.getCoordinateX()).append("'")
//                    .append(" and coordinateY = '").append(bean.getCoordinateY()).append("'");
//            ResultSet resultSet = Db.getSM().executeQuery(countSql.toString());
//            resultSet.next();
//            count = resultSet.getInt(1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return count;
//    }

    public static int count(Class clazz) {
        int count = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(*) from ").append(clazz.getSimpleName());
            ResultSet resultSet = Db.getSM().executeQuery(sql.toString());
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

}





































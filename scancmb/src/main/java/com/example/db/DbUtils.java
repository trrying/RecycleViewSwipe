package com.example.db;

import com.example.util.LogUtils;
import com.example.util.Utils;

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

    public static <T> void deleteAndSave(T t, String keyName,String keyValue) {
        if (t == null || Utils.isEmpty(keyName) || Utils.isEmpty(keyValue)) {
            return;
        }
        delete(t, keyName, keyValue);
        save(t);
    }

    public static <T> void delete(T t, String keyName,String keyValue) {
        if (t == null || Utils.isEmpty(keyName) || Utils.isEmpty(keyValue)) {
            return;
        }
        StringBuilder deleteSql = new StringBuilder();

        try {
            Class clazz = t.getClass();
            Statement statement = Db.getSM();

            deleteSql.append("delete from ").append(clazz.getSimpleName()).append(" where ").append(keyName).append(" = '").append(keyValue).append("'");

            statement.executeUpdate(deleteSql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            String selectSql = "select * from "+clazz.getSimpleName() + " order by cityNo";
            ResultSet resultSet = Db.getSM().executeQuery(selectSql);

            Field[] fields = clazz.getDeclaredFields();
            while (resultSet.next()) {
                T bean = clazz.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    String value = resultSet.getString(fields[i].getName());
                    if (Utils.isEmpty(value)) {
                        continue;
                    }
                    fields[i].setAccessible(true);
                    fields[i].set(bean, value);
                }
                list.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> List<T> query(Class<T> clazz, String keyColumn,String keyValue) {
        List<T> list = new ArrayList<>();
        if (clazz == null || Utils.isEmpty(keyColumn) || Utils.isEmpty(keyValue)) {
            return list;
        }
        try {
            String selectSql = "select * from " + clazz.getSimpleName() + " where " + keyColumn + " = '" + keyValue + "'" + " order by cityNo";
            ResultSet resultSet = Db.getSM().executeQuery(selectSql);

            Field[] fields = clazz.getDeclaredFields();
            while (resultSet.next()) {
                T bean = clazz.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    String value = resultSet.getString(fields[i].getName());
                    if (Utils.isEmpty(value)) {
                        continue;
                    }
                    fields[i].setAccessible(true);
                    fields[i].set(bean, value);
                }
                list.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> boolean update(Class<T> clazz, String[] columns, Object[] values, String keyColumn,String keyValue) {
        if (columns == null || columns.length <= 0 || values == null || values.length <= 0 || columns.length != values.length || clazz == null || Utils.isEmpty(keyColumn) || Utils.isEmpty(keyValue)) {
            return false;
        }
        try {
            StringBuilder updateSql = new StringBuilder();
            updateSql.append("update product set ");
            for (int i = 0; i < columns.length; i++) {
                if (values[i] == null) {
                    continue;
                }
                if (values[i] instanceof String) {
                    values[i] = values[i].toString().replace("'","''");
                }
                updateSql.append(columns[i]).append(" = '").append(values[i]).append("'");
                if (i != columns.length -1) {
                    updateSql.append(",");
                }
            }
            updateSql.append(" where ").append(keyColumn).append(" = '").append(keyValue).append("'");
            Db.getSM().executeUpdate(updateSql.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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





































package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库配置
 * Created by owm on 2017/7/1.
 */

public class Db {

    public static final String DB_CLASS_NAME = "org.sqlite.JDBC";

//    public static final String DB_NAME = "simple.db";
    public static final String DB_NAME = "test.db";

    public static final String DB_PATH = "jdbc:sqlite:" + DB_NAME;

    public static volatile Connection mConnection;
    public static final Object synConnection = new Object();
    public static volatile Statement mStatement;
    public static final Object synStatement = new Object();

    public static Connection openDB() {
        if (mConnection == null) {
            synchronized (synConnection) {
                if (mConnection == null) {
                    try {
                        mConnection = DriverManager.getConnection(DB_PATH);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mConnection;
    }

    public static Statement getSM() {
        try {
            if (mStatement == null) {
                synchronized (synStatement) {
                    if (mStatement == null) {
                        mStatement = openDB().createStatement();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStatement;
    }

}

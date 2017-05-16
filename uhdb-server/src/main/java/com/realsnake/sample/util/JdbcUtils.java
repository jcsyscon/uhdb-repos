/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 28.
 */
package com.realsnake.sample.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <pre>
 * Class Name : JdbcConnection.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 28.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 28.
 * @version 1.0
 */
public class JdbcUtils {

    public static final String JDBC_DRIVER_NAME = "com.mysql.jdbc.Driver";

    public static final String CONN_URL = "jdbc:mysql:%s:3306/DB명";

    public Connection getConnection(String dpIp) {
        Connection conn = null;

        try {
            Class.forName(JDBC_DRIVER_NAME);
            conn = DriverManager.getConnection(String.format(CONN_URL, dpIp), "username", "password");
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int execUpdateQuery(Connection conn, String query) {
        PreparedStatement ps = null;
        int result = 0;

        try {
            ps = conn.prepareStatement(query);
            // ps.setString(1, ""); // 1부터 시작
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    //
                }
            }
        }

        return result;
    }

}

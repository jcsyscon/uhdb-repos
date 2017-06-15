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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String JDBC_DRIVER_NAME = "com.mysql.jdbc.Driver";

    private static final String CONN_URL = "jdbc:mysql://%s:%s/safe_db"; // IP, 포트

    private static final String ID = "safe";
    private static final String PW = "syscorn2";

    public Connection getConnection(String dpIp, String port) {
        Connection conn = null;

        try {
            Class.forName(JDBC_DRIVER_NAME);
            conn = DriverManager.getConnection(String.format(CONN_URL, dpIp, port), ID, PW);
            // conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("<<DB Connection open 오류>>", e);
        }

        return conn;
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("<<DB Connection close 오류>>", e);
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

    private static final String BOX_OPEN_UPDATE_QUERY = "UPDATE tb_cd0102 SET code_nm = ? WHERE aptid = ? AND code = ? AND code_type = 'BOX_OPEN'";

    public int openBox(String gonginIp, String port, String aptId, String aptPosi, String boxNo) throws Exception {
        Connection conn = this.getConnection(gonginIp, port);

        if (conn == null) {
            logger.info("<<DB Connection 가져오기 실패, gonginIp: {}, port: {}>>", gonginIp, port);
            return 0;
        }

        PreparedStatement ps = conn.prepareStatement(BOX_OPEN_UPDATE_QUERY);
        ps.setString(1, boxNo);
        ps.setString(2, aptId);
        ps.setString(3, aptPosi);
        int result = ps.executeUpdate();
        ps.close();

        this.closeConnection(conn);

        return result;
    }

    private static final String BOX_INIT_UPDATE_QUERY = "UPDATE tb_cd0102 SET code_nm = ? WHERE aptid = ? AND code = ? AND code_type = 'BOX_DATA_INIT'";

    public int initBox(String gonginIp, String port, String aptId, String aptPosi, String boxNo) throws Exception {
        Connection conn = this.getConnection(gonginIp, port);

        if (conn == null) {
            logger.info("<<DB Connection 가져오기 실패, gonginIp: {}, port: {}>>", gonginIp, port);
            return 0;
        }

        PreparedStatement ps = conn.prepareStatement(BOX_INIT_UPDATE_QUERY);
        ps.setString(1, boxNo);
        ps.setString(2, aptId);
        ps.setString(3, aptPosi);
        int result = ps.executeUpdate();
        ps.close();

        this.closeConnection(conn);

        return result;
    }

}

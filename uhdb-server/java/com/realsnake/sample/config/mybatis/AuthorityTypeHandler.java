/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 14.
 */
package com.realsnake.sample.config.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * <pre>
 * Class Name : AuthorityTypeHandler.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 14.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 14.
 * @version 1.0
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class AuthorityTypeHandler extends BaseTypeHandler<SimpleGrantedAuthority> {

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SimpleGrantedAuthority parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getAuthority());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, java.lang.String)
     */
    @Override
    public SimpleGrantedAuthority getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return new SimpleGrantedAuthority(rs.getString(columnName));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, int)
     */
    @Override
    public SimpleGrantedAuthority getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return new SimpleGrantedAuthority(rs.getString(columnIndex));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement, int)
     */
    @Override
    public SimpleGrantedAuthority getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return new SimpleGrantedAuthority(cs.getString(columnIndex));
    }

}

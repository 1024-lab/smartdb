package net.lab1024.smartdb.mapping.handler.type;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String> {
    @Override
    public String getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        if (JdbcType.CHAR == jdbcType) {
            return rs.getString(columnIndex);
        } else if (JdbcType.CLOB == jdbcType) {
            Clob clob = rs.getClob(columnIndex);
            return toString(clob);
        } else if (JdbcType.VARCHAR == jdbcType) {
            return rs.getString(columnIndex);
        } else if (JdbcType.LONGVARCHAR == jdbcType) {
            return rs.getString(columnIndex);
        } else if (JdbcType.NVARCHAR == jdbcType) {
            return rs.getNString(columnIndex);
        } else if (JdbcType.NCHAR == jdbcType) {
            return rs.getNString(columnIndex);
        } else if (JdbcType.NCLOB == jdbcType) {
            Clob clob = rs.getClob(columnIndex);
            return toString(clob);
        }
        return rs.getString(columnIndex);
    }

    @Override
    public String getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        if (JdbcType.CHAR == jdbcType) {
            return rs.getString(columnName);
        } else if (JdbcType.CLOB == jdbcType) {
            Clob clob = rs.getClob(columnName);
            return toString(clob);
        } else if (JdbcType.VARCHAR == jdbcType) {
            return rs.getString(columnName);
        } else if (JdbcType.LONGVARCHAR == jdbcType) {
            return rs.getString(columnName);
        } else if (JdbcType.NVARCHAR == jdbcType) {
            return rs.getNString(columnName);
        } else if (JdbcType.NCHAR == jdbcType) {
            return rs.getNString(columnName);
        } else if (JdbcType.NCLOB == jdbcType) {
            Clob clob = rs.getClob(columnName);
            return toString(clob);
        }
        return rs.getString(columnName);
    }

    private String toString(Clob clob) throws SQLException {
        return clob == null ? null : clob.getSubString(1, (int) clob.length());
    }
}

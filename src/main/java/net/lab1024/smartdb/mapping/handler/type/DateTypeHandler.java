package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class DateTypeHandler implements TypeHandler<Date> {

    @Override
    public Date getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.DATE) {
            java.sql.Date sqlDate = rs.getDate(columnIndex);
            if (sqlDate != null) {
                return new Date(sqlDate.getTime());
            }
        } else if (jdbcType == JdbcType.TIME) {
            Time sqlTime = rs.getTime(columnIndex);
            if (sqlTime != null) {
                return new Date(sqlTime.getTime());
            }

        } else {
            Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
            if (sqlTimestamp != null) {
                return new Date(sqlTimestamp.getTime());
            }
        }
        return null;
    }

    @Override
    public Date getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.DATE) {
            java.sql.Date sqlDate = rs.getDate(columnName);
            if (sqlDate != null) {
                return new Date(sqlDate.getTime());
            }
        } else if (jdbcType == JdbcType.TIME) {
            Time sqlTime = rs.getTime(columnName);
            if (sqlTime != null) {
                return new Date(sqlTime.getTime());
            }

        } else {
            Timestamp sqlTimestamp = rs.getTimestamp(columnName);
            if (sqlTimestamp != null) {
                return new Date(sqlTimestamp.getTime());
            }
        }
        return null;
    }

}

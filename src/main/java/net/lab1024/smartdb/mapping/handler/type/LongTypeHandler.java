package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler implements TypeHandler<Long> {

    @Override
    public Long getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        long result = rs.getLong(columnIndex);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public Long getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        long result = rs.getLong(columnName);
        return result == 0 && rs.wasNull() ? null : result;
    }
}

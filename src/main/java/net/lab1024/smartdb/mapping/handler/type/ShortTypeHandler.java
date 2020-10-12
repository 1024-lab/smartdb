package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortTypeHandler implements TypeHandler<Short> {
    @Override
    public Short getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        short result = rs.getShort(columnIndex);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public Short getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        short result = rs.getShort(columnName);
        return result == 0 && rs.wasNull() ? null : result;
    }
}

package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler implements TypeHandler<Float> {
    @Override
    public Float getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        float result = rs.getFloat(columnIndex);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public Float getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        float result = rs.getFloat(columnName);
        return result == 0 && rs.wasNull() ? null : result;
    }
}

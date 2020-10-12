package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleTypeHandler implements TypeHandler<Double> {
    @Override
    public Double getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        double result = rs.getDouble(columnIndex);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public Double getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        double result = rs.getDouble(columnName);
        return result == 0 && rs.wasNull() ? null : result;
    }
}

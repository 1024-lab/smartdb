package net.lab1024.smartdb.mapping.handler.type;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalTypeHandler implements TypeHandler<BigDecimal> {
    @Override
    public BigDecimal getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        return rs.getBigDecimal(columnName);
    }
}

package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteTypeHandler implements TypeHandler<Byte> {
    @Override
    public Byte getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        byte result = rs.getByte(columnIndex);
        return result == 0 && rs.wasNull() ? null : result;
    }

    @Override
    public Byte getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        byte result = rs.getByte(columnName);
        return result == 0 && rs.wasNull() ? null : result;
    }
}

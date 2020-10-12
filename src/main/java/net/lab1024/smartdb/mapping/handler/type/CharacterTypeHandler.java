package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterTypeHandler implements TypeHandler<Character> {
    @Override
    public Character getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (columnValue != null) {
            return columnValue.charAt(0);
        } else {
            return null;
        }
    }

    @Override
    public Character getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        String columnValue = rs.getString(columnName);
        if (columnValue != null) {
            return columnValue.charAt(0);
        } else {
            return null;
        }
    }
}

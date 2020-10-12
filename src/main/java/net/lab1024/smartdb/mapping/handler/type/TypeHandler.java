package net.lab1024.smartdb.mapping.handler.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

    T getResult(ResultSet rs, int columnIndex,JdbcType jdbcType) throws SQLException;

    T getResult(ResultSet rs, String columnName,JdbcType jdbcType) throws SQLException;

}

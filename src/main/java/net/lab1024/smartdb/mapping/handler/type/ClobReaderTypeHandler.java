package net.lab1024.smartdb.mapping.handler.type;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobReaderTypeHandler implements TypeHandler<Reader>{
    @Override
    public Reader getResult(ResultSet rs, int columnIndex, JdbcType jdbcType) throws SQLException {
        return toReader(rs.getClob(columnIndex));
    }

    @Override
    public Reader getResult(ResultSet rs, String columnName, JdbcType jdbcType) throws SQLException {
        return toReader(rs.getClob(columnName));
    }

    private Reader toReader(Clob clob) throws SQLException {
        if (clob == null) {
            return null;
        } else {
            return clob.getCharacterStream();
        }
    }
}

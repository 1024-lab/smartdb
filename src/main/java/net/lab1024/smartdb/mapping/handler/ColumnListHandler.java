package net.lab1024.smartdb.mapping.handler;

import net.lab1024.smartdb.mapping.handler.type.JdbcType;
import net.lab1024.smartdb.mapping.handler.type.TypeHandler;
import net.lab1024.smartdb.mapping.handler.type.TypeHandlerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author zhuoluodada@qq.com
 */
public class ColumnListHandler<T> extends AbstractListHandler<T> {

    private int columnIndex;

    private String columnName;

    private Class<T> cls;

    public ColumnListHandler() {
        this(1, null, null);
    }

    public ColumnListHandler(int columnIndex, String columnName, Class<T> cls) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.cls = cls;
    }

    public ColumnListHandler(int columnIndex, Class<T> cls) {
        this(columnIndex, null, cls);
    }

    public ColumnListHandler(String columnName) {
        this(1, columnName, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T handleRow(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnType = metaData.getColumnType(this.columnIndex);
        TypeHandler<?> handler = TypeHandlerFactory.getHandler(this.cls);
        JdbcType jdbcType = JdbcType.forCode(columnType);
        if (this.columnName == null) {
            return (T) handler.getResult(rs, this.columnIndex, jdbcType);
        }
        return (T) rs.getObject(this.columnName);
    }

}

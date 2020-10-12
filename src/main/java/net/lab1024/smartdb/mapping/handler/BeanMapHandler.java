package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhuoluodada@qq.com
 */
public class BeanMapHandler<K, V> extends AbstractKeyedHandler<K, V> {

    private final Class<V> type;

    private final RowConverter convert;

    private final int columnIndex;

    private final String columnName;

    private BeanMapHandler(Class<V> type, RowConverter convert,
                           int columnIndex, String columnName) {
        super();
        this.type = type;
        this.convert = convert;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected K createKey(ResultSet rs) throws SQLException {
        return (columnName == null) ?
                (K) rs.getObject(columnIndex) :
                (K) rs.getObject(columnName);
    }

    @Override
    protected V createRow(ResultSet rs) throws SQLException {
        return this.convert.toBean(rs, type);
    }

}

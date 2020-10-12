package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhuoluodada@qq.com
 */
public class BeanHandler<T> implements ResultSetHandler<T> {

    private final Class<T> type;
    private final RowConverter convert;


    public BeanHandler(Class<T> type, RowConverter convert) {
        this.type = type;
        this.convert = convert;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toBean(rs, this.type) : null;
    }

}

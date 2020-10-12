package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zhuoluodada@qq.com
 */
public class BeanListHandler<T> extends  AbstractListHandler<T> {

    private final Class<T> type;

    private final RowConverter convert;

    public BeanListHandler(Class<T> type, RowConverter convert) {
        this.type = type;
        this.convert = convert;
    }


    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        return this.convert.toBeanList(rs, type);
    }

    @Override
    protected T handleRow(ResultSet rs) throws SQLException {
        return null;
    }
}

package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author zhuoluodada@qq.com
 */
public class ArrayHandler implements ResultSetHandler<Object[]> {

    private static final Object[] EMPTY_ARRAY = new Object[0];
    private final RowConverter convert;

    public ArrayHandler(RowConverter convert) {
        super();
        this.convert = convert;
    }

    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }

}

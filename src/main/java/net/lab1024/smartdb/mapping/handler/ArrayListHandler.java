package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhuoluodada@qq.com
 */
public class ArrayListHandler extends AbstractListHandler<Object[]> {

    private final RowConverter convert;

    public ArrayListHandler(RowConverter convert) {
        super();
        this.convert = convert;
    }


    @Override
    protected Object[] handleRow(ResultSet rs) throws SQLException {
        return this.convert.toArray(rs);
    }

}

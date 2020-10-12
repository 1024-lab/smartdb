package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author zhuoluodada@qq.com
 */
public class MapListHandler extends AbstractListHandler<Map<String, Object>> {

    private final RowConverter convert;

    public MapListHandler(RowConverter convert) {
        super();
        this.convert = convert;
    }

    @Override
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}

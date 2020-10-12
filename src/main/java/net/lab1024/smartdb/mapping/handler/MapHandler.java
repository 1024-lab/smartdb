package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author zhuoluodada@qq.com
 */
public class MapHandler implements ResultSetHandler<Map<String, Object>> {

    private final RowConverter convert;

    public MapHandler(RowConverter convert) {
        super();
        this.convert = convert;
    }

    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }

}

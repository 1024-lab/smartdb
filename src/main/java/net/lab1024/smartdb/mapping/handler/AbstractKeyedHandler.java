package net.lab1024.smartdb.mapping.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuoluodada@qq.com
 */
public abstract class AbstractKeyedHandler<K, V> implements ResultSetHandler<Map<K, V>> {


    @Override
    public Map<K, V> handle(ResultSet rs) throws SQLException {
        Map<K, V> result = createMap();
        while (rs.next()) {
            result.put(createKey(rs), createRow(rs));
        }
        return result;
    }

    protected Map<K, V> createMap() {
        return new HashMap<K, V>();
    }

    protected abstract K createKey(ResultSet rs) throws SQLException;

    protected abstract V createRow(ResultSet rs) throws SQLException;

}

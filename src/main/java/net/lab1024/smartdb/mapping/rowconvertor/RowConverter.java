package net.lab1024.smartdb.mapping.rowconvertor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author zhuoluodada@qq.com
 */
public interface RowConverter {

    Object[] toArray(ResultSet rs) throws SQLException;

    <T> T toBean(ResultSet rs, Class<T> type) throws SQLException;

    <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException;

    Map<String, Object> toMap(ResultSet rs) throws SQLException;

}

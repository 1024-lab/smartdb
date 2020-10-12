package net.lab1024.smartdb.mapping.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhuoluodada@qq.com
 */
public interface ResultSetHandler<T> {

    T handle(ResultSet rs) throws SQLException;

}

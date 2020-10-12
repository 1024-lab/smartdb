package net.lab1024.smartdb;

import java.sql.SQLException;

/**
 * 支持事务的smartdb
 *
 * @author zhuoluodada@qq.com
 */
public interface TransactionSmartDbNode extends SmartDbNode {

    /**
     * 回滚
     *
     * @throws SQLException
     */
    void rollback() throws SQLException;

    /**
     * 开启事务
     *
     * @throws SQLException
     */
    void begin(int transactionLevel) throws SQLException;

    /**
     * 开启事务
     *
     * @throws SQLException
     */
    void begin() throws SQLException;

    /**
     * 提交事务
     *
     * @throws SQLException
     */
    void commit() throws SQLException;

    /**
     * 释放连接
     */
    void releaseConnection();
}

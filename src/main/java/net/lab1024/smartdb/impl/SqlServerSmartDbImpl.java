package net.lab1024.smartdb.impl;

import net.lab1024.smartdb.AbstractSmartDbNode;
import net.lab1024.smartdb.SmartDbConfig;
import net.lab1024.smartdb.TransactionSmartDbNode;
import net.lab1024.smartdb.datasource.SmartDbDataSource;
import net.lab1024.smartdb.sqlbuilder.ReplaceSqlBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * sqlserver实现类
 *
 * @author zhuoluodada@qq.com
 */
public class SqlServerSmartDbImpl extends AbstractSmartDbNode {

    public SqlServerSmartDbImpl(SmartDbDataSource dataSource, SmartDbConfig smartDbConfig) {
        super(dataSource, smartDbConfig);
    }

    @Override
    public TransactionSmartDbNode getTransaction() {
        return new SqlServer4Transaction(this.smartDbDataSource, this.smartDbConfig);
    }

    @Override
    public int execute(ReplaceSqlBuilder replaceSqlBuilder) {
        throw new UnsupportedOperationException("oracle cannot support replace operate");
    }

    private class SqlServer4Transaction extends OracleSmartDbImpl implements TransactionSmartDbNode {

        private Connection transactionConn;
        private Boolean originalAutoCommit;

        public SqlServer4Transaction(SmartDbDataSource dataSource, SmartDbConfig smartDbConfig) {
            super(dataSource, smartDbConfig);
        }

        @Override
        public TransactionSmartDbNode getTransaction() {
            return this;
        }

        @Override
        public Connection getConnection() throws SQLException {
            return this.getTransactionConnection();
        }

        @Override
        public void rollback() throws SQLException {
            this.getTransactionConnection().rollback();
        }

        @Override
        public void begin(int transactionLevel) throws SQLException {
            this.originalAutoCommit = this.getTransactionConnection().getAutoCommit();
            this.transactionConn.setTransactionIsolation(transactionLevel);
            this.transactionConn.setAutoCommit(false);
        }

        @Override
        public void begin() throws SQLException {
            this.getTransactionConnection().setAutoCommit(false);
        }

        private Connection getTransactionConnection() throws SQLException {
            if (this.transactionConn == null) {
                synchronized (this) {
                    if (this.transactionConn == null) {
                        this.transactionConn = this.smartDbDataSource.getConnection();
                    }
                }
            }
            return this.transactionConn;
        }

        public void releaseResources(ResultSet rs, Statement stmt, Connection conn) {
            super.releaseResources(rs, stmt, null);
        }

        @Override
        public void commit() throws SQLException {
            this.getTransactionConnection().commit();
        }

        @Override
        public void releaseConnection() {
            if (this.transactionConn != null) {
                if (this.originalAutoCommit != null) {
                    try {
                        this.transactionConn.setAutoCommit(this.originalAutoCommit);
                    } catch (SQLException e) {
                        LOG.error("", e);
                    }
                }
                this.smartDbDataSource.releaseConnection(transactionConn);
                this.transactionConn = null;
            }
        }
    }
}

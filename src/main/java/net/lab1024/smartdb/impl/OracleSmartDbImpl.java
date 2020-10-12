package net.lab1024.smartdb.impl;

import net.lab1024.smartdb.*;
import net.lab1024.smartdb.datasource.OptEnum;
import net.lab1024.smartdb.datasource.SmartDbDataSource;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.pagination.PaginateParam;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.sqlbuilder.ReplaceSqlBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * oracle实现类
 *
 * @author zhuoluodada@qq.com
 */
public class OracleSmartDbImpl extends AbstractSmartDbNode {

    public OracleSmartDbImpl(SmartDbDataSource dataSource, SmartDbConfig smartDbConfig) {
        super(dataSource, smartDbConfig);
    }

    @Override
    public TransactionSmartDbNode getTransaction() {
        return new Oracle4Transaction(this.smartDbDataSource, this.smartDbConfig);
    }

    @Override
    public int execute(ReplaceSqlBuilder replaceSqlBuilder) {
        throw new UnsupportedOperationException("oracle cannot support replace operate");
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> handler, PaginateParam paginateParam, String sql, Object... params) {
        int pageNumber = paginateParam.getPageNumber();
        int pageSize = paginateParam.getPageSize();
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        if (pageSize < 1) {
            pageSize = 10;
        }

        Integer total = 0;
        if (paginateParam.isSearchCount()) {
            String totalRowSql = getSupportDatabaseType().getPaginateSqlGenerator().generateCountSql(pageNumber, pageSize, sql);
            total = query(ScalarHandler.BigDecimal, totalRowSql, params).intValue();
            if (total == null || total == 0) {
                return new PaginateResult<T>(pageNumber, pageSize);
            }

            int pageCount = (total / pageSize);
            if (total % pageSize != 0) {
                pageCount++;
            }

            if (pageNumber > pageCount) {
                return new PaginateResult<T>(new ArrayList<T>(0), total, pageNumber, pageSize);
            }
        }

        String pageSql = getSupportDatabaseType().getPaginateSqlGenerator().generatePaginateSql(pageNumber, pageSize, sql);
        List<T> list = queryList(handler, pageSql, params);
        return new PaginateResult<T>(list, total, pageNumber, pageSize);
    }

    private class Oracle4Transaction extends OracleSmartDbImpl implements TransactionSmartDbNode {

        private Connection transactionConn;
        private Boolean originalAutoCommit;

        public Oracle4Transaction(SmartDbDataSource dataSource, SmartDbConfig smartDbConfig) {
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

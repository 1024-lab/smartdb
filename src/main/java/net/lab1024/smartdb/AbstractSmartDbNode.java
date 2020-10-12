package net.lab1024.smartdb;

import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.datasource.SmartDbDataSource;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.mapping.handler.AbstractListHandler;
import net.lab1024.smartdb.mapping.handler.ResultSetHandler;
import net.lab1024.smartdb.mapping.handler.ResultSetHandlerFactory;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.mapping.reflect.OrmClassFieldMeta;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;
import net.lab1024.smartdb.pagination.PaginateParam;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.sqlbuilder.*;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 通用实现父类
 *
 * @author zhuoluodada@qq.com
 */
public abstract class AbstractSmartDbNode implements SmartDbNode {

    protected static Logger LOG = LoggerFactory.getLogger(AbstractSmartDbNode.class);
    protected static final Object[] EMPTY_PARAM = new Object[0];

    protected ResultSetHandlerFactory resultSetHandlerFactory;

    /**
     * smartdb的配置信息，对于一个smartdb集群各个节点公用一个配置
     */
    protected SmartDbConfig smartDbConfig;

    /**
     * 当前节点的数据源
     */
    protected SmartDbDataSource smartDbDataSource;

    public AbstractSmartDbNode(SmartDbDataSource dataSource, SmartDbConfig smartDbConfig) {
        this.smartDbDataSource = dataSource;
        this.smartDbConfig = smartDbConfig;
        this.resultSetHandlerFactory = new ResultSetHandlerFactory(smartDbConfig.getRowConverter());
    }

    @Override
    public boolean runTransaction(TransactionRunnable transactionRunnable) {
        TransactionSmartDbNode transactionSmartDb = null;
        try {

            transactionSmartDb = this.getTransaction();
            transactionSmartDb.begin();
            boolean result = transactionRunnable.run(transactionSmartDb);
            if (result) {
                transactionSmartDb.commit();
            } else {
                transactionSmartDb.rollback();
            }
            return result;
        } catch (Exception e) {
            if (transactionSmartDb != null) {
                try {
                    transactionSmartDb.rollback();
                } catch (Exception e1) {
                    LOG.error("", e1);
                }
            }
            return false;
        } finally {
            try {
                if (transactionSmartDb != null) {
                    transactionSmartDb.releaseConnection();
                }
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return smartDbDataSource.getConnection();
    }

    @Override
    public boolean runTransaction(TransactionRunnable transactionRunnable, int transactionLevel) {
        TransactionSmartDbNode transactionSmartDb = null;
        try {

            transactionSmartDb = this.getTransaction();
            transactionSmartDb.begin(transactionLevel);
            boolean result = transactionRunnable.run(transactionSmartDb);
            if (result) {
                transactionSmartDb.commit();
            } else {
                transactionSmartDb.rollback();
            }
            return result;
        } catch (Exception e) {
            if (transactionSmartDb != null) {
                try {
                    transactionSmartDb.rollback();
                } catch (Exception e1) {
                    LOG.error("", e1);
                }
            }
            return false;
        } finally {
            try {
                if (transactionSmartDb != null) {
                    transactionSmartDb.releaseConnection();
                }
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
    }

    @Override
    public SmartDbDataSource getDataSource() {
        return this.smartDbDataSource;
    }

    @Override
    public RowConverter getRowConverter() {
        return this.smartDbConfig.getRowConverter();
    }

    public boolean isShowSql() {
        return this.smartDbConfig.isShowSql();
    }

    @Override
    public ColumnNameConverter getColumnNameConverter() {
        return this.smartDbConfig.getColumnNameConverter();
    }


    @Override
    public TableNameConverter getTableNameConverter() {
        return this.smartDbConfig.getTableNameConverter();
    }


    @Override
    public SelectSqlBuilder selectSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().select(this);
    }

    @Override
    public InsertSqlBuilder insertSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().insert(this);
    }

    @Override
    public UpdateSqlBuilder updateSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().update(this);
    }

    @Override
    public DeleteSqlBuilder deleteSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().delete(this);
    }

    @Override
    public ReplaceSqlBuilder replaceSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().replace(this);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return this.smartDbConfig.getSupportDatabaseType();
    }

    @Override
    public int execute(String sql) {
        return execute(sql, EMPTY_PARAM);
    }

    @Override
    public int execute(DeleteSqlBuilder deleteSqlBuilder) {
        return execute(deleteSqlBuilder.generateSql(false), deleteSqlBuilder.getAllParams());
    }

    @Override
    public int execute(InsertSqlBuilder insertSqlBuilder) {
        return execute(insertSqlBuilder.generateSql(false), insertSqlBuilder.getAllParams());
    }

    @Override
    public int execute(UpdateSqlBuilder updateSqlBuilder) {
        return execute(updateSqlBuilder.generateSql(false), updateSqlBuilder.getAllParams());
    }

    @Override
    public int execute(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement(sql);
            //show sql
            showSql(sql, params);
            SmartDbHelper.setParams(stmt, params);
            return stmt.executeUpdate();
        } catch (Throwable t) {
            throw new SmartDbException(String.format("executeCommand error, sql:%s params:%s", sql, SmartDbHelper.toString(params)), t);
        } finally {
            releaseResources(null, stmt, conn);
        }
    }

    @Override
    public int execute(String sql, List<Object> params) {
        return execute(sql, params == null ? EMPTY_PARAM : params.toArray(new Object[params.size()]));
    }

    @Override
    public int[] batch(String sql, Collection<Object[]> paramsCollection) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement(sql);
            //show sql
            showSql(sql, paramsCollection);
            for (Object[] params : paramsCollection) {
                if (SmartDbHelper.setParams(stmt, params)) {
                    stmt.addBatch();
                }
            }
            return stmt.executeBatch();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            releaseResources(null, stmt, conn);
        }
    }

    @Override
    public <T> List<T> batchInsert(List<T> tList) {
        return this.commonBatchInsert(tList, false);
    }

    protected <T> List<T> commonBatchInsert(List<T> tList, boolean isSelective) {
        if (tList == null || tList.isEmpty()) {
            return tList;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            T t = tList.get(0);
            OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(t.getClass());
            InsertSqlBuilder insertSqlBuilder = insertSqlBuilder();
            if (isSelective) {
                insertSqlBuilder.insertEntitySelective(t);
            } else {
                insertSqlBuilder.insertEntity(t);
            }


            String sql = insertSqlBuilder.generateSql();
            Collection<Object[]> paramsArrayList = Lists.newArrayListWithExpectedSize(tList.size());
            try {
                for (T t1 : tList) {
                    List<Object> paramsList = new ArrayList<Object>();

                    OrmClassFieldMeta[] primaryKeyFields = classMeta.getPrimaryKeyFields();
                    for (int i = 0; i < primaryKeyFields.length; i++) {
                        OrmClassFieldMeta f = primaryKeyFields[i];
                        Object o = f.getField().get(t1);
                        if (o == null) {
                            continue;
                        }
                        paramsList.add(o);
                    }

                    for (OrmClassFieldMeta f : classMeta.getColumnsFields()) {
                        Object o = f.getField().get(t1);
                        if (isSelective && o == null) {
                            continue;
                        }
                        paramsList.add(o);
                    }

                    paramsArrayList.add(paramsList.toArray());
                }
            } catch (IllegalAccessException e) {
                throw new SmartDbException(e);
            }

            conn = this.getConnection();
            if (classMeta.isUseGeneratedKey()) {
                stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                stmt = conn.prepareStatement(sql);
            }

            //show sql
            showSql(sql, paramsArrayList);
            for (Object[] params : paramsArrayList) {
                if (SmartDbHelper.setParams(stmt, params)) {
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();

            if (!Objects.equal(SupportDatabaseType.ORACLE, this.smartDbConfig.getSupportDatabaseType()) && classMeta.isUseGeneratedKey()) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                int tIndex = 0;
                while (generatedKeys.next()) {
                    classMeta.injectGeneratedKeys(generatedKeys, tList.get(tIndex));
                    tIndex++;
                }
            }

            return tList;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            releaseResources(null, stmt, conn);
        }
    }

    @Override
    public <T> List<T> batchInsertSelective(List<T> tList) {
        return this.commonBatchInsert(tList, true);
    }


    @Override
    public <T> T query(ResultSetHandler<T> handler, SelectSqlBuilder selectSqlBuilder) {
        return query(handler, selectSqlBuilder.generateSql(false), selectSqlBuilder.getAllParams());
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql) {
        return query(handler, sql, EMPTY_PARAM);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            SmartDbHelper.setParams(stmt, params);
            //show sql
            showSql(sql, params);
            rs = stmt.executeQuery();
            return handler.handle(rs);
        } catch (Throwable t) {
            throw new SmartDbException(t);
        } finally {
            releaseResources(rs, stmt, conn);
        }
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql, List<Object> params) {
        return query(handler, sql, params == null ? EMPTY_PARAM : params.toArray(new Object[params.size()]));
    }

    @Override
    public <T> T queryFirst(Class<T> cls, SelectSqlBuilder selectSqlBuilder) {
        return queryFirst(cls, selectSqlBuilder.generateSql(false), selectSqlBuilder.getAllParams());
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql) {
        return queryFirst(cls, sql, EMPTY_PARAM);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement(sql);
            //show sql
            showSql(sql, params);
            SmartDbHelper.setParams(stmt, params);
            rs = stmt.executeQuery();
            return this.resultSetHandlerFactory.getObjectResultHandler(cls).handle(rs);
        } catch (Throwable t) {
            throw new SmartDbException(t);
        } finally {
            releaseResources(rs, stmt, conn);
        }
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql, List<Object> params) {
        return queryFirst(cls, sql, params == null ? EMPTY_PARAM : params.toArray(new Object[params.size()]));
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, SelectSqlBuilder selectSqlBuilder) {
        return queryList(cls, selectSqlBuilder.generateSql(false), selectSqlBuilder.getAllParams());
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql) {
        return queryList(cls, sql, EMPTY_PARAM);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement(sql);
            SmartDbHelper.setParams(stmt, params);
            //show sql
            showSql(sql, params);
            rs = stmt.executeQuery();
            return (List<T>) this.resultSetHandlerFactory.getObjectListResultHandler(cls).handle(rs);
        } catch (Throwable t) {
            throw new SmartDbException(t);
        } finally {
            releaseResources(rs, stmt, conn);
        }
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql, List<Object> params) {
        return queryList(cls, sql, params == null ? EMPTY_PARAM : params.toArray(new Object[params.size()]));
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, List<Object> params) {
        return paginate(handler, paginateParam, sql, params == null ? EMPTY_PARAM : params.toArray(new Object[params.size()]));
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, Object... params) {
        int pageNumber = paginateParam.getPageNumber();
        int pageSize = paginateParam.getPageSize();
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        if (pageSize < 1) {
            pageSize = 10;
        }

        Long total = 0L;
        if (paginateParam.isSearchCount()) {
            String totalRowSql = getSupportDatabaseType().getPaginateSqlGenerator().generateCountSql(pageNumber, pageSize, sql);
            total = query(ScalarHandler.Long, totalRowSql, params);
            if (total == null || total == 0) {
                return new PaginateResult<T>(pageNumber, pageSize);
            }

            int pageCount = (int) (total / pageSize);
            if (total % pageSize != 0) {
                pageCount++;
            }

            if (pageNumber > pageCount) {
                return new PaginateResult<T>(new ArrayList<T>(0), total, pageNumber, pageSize);
            }
        }

        String pageSql = getSupportDatabaseType().getPaginateSqlGenerator().generatePaginateSql(pageNumber, pageSize, sql);
        List<T> list = query(handler, pageSql, params);
        return new PaginateResult<T>(list, total, pageNumber, pageSize);
    }


    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder) {
        return paginate(handler, paginateParam, selectSqlBuilder.generateSql(false), selectSqlBuilder.getAllParams().toArray(new Object[selectSqlBuilder.getAllParams().size()]));
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, String sql, List<Object> params) {
        return paginate(cls, paginateParam, sql, params == null ? EMPTY_PARAM : params.toArray(new Object[params.size()]));
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

        Long total = 0L;
        if (paginateParam.isSearchCount()) {
            String totalRowSql = getSupportDatabaseType().getPaginateSqlGenerator().generateCountSql(pageNumber, pageSize, sql);
            total = query(ScalarHandler.Long, totalRowSql, params);
            if (total == null || total == 0) {
                return new PaginateResult<T>(pageNumber, pageSize);
            }

            int pageCount = (int) (total / pageSize);
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

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder) {
        return paginate(cls, paginateParam, selectSqlBuilder.generateSql(false), selectSqlBuilder.getAllParams().toArray(new Object[selectSqlBuilder.getAllParams().size()]));
    }

    @Override
    public <T> T insertSelective(T t) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(t.getClass());
        InsertSqlBuilder insertSqlBuilder = insertSqlBuilder();
        insertSqlBuilder.insertEntitySelective(t);
        if (classMeta.isUseGeneratedKey()) {
            return this.executeAndReturnGeneratedKey(insertSqlBuilder, t);
        } else {
            this.execute(insertSqlBuilder);
            return t;
        }
    }

    @Override
    public <T> T insert(T t) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(t.getClass());
        InsertSqlBuilder insertSqlBuilder = insertSqlBuilder();
        insertSqlBuilder.insertEntity(t);
        if (classMeta.isUseGeneratedKey()) {
            return executeAndReturnGeneratedKey(insertSqlBuilder, t);
        } else {
            execute(insertSqlBuilder);
        }
        return t;
    }


    public <T> T executeAndReturnGeneratedKey(InsertSqlBuilder insertSqlBuilder, T t) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = insertSqlBuilder.generateSql();
        List<Object> allParams = insertSqlBuilder.getAllParams();
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            SmartDbHelper.setParams(stmt, allParams);
            //show sql
            showSql(sql, allParams);
            stmt.executeUpdate();

            OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(t.getClass());
            if (!Objects.equal(SupportDatabaseType.ORACLE, this.smartDbConfig.getSupportDatabaseType()) && classMeta.isUseGeneratedKey()) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    classMeta.injectGeneratedKeys(rs, t);
                }
            }
            return t;
        } catch (Throwable throwable) {
            throw new SmartDbException(String.format("executeCommand error, sql:%s params:%s", sql, SmartDbHelper.toString(allParams)), throwable);
        } finally {
            releaseResources(rs, stmt, conn);
        }
    }


    @Override
    public <T> int updateSelective(T t) {
        UpdateSqlBuilder updateSqlBuilder = updateSqlBuilder();
        updateSqlBuilder.updateEntitySelective(t);
        return execute(updateSqlBuilder);
    }

    @Override
    public <T> int update(T t) {
        UpdateSqlBuilder updateSqlBuilder = updateSqlBuilder();
        updateSqlBuilder.updateEntity(t);
        return execute(updateSqlBuilder);
    }

    @Override
    public <T> int delete(T t) {
        DeleteSqlBuilder deleteSqlBuilder = deleteSqlBuilder();
        deleteSqlBuilder.deleteEntity(t);
        return execute(deleteSqlBuilder);
    }

    protected void showSql(String sql, Object[] params) {
        if (this.smartDbConfig.isShowSql()) {
            if (params == null || params.length == 0) {
                LOG.info("\r\n------------------------------------------------------ \r\n <<SmartDb SQL>> : {}  \r\n------------------------------------------------------ \r\n", sql);
            } else {
                List<Object> objects = Arrays.asList(params);
                LOG.info("\r\n------------------------------------------------------ \r\n <<SmartDb SQL>> : {}  \r\n <<SmartDb SQL Params>> : {} \n------------------------------------------------------ \r\n", sql, objects.toString());
            }
        }
    }

    protected void showSql(String sql, List<Object> params) {
        if (this.smartDbConfig.isShowSql()) {
            if (params == null || params.isEmpty()) {
                LOG.info("\n------------------------------------------------------ \r\n <<SmartDb SQL>> : {}  \r\n------------------------------------------------------\r\n ", sql);
            } else {
                LOG.info("\n------------------------------------------------------ \r\n <<SmartDb SQL>> : {}  \r\n <<SmartDb SQL Params>> : {} \n------------------------------------------------------\n\r ", sql, params.toString());
            }
        }
    }

    protected void showSql(String sql, Collection<Object[]> params) {
        if (this.smartDbConfig.isShowSql()) {
            if (params == null || params.isEmpty()) {
                LOG.info("\r\n------------------------------------------------------ <<SmartDb SQL>> : {}  \r\n------------------------------------------------------ \r\n ", sql);
            } else {
                LOG.info("\r\n------------------------------------------------------ <<SmartDb SQL>> : {}  \r\n------------------------------------------------------ \r\n <<SmartDb SQL Batch Params Size>> : {}  \r\n", sql, params.size());
            }
        }
    }


    public void releaseResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.error("cannot releaseConnection ResultSet", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.error("cannot releaseConnection Statement", e);
            }
        }
        smartDbDataSource.releaseConnection(conn);
    }
}

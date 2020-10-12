package net.lab1024.smartdb;


import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.datasource.OptEnum;
import net.lab1024.smartdb.datasource.SmartDbDataSource;
import net.lab1024.smartdb.mapping.handler.AbstractListHandler;
import net.lab1024.smartdb.mapping.handler.ResultSetHandler;
import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;
import net.lab1024.smartdb.pagination.PaginateParam;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.sqlbuilder.*;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 使用wrapper包装下真正的smartdb，用于相关代理以及后期扩展
 *
 * @author zhuoluodada@qq.com
 */
public class SmartDbWrapper implements SmartDb {

    protected SmartDb db;

    public SmartDbWrapper(SmartDb db) {
        this.db = db;
    }

    public SmartDbWrapper() {
    }

    void setDb(SmartDb db) {
        this.db = db;
    }


    @Override
    public SmartDbDataSource getDataSource() {
        return db.getDataSource();
    }

    @Override
    public TransactionSmartDbNode getTransaction() {
        return db.getTransaction();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return db.getConnection();
    }

    @Override
    public boolean runTransaction(TransactionRunnable transactionRunnable) {
        return db.runTransaction(transactionRunnable);
    }

    @Override
    public boolean runTransaction(TransactionRunnable transactionRunnable, int transactionLevel) {
        return db.runTransaction(transactionRunnable, transactionLevel);
    }

    @Override
    public RowConverter getRowConverter() {
        return db.getRowConverter();
    }

    @Override
    public ColumnNameConverter getColumnNameConverter() {
        return db.getColumnNameConverter();
    }

    @Override
    public TableNameConverter getTableNameConverter() {
        return db.getTableNameConverter();
    }

    @Override
    public SelectSqlBuilder selectSqlBuilder() {
        return db.selectSqlBuilder();
    }

    @Override
    public InsertSqlBuilder insertSqlBuilder() {
        return db.insertSqlBuilder();
    }

    @Override
    public UpdateSqlBuilder updateSqlBuilder() {
        return db.updateSqlBuilder();
    }

    @Override
    public DeleteSqlBuilder deleteSqlBuilder() {
        return db.deleteSqlBuilder();
    }

    @Override
    public ReplaceSqlBuilder replaceSqlBuilder() {
        return db.replaceSqlBuilder();
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return db.getSupportDatabaseType();
    }

    @Override
    public Connection getConnection(OptEnum opt) throws SQLException {
        return db.getConnection(opt);
    }

    @Override
    public <T> T insertSelective(T t) {
        return db.insertSelective(t);
    }

    @Override
    public <T> T insert(T t) {
        return db.insert(t);
    }

    @Override
    public <T> int updateSelective(T t) {
        return db.updateSelective(t);
    }

    @Override
    public <T> int update(T t) {
        return db.update(t);
    }

    @Override
    public <T> int delete(T t) {
        return db.delete(t);
    }

    @Override
    public int execute(DeleteSqlBuilder deleteSqlBuilder) {
        return db.execute(deleteSqlBuilder);
    }

    @Override
    public int execute(InsertSqlBuilder insertSqlBuilder) {
        return db.execute(insertSqlBuilder);
    }

    @Override
    public int execute(ReplaceSqlBuilder replaceSqlBuilder) {
        return db.execute(replaceSqlBuilder);
    }

    @Override
    public int execute(UpdateSqlBuilder updateSqlBuilder) {
        return db.execute(updateSqlBuilder);
    }

    @Override
    public int execute(String sql) {
        return db.execute(sql);
    }

    @Override
    public int execute(String sql, Object... params) {
        return db.execute(sql, params);
    }

    @Override
    public int execute(String sql, List<Object> params) {
        return db.execute(sql, params);
    }

    @Override
    public int[] batch(String sql, Collection<Object[]> paramsCollection) {
        return db.batch(sql, paramsCollection);
    }

    @Override
    public <T> List<T> batchInsert(List<T> ts) {
        return db.batchInsert(ts);
    }

    @Override
    public <T> List<T> batchInsertSelective(List<T> ts) {
        return db.batchInsertSelective(ts);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, SelectSqlBuilder selectSqlBuilder) {
        return db.query(handler, selectSqlBuilder);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql) {
        return db.query(handler, sql);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql, Object... params) {
        return db.query(handler, sql, params);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql, List<Object> params) {
        return db.query(handler, sql, params);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, SelectSqlBuilder selectSqlBuilder) {
        return db.queryFirst(cls, selectSqlBuilder);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql) {
        return db.queryFirst(cls, sql);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql, Object... params) {
        return db.queryFirst(cls, sql, params);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql, List<Object> params) {
        return db.queryFirst(cls, sql, params);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, SelectSqlBuilder selectSqlBuilder) {
        return db.queryList(cls, selectSqlBuilder);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql) {
        return db.queryList(cls, sql);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql, Object... params) {
        return db.queryList(cls, sql, params);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql, List<Object> params) {
        return db.queryList(cls, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, List<Object> params) {
        return db.paginate(handler, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, Object... params) {
        return db.paginate(handler, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder) {
        return db.paginate(handler, paginateParam, selectSqlBuilder);
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, String sql, List<Object> params) {
        return db.paginate(cls, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, String sql, Object... params) {
        return db.paginate(cls, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder) {
        return db.paginate(cls, paginateParam, selectSqlBuilder);
    }

    @Override
    public SmartDbNode getWriteSmartDb() {
        return db.getWriteSmartDb();
    }

    @Override
    public SmartDbNode getMaster() {
        return db.getMaster();
    }

    @Override
    public List<SmartDbNode> getSlaves() {
        return db.getSlaves();
    }

    @Override
    public SmartDbNode getSlave(int index) {
        return db.getSlave(index);
    }
}

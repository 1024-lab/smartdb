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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通用实现父类
 *
 * @author zhuoluodada@qq.com
 */
class SmartDbImpl implements SmartDb {

    /**
     * 随机获取slave库的索引
     */
    private AtomicInteger roundIndex = new AtomicInteger(-1);

    /**
     * 主库 （ 写 库 ）
     */
    private SmartDbNode master;
    /**
     * 从库 （ 读 库 ）
     */
    private List<SmartDbNode> slaves;

    /**
     * smartdb 配置信息
     */
    private SmartDbConfig smartDbConfig;

    /**
     * 是否存在 从库（ 读 库 ）
     */
    private boolean existSlave;


    public SmartDbImpl() {
    }

    public void reload(SmartDbConfig smartDbConfig, SmartDbNode master, List<SmartDbNode> slaves) {
        this.smartDbConfig = smartDbConfig;
        this.master = master;
        this.slaves = slaves;
        this.existSlave = slaves == null || slaves.isEmpty() ? false : true;
    }


    public SmartDbNode getSmartDbNode4Query() {
        if (this.existSlave) {
            int round = this.roundIndex.incrementAndGet();
            return this.slaves.get(Math.abs(round % this.slaves.size()));
        } else {
            return this.master;
        }
    }

    @Override
    public SmartDbDataSource getDataSource() {
        return master.getDataSource();
    }

    @Override
    public TransactionSmartDbNode getTransaction() {
        return master.getTransaction();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.master.getConnection();
    }

    @Override
    public boolean runTransaction(TransactionRunnable transactionRunnable) {
        return this.master.runTransaction(transactionRunnable);
    }

    @Override
    public boolean runTransaction(TransactionRunnable transactionRunnable, int transactionLevel) {
        return this.master.runTransaction(transactionRunnable, transactionLevel);
    }


    @Override
    public RowConverter getRowConverter() {
        return smartDbConfig.getRowConverter();
    }


    public SupportDatabaseType getDatabaseType() {
        return this.smartDbConfig.getSupportDatabaseType();
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
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().select(getSmartDbNode4Query());
    }

    @Override
    public InsertSqlBuilder insertSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().insert(this.master);
    }

    @Override
    public UpdateSqlBuilder updateSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().update(this.master);
    }

    @Override
    public DeleteSqlBuilder deleteSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().delete(this.master);
    }

    @Override
    public ReplaceSqlBuilder replaceSqlBuilder() {
        return this.smartDbConfig.getSupportDatabaseType().getSqlBuilderFactory().replace(this.master);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return this.smartDbConfig.getSupportDatabaseType();
    }

    @Override
    public Connection getConnection(OptEnum opt) throws SQLException {
        if (opt == OptEnum.WRITE) {
            return master.getConnection();
        } else {
            return getSmartDbNode4Query().getConnection();
        }
    }

    @Override
    public SmartDbNode getWriteSmartDb() {
        return this.master;
    }

    @Override
    public SmartDbNode getMaster() {
        return this.master;
    }

    @Override
    public List<SmartDbNode> getSlaves() {
        return this.slaves;
    }

    @Override
    public SmartDbNode getSlave(int index) {
        return this.slaves.get(index);
    }

    @Override
    public int execute(String sql) {
        return this.master.execute(sql);
    }

    @Override
    public int execute(DeleteSqlBuilder deleteSqlBuilder) {
        return this.master.execute(deleteSqlBuilder);
    }

    @Override
    public int execute(InsertSqlBuilder insertSqlBuilder) {
        return this.master.execute(insertSqlBuilder);
    }

    @Override
    public int execute(ReplaceSqlBuilder replaceSqlBuilder) {
        return this.master.execute(replaceSqlBuilder);
    }

    @Override
    public int execute(UpdateSqlBuilder updateSqlBuilder) {
        return this.master.execute(updateSqlBuilder);
    }

    @Override
    public int execute(String sql, Object... params) {
        return this.master.execute(sql, params);
    }

    @Override
    public int execute(String sql, List<Object> params) {
        return this.master.execute(sql, params);
    }

    @Override
    public int[] batch(String sql, Collection<Object[]> paramsCollection) {
        return this.master.batch(sql, paramsCollection);
    }

    @Override
    public <T> List<T> batchInsert(List<T> tList) {
        return this.master.batchInsert(tList);
    }

    @Override
    public <T> List<T> batchInsertSelective(List<T> tList) {
        return this.master.batchInsertSelective(tList);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, SelectSqlBuilder selectSqlBuilder) {
        return this.getSmartDbNode4Query().query(handler, selectSqlBuilder);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql) {
        return this.getSmartDbNode4Query().query(handler, sql);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql, Object... params) {
        return this.getSmartDbNode4Query().query(handler, sql, params);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler, String sql, List<Object> params) {
        return this.getSmartDbNode4Query().query(handler, sql, params);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, SelectSqlBuilder selectSqlBuilder) {
        return this.getSmartDbNode4Query().queryFirst(cls, selectSqlBuilder);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql) {
        return this.getSmartDbNode4Query().queryFirst(cls, sql);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql, Object... params) {
        return this.getSmartDbNode4Query().queryFirst(cls, sql, params);
    }

    @Override
    public <T> T queryFirst(Class<T> cls, String sql, List<Object> params) {
        return this.getSmartDbNode4Query().queryFirst(cls, sql, params);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, SelectSqlBuilder selectSqlBuilder) {
        return this.getSmartDbNode4Query().queryList(cls, selectSqlBuilder);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql) {
        return this.getSmartDbNode4Query().queryList(cls, sql);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql, Object... params) {
        return this.getSmartDbNode4Query().queryList(cls, sql, params);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls, String sql, List<Object> params) {
        return this.getSmartDbNode4Query().queryList(cls, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, List<Object> params) {
        return this.getSmartDbNode4Query().paginate(handler, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, Object... params) {
        return this.getSmartDbNode4Query().paginate(handler, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder) {
        return this.getSmartDbNode4Query().paginate(handler, paginateParam, selectSqlBuilder);
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, String sql, List<Object> params) {
        return this.getSmartDbNode4Query().paginate(cls, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> handler, PaginateParam paginateParam, String sql, Object... params) {
        return this.getSmartDbNode4Query().paginate(handler, paginateParam, sql, params);
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder) {
        return this.getSmartDbNode4Query().paginate(cls, paginateParam, selectSqlBuilder);
    }

    @Override
    public <T> T insertSelective(T t) {
        return this.master.insertSelective(t);
    }

    @Override
    public <T> T insert(T t) {
        return this.master.insert(t);
    }

    @Override
    public <T> int updateSelective(T t) {
        return this.master.updateSelective(t);
    }

    @Override
    public <T> int update(T t) {
        return this.master.update(t);
    }

    @Override
    public <T> int delete(T t) {
        return this.master.delete(t);
    }

}

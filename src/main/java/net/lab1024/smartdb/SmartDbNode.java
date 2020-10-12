package net.lab1024.smartdb;

import net.lab1024.smartdb.database.SupportDatabaseType;
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
 * 一个Node标识一个数据库实例节点 <br>
 * <p>
 * 多个Node组成一个SmartDb对象，即一个SmartDb表示一个集群
 *
 * @author zhuoda
 */
public interface SmartDbNode {

    /**
     * 获取数据库连接池
     *
     * @return
     */
    SmartDbDataSource getDataSource();

    /**
     * 获取带事务的db
     *
     * @return 返回一个带操作事务的db
     */
    TransactionSmartDbNode getTransaction();

    /**
     * 获取真实数据库连接
     *
     * @return 返回真实的数据连接
     * @throws SQLException
     */
    Connection getConnection() throws SQLException;

    /**
     * 执行一个事务操作
     *
     * @param transactionRunnable
     * @return 执行成功返回true, 执行失败返回false
     */
    boolean runTransaction(TransactionRunnable transactionRunnable);

    /**
     * 执行一个事务操作
     *
     * @param transactionRunnable
     * @param transactionLevel    事务级别
     * @return 执行成功返回true, 执行失败返回false
     */
    boolean runTransaction(TransactionRunnable transactionRunnable, int transactionLevel);

    /**
     * 表中的行数据转换器
     *
     * @return 表中的行数据转换器
     */
    RowConverter getRowConverter();

    /**
     * 列名转换器
     *
     * @return 表名转换器
     */
    ColumnNameConverter getColumnNameConverter();

    /**
     * 表名转换器
     *
     * @return 表名转换器
     */
    TableNameConverter getTableNameConverter();

    /**
     * 获取一个select查询语句构器
     *
     * @return select语句构器
     */
    SelectSqlBuilder selectSqlBuilder();

    /**
     * 获取一个插入insert语句构造器
     *
     * @return 插入insert语句构造器
     */
    InsertSqlBuilder insertSqlBuilder();

    /**
     * 获取一个更新update语句的构造器
     *
     * @return 更新update语句的构造器
     */
    UpdateSqlBuilder updateSqlBuilder();

    /**
     * 获取一个删除delete语句的构造器
     *
     * @return 删除delete语句的构造器
     */
    DeleteSqlBuilder deleteSqlBuilder();

    /**
     * 获取一个replace语句的构造器
     * @return
     */
    ReplaceSqlBuilder replaceSqlBuilder();

    /**
     * 获取当前的数据库类型
     *
     * @return 数据库类型
     */
    SupportDatabaseType getSupportDatabaseType();

    /**
     * ORM插入对象，排除属性为null的数据
     *
     * @param t   orm对象
     * @param <T>
     * @return 原始对象，如果原始orm对象id为自增，则也会返回
     */
    <T> T insertSelective(T t);

    /**
     * ORM插入对象(包含属性为null的数据)
     *
     * @param t
     * @param <T>
     * @return 原始对象，如果原始orm对象id为  @see UseGeneratedKey，则也会返回
     */
    <T> T insert(T t);

    /**
     * ORM更新一个对象, 去除属性为null的列
     *
     * @param t
     * @param <T>
     * @return 返回数据库受影响的行数
     */
    <T> int updateSelective(T t);

    /**
     * ORM更新一个对象(包含属性为null的列)
     *
     * @param t
     * @param <T>
     * @return 返回数据库受影响的行数
     */
    <T> int update(T t);

    /**
     * 根据ORM对象的 primary 删除对象
     *
     * @param t
     * @param <T>
     * @return 返回数据库受影响的行数
     */
    <T> int delete(T t);

    /**
     * 执行DeleteSqlBuilder构造的删除sql
     *
     * @param deleteSqlBuilder
     * @return 返回数据库受影响的行数
     */
    int execute(DeleteSqlBuilder deleteSqlBuilder);

    /**
     * 执行InsertSqlBuilder构造的插入sql
     *
     * @param insertSqlBuilder
     * @return 返回数据库受影响的行数
     */
    int execute(InsertSqlBuilder insertSqlBuilder);

    /**
     * 执行ReplaceqlBuilder构造的插入sql
     * @param replaceSqlBuilder
     * @return
     */
    int execute(ReplaceSqlBuilder replaceSqlBuilder);

    /**
     * 执行UpdateSqlBuilder构造的更新sql
     *
     * @param updateSqlBuilder
     * @return 返回数据库受影响的行数
     */
    int execute(UpdateSqlBuilder updateSqlBuilder);

    /**
     * 执行特定的sql语句
     *
     * @return 返回数据库受影响的行数
     */
    int execute(String sql);

    /**
     * 执行特定的sql语句,并传入指定参数
     *
     * @param sql
     * @param params
     * @return
     */
    int execute(String sql, Object... params);

    /**
     * 执行特定的sql语句,并传入指定参数
     *
     * @param sql
     * @param params
     * @return
     */
    int execute(String sql, List<Object> params);

    /**
     * 批量执行sql
     *
     * @param sql
     * @param paramsCollection 参数集合
     * @return an array of update counts containing one element for each
     * command in the batch.  The elements of the array are ordered according
     * to the order in which commands were added to the batch.
     */
    int[] batch(String sql, Collection<Object[]> paramsCollection);

    /**
     * 批量插入对象(包含属性为null的列)
     *
     * @param tList
     * @param <T>
     * @return 原始对象集合，如果原始orm对象id为  @see UseGeneratedKey，则也会返回
     */
    <T> List<T> batchInsert(List<T> tList);

    /**
     * 批量插入对象 ,排除属性为null的列
     *
     * @param tList
     * @param <T>
     * @return 原始对象集合，如果原始orm对象id为  @see UseGeneratedKey，则也会返回
     */
    <T> List<T> batchInsertSelective(List<T> tList);

    /**
     * 查询
     *
     * @param handler
     * @param selectSqlBuilder
     * @param <T>
     * @return
     */
    <T> T query(ResultSetHandler<T> handler, SelectSqlBuilder selectSqlBuilder);

    /**
     * 查询
     *
     * @param handler
     * @param sql
     * @param <T>
     * @return
     */
    <T> T query(ResultSetHandler<T> handler, String sql);

    /**
     * 查询
     *
     * @param sql
     * @param params
     * @param handler
     * @param <T>
     * @return
     */
    <T> T query(ResultSetHandler<T> handler, String sql, Object... params);

    /**
     * 查询
     *
     * @param handler
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> T query(ResultSetHandler<T> handler, String sql, List<Object> params);

    /**
     * 查询单个对象，如果查出来多个，则取出第一个
     *
     * @param cls              支持基本数据类型，比如int.class , Integer.class
     * @param selectSqlBuilder
     * @param <T>
     * @return
     */
    <T> T queryFirst(Class<T> cls, SelectSqlBuilder selectSqlBuilder);

    /**
     * 查询单个对象，如果查出来多个，则取出第一个
     *
     * @param cls 支持基本数据类型，比如int.class , Integer.class
     * @param sql
     * @param <T>
     * @return
     */
    <T> T queryFirst(Class<T> cls, String sql);

    /**
     * 查询单个对象，如果查出来多个，则取出第一个
     *
     * @param cls    支持基本数据类型，比如int.class , Integer.class
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> T queryFirst(Class<T> cls, String sql, Object... params);

    /**
     * 查询单个对象，如果查出来多个，则取出第一个
     *
     * @param cls    支持基本数据类型，比如int.class , Integer.class
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> T queryFirst(Class<T> cls, String sql, List<Object> params);

    /**
     * 查询列表
     *
     * @param cls              支持基本数据类型，比如int.class , Integer.class
     * @param selectSqlBuilder
     * @param <T>
     * @return
     */
    <T> List<T> queryList(Class<T> cls, SelectSqlBuilder selectSqlBuilder);

    /**
     * 查询列表
     *
     * @param cls 支持基本数据类型，比如int.class , Integer.class
     * @param sql
     * @param <T>
     * @return
     */
    <T> List<T> queryList(Class<T> cls, String sql);

    /**
     * 查询列表
     *
     * @param cls    支持基本数据类型，比如int.class , Integer.class
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> queryList(Class<T> cls, String sql, Object... params);

    /**
     * 查询列表
     *
     * @param cls    支持基本数据类型，比如int.class , Integer.class
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> queryList(Class<T> cls, String sql, List<Object> params);

    /**
     * 分页查询
     *
     * @param handler
     * @param paginateParam
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, List<Object> params);

    /**
     * 分页查询
     *
     * @param handler
     * @param paginateParam
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, String sql, Object... params);

    /**
     * 分页查询
     *
     * @param handler
     * @param paginateParam
     * @param selectSqlBuilder
     * @param <T>
     * @return
     */
    <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder);

    /**
     * 分页查询
     *
     * @param cls
     * @param paginateParam
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, String sql, List<Object> params);

    /**
     * 分页查询
     *
     * @param cls
     * @param paginateParam
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, String sql, Object... params);


    /**
     * 分页查询
     *
     * @param cls
     * @param paginateParam
     * @param selectSqlBuilder
     * @param <T>
     * @return
     */
    <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam, SelectSqlBuilder selectSqlBuilder);

}

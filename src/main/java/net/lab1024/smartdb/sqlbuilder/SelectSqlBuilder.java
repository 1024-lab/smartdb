package net.lab1024.smartdb.sqlbuilder;

import net.lab1024.smartdb.mapping.handler.AbstractListHandler;
import net.lab1024.smartdb.mapping.handler.ResultSetHandler;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.pagination.PaginateParam;
import net.lab1024.smartdb.pagination.PaginateResult;

import java.util.List;

public interface SelectSqlBuilder extends SqlBuilder {

    SelectSqlBuilder select(String column);

    SelectSqlBuilder select(String column, Object... params);

    SelectSqlBuilder from(String table);

    SelectSqlBuilder from(Class entityClass);

    SelectSqlBuilder from(Class entityClass, String aliasTableName);

    SelectSqlBuilder from(String... tables);

    SelectSqlBuilder joinInner(String joinClause);

    SelectSqlBuilder joinLeft(String joinClause);

    SelectSqlBuilder joinRight(String joinClause);

    SelectSqlBuilder joinFull(String joinClause);

    SelectSqlBuilder whereAnd(String where);

    <T> SelectSqlBuilder whereAndIn(String column, List<T> inList);

    <T> SelectSqlBuilder whereAndNotIn(String column, List<T> inList);

    SelectSqlBuilder whereAnd(String where, Object... param);

    SelectSqlBuilder whereAndLikeColumns(String like, String... column);

    SelectSqlBuilder whereAndLikePatterns(String column, String... patterns);

    SelectSqlBuilder whereOr(String where);

    SelectSqlBuilder whereOr(String where, Object... param);

    <T> SelectSqlBuilder whereOrIn(String column, List<T> inList);

    SelectSqlBuilder whereOrLikeColumns(String like, String... column);

    SelectSqlBuilder whereOrLikePatterns(String column, String... likes);

    SelectSqlBuilder groupby(String groupby);

    SelectSqlBuilder havingAnd(String condition);

    SelectSqlBuilder havingAnd(String condition, Object... param);

    SelectSqlBuilder havingOr(String condition);

    SelectSqlBuilder havingOr(String condition, Object... param);

    SelectSqlBuilder orderby(String column, boolean isAsc);

    SelectSqlBuilder orderby(String orderbyClause);

    SelectSqlBuilder limit(long offset, long rowCount);

    List<Object> getSelectParams();

    List<Object> getWhereParams();

    List<Object> getHavingParams();

    <T> T queryFirst(Class<T> cls);

    <T> T queryFirst(ScalarHandler<T> resultSetHandler);

    <T> List<T> queryList(Class<T> cls);

    <T> List<T> queryList(AbstractListHandler<T> resultSetHandler);

    <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam);

    <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam);

    <T> T query(ResultSetHandler<T> handler);

}

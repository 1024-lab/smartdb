package net.lab1024.smartdb.sqlbuilder;

import java.util.List;

public interface DeleteSqlBuilder extends SqlBuilder {

    DeleteSqlBuilder table(String table);

    DeleteSqlBuilder table(Class entityClass);

    DeleteSqlBuilder deleteEntity(Object obj);

    DeleteSqlBuilder whereAnd(String where);

    DeleteSqlBuilder whereAnd(String where, Object... param);

    <T> DeleteSqlBuilder whereAndIn(String column, List<T> inList);

    DeleteSqlBuilder whereAndLikeColumns(String like, String... columns);

    DeleteSqlBuilder whereAndLikePatterns(String column, String... patterns);

    DeleteSqlBuilder whereOr(String where);

    DeleteSqlBuilder whereOr(String where, Object... param);

    DeleteSqlBuilder whereOrLikeColumns(String like, String... columns);

    DeleteSqlBuilder whereOrLikePatterns(String column, String... likes);

    int execute();
}

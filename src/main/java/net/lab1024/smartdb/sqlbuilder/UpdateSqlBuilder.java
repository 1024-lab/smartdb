package net.lab1024.smartdb.sqlbuilder;


import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;

import java.util.List;

public interface UpdateSqlBuilder extends SqlBuilder {

    UpdateSqlBuilder table(String table);

    UpdateSqlBuilder table(Class entityClass);

    UpdateSqlBuilder updateColumn(String updateClause);

    UpdateSqlBuilder updateColumn(String column, Object param);

    UpdateSqlBuilder updateEntitySelective(Object obj);

    UpdateSqlBuilder updateEntitySelective(Object obj, ColumnNameConverter nameConverter);

    UpdateSqlBuilder updateEntity(Object obj);

    UpdateSqlBuilder updateEntity(Object obj, ColumnNameConverter nameConverter);

    UpdateSqlBuilder clearWhere();

    UpdateSqlBuilder whereAnd(String where);

    UpdateSqlBuilder whereAnd(String where, Object... param);

    UpdateSqlBuilder whereAndLikeForMultiColumn(String like, String... column);

    UpdateSqlBuilder whereAndLikeForMultiPattern(String column, String... likes);

    UpdateSqlBuilder whereAndLikeForMultiColumn(String like, PatternLocationEnum location, String... column);

    UpdateSqlBuilder whereAndLikeForMultiPattern(String column, PatternLocationEnum location, String... likes);

    UpdateSqlBuilder whereOr(String where);

    UpdateSqlBuilder whereOr(String where, Object... param);

    UpdateSqlBuilder whereOrLikeForMultiColumn(String like, String... column);

    UpdateSqlBuilder whereOrLikeForMultiPattern(String column, String... likes);

    UpdateSqlBuilder whereOrLikeForMultiColumn(String like, PatternLocationEnum location, String... column);

    UpdateSqlBuilder whereOrLikeForMultiPattern(String column, PatternLocationEnum location, String... likes);

    List<Object> getWhereParamList();

    List<Object> getUpdateParams();

    int execute();
}

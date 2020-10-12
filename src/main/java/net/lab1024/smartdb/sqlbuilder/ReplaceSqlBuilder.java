package net.lab1024.smartdb.sqlbuilder;


import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;

public interface ReplaceSqlBuilder extends SqlBuilder {

    ReplaceSqlBuilder table(String table);

    ReplaceSqlBuilder table(Class clazz);

    ReplaceSqlBuilder replaceColumn(String column, Object param);

    ReplaceSqlBuilder replaceFunctionColumn(String column, String sqlFunction);

    ReplaceSqlBuilder replaceEntitySelective(Object obj);

    ReplaceSqlBuilder replaceEntitySelective(Object obj, ColumnNameConverter nameConverter);

    ReplaceSqlBuilder replaceEntity(Object obj);

    ReplaceSqlBuilder replaceEntity(Object obj, ColumnNameConverter nameConverter);

    int execute();
}

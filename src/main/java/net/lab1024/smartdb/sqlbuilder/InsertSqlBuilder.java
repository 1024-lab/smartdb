package net.lab1024.smartdb.sqlbuilder;


import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;

public interface InsertSqlBuilder extends  SqlBuilder{

    InsertSqlBuilder table(String table);

    InsertSqlBuilder table(Class clazz);

    InsertSqlBuilder insertColumn(String column, Object param);

    InsertSqlBuilder insertFunctionColumn(String column, String sqlFunction);

    InsertSqlBuilder insertEntitySelective(Object obj);

    InsertSqlBuilder insertEntitySelective(Object obj, ColumnNameConverter nameConverter);

    InsertSqlBuilder insertEntity(Object obj);

    InsertSqlBuilder insertEntity(Object obj, ColumnNameConverter nameConverter);

    int execute();

}

package net.lab1024.smartdb.sqlbuilder;


import net.lab1024.smartdb.SmartDbNode;

public interface SqlBuilderFactory {

    InsertSqlBuilder insert(SmartDbNode smartDbNode);

    UpdateSqlBuilder update(SmartDbNode smartDbNode);

    DeleteSqlBuilder delete(SmartDbNode smartDbNode);

    SelectSqlBuilder select(SmartDbNode smartDbNode);

    ReplaceSqlBuilder replace(SmartDbNode smartDbNode);

    String wrapSpecialCharacterField(String field);

}

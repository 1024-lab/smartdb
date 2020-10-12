package net.lab1024.smartdb.sqlbuilder.impl.sqlserver;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.sqlbuilder.*;

public class SqlServerBuilderFactory implements SqlBuilderFactory {

    @Override
    public InsertSqlBuilder insert(SmartDbNode smartDbNode) {
        return new SqlServerInsertSqlBuilder(smartDbNode);
    }

    @Override
    public UpdateSqlBuilder update(SmartDbNode smartDbNode) {
        return new SqlServerUpdateSqlBuilder(smartDbNode);
    }

    @Override
    public DeleteSqlBuilder delete(SmartDbNode smartDbNode) {
        return new SqlServerDeleteSqlBuilder(smartDbNode);
    }

    @Override
    public SelectSqlBuilder select(SmartDbNode smartDbNode) {
        return new SqlServerSelectSqlBuilder(smartDbNode);
    }

    @Override
    public ReplaceSqlBuilder replace(SmartDbNode smartDbNode) {
        throw new UnsupportedOperationException("oracle cannot support replace operate");
    }

    @Override
    public String wrapSpecialCharacterField(String field) {
        StringBuilder sb = new StringBuilder();
        return sb.append(SqlserverConst.RESERVED_WORD_CHAR_PREFIX).append(field).append(SqlserverConst.RESERVED_WORD_CHAR_SUFFIX).toString();
    }
}

package net.lab1024.smartdb.sqlbuilder.impl.postgresql;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.sqlbuilder.*;

public class PostgreSqlSqlBuilderFactory implements SqlBuilderFactory {

    @Override
    public InsertSqlBuilder insert(SmartDbNode smartDb) {
        return new PostgreSqlInsertSqlBuilder(smartDb);
    }

    @Override
    public UpdateSqlBuilder update(SmartDbNode smartDb) {
        return new PostgreSqlUpdateSqlBuilder(smartDb);
    }

    @Override
    public DeleteSqlBuilder delete(SmartDbNode smartDb) {
        return new PostgreSqlDeleteSqlBuilder(smartDb);
    }

    @Override
    public SelectSqlBuilder select(SmartDbNode smartDb) {
        return new PostgreSqlSelectSqlBuilder(smartDb);
    }

    @Override
    public ReplaceSqlBuilder replace(SmartDbNode smartDbNode) {
        throw new UnsupportedOperationException("oracle cannot support replace operate");
    }

    @Override
    public String wrapSpecialCharacterField(String field) {
        StringBuilder sb = new StringBuilder();
        return sb.append(PostgreSqlConst.RESERVED_WORD_CHAR).append(field).append(PostgreSqlConst.RESERVED_WORD_CHAR).toString();
    }

}

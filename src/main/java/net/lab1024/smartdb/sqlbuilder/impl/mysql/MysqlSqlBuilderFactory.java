package net.lab1024.smartdb.sqlbuilder.impl.mysql;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.sqlbuilder.*;

public class MysqlSqlBuilderFactory implements SqlBuilderFactory {

    @Override
    public InsertSqlBuilder insert(SmartDbNode smartDb) {
        return new MysqlInsertSqlBuilder(smartDb);
    }

    @Override
    public UpdateSqlBuilder update(SmartDbNode smartDb) {
        return new MysqlUpdateSqlBuilder(smartDb);
    }

    @Override
    public DeleteSqlBuilder delete(SmartDbNode smartDb) {
        return new MysqlDeleteSqlBuilder(smartDb);
    }

    @Override
    public SelectSqlBuilder select(SmartDbNode smartDb) {
        return new MysqlSelectSqlBuilder(smartDb);
    }

    @Override
    public ReplaceSqlBuilder replace(SmartDbNode smartDbNode) {
        return new MysqlReplaceSqlBuilder(smartDbNode);
    }


    @Override
    public String wrapSpecialCharacterField(String field) {
        StringBuilder sb = new StringBuilder();
        return sb.append(MysqlConst.RESERVED_WORD_CHAR).append(field).append(MysqlConst.RESERVED_WORD_CHAR).toString();
    }

}

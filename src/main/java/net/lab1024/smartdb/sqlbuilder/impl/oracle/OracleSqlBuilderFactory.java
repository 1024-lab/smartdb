package net.lab1024.smartdb.sqlbuilder.impl.oracle;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.sqlbuilder.*;

public class OracleSqlBuilderFactory implements SqlBuilderFactory {

    @Override
    public InsertSqlBuilder insert(SmartDbNode smartDb) {
        return new OracleInsertSqlBuilder(smartDb);
    }

    @Override
    public UpdateSqlBuilder update(SmartDbNode smartDb) {
        return new OracleUpdateSqlBuilder(smartDb);
    }

    @Override
    public DeleteSqlBuilder delete(SmartDbNode smartDb) {
        return new OracleDeleteSqlBuilder(smartDb);
    }

    @Override
    public SelectSqlBuilder select(SmartDbNode smartDb) {
        return new OracleSelectSqlBuilder(smartDb);
    }

    @Override
    public ReplaceSqlBuilder replace(SmartDbNode smartDbNode) {
        throw new UnsupportedOperationException("oracle cannot support replace operate");
    }

    @Override
    public String wrapSpecialCharacterField(String field) {
        StringBuilder sb = new StringBuilder();
        return sb.append(OracleConst.RESERVED_WORD_CHAR).append(field).append(OracleConst.RESERVED_WORD_CHAR).toString();
    }
}

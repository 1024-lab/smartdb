package net.lab1024.smartdb.sqlbuilder.impl.sqlserver;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractUpdateSqlBuilder;

public class SqlServerUpdateSqlBuilder extends AbstractUpdateSqlBuilder {

    public SqlServerUpdateSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public String generateQuestionMarkPart(String column) {
        if (asName == null) {
            StringBuilder sb = new StringBuilder(column.length() + 2);
            sb.append(SqlserverConst.RESERVED_WORD_CHAR_PREFIX).append(column).append(SqlserverConst.RESERVED_WORD_CHAR_SUFFIX).append("=?");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder(column.length() + 3 + asName.length());
            sb.append(asName).append(".").append("=?");
            return sb.toString();
        }
    }

    @Override
    public String generateSetNullPart(String column) {
        if (asName == null) {
            StringBuilder sb = new StringBuilder(column.length() + 6);
            sb.append(SqlserverConst.RESERVED_WORD_CHAR_PREFIX).append(column).append(SqlserverConst.RESERVED_WORD_CHAR_SUFFIX).append("= NULL");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder(column.length() + 7 + asName.length());
            sb.append(asName).append(".").append(column).append("= NULL");
            return sb.toString();
        }
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.SQL_SERVER;
    }
}

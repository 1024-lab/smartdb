package net.lab1024.smartdb.sqlbuilder.impl.oracle;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractUpdateSqlBuilder;

public class OracleUpdateSqlBuilder extends AbstractUpdateSqlBuilder {

    public OracleUpdateSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.ORACLE;
    }

    @Override
    public String generateQuestionMarkPart(String column) {
        if (asName == null) {
            StringBuilder sb = new StringBuilder(column.length() + 2);
            sb.append(OracleConst.RESERVED_WORD_CHAR).append(column).append(OracleConst.RESERVED_WORD_CHAR).append("=?");
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
            sb.append(OracleConst.RESERVED_WORD_CHAR).append(column).append(OracleConst.RESERVED_WORD_CHAR).append("= NULL");
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder(column.length() + 7 + asName.length());
            sb.append(asName).append(".").append(column).append("= NULL");
            return sb.toString();
        }
    }
}

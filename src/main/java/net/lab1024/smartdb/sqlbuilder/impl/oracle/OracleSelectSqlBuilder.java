package net.lab1024.smartdb.sqlbuilder.impl.oracle;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractSelectSqlBuilder;

public class OracleSelectSqlBuilder extends AbstractSelectSqlBuilder {

    public OracleSelectSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.ORACLE;
    }


}

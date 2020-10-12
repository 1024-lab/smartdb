package net.lab1024.smartdb.sqlbuilder.impl.oracle;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractDeleteSqlBuilder;

public class OracleDeleteSqlBuilder extends AbstractDeleteSqlBuilder {

    public OracleDeleteSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.ORACLE;
    }
}

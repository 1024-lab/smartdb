package net.lab1024.smartdb.sqlbuilder.impl.sqlserver;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractSelectSqlBuilder;

public class SqlServerSelectSqlBuilder extends AbstractSelectSqlBuilder {

    public SqlServerSelectSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.SQL_SERVER;
    }
}

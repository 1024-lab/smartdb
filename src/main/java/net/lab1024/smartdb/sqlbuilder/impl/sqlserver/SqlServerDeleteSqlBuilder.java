package net.lab1024.smartdb.sqlbuilder.impl.sqlserver;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractDeleteSqlBuilder;

public class SqlServerDeleteSqlBuilder extends AbstractDeleteSqlBuilder {

    public SqlServerDeleteSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.SQL_SERVER;
    }
}

package net.lab1024.smartdb.sqlbuilder.impl.mysql;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractDeleteSqlBuilder;

public class MysqlDeleteSqlBuilder extends AbstractDeleteSqlBuilder {

    public MysqlDeleteSqlBuilder(SmartDbNode smartDbNode) {  super(smartDbNode); }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.MYSQL;
    }
}

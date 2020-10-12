package net.lab1024.smartdb.sqlbuilder.impl.mysql;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractSelectSqlBuilder;

public class MysqlSelectSqlBuilder extends AbstractSelectSqlBuilder {

    public MysqlSelectSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.MYSQL;
    }

}
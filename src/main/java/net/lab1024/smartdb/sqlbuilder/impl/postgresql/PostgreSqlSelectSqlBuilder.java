package net.lab1024.smartdb.sqlbuilder.impl.postgresql;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractSelectSqlBuilder;

public class PostgreSqlSelectSqlBuilder extends AbstractSelectSqlBuilder {

    public PostgreSqlSelectSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.POSTGRE_SQL;
    }

}

package net.lab1024.smartdb.sqlbuilder.impl.postgresql;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.impl.AbstractDeleteSqlBuilder;

public class PostgreSqlDeleteSqlBuilder extends AbstractDeleteSqlBuilder {

    public PostgreSqlDeleteSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.POSTGRE_SQL;
    }

}

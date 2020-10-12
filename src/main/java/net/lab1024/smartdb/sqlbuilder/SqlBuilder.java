package net.lab1024.smartdb.sqlbuilder;


import net.lab1024.smartdb.database.DatabaseType;

import java.util.List;

public interface SqlBuilder extends DatabaseType {

    SqlBuilderType getSqlBuilderType();

    String generateSql();

    String generateSql(boolean isPretty);

    List<Object> getAllParams();

    <T extends SqlBuilder> T appendSql(String sqlClause);
}

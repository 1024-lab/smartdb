package net.lab1024.smartdb.sqlbuilder;

import java.util.concurrent.ConcurrentHashMap;

public class SqlBuilderType {
    private static final ConcurrentHashMap<String, SqlBuilderType> SqlBuilderTypes = new ConcurrentHashMap<String, SqlBuilderType>(10);

    public static final SqlBuilderType INSERT = new SqlBuilderType("insert");
    public static final SqlBuilderType DELETE = new SqlBuilderType("delete");
    public static final SqlBuilderType SELECT = new SqlBuilderType("select");
    public static final SqlBuilderType UPDATE = new SqlBuilderType("update");
    public static final SqlBuilderType REPLACE = new SqlBuilderType("replace");

    protected final String sqlBuildType;

    public SqlBuilderType(String sqlBuildType) {
        this.sqlBuildType = sqlBuildType;
        SqlBuilderType previousType = SqlBuilderTypes.putIfAbsent(this.sqlBuildType, this);
        if (previousType != null) {
            throw new IllegalArgumentException("duplicate database type , " + this.sqlBuildType);
        }
    }

    public String getSqlBuildType() {
        return sqlBuildType;
    }

    public static SqlBuilderType getSqlBuildType(String type) {
        return SqlBuilderTypes.get(type);
    }
}

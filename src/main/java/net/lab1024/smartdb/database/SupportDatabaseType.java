package net.lab1024.smartdb.database;


import net.lab1024.smartdb.codegenerator.*;
import net.lab1024.smartdb.impl.MysqlSmartDbNodeImpl;
import net.lab1024.smartdb.impl.OracleSmartDbImpl;
import net.lab1024.smartdb.impl.PostgreSqlSmartDbNodeImpl;
import net.lab1024.smartdb.impl.SqlServerSmartDbImpl;
import net.lab1024.smartdb.pagination.PaginateSqlGenerator;
import net.lab1024.smartdb.pagination.dbstyle.MysqlPaginateSqlGen;
import net.lab1024.smartdb.pagination.dbstyle.OraclePaginateSqlGen;
import net.lab1024.smartdb.pagination.dbstyle.PostgreSqlPaginateSqlGen;
import net.lab1024.smartdb.pagination.dbstyle.SqlServerlPaginateSqlGen;
import net.lab1024.smartdb.sqlbuilder.SqlBuilderFactory;
import net.lab1024.smartdb.sqlbuilder.impl.mysql.MysqlSqlBuilderFactory;
import net.lab1024.smartdb.sqlbuilder.impl.oracle.OracleSqlBuilderFactory;
import net.lab1024.smartdb.sqlbuilder.impl.postgresql.PostgreSqlSqlBuilderFactory;
import net.lab1024.smartdb.sqlbuilder.impl.sqlserver.SqlServerBuilderFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持的数据库类型<br>
 * 如需要扩展自定义的实现，可以继承此类
 * @author  zhuoluodada
 */
public class SupportDatabaseType {

    private static final ConcurrentHashMap<String, SupportDatabaseType> DatabaseTypes = new ConcurrentHashMap<String, SupportDatabaseType>(10);

    public static final SupportDatabaseType MYSQL = new SupportDatabaseType("mysql", MysqlSmartDbNodeImpl.class, MysqlPaginateSqlGen.INSTANCE, new MysqlSqlBuilderFactory(), MysqlEntityGenerator.class);
    public static final SupportDatabaseType SQL_SERVER = new SupportDatabaseType("sqlserver", SqlServerSmartDbImpl.class, SqlServerlPaginateSqlGen.INSTANCE, new SqlServerBuilderFactory(), SqlServerEntityGenerator.class);
    public static final SupportDatabaseType ORACLE = new SupportDatabaseType("oracle", OracleSmartDbImpl.class, OraclePaginateSqlGen.INSTANCE, new OracleSqlBuilderFactory(), OracleEntityGenerator.class);
    public static final SupportDatabaseType POSTGRE_SQL = new SupportDatabaseType("postgresql", PostgreSqlSmartDbNodeImpl.class, PostgreSqlPaginateSqlGen.INSTANCE, new PostgreSqlSqlBuilderFactory(), PostgreSqlEntityGenerator.class);

    protected final String databaseType;
    protected final Class smartDbNodeImplClass;
    protected final PaginateSqlGenerator paginateSqlGen;
    protected final SqlBuilderFactory sqlBuilderFactory;
    protected final Class smartDbEntityGeneratorClass;

    public SupportDatabaseType(String databaseType, Class<?> cls, PaginateSqlGenerator paginateSqlGen, SqlBuilderFactory sqlBuilderFactory, Class smartDbEntityGeneratorClass) {
        //TODO check class must be extends AbstractSmartDb
        this.databaseType = databaseType;
        SupportDatabaseType previousType = DatabaseTypes.putIfAbsent(this.databaseType.toLowerCase(), this);
        if (previousType != null) {
            throw new IllegalArgumentException("duplicate database type , " + this.databaseType);
        }
        this.smartDbNodeImplClass = cls;
        if (paginateSqlGen == null) {
            throw new IllegalArgumentException("PaginateSqlGenerator cannot be null  ");
        }
        this.paginateSqlGen = paginateSqlGen;
        if (sqlBuilderFactory == null) {
            throw new IllegalArgumentException("sqlBuilderFactory cannot be null  ");
        }
        this.smartDbEntityGeneratorClass = smartDbEntityGeneratorClass;
        if (smartDbEntityGeneratorClass == null) {
            throw new IllegalArgumentException("smartDbEntityGeneratorClass cannot be null  ");
        }
        if (!SmartDbEntityGenerator.class.isAssignableFrom(this.smartDbEntityGeneratorClass)) {
            throw new IllegalArgumentException(this.smartDbEntityGeneratorClass.getSimpleName() + " must extends SmartDbEntityGenerator  ");
        }
        this.sqlBuilderFactory = sqlBuilderFactory;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public PaginateSqlGenerator getPaginateSqlGenerator() {
        return paginateSqlGen;
    }

    public Class getSmartDbNodeImplClass() {
        return smartDbNodeImplClass;
    }

    public Class getSmartDbEntityGeneratorClass() {
        return smartDbEntityGeneratorClass;
    }

    public static SupportDatabaseType getDatabaseType(String dbType) {
        return DatabaseTypes.get(dbType.toLowerCase());
    }

    public SqlBuilderFactory getSqlBuilderFactory() {
        return sqlBuilderFactory;
    }
}

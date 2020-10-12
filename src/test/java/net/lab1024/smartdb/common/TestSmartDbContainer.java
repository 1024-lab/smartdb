package net.lab1024.smartdb.common;

import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.CaseFormat;
import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.SmartDbBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class TestSmartDbContainer {
    protected static DruidDataSource sqlserverDruidDataSource = null;
    protected static SmartDb sqlserver = null;
    protected static DruidDataSource mysqlDruidDataSource = null;
    protected static SmartDb mysql = null;
    protected static DruidDataSource postgreDruidDataSource = null;
    protected static SmartDb postgresql = null;
    protected static DruidDataSource oracleDruidDataSource = null;
    protected static SmartDb oracle = null;

    public static void stop() {
        if (sqlserverDruidDataSource != null) {
            sqlserverDruidDataSource.close();
        }
        if (mysqlDruidDataSource != null) {
            mysqlDruidDataSource.close();
        }
        if (postgreDruidDataSource != null) {
            postgreDruidDataSource.close();
        }
        if (oracleDruidDataSource != null) {
            oracleDruidDataSource.close();
        }
    }

    public static SmartDb getMysql() {
        return mysql;
    }

    public static SmartDb getSqlserver() {
        return sqlserver;
    }

    public static SmartDb getPostgresql() {
        return postgresql;
    }

    public static SmartDb getOracle() {
        return oracle;
    }

    public static void init() {
        try {
            /**
             * init sqlserver
             */
            Properties props = new Properties();
            props.load(TestSmartDbContainer.class.getClassLoader().getResourceAsStream("sqlserver.properties"));

            sqlserverDruidDataSource = new DruidDataSource();
            sqlserverDruidDataSource.setDriverClassName(props.getProperty("driverClassName"));
            sqlserverDruidDataSource.setUrl(props.getProperty("jdbcUrl"));
            sqlserverDruidDataSource.setUsername(props.getProperty("username"));
            sqlserverDruidDataSource.setPassword(props.getProperty("password"));
            sqlserverDruidDataSource.setInitialSize(5);
            sqlserverDruidDataSource.setMinIdle(1);
            sqlserverDruidDataSource.setMaxActive(10);
            sqlserverDruidDataSource.init();

            sqlserver = new SmartDbBuilder()//
                    .setMasterDataSource(sqlserverDruidDataSource)//
                    .setColumnNameConverter(new ColumnNameConverter() {
                        @Override
                        public String columnConvertToField(String column) {
                            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
                        }

                        @Override
                        public String fieldConvertToColumn(String filed) {
                            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filed);
                        }
                    })
                    .setShowSql(true)//
                    .setSupportDatabaseType(SupportDatabaseType.SQL_SERVER)//
                    .setTableNameConverter(new TableNameConverter() {
                        @Override
                        public String classToTableName(Class<?> cls) {
                            return "t_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
                        }
                    }).build();

            /**
             * init mysql
             */
            props = new Properties();
            props.load(TestSmartDbContainer.class.getClassLoader().getResourceAsStream("mysql.properties"));

            mysqlDruidDataSource = new DruidDataSource();
            mysqlDruidDataSource.setDriverClassName(props.getProperty("driverClassName"));
            mysqlDruidDataSource.setUrl(props.getProperty("jdbcUrl"));
            mysqlDruidDataSource.setUsername(props.getProperty("username"));
            mysqlDruidDataSource.setPassword(props.getProperty("password"));
            mysqlDruidDataSource.setInitialSize(5);
            mysqlDruidDataSource.setMinIdle(1);
            mysqlDruidDataSource.setMaxActive(10);
            mysqlDruidDataSource.init();

            mysql = new SmartDbBuilder()//
                    .setMasterDataSource(mysqlDruidDataSource)//
                    .setColumnNameConverter(new ColumnNameConverter() {
                        @Override
                        public String columnConvertToField(String column) {
                            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
                        }

                        @Override
                        public String fieldConvertToColumn(String filed) {
                            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filed);
                        }
                    })
                    .setShowSql(true)//
                    .setSupportDatabaseType(SupportDatabaseType.MYSQL)//
                    .setTableNameConverter(new TableNameConverter() {
                        @Override
                        public String classToTableName(Class<?> cls) {
                            return "t_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
                        }
                    }).build();
//

            /**
             * init postgre
             */
            props = new Properties();
            props.load(TestSmartDbContainer.class.getClassLoader().getResourceAsStream("postgresql.properties"));

            postgreDruidDataSource = new DruidDataSource();
            postgreDruidDataSource.setDriverClassName(props.getProperty("driverClassName"));
            postgreDruidDataSource.setUrl(props.getProperty("jdbcUrl"));
            postgreDruidDataSource.setUsername(props.getProperty("username"));
            postgreDruidDataSource.setPassword(props.getProperty("password"));
            postgreDruidDataSource.setInitialSize(5);
            postgreDruidDataSource.setMinIdle(1);
            postgreDruidDataSource.setMaxActive(10);
            postgreDruidDataSource.init();

            postgresql = new SmartDbBuilder()//
                    .setMasterDataSource(postgreDruidDataSource)//
                    .setColumnNameConverter(new ColumnNameConverter() {
                        @Override
                        public String columnConvertToField(String column) {
                            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
                        }

                        @Override
                        public String fieldConvertToColumn(String filed) {
                            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filed);
                        }
                    })
                    .setShowSql(true)//
                    .setSupportDatabaseType(SupportDatabaseType.POSTGRE_SQL)//
                    .setTableNameConverter(new TableNameConverter() {
                        @Override
                        public String classToTableName(Class<?> cls) {
                            return "t_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
                        }
                    }).build();

            /**
             * init oracle
             */
            props = new Properties();
            props.load(TestSmartDbContainer.class.getClassLoader().getResourceAsStream("oracle.properties"));

            oracleDruidDataSource = new DruidDataSource();
            oracleDruidDataSource.setDriverClassName(props.getProperty("driverClassName"));
            oracleDruidDataSource.setUrl(props.getProperty("jdbcUrl"));
            oracleDruidDataSource.setUsername(props.getProperty("username"));
            oracleDruidDataSource.setPassword(props.getProperty("password"));
            oracleDruidDataSource.setInitialSize(5);
            oracleDruidDataSource.setMinIdle(1);
            oracleDruidDataSource.setMaxActive(10);
            oracleDruidDataSource.init();

            oracle = new SmartDbBuilder()//
                    .setMasterDataSource(oracleDruidDataSource)//
                    .setColumnNameConverter(new ColumnNameConverter() {
                        @Override
                        public String columnConvertToField(String column) {
                            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
                        }

                        @Override
                        public String fieldConvertToColumn(String filed) {
                            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filed);
                        }
                    })
                    .setShowSql(true)//
                    .setSupportDatabaseType(SupportDatabaseType.ORACLE)//
                    .setTableNameConverter(new TableNameConverter() {
                        @Override
                        public String classToTableName(Class<?> cls) {
                            return "t_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
                        }
                    }).build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

package net.lab1024.smartdb;

import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.datasource.SmartDbDataSource;
import net.lab1024.smartdb.datasource.SmartDbDataSourceImpl;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.ext.SmartDbExtEnum;
import net.lab1024.smartdb.ext.spring.SmartDbDataSource4Spring;
import net.lab1024.smartdb.mapping.rowconvertor.CaseFormatRowConverter;
import net.lab1024.smartdb.mapping.rowconvertor.DefaultRowConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.DefaultColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;

/**
 * 构造SmartDb的工厂
 *
 * @author zhuoluodada@qq.com
 */
public class SmartDbFactory {

    protected static Logger LOG = LoggerFactory.getLogger(SmartDbFactory.class);

    public static SmartDb build(SmartDbBuilder builder) {
        //validate
        validate(builder);
        //create proxy
        SmartDbProxy proxy = proxy(builder);

        SmartDbWrapper smartDbWrapper = new SmartDbWrapper();
        smartDbWrapper.setDb(proxy.getInstance());

        return smartDbWrapper;
    }

    private static void validate(SmartDbBuilder builder) {
        if (builder.getSupportDatabaseType() == null) {
            throw new IllegalArgumentException("SupportDatabaseType cannot be null");
        }

        boolean isMasterDatasourceIsNull = false;
        if (builder.getMasterDataSource() == null) {
            isMasterDatasourceIsNull = true;
        }

        boolean isConnectInfoIsNull = false;
        if (builder.getUrl() == null && builder.getPassword() == null && builder.getUsername() == null && builder.getDriverClassName() == null) {
            isConnectInfoIsNull = true;
        }

        if (isConnectInfoIsNull && isMasterDatasourceIsNull) {
            throw new IllegalArgumentException("MasterDataSource cannot be null or connect info cannot be null");
        }

        if (builder.getSlaveDataSource() != null) {
            for (DataSource dataSource : builder.getSlaveDataSource()) {
                if (dataSource == null) {
                    throw new IllegalArgumentException("SlaveDataSource cannot be null");
                }
            }
        }

        ColumnNameConverter columnNameConverter = builder.getColumnNameConverter();
        if (columnNameConverter == null) {
            columnNameConverter = new DefaultColumnNameConverter();
            builder.setColumnNameConverter(columnNameConverter);
        }

        TableNameConverter tableNameConverter = builder.getTableNameConverter();
        if (tableNameConverter == null) {
            tableNameConverter = new TableNameConverter() {
                @Override
                public String classToTableName(Class<?> cls) {
                    return cls.getSimpleName();
                }
            };
            builder.setTableNameConverter(tableNameConverter);
        }

        Class smartDbImplClass = builder.getSupportDatabaseType().getSmartDbNodeImplClass();
        if (smartDbImplClass == null) {
            throw new IllegalArgumentException("SmartDbImplClass cannot be null");
        }
    }

    private static SmartDbNode buildSmartDbNode(SmartDbConfig smartDbConfig, SmartDbDataSource smartDbDataSource) {
        Class smartDbImplClass = smartDbConfig.getSupportDatabaseType().getSmartDbNodeImplClass();
        try {
            Constructor constructor = smartDbImplClass.getConstructor(SmartDbDataSource.class, SmartDbConfig.class);
            SmartDbNode dbNode = (SmartDbNode) constructor.newInstance(smartDbDataSource, smartDbConfig);
            return dbNode;
        } catch (Throwable e) {
            throw new SmartDbException(e);
        }
    }


    private static SmartDbProxy proxy(SmartDbBuilder builder) {
        if (builder.getRowConverter() == null) {
            builder.setRowConverter(new CaseFormatRowConverter(builder.getColumnNameConverter()));
        } else {
            builder.setRowConverter(builder.getRowConverter());
        }
        //connect pool
        DataSource masterDataSource = builder.getMasterDataSource();
        if (masterDataSource == null) {
            masterDataSource = new InnerDataSource(builder);
        }

        //主库
        SmartDbDataSource master = null;
        SmartDbExtEnum extEnum = builder.getSmartDbExtEnum();
        if (extEnum == null) {
            master = new SmartDbDataSourceImpl(masterDataSource);
        } else if (extEnum == SmartDbExtEnum.SPRING4) {
            master = new SmartDbDataSource4Spring(masterDataSource);
        } else if (extEnum == SmartDbExtEnum.SPRING5) {
            master = new SmartDbDataSource4Spring(masterDataSource);
        } else {
            master = new SmartDbDataSourceImpl(masterDataSource);
        }

        SmartDbNode masterNode = buildSmartDbNode(builder, master);

        //从库
        List<SmartDbNode> slaves = new ArrayList<>();
        DataSource[] slaveDataSourceArray = builder.getSlaveDataSource();
        if (slaveDataSourceArray != null) {
            for (DataSource slaveDataSource : slaveDataSourceArray) {
                slaves.add(buildSmartDbNode(builder, new SmartDbDataSourceImpl(slaveDataSource)));
            }
        }

        //create smart db impl
        SmartDbImpl db = new SmartDbImpl();
        db.reload(builder, masterNode, slaves);
        return new SmartDbProxy(db, builder.getFilters());

    }

    private static class InnerDataSource implements DataSource {

        private String url;
        private String driverClassName;
        private String username;
        private String password;

        public InnerDataSource(SmartDbBuilder smartDbBuilder) {
            this.url = smartDbBuilder.getUrl();
            this.driverClassName = smartDbBuilder.getDriverClassName();
            this.username = smartDbBuilder.getUsername();
            this.password = smartDbBuilder.getPassword();
        }

        @Override
        public Connection getConnection() throws SQLException {
            try {
                Class.forName(this.driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SmartDbException(e);
            }
            return DriverManager.getConnection(this.url, this.username, this.password);
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            try {
                Class.forName(this.driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SmartDbException(e);
            }
            return DriverManager.getConnection(this.url, username, password);
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }


}

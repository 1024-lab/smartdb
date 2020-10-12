package net.lab1024.smartdb;


import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.ext.SmartDbExtEnum;
import net.lab1024.smartdb.filter.SmartDbFilter;
import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;

import javax.sql.DataSource;
import java.util.List;

/**
 * smartdb的构建 builder
 *
 * @author zhuoluodada@qq.com
 */
public class SmartDbBuilder extends SmartDbConfig {

    /**
     * 简单的数据库配置
     */
    protected String url;
    protected String username;
    protected String password;
    protected String driverClassName;

    private DataSource masterDataSource;
    private DataSource[] slaveDataSource;

    public SmartDb build() {
        return SmartDbFactory.build(this);
    }

    public static SmartDbBuilder create() {
        return new SmartDbBuilder();
    }

    public SmartDbBuilder setShowSql(boolean showSql) {
        this.showSql = showSql;
        return this;
    }

    /**
     * 表名转换器
     *
     * @param tableNameConverter
     * @return
     */
    public SmartDbBuilder setTableNameConverter(TableNameConverter tableNameConverter) {
        this.tableNameConverter = tableNameConverter;
        return this;
    }

    public SmartDbBuilder addSmartDbFilter(SmartDbFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("filter cannot be null");
        }

        if (filters.contains(filter)) {
            throw new IllegalArgumentException("exist filter " + filter);
        }

        filters.add(filter);
        return this;
    }


    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public DataSource[] getSlaveDataSource() {
        return slaveDataSource;
    }

    public void setFilters(List<SmartDbFilter> filters) {
        this.filters = filters;
    }


    /**
     * 设置smartdb的扩展支持<br>
     * 支持Spring,JFinal
     *
     * @param ext
     * @return
     */
    public SmartDbBuilder setSmartDbExtEnum(SmartDbExtEnum ext) {
        this.smartDbExtEnum = ext;
        return this;
    }

    /**
     * 设置数据库类型
     *
     * @param sqlType
     * @return
     */
    public SmartDbBuilder setSupportDatabaseType(SupportDatabaseType sqlType) {
        if (sqlType == null) {
            throw new IllegalArgumentException("sqltype cannot be null");
        }
        this.supportDatabaseType = sqlType;
        return this;
    }

    /**
     * 设置 master数据源
     *
     * @param ds
     * @return
     */
    public SmartDbBuilder setMasterDataSource(DataSource ds) {
        this.masterDataSource = ds;
        return this;
    }

    /**
     * 设置 从 数据源 集群
     *
     * @param ds
     * @return
     */
    public SmartDbBuilder setSlaveDataSource(DataSource... ds) {
        this.slaveDataSource = ds;
        return this;
    }

    /**
     * 设置 表的列表和类的属性名转换器
     *
     * @param columnNameConverter
     * @return
     */
    public SmartDbBuilder setColumnNameConverter(ColumnNameConverter columnNameConverter) {
        this.columnNameConverter = columnNameConverter;
        return this;
    }

    /**
     * 设置orm转换器
     *
     * @param rowConverter
     * @return
     */
    public SmartDbBuilder setRowConverter(RowConverter rowConverter) {
        this.rowConverter = rowConverter;
        return this;
    }

    public SmartDbBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public SmartDbBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public SmartDbBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public SmartDbBuilder setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}

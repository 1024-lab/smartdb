package net.lab1024.smartdb.codegenerator;

import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.exception.SmartDbException;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.Objects;

public class SmartDbEntityGeneratorBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(SmartDbEntityGeneratorBuilder.class);

    protected String driverClassName;
    protected String username;
    protected String password;
    protected String jdbcUrl;

    protected String tableName;
    protected boolean isComment;
    protected String packageName;
    protected String className;

    protected  boolean isGenerateGetterSetter;

    protected SupportDatabaseType supportDatabaseType;

    /**
     * 生产字段为 java 8种基本数据类型。 比如int , double , boolean
     */
    protected boolean isFieldBasicType = false;

    /**
     * 目录
     */
    protected String entityClassFileDir;

    protected CaseFormat tableColumnCaseFormat;
    protected CaseFormat entityFieldCaseFormat;


    SupportDatabaseType getSupportDatabaseType() {
        return supportDatabaseType;
    }

    public void build() {
        Objects.requireNonNull(this.supportDatabaseType);

        Class smartDbEntityGeneratorClass = this.supportDatabaseType.getSmartDbEntityGeneratorClass();
        try {
            Constructor constructor = smartDbEntityGeneratorClass.getConstructor(this.getClass());
            constructor.setAccessible(true);
            SmartDbEntityGenerator smartDbEntityGenerator = (SmartDbEntityGenerator) constructor.newInstance(this);
            smartDbEntityGenerator.generate();
        } catch (Exception e) {
            throw new SmartDbException(e);
        }
    }

    public SmartDbEntityGeneratorBuilder setSupportDatabaseType(SupportDatabaseType supportDatabaseType) {
        this.supportDatabaseType = supportDatabaseType;
        return this;
    }

    public boolean isFieldBasicType() {
        return isFieldBasicType;
    }

    /**
     * 生产字段为 java 8种基本数据类型。 比如int , double , boolean
     */
    public SmartDbEntityGeneratorBuilder setFieldBasicType(boolean fieldBasicType) {
        isFieldBasicType = fieldBasicType;
        return this;
    }

    public SmartDbEntityGeneratorBuilder setGenerateGetterSetter(boolean generateGetterSetter) {
        isGenerateGetterSetter = generateGetterSetter;
        return this;
    }

    public String getEntityClassFileDir() {
        return entityClassFileDir;
    }

    public SmartDbEntityGeneratorBuilder setEntityClassFileDir(String entityClassFileDir) {
        this.entityClassFileDir = entityClassFileDir;
        return this;
    }

    public CaseFormat getTableColumnCaseFormat() {
        return tableColumnCaseFormat;
    }

    public SmartDbEntityGeneratorBuilder setTableColumnCaseFormat(CaseFormat tableColumnCaseFormat) {
        this.tableColumnCaseFormat = tableColumnCaseFormat;
        return this;
    }

    public CaseFormat getEntityFieldCaseFormat() {
        return entityFieldCaseFormat;
    }

    public SmartDbEntityGeneratorBuilder setEntityFieldCaseFormat(CaseFormat entityFieldCaseFormat) {
        this.entityFieldCaseFormat = entityFieldCaseFormat;
        return this;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public SmartDbEntityGeneratorBuilder setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SmartDbEntityGeneratorBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SmartDbEntityGeneratorBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public SmartDbEntityGeneratorBuilder setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public SmartDbEntityGeneratorBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public boolean isComment() {
        return isComment;
    }

    public SmartDbEntityGeneratorBuilder setComment(boolean comment) {
        isComment = comment;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public SmartDbEntityGeneratorBuilder setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public SmartDbEntityGeneratorBuilder setClassName(String className) {
        this.className = className;
        return this;
    }

    public Connection getConnection() throws Exception {
        Class.forName(driverClassName);
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public boolean isGenerateGetterSetter() {
        return isGenerateGetterSetter;
    }

    protected void releaseResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {

            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.error("cannot releaseConnection Statement", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("cannot releaseConnection conn", e);
            }
        }
    }

}

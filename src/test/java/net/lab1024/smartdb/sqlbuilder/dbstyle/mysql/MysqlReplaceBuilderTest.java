package net.lab1024.smartdb.sqlbuilder.dbstyle.mysql;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.SmartDbBuilder;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.convertor.CaseFormatColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import net.lab1024.smartdb.sqlbuilder.insert.InsertSqlBuilderEntity;
import net.lab1024.smartdb.sqlbuilder.select.BaseSelectBuilderTest;
import com.google.common.base.CaseFormat;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MysqlReplaceBuilderTest extends BaseSelectBuilderTest {

    protected static final String INSERT_BUILD_TABLE_NAME = "t_insert_sql_builder";
    protected static final List<InsertSqlBuilderEntity> InsertEntityList = new ArrayList<InsertSqlBuilderEntity>();
    private static InsertSqlBuilderEntity InsertEntity = new InsertSqlBuilderEntity();
    private static InsertSqlBuilderEntity InsertEntitySelective = new InsertSqlBuilderEntity();

    @Override
    public SmartDb getDb() {
        SmartDb smartDb = SmartDbBuilder.create()
                .setUrl(mysqlProp.getProperty("jdbcUrl"))
                .setDriverClassName(mysqlProp.getProperty("driverClassName"))
                .setUsername(mysqlProp.getProperty("username"))
                .setPassword(mysqlProp.getProperty("password"))
                .setShowSql(true)
                .setColumnNameConverter(new CaseFormatColumnNameConverter(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_UNDERSCORE))
                .setSupportDatabaseType(SupportDatabaseType.MYSQL)
                .setTableNameConverter(new TableNameConverter() {
                    @Override
                    public String classToTableName(Class<?> cls) {
                        return "t_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
                    }
                })
                .build();
        return smartDb;
    }

    @Test
    public void test() throws ParseException {

        //column
        clearData();
        testColumn();

        //entity
        clearData();
        testReplaceEntity();

        //entity selective
        clearData();
        testInsertEntitySelective();

    }

    private void testInsertEntitySelective() {
        int rows = getDb().replaceSqlBuilder()
                .table(INSERT_BUILD_TABLE_NAME)
                .replaceEntitySelective(InsertEntitySelective)
                .execute();
        Assert.assertEquals(1, rows);

        InsertSqlBuilderEntity insertSqlBuilderEntity = getDb().selectSqlBuilder()
                .select("*")
                .from(INSERT_BUILD_TABLE_NAME)
                .whereAnd("id = ?", InsertEntitySelective.getId())
                .queryFirst(InsertSqlBuilderEntity.class);

        Assert.assertEquals(InsertEntitySelective.getId(), insertSqlBuilderEntity.getId());
        Assert.assertNull(insertSqlBuilderEntity.getKey());
        Assert.assertNull(insertSqlBuilderEntity.getBalance());
        Assert.assertNotNull(insertSqlBuilderEntity.getCreateTime());
    }

    private void testReplaceEntity() {
        int rows = getDb().replaceSqlBuilder()
                .table(INSERT_BUILD_TABLE_NAME)
                .replaceEntity(InsertEntity)
                .execute();
        Assert.assertEquals(1, rows);

        InsertSqlBuilderEntity insertSqlBuilderEntity = getDb().selectSqlBuilder()
                .select("*")
                .from(INSERT_BUILD_TABLE_NAME)
                .whereAnd("id = ?", InsertEntity.getId())
                .queryFirst(InsertSqlBuilderEntity.class);

        Assert.assertEquals(InsertEntity.getId(), insertSqlBuilderEntity.getId());
        Assert.assertEquals(InsertEntity.getBalance(), insertSqlBuilderEntity.getBalance());
        Assert.assertEquals(InsertEntity.getKey(), insertSqlBuilderEntity.getKey());
    }

    private void testColumn() {
        int rows = getDb().replaceSqlBuilder()
                .table(INSERT_BUILD_TABLE_NAME)
                .replaceColumn("id", InsertEntity.getId())
                .replaceColumn("key", InsertEntity.getKey())
                .replaceFunctionColumn("create_time", "now()")
                .replaceColumn("balance", InsertEntity.getBalance())
                .execute();
        Assert.assertEquals(1, rows);

        InsertSqlBuilderEntity insertSqlBuilderEntity = getDb().selectSqlBuilder()
                .select("*")
                .from(INSERT_BUILD_TABLE_NAME)
                .whereAnd("id = ?", InsertEntity.getId())
                .queryFirst(InsertSqlBuilderEntity.class);

        Assert.assertEquals(InsertEntity.getId(), insertSqlBuilderEntity.getId());
        Assert.assertEquals(InsertEntity.getBalance(), insertSqlBuilderEntity.getBalance());
        Assert.assertEquals(InsertEntity.getKey(), insertSqlBuilderEntity.getKey());
    }

    public void clearData() {
        getDb().execute("delete from " + INSERT_BUILD_TABLE_NAME);
        InsertEntity.setBalance(new BigDecimal("2.222"));
        InsertEntity.setCreateTime(new Date());
        InsertEntity.setId(1);
        InsertEntity.setKey("key");

        InsertEntitySelective.setCreateTime(new Date());
        InsertEntitySelective.setId(2);
    }


}

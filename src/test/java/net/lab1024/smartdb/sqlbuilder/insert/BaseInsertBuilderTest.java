package net.lab1024.smartdb.sqlbuilder.insert;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.common.BaseTest;
import org.junit.Assert;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseInsertBuilderTest extends BaseTest {

    protected static final String INSERT_BUILD_TABLE_NAME = "t_insert_sql_builder";
    protected static final List<InsertSqlBuilderEntity> InsertEntityList = new ArrayList<InsertSqlBuilderEntity>();
    private static InsertSqlBuilderEntity InsertEntity = new InsertSqlBuilderEntity();
    private static InsertSqlBuilderEntity InsertEntitySelective = new InsertSqlBuilderEntity();

    abstract SmartDb getDb();


    public void test() throws ParseException {

        //column
        clearData();
        testColumn();

        //entity
        clearData();
        testInsertEntity();

        //entity selective
        clearData();
        testInsertEntitySelective();

    }

    private void testInsertEntitySelective() {
        int rows = getDb().insertSqlBuilder()
                .table(INSERT_BUILD_TABLE_NAME)
                .insertEntitySelective(InsertEntitySelective)
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

    private void testInsertEntity() {
        int rows = getDb().insertSqlBuilder()
                .table(INSERT_BUILD_TABLE_NAME)
                .insertEntity(InsertEntity)
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
        int rows = getDb().insertSqlBuilder()
                .table(INSERT_BUILD_TABLE_NAME)
                .insertColumn("id", InsertEntity.getId())
                .insertColumn("key", InsertEntity.getKey())
                .insertFunctionColumn("create_time", "now()")
                .insertColumn("balance", InsertEntity.getBalance())
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

package net.lab1024.smartdb.transaction;

import net.lab1024.smartdb.*;
import net.lab1024.smartdb.common.TestSmartDbContainer;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.sqlbuilder.convertor.CaseFormatColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import com.google.common.base.CaseFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Properties;

public class TestTransaction {
    public static Properties mysqlProp = null;

    static {
        try {
            mysqlProp = new Properties();
            mysqlProp.load(TestSmartDbContainer.class.getClassLoader().getResourceAsStream("mysql.properties"));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    SmartDb getDb() {
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

    @Before
    public void deleteData() {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(TestTransactionEntity.class);
        String tableName = classMeta.getTableName(null);
        getDb().execute("delete from " + tableName);
    }


    @Test
    public void testSeparateMethods() {
        TestTransactionEntity transactionEntity = new TestTransactionEntity();
        transactionEntity.setName("i am TestTransaction");

        TransactionSmartDbNode transaction = getDb().getTransaction();
        try {
            transaction.begin();
            TestTransactionEntity transactionEntity1 = transaction.insertSelective(transactionEntity);
            System.err.println(transactionEntity1.getId());

            long count = transaction.query(ScalarHandler.Long, "select count(1) from " + SmartDbOrmClassMetaCache.getClassMeta(transactionEntity.getClass()).getTableName(null));
            Assert.assertEquals(1, count);
            System.out.println(1 / 0);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.err.println("roll back ...");
                transaction.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            transaction.releaseConnection();
        }
        long count = getDb().query(ScalarHandler.Long, "select count(1) from " + SmartDbOrmClassMetaCache.getClassMeta(transactionEntity.getClass()).getTableName(null));
        System.out.println(String.format("expect : %s , actual : %s", 0, count));
        Assert.assertEquals(0, count);

    }

    @Test
    public void testTransactionRunnableMethods() {
        final TestTransactionEntity transactionEntity = new TestTransactionEntity();
        transactionEntity.setName("i am TestTransaction");

        getDb().runTransaction(new TransactionRunnable() {
            @Override
            public boolean run(SmartDbNode smartDb) {
                TestTransactionEntity transactionEntity1 = smartDb.insertSelective(transactionEntity);
                System.err.println(transactionEntity1.getId());

                long count = smartDb.query(ScalarHandler.Long, "select count(1) from " + SmartDbOrmClassMetaCache.getClassMeta(transactionEntity.getClass()).getTableName(null));
                Assert.assertEquals(1, count);
                System.out.println(1 / 0);

                return true;
            }
        });

        long count = getDb().query(ScalarHandler.Long, "select count(1) from " + SmartDbOrmClassMetaCache.getClassMeta(transactionEntity.getClass()).getTableName(null));
        Assert.assertEquals(0, count);
    }

    @Test
    public void testTransactionRunnableMethodsReturnFalse() {
        final TestTransactionEntity transactionEntity = new TestTransactionEntity();
        transactionEntity.setName("i am TestTransaction");

        getDb().runTransaction(new TransactionRunnable() {
            @Override
            public boolean run(SmartDbNode smartDb) {
                TestTransactionEntity transactionEntity1 = smartDb.insertSelective(transactionEntity);
                System.err.println(transactionEntity1.getId());
                long count = smartDb.query(ScalarHandler.Long, "select count(1) from " + SmartDbOrmClassMetaCache.getClassMeta(transactionEntity.getClass()).getTableName(null));
                Assert.assertEquals(1, count);
                System.out.println(1 / 0);
                return false;
            }
        });

        long count = getDb().query(ScalarHandler.Long, "select count(1) from " + SmartDbOrmClassMetaCache.getClassMeta(transactionEntity.getClass()).getTableName(null));
        Assert.assertEquals(0, count);
    }
}

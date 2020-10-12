package net.lab1024.smartdb.xenum;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.SmartDbBuilder;
import net.lab1024.smartdb.common.BaseTest;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.convertor.CaseFormatColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import com.google.common.base.CaseFormat;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EnumTest extends BaseTest {

    private static SmartDb smartDb = null;

    @BeforeClass
    public static void getDb() {
        smartDb = SmartDbBuilder.create()
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
    }

    @Test
    public void testEnum() {
        smartDb.deleteSqlBuilder()
                .table(EnumEntity.class)
                .execute();

        //insert
        EnumEntity enumEntity = new EnumEntity();
        enumEntity.setLevel(Level.HIGH);
        enumEntity.setUserName("hi");
        enumEntity.setSex(Sex.BOY);

        smartDb.insertSelective(enumEntity);

        EnumEntity queryResult = smartDb.selectSqlBuilder()
                .select("*")
                .from(enumEntity.getClass())
                .whereAnd("id = ?", enumEntity.getId())
                .queryFirst(EnumEntity.class);

        Assert.assertEquals(enumEntity, queryResult);

        //update
        enumEntity.setSex(Sex.GIRL);
        enumEntity.setUserName("world");
        enumEntity.setLevel(Level.LOW);
        smartDb.updateSelective(enumEntity);

        queryResult = smartDb.selectSqlBuilder()
                .select("*")
                .from(enumEntity.getClass())
                .whereAnd("id = ?", enumEntity.getId())
                .queryFirst(EnumEntity.class);

        Assert.assertEquals(enumEntity, queryResult);



    }


}

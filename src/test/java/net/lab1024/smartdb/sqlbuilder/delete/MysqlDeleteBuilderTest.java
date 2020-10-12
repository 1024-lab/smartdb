package net.lab1024.smartdb.sqlbuilder.delete;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.SmartDbBuilder;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.sqlbuilder.convertor.CaseFormatColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import com.google.common.base.CaseFormat;
import org.junit.Test;

import java.text.ParseException;

public class MysqlDeleteBuilderTest extends BaseDeleteBuilderTest{


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
    public void test() {
        try {
            super.test();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

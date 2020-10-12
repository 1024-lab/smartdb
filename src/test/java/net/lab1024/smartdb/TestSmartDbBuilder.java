package net.lab1024.smartdb;

import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.ext.SmartDbExtEnum;
import net.lab1024.smartdb.sqlbuilder.convertor.CaseFormatColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;
import com.google.common.base.CaseFormat;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class TestSmartDbBuilder {


    @BeforeClass
    public static void init() {
        System.out.println("dev");
        int a = 1;
        int c = 2;
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
        System.out.println("dev");
    }

    @Test
    public void testBuild() {
        //创建构建对象
        SmartDbBuilder builder = new SmartDbBuilder();
        //设置 写库的数据源
        builder.setMasterDataSource(getMasterDataSource());
        //设置 n个读库 数据源数组，若没有可以不进行set
        builder.setSlaveDataSource(getSlaveDataSource());
        //设置为哪种数据库类型
        builder.setSupportDatabaseType(SupportDatabaseType.MYSQL);
        //设置 是不是在spring中使用(若不是spring, 可以省略)
        builder.setSmartDbExtEnum(SmartDbExtEnum.SPRING4);
        //设置ORM转换格式， CaseFormatColumnNameConverter类，定义了列名命名和字段命名的转换
        builder.setColumnNameConverter(new CaseFormatColumnNameConverter(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_UNDERSCORE));
        //设置类型转换表名 ， 比如UserLog类，对象的表为：t_user_log
        builder.setTableNameConverter(new TableNameConverter() {
            @Override
            public String classToTableName(Class<?> cls) {
                return "t_" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
            }
        });
        //最后，进行构建,smartdb 对象
        SmartDb smartDb = builder.build();



        //....


        //强制读取写库( 若只有一个库，则没有任何区别)
        List<Map> query = smartDb.getMaster().queryList(Map.class, "select * from hostinfo");
        for (Map<String, Object> stringObjectMap : query) {
            System.out.println(stringObjectMap);
        }

        //默认从 读库读取
        query = smartDb.queryList(Map.class, "select * from user");
        for (Map<String, Object> stringObjectMap : query) {
            System.out.println(stringObjectMap);
        }
    }


    public DataSource getSlaveDataSource() {
        return null;
    }

    public DataSource getMasterDataSource() {
        return null;
    }
}

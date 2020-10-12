package net.lab1024.smartdb.generator;

import net.lab1024.smartdb.codegenerator.SmartDbEntityGeneratorBuilder;
import net.lab1024.smartdb.common.BaseTest;
import com.google.common.base.CaseFormat;

public class BaseGeneratorTest extends BaseTest {

    public SmartDbEntityGeneratorBuilder getSmartDbEntityGeneratorBuilder() {
        //创建一个代码生成的 helper
        SmartDbEntityGeneratorBuilder builder = new SmartDbEntityGeneratorBuilder();

        builder.setJdbcUrl(mysqlProp.getProperty("jdbcUrl"));
        builder.setUsername(mysqlProp.getProperty("username"));
        builder.setPassword(mysqlProp.getProperty("password"));
        builder.setDriverClassName(mysqlProp.getProperty("driverClassName"));

        //设置生成entity的包名
        builder.setPackageName("net.lab1024.smartdb.domain");
        //设置生成entity的类名
        builder.setClassName("UserEntity");
        //设置数据库表名
        builder.setTableName("t_user");
        //设置是否生成setter/getter
        builder.setGenerateGetterSetter(true);
        //设置数据库列名 命名格式
        builder.setEntityFieldCaseFormat(CaseFormat.LOWER_CAMEL);
        //设置 entity 命名格式
        builder.setTableColumnCaseFormat(CaseFormat.LOWER_UNDERSCORE);
        //设置 文件的生成目录, 若不设置，则生成到  桌面
//        helper.setEntityClassFileDir("c:/");
        return builder;
    }

}

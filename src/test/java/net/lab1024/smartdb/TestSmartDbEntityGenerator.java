package net.lab1024.smartdb;

import net.lab1024.smartdb.codegenerator.MysqlEntityGenerator;
import net.lab1024.smartdb.codegenerator.SmartDbEntityGeneratorBuilder;
import com.google.common.base.CaseFormat;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestSmartDbEntityGenerator {
    @Test
    public void testGenerate() {
        //创建一个代码生成的 helper
        SmartDbEntityGeneratorBuilder helper = new SmartDbEntityGeneratorBuilder();
        //从配置文件中读取数据库连接属性
        InputStream resourceAsStream = TestSmartDbEntityGenerator.class.getClassLoader().getResourceAsStream("mysql.properties");
        Properties prop = new Properties();
        try {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

        //设置数据库连接
//        helper.setJdbcUrl(prop.getProperty("master.jdbc.url"));
//        helper.setUsername(prop.getProperty("master.jdbc.username"));
//        helper.setPassword(prop.getProperty("master.jdbc.password"));
//        helper.setDriverClassName(prop.getProperty("master.jdbc.driverClassName"));
        helper.setJdbcUrl(prop.getProperty("jdbcUrl"));
        helper.setUsername(prop.getProperty("username"));
        helper.setPassword(prop.getProperty("password"));
        helper.setDriverClassName(prop.getProperty("driverClassName"));

        //设置生成entity的包名
        helper.setPackageName("cn.xxx.project.smartdb.mysql.replace");
        //设置生成entity的类名
        helper.setClassName("ReplaceSqlBuilderEntity");
        //设置数据库表名
        helper.setTableName("t_replace_sql_builder");
        //设置数据库列名 命名格式
        helper.setEntityFieldCaseFormat(CaseFormat.LOWER_CAMEL);
        //设置 entity 命名格式
        helper.setTableColumnCaseFormat(CaseFormat.LOWER_UNDERSCORE);
        //设置 文件的生成目录, 若不设置，则生成到  桌面
//        helper.setEntityClassFileDir("c:/");

        //创建一个mysql的代码生成器，然后进行生成代码
        new MysqlEntityGenerator(helper).generate();

        //创建一个sqlserver的代码生成器，然后进行生成代码
//        new SqlServerEntityGenerator(helper).generate();
    }

}

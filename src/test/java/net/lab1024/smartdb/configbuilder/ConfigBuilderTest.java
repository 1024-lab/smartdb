package net.lab1024.smartdb.configbuilder;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.common.BaseTest;
import net.lab1024.smartdb.SmartDbBuilder;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.datasource.OptEnum;
import net.lab1024.smartdb.ext.SmartDbExtEnum;
import net.lab1024.smartdb.filter.impl.SqlExecutorTimeFilter;
import net.lab1024.smartdb.sqlbuilder.convertor.CaseFormatColumnNameConverter;
import com.google.common.base.CaseFormat;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhuoluodada@qq.com
 */
public class ConfigBuilderTest extends BaseTest {

    @Test
public void textConnectTest() throws SQLException {
SmartDb smartDb = SmartDbBuilder.create()
        .setUrl("jdbc:mysql://127.0.0.1:3306/smartdb?useSSL=false&allowPublicKeyRetrieval=true")
        .setDriverClassName("com.mysql.jdbc.Driver")
        .setUsername("root")
        .setPassword("root")
        // 打印 info 级别sql
        .setShowSql(true)
        //设置数据库类型
        .setSupportDatabaseType(SupportDatabaseType.MYSQL)
        //设置支持spring
        .setSmartDbExtEnum(SmartDbExtEnum.SPRING5)
        //表名与类名转换，将Class类名转为 数据库表 名字，假设数据库表名以 t_ 开头，小写下划线分隔，eg: t_order_item
        .setTableNameConverter(cls -> "t_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName()))
        //列名字 转换
        .setColumnNameConverter(new CaseFormatColumnNameConverter(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_UNDERSCORE))
        //添加  Sql执行时间 Filter拦截器
        .addSmartDbFilter(new SqlExecutorTimeFilter())
        .build();

        Connection writeConnection = smartDb.getConnection(OptEnum.WRITE);
        LOG.info("write connection : {} ", writeConnection);
        Assert.assertTrue(writeConnection != null);

        Connection readConnection = smartDb.getConnection(OptEnum.READ);
        LOG.info("read connection : {} ", readConnection);
        Assert.assertTrue(readConnection != null);
    }
}

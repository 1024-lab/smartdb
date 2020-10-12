package net.lab1024.smartdb.datasource;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 再次封装数据源，为了管理connection的释放，也为了将来的扩展
 *
 * @author zhuoluodada@qq.com
 */
public interface SmartDbDataSource extends DataSource{

    void releaseConnection(Connection connection);

}

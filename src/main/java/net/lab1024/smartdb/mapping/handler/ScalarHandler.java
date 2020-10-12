package net.lab1024.smartdb.mapping.handler;

import net.lab1024.smartdb.mapping.handler.type.JdbcType;
import net.lab1024.smartdb.mapping.handler.type.TypeHandler;
import net.lab1024.smartdb.mapping.handler.type.TypeHandlerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * ResultSetHandler 的实现
 * 将一个 ResultSet 列转换为对象
 * 这个类是线程安全的
 * <p>
 * T： 要转化为的对象的类型
 *
 * @author zhuoluodada@qq.com
 */
public class ScalarHandler<T> implements ResultSetHandler<T> {

    public static final ScalarHandler<Integer> Integer = new ScalarHandler<Integer>(1, Integer.class);
    public static final ScalarHandler<Long> Long = new ScalarHandler<Long>(1,Long.class);
    public static final ScalarHandler<Byte> Byte = new ScalarHandler<Byte>(1,Byte.class);
    public static final ScalarHandler<Short> Short = new ScalarHandler<Short>(1, Short.class);

    public static final ScalarHandler<Double> Double = new ScalarHandler<Double>(1,Double.class);
    public static final ScalarHandler<Float> Float = new ScalarHandler<Float>(1,Float.class);

    public static final ScalarHandler<Boolean> Boolean = new ScalarHandler<Boolean>(1,Boolean.class);

    public static final ScalarHandler<Character> Char = new ScalarHandler<Character>(1,Character.class);

    public static final ScalarHandler<String> String = new ScalarHandler<String>(1,String.class);

    public static final ScalarHandler<BigDecimal> BigDecimal = new ScalarHandler<BigDecimal>(1, BigDecimal.class);

    public static final ResultSetHandler<Date> Date = new ResultSetHandler<Date>() {
        @Override
        public Date handle(ResultSet rs) throws SQLException {
            Timestamp timestamp = rs.getTimestamp(1);
            if (timestamp == null) {
                return null;
            }
            return new Date(timestamp.getTime());
        }
    };

    /**
     * 检索的列编号（从1开始）
     */
    private int columnIndex;

    /**
     * 检索的列名
     * columnName 与 columnIndex
     * 只会有一个被用到
     */
    private String columnName;

    private Class<T> cls;

    public ScalarHandler(int columnIndex, Class<T> cls) {
        this.columnIndex = columnIndex;
        this.cls = cls;
        this.columnName = null;
    }

    /**
     * 创建 ScalarHandler 的新实例
     * 第一列将会从 handle() 方法中返回
     */
    public ScalarHandler() {
        this(1);
    }

    /**
     * 创建 ScalarHandler 的新实例
     *
     * @param columnIndex 检索的列编号
     */
    public ScalarHandler(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * 创建 ScalarHandler 的新实例
     *
     * @param columnName 检索的列名
     */
    public ScalarHandler(String columnName) {
        this(1, columnName);
    }

    /**
     * 辅助构造函数
     *
     * @param columnIndex 检索的列编号
     * @param columnName  检索的列名
     */
    private ScalarHandler(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    /**
     * 通过 ResultSet.getObject() 方法返回一个 ResultSet 列作为一个对象，该方法执行类型转换
     *
     * @param rs ResultSet
     * @return ResultSet 列， 如果 ResultSet 为空，则返回null
     * @throws SQLException 数据库访问出错抛出 SQLException 异常
     *                      类数据类型（T）与列类型不匹配抛出 ClassCastException 异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public T handle(ResultSet rs) throws SQLException {

        if (rs.next()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnType = metaData.getColumnType(this.columnIndex);
            TypeHandler<?> handler = TypeHandlerFactory.getHandler(this.cls);
            JdbcType jdbcType = JdbcType.forCode(columnType);
            if (this.columnName == null) {
                return (T) handler.getResult(rs, this.columnIndex, jdbcType);
            }
            return (T) rs.getObject(this.columnName);
        }
        return null;
    }
}

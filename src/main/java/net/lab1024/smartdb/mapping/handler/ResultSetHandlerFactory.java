package net.lab1024.smartdb.mapping.handler;


import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuoluodada@qq.com
 */
public class ResultSetHandlerFactory {

    private RowConverter rowConverter;
    private Map<Class<?>, ResultSetHandler<?>> ListHandlerMap = new ConcurrentHashMap<Class<?>, ResultSetHandler<?>>();
    private Map<Class<?>, ResultSetHandler<?>> BeanHandlerMap = new ConcurrentHashMap<Class<?>, ResultSetHandler<?>>();

    public ResultSetHandlerFactory(RowConverter rowConverter) {
        this.rowConverter = rowConverter;

        ListHandlerMap.put(int.class, new ColumnListHandler<Integer>(1,int.class));
        ListHandlerMap.put(Integer.class, new ColumnListHandler<Integer>(1,Integer.class));
        ListHandlerMap.put(long.class, new ColumnListHandler<Long>(1,long.class));
        ListHandlerMap.put(Long.class, new ColumnListHandler<Long>(1,Long.class));
        ListHandlerMap.put(byte.class, new ColumnListHandler<Byte>(1,byte.class));
        ListHandlerMap.put(Byte.class, new ColumnListHandler<Byte>(1,Byte.class));
        ListHandlerMap.put(short.class, new ColumnListHandler<Short>(1,short.class));
        ListHandlerMap.put(Short.class, new ColumnListHandler<Short>(1,Short.class));

        ListHandlerMap.put(double.class, new ColumnListHandler<Double>(1,double.class));
        ListHandlerMap.put(Double.class, new ColumnListHandler<Double>(1,Double.class));
        ListHandlerMap.put(float.class, new ColumnListHandler<Float>(1,float.class));
        ListHandlerMap.put(Float.class, new ColumnListHandler<Float>(1,Float.class));

        ListHandlerMap.put(boolean.class, new ColumnListHandler<Boolean>(1,boolean.class));
        ListHandlerMap.put(Boolean.class, new ColumnListHandler<Boolean>(1,Boolean.class));

        ListHandlerMap.put(char.class, new ColumnListHandler<Character>(1,char.class));
        ListHandlerMap.put(Character.class, new ColumnListHandler<Character>(1,Character.class));
        ListHandlerMap.put(String.class, new ColumnListHandler<String>(1,String.class));

        ListHandlerMap.put(BigDecimal.class, new ColumnListHandler<BigDecimal>(1,BigDecimal.class));
        ListHandlerMap.put(Map.class, new MapListHandler(rowConverter));
        ListHandlerMap.put(HashMap.class, new MapListHandler(rowConverter));

        ListHandlerMap.put(Date.class, new ResultSetHandler<List<Date>>() {
            @Override
            public List<Date> handle(ResultSet rs) throws SQLException {
                List<Date> list = new ArrayList<Date>();
                Timestamp timestamp = rs.getTimestamp(1);
                if (timestamp == null) {
                    list.add(null);
                } else {
                    list.add(new Date(timestamp.getTime()));
                }
                return list;
            }
        });

        BeanHandlerMap.put(int.class, ScalarHandler.Integer);
        BeanHandlerMap.put(Integer.class, ScalarHandler.Integer);
        BeanHandlerMap.put(long.class, ScalarHandler.Long);
        BeanHandlerMap.put(Long.class, ScalarHandler.Long);
        BeanHandlerMap.put(byte.class, ScalarHandler.Byte);
        BeanHandlerMap.put(Byte.class, ScalarHandler.Byte);
        BeanHandlerMap.put(short.class, ScalarHandler.Short);
        BeanHandlerMap.put(Short.class, ScalarHandler.Short);
        BeanHandlerMap.put(double.class, ScalarHandler.Double);
        BeanHandlerMap.put(Double.class, ScalarHandler.Double);
        BeanHandlerMap.put(float.class, ScalarHandler.Float);
        BeanHandlerMap.put(Float.class, ScalarHandler.Float);
        BeanHandlerMap.put(boolean.class, ScalarHandler.Boolean);
        BeanHandlerMap.put(Boolean.class, ScalarHandler.Boolean);
        BeanHandlerMap.put(char.class, ScalarHandler.Char);
        BeanHandlerMap.put(Character.class, ScalarHandler.Char);
        BeanHandlerMap.put(BigDecimal.class, ScalarHandler.BigDecimal);
        BeanHandlerMap.put(String.class, ScalarHandler.String);
        BeanHandlerMap.put(Map.class, new MapHandler(rowConverter));
        BeanHandlerMap.put(HashMap.class, new MapHandler(rowConverter));

        BeanHandlerMap.put(Date.class, new ResultSetHandler<Date>() {
            @Override
            public Date handle(ResultSet rs) throws SQLException {
                Timestamp timestamp = rs.getTimestamp(1);
                if (timestamp == null) {
                    return null;
                } else {
                    return new Date(timestamp.getTime());
                }
            }
        });
    }

    public <T> ResultSetHandler<T> getObjectResultHandler(Class<T> cls) {
        ResultSetHandler resultSetHandler = BeanHandlerMap.get(cls);
        if (resultSetHandler == null) {
            synchronized (this) {
                ResultSetHandler againGet = BeanHandlerMap.get(cls);
                if (againGet == null) {
                    BeanHandler handler = new BeanHandler<T>(cls, rowConverter);
                    BeanHandlerMap.put(cls, handler);
                    return handler;
                }
                return againGet;
            }
        }
        return resultSetHandler;
    }

    public <T> ResultSetHandler<T> getObjectListResultHandler(Class<T> cls) {
        ResultSetHandler resultSetHandler = ListHandlerMap.get(cls);
        if (resultSetHandler == null) {
            synchronized (this) {
                ResultSetHandler againGet = ListHandlerMap.get(cls);
                if (againGet == null) {
                    BeanListHandler handler = new BeanListHandler<T>(cls, rowConverter);
                    ListHandlerMap.put(cls, handler);
                    return handler;
                }
                return againGet;
            }
        }
        return resultSetHandler;
    }

}


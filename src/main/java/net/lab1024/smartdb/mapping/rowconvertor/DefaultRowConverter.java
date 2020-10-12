package net.lab1024.smartdb.mapping.rowconvertor;


import net.lab1024.smartdb.exception.SmartDbException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * @author zhuoluodada@qq.com
 */
public class DefaultRowConverter extends BaseRowConverter {

    public DefaultRowConverter(Map<String, String> columnToPropertyOverrides) {
        super(columnToPropertyOverrides);
    }

    public DefaultRowConverter() {
    }

    @Override
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        PropertyDescriptor[] props = this.propertyDescriptors(type);
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);
        return this.createBean(rs, type, props, columnToProperty,rsmd);
    }

    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
        if (!rs.next()) {
            return Collections.emptyList();
        }
        PropertyDescriptor[] props = this.propertyDescriptors(type);
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);
        List<T> results = new ArrayList<T>();
        do {
            results.add(this.createBean(rs, type, props, columnToProperty,rsmd));
        } while (rs.next());

        return results;
    }

    @Override
    public Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnName(i);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(i);
            }
            result.put(columnName, rs.getObject(i));
        }

        return result;
    }

    private PropertyDescriptor[] propertyDescriptors(Class<?> c) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);
        } catch (IntrospectionException e) {
            throw new SmartDbException("Bean introspection failed: " + e.getMessage());
        }
        return beanInfo.getPropertyDescriptors();
    }

    protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {
        int cols = rsmd.getColumnCount();
        int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnName(col);
            String propertyName = null;
            if (columnToPropertyOverrides.isEmpty()) {
                propertyName = columnName;
            } else {
                propertyName = columnToPropertyOverrides.get(columnName);
                if (propertyName == null) {
                    propertyName = columnName;
                }
            }
            for (int i = 0; i < props.length; i++) {
                if (propertyName.equalsIgnoreCase(props[i].getName())) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }


}

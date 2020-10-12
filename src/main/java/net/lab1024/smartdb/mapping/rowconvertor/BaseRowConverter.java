package net.lab1024.smartdb.mapping.rowconvertor;

import net.lab1024.smartdb.SmartDbEnum;
import net.lab1024.smartdb.mapping.handler.type.JdbcType;
import net.lab1024.smartdb.mapping.handler.type.TypeHandler;
import net.lab1024.smartdb.mapping.handler.type.TypeHandlerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public abstract class BaseRowConverter implements RowConverter {

    protected static final int PROPERTY_NOT_FOUND = -1;
    protected final Map<String, String> columnToPropertyOverrides;

    public BaseRowConverter(Map<String, String> columnToPropertyOverrides) {
        this.columnToPropertyOverrides = columnToPropertyOverrides;
    }

    public BaseRowConverter() {
        this.columnToPropertyOverrides = Collections.emptyMap();
    }

    @Override
    public Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    }

    protected <T> T createBean(ResultSet rs, Class<T> type, PropertyDescriptor[] props, int[] columnToProperty, ResultSetMetaData rsmd) throws SQLException {
        T bean = this.newInstance(type);
        for (int i = 1; i < columnToProperty.length; i++) {
            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }
            PropertyDescriptor prop = props[columnToProperty[i]];
            Class<?> propType = prop.getPropertyType();
            Object value = null;
            if (propType != null) {
                int columnType = rsmd.getColumnType(i);
                JdbcType jdbcType = JdbcType.forCode(columnType);
                TypeHandler<?> handler = null;
                if (SmartDbEnum.class.isAssignableFrom(propType)) {
                    Class<? extends SmartDbEnum> cls = (Class<? extends SmartDbEnum>) propType;
                    handler = TypeHandlerFactory.getSmartDbEnumTypeHandler(cls);
                } else if (Enum.class.isAssignableFrom(propType)) {
                    Class<? extends Enum> cls = (Class<? extends Enum>) propType;
                    handler = TypeHandlerFactory.getEnumTypeHandler(cls);
                } else {
                    handler = TypeHandlerFactory.getHandler(propType);
                }
                if (handler != null) {
                    value = handler.getResult(rs, i, jdbcType);
                }
            }
            this.callSetter(bean, prop, value);
        }
        return bean;
    }

    protected boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (type.equals(Integer.TYPE) && value instanceof Integer) {
            return true;

        } else if (type.equals(Long.TYPE) && value instanceof Long) {
            return true;

        } else if (type.equals(Double.TYPE) && value instanceof Double) {
            return true;

        } else if (type.equals(Float.TYPE) && value instanceof Float) {
            return true;

        } else if (type.equals(Short.TYPE) && value instanceof Short) {
            return true;

        } else if (type.equals(Byte.TYPE) && value instanceof Byte) {
            return true;

        } else if (type.equals(Character.TYPE) && value instanceof Character) {
            return true;

        } else if (type.equals(Boolean.TYPE) && value instanceof Boolean) {
            return true;

        }
        return false;
    }

    protected <T> T newInstance(Class<T> c) throws SQLException {
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException("Cannot create " + c.getName() + ": " + e.getMessage());
        }
    }

    protected void callSetter(Object target, PropertyDescriptor prop, Object value)
            throws SQLException {

        Method setter = prop.getWriteMethod();
        if (setter == null) {
            return;
        }

        Class<?>[] params = setter.getParameterTypes();
        try {
            if (value instanceof String && params[0].isEnum()) {
                value = Enum.valueOf(params[0].asSubclass(Enum.class), (String) value);
            }
            setter.invoke(target, new Object[]{value});
        } catch (IllegalArgumentException e) {
            throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

}

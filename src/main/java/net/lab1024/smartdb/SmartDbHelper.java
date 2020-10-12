package net.lab1024.smartdb;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author zhuoluodada@qq.com
 */
public class SmartDbHelper {

    public static boolean setParams(PreparedStatement stmt, List<Object> params) throws SQLException {
        if (params == null) {
            return false;
        }
        for (int i = 0; i < params.size(); i++) {
            setParam(stmt, i + 1, params.get(i));
        }
        return true;
    }

    public static boolean setParams(PreparedStatement stmt, Object[] params) throws SQLException {
        if (params == null) {
            return false;
        }
        for (int i = 0; i < params.length; i++) {
            setParam(stmt, i + 1, params[i]);
        }
        return true;
    }

    private static void setParam(PreparedStatement pst, int parameterIndex, Object value) throws SQLException {
        if (value == null) {
            pst.setObject(parameterIndex, null);
            return;
        }

        boolean isEnum = Enum.class.isAssignableFrom(value.getClass());
        if (value instanceof java.util.Date) {
            if (value instanceof java.sql.Date) {
                pst.setDate(parameterIndex, (java.sql.Date) value);
            } else if (value instanceof java.sql.Timestamp) {
                pst.setTimestamp(parameterIndex, (java.sql.Timestamp) value);
            } else {
                // Oracle、SqlServer 中的 TIMESTAMP、DATE 支持 new Date() 给值
                java.util.Date d = (java.util.Date) value;
                pst.setTimestamp(parameterIndex, new java.sql.Timestamp(d.getTime()));
            }
        } else if (value instanceof SmartDbEnum) {
            SmartDbEnum val = (SmartDbEnum) value;
            pst.setObject(parameterIndex, val.getValue());
        } else if (isEnum) {
            Enum val = (Enum) value;
            pst.setObject(parameterIndex, val.name());
        } else {
            pst.setObject(parameterIndex, value);
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        toString(obj, sb);
        return sb.toString();
    }

    private static void toString(Object obj, StringBuilder sb) {
        if (obj == null) {
            sb.append("null");
            return;
        }
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            int length = Array.getLength(obj);
            if (length == 0) {
                sb.append("[]");
            } else {
                sb.append('[');
                for (int i = 0; i < length; i++) {
                    if (i > 0) {
                        sb.append(',').append(' ');
                    }
                    toString(Array.get(obj, i), sb);
                }
                sb.append(']');
            }
        } else if (clazz.isEnum()) {
            sb.append(clazz.getSimpleName()).append(":").append(obj);
        } else if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            Iterator<?> it = collection.iterator();
            sb.append('[');
            while (it.hasNext()) {
                Object e = it.next();
                if (e == collection) {
                    sb.append("(this Collection)");
                } else {
                    toString(e, sb);
                }
                if (it.hasNext()) {
                    sb.append(',').append(' ');
                }
            }
            sb.append(']');
        } else if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            Iterator<?> i = map.entrySet().iterator();
            sb.append('{');
            while (i.hasNext()) {
                Entry<?, ?> e = (Entry<?, ?>) i.next();
                Object key = e.getKey();
                Object value = e.getValue();
                if (key == map) {
                    sb.append("(this Map)");
                } else {
                    toString(key, sb);
                }
                sb.append('=');
                if (value == map) {
                    sb.append("(this Map)");
                } else {
                    toString(value, sb);
                }
                if (i.hasNext()) {
                    sb.append(',').append(' ');
                }
            }
            sb.append('}');
        } else {
            sb.append(obj);
        }
    }

    public static String getValue(Map<String, String> map, String key, String defaultValue) {
        String value = map.get(key);
        return value == null ? defaultValue : value;
    }

    public static Integer getValue(Map<String, String> map, String key, Integer defaultValue) {
        String value = map.get(key);
        return value == null ? defaultValue : Integer.valueOf(value);
    }

    public static Boolean getValue(Map<String, String> map, String key, Boolean defaultValue) {
        String value = map.get(key);
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

}

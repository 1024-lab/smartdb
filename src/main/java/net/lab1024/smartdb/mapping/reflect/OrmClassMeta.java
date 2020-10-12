package net.lab1024.smartdb.mapping.reflect;


import net.lab1024.smartdb.annotation.*;
import net.lab1024.smartdb.annotation.*;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuoluodada@qq.com
 */
public class OrmClassMeta {

    private Class<?> cls;

    private String tableName;

    private OrmClassFieldMeta[] primaryKeyFields;

    private OrmClassFieldMeta[] columnsFields;

    private OrmClassFieldMeta[] useGeneratedKeyFields;

    private boolean isUseGeneratedKey = false;

    OrmClassMeta(Class cls) {
        this.cls = cls;
        List<OrmClassFieldMeta> primaryList = new ArrayList<OrmClassFieldMeta>();
        List<OrmClassFieldMeta> columnsFieldsList = new ArrayList<OrmClassFieldMeta>();
        List<OrmClassFieldMeta> useGeneratedKeyFieldsList = new ArrayList<OrmClassFieldMeta>();

        while (cls != Object.class) {
            TableAlias tableAnno = this.cls.getAnnotation(TableAlias.class);
            if (tableName == null && tableAnno != null && tableAnno.value() != null && tableAnno.value().length() > 0) {
                tableName = tableAnno.value();
            }

            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                ColumnIgnore annotation = f.getAnnotation(ColumnIgnore.class);
                if (annotation != null) {
                    continue;
                }

                f.setAccessible(true);
                ColumnAlias columnAliasAnno = f.getAnnotation(ColumnAlias.class);
                String columnAlias = columnAliasAnno == null ? null : columnAliasAnno.value();
                PrimaryKey primaryKey = f.getAnnotation(PrimaryKey.class);
                if (primaryKey != null) {
                    primaryList.add(new OrmClassFieldMeta(columnAlias, f));
                } else {
                    columnsFieldsList.add(new OrmClassFieldMeta(columnAlias, f));
                }

                UseGeneratedKey useGeneratedKey = f.getAnnotation(UseGeneratedKey.class);
                if (useGeneratedKey != null) {
                    useGeneratedKeyFieldsList.add(new OrmClassFieldMeta(columnAlias, f));
                }
            }
            cls = cls.getSuperclass();
        }
        this.primaryKeyFields = primaryList.toArray(new OrmClassFieldMeta[primaryList.size()]);
        this.columnsFields = columnsFieldsList.toArray(new OrmClassFieldMeta[columnsFieldsList.size()]);
        this.useGeneratedKeyFields = useGeneratedKeyFieldsList.toArray(new OrmClassFieldMeta[useGeneratedKeyFieldsList.size()]);
        this.isUseGeneratedKey = useGeneratedKeyFieldsList.size() > 0;
    }

    public OrmClassFieldMeta[] getPrimaryKeyFields() {
        return primaryKeyFields;
    }

    public OrmClassFieldMeta[] getUseGeneratedKeyFields() {
        return useGeneratedKeyFields;
    }

    public boolean isUseGeneratedKey() {
        return isUseGeneratedKey;
    }

    public OrmClassFieldMeta[] getColumnsFields() {
        return columnsFields;
    }

    public String getTableName(TableNameConverter tableNameConverter) {
        if (this.tableName == null) {
            return tableNameConverter.classToTableName(this.cls);
        }
        return this.tableName;
    }

    private Object convert2Long(Object val) {
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).longValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).longValue();
        }else if (val instanceof Long) {
            return ((Long) val).longValue();
        } else {
            return ((Integer) val).longValue();
        }
    }

    private Integer convert2Integer(Object val) {
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).intValue();
        } else if (val instanceof Long) {
            return ((Long) val).intValue();
        } else if (val instanceof BigInteger) {
            return ((BigInteger) val).intValue();
        } else {
            return ((Integer) val).intValue();
        }
    }

    public <T> void injectGeneratedKeys(ResultSet rs, T t) throws SQLException, IllegalAccessException {
        final ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            OrmClassFieldMeta ormClassFieldMeta = this.useGeneratedKeyFields[i];
            if (ormClassFieldMeta != null) {
                Class<?> type = ormClassFieldMeta.getField().getType();
                Object value = rs.getObject(i + 1);
                if (type.equals(Integer.class) || type.equals(int.class)) {
                    ormClassFieldMeta.getField().set(t, convert2Integer(value));
                    return;
                }
                if (type.equals(Long.class) || type.equals(long.class)) {
                    ormClassFieldMeta.getField().set(t, convert2Long(value));
                    return;
                }
                ormClassFieldMeta.getField().set(t, value);
            }
        }
    }
}

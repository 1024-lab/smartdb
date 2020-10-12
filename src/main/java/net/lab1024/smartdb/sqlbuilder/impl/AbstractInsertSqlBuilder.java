package net.lab1024.smartdb.sqlbuilder.impl;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.mapping.reflect.OrmClassFieldMeta;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.sqlbuilder.AbstractSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.InsertSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.SqlBuilderType;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractInsertSqlBuilder extends AbstractSqlBuilder implements InsertSqlBuilder {

    protected String table;
    protected OrmClassFieldMeta[] useGeneratedKeyFields;
    protected Map<String, InsertParamObject> columnAndParam = new LinkedHashMap<String, InsertParamObject>();

    public AbstractInsertSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SqlBuilderType getSqlBuilderType() {
        return SqlBuilderType.INSERT;
    }

    @Override
    public InsertSqlBuilder table(String table) {
        if (table != null) {
            this.table = table;
        }
        return this;
    }

    @Override
    public InsertSqlBuilder table(Class clazz) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(clazz);
        if (classMeta != null) {
            this.table(classMeta.getTableName(this.tableNameConverter));
        }
        return this;
    }

    @Override
    public InsertSqlBuilder appendSql(String sqlClause) {
        appendSqlBuilder.append(BLANK).append(sqlClause);
        return this;
    }

    @Override
    public InsertSqlBuilder insertColumn(String column, Object param) {
        columnAndParam.put(column, new InsertParamObject(false, param));
        return this;
    }

    @Override
    public InsertSqlBuilder insertFunctionColumn(String column, String sqlFunction) {
        columnAndParam.put(column, new InsertParamObject(true, sqlFunction));
        return this;
    }

    @Override
    public InsertSqlBuilder insertEntitySelective(Object obj) {
        return insertEntitySelective(obj, columnNameConverter);
    }

    @Override
    public InsertSqlBuilder insertEntitySelective(Object obj, ColumnNameConverter columnNameConverter) {
        reflectFieldAndInsert(obj, columnNameConverter, true);
        return this;
    }

    private void reflectFieldAndInsert(Object obj, ColumnNameConverter columnNameConverter, boolean ignoreNull) {
        Class<?> cls = obj.getClass();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(cls);
        this.table(classMeta.getTableName(this.tableNameConverter));
        try {
            OrmClassFieldMeta[] primaryKeyFields = classMeta.getPrimaryKeyFields();
            useGeneratedKeyFields = classMeta.getUseGeneratedKeyFields();
            for (int i = 0; i < primaryKeyFields.length; i++) {
                OrmClassFieldMeta f = primaryKeyFields[i];
                Object o = f.getField().get(obj);
                if (o == null) {
                    continue;
                }
                String name = f.getOrmColumnName();
                if (columnNameConverter != null) {
                    name = columnNameConverter.fieldConvertToColumn(name);
                }
                insertColumn(name, o);
            }

            for (OrmClassFieldMeta f : classMeta.getColumnsFields()) {
                Object o = f.getField().get(obj);
                if (ignoreNull && o == null) {
                    continue;
                }
                String name = f.getOrmColumnName();
                if (columnNameConverter != null) {
                    name = columnNameConverter.fieldConvertToColumn(name);
                }
                insertColumn(name, o);
            }

        } catch (IllegalAccessException e) {
            throw new SmartDbException(e);
        }
    }

    @Override
    public InsertSqlBuilder insertEntity(Object obj) {
        return insertEntity(obj, columnNameConverter);
    }

    @Override
    public InsertSqlBuilder insertEntity(Object obj, ColumnNameConverter columnNameConverter) {
        reflectFieldAndInsert(obj, columnNameConverter, false);
        return this;
    }

    @Override
    public String generateSql() {
        return generateSql(false);
    }

    @Override
    public String toString() {
        return generateSql(false);
    }

    @Override
    public List<Object> getAllParams() {
        ArrayList<Object> list = new ArrayList<Object>(columnAndParam.size());
        for (InsertParamObject param : columnAndParam.values()) {
            if (!param.isSqlFunction) {
                list.add(param.param);
            }
        }
        return list;
    }

    /**
     * 参数包装类
     */
    protected class InsertParamObject {
        protected boolean isSqlFunction;
        Object param;

        public boolean isSqlFunction() {
            return isSqlFunction;
        }

        public Object getParam() {
            return param;
        }

        public InsertParamObject(boolean isSqlFunction, Object param) {
            this.isSqlFunction = isSqlFunction;
            this.param = param;
        }
    }

    @Override
    public int execute() {
        return this.smartDbNode.execute(this);
    }
}

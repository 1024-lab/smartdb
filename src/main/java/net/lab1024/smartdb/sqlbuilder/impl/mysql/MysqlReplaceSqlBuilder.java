package net.lab1024.smartdb.sqlbuilder.impl.mysql;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.mapping.reflect.OrmClassFieldMeta;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.sqlbuilder.AbstractSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.ReplaceSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.SqlBuilderType;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MysqlReplaceSqlBuilder extends AbstractSqlBuilder implements ReplaceSqlBuilder {

    protected String table;
    protected Map<String, MysqlReplaceSqlBuilder.ReplaceParamObject> columnAndParam = new LinkedHashMap<String, MysqlReplaceSqlBuilder.ReplaceParamObject>();

    public MysqlReplaceSqlBuilder(SmartDbNode smartDbNode) {  super(smartDbNode); }

    @Override
    public ReplaceSqlBuilder table(String table) {
        if (table != null) {
            this.table = table;
        }
        return this;
    }

    @Override
    public ReplaceSqlBuilder table(Class clazz) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(clazz);
        if (classMeta != null) {
            this.table(classMeta.getTableName(this.tableNameConverter));
        }
        return this;
    }

    @Override
    public ReplaceSqlBuilder appendSql(String sqlClause) {
        appendSqlBuilder.append(BLANK).append(sqlClause);
        return this;
    }

    @Override
    public ReplaceSqlBuilder replaceColumn(String column, Object param) {
        columnAndParam.put(column, new ReplaceParamObject(false, param));
        return this;
    }

    @Override
    public ReplaceSqlBuilder replaceFunctionColumn(String column, String sqlFunction) {
        columnAndParam.put(column, new ReplaceParamObject(true, sqlFunction));
        return this;
    }

    @Override
    public ReplaceSqlBuilder replaceEntitySelective(Object obj) {
        return replaceEntitySelective(obj, columnNameConverter);
    }

    @Override
    public ReplaceSqlBuilder replaceEntitySelective(Object obj, ColumnNameConverter nameConverter) {
        reflectFieldAndInsert(obj, columnNameConverter, true);
        return this;
    }

    private void reflectFieldAndInsert(Object obj, ColumnNameConverter columnNameConverter, boolean ignoreNull) {
        Class<?> cls = obj.getClass();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(cls);
        this.table(classMeta.getTableName(this.tableNameConverter));
        try {
            OrmClassFieldMeta[] primaryKeyFields = classMeta.getPrimaryKeyFields();
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
                replaceColumn(name, o);
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
                replaceColumn(name, o);
            }

        } catch (IllegalAccessException e) {
            throw new SmartDbException(e);
        }
    }

    @Override
    public ReplaceSqlBuilder replaceEntity(Object obj) {
        return replaceEntity(obj, columnNameConverter);
    }

    @Override
    public ReplaceSqlBuilder replaceEntity(Object obj, ColumnNameConverter nameConverter) {
        reflectFieldAndInsert(obj, columnNameConverter, false);
        return this;
    }

    @Override
    public SqlBuilderType getSqlBuilderType() {
        return SqlBuilderType.REPLACE;
    }

    @Override
    public String generateSql() {
        return generateSql(false);
    }

    @Override
    public String generateSql(boolean isPretty) {
        StringBuilder replaceClause = new StringBuilder();
        replaceClause.append("replace into ");
        replaceClause.append(this.table);
        replaceClause.append(" ( ");

        StringBuilder valuesClause = new StringBuilder();
        valuesClause.append(" values ( ");

        if (columnAndParam.size() > 0) {
            int count = 1;
            boolean isNotLast = false;
            for (Map.Entry<String, ReplaceParamObject> entry : columnAndParam.entrySet()) {
                //replace clause
                replaceClause.append(MysqlConst.RESERVED_WORD_CHAR).append(entry.getKey()).append(MysqlConst.RESERVED_WORD_CHAR);

                //value clause
                ReplaceParamObject paramObject = entry.getValue();
                if (paramObject.isSqlFunction()) {
                    valuesClause.append(BLANK).append(paramObject.getParam());
                } else {
                    valuesClause.append(BLANK).append(PARAM_PLACEHOLDER);
                }

                isNotLast = count < columnAndParam.size();
                if (isNotLast) {
                    replaceClause.append(",");
                    valuesClause.append(",");
                }
                count++;
            }
        }

        replaceClause.append(")");
        valuesClause.append(")");
        replaceClause.append(isPretty ? LINE_SEPARATOR : BLANK);
        replaceClause.append(valuesClause);
        replaceClause.append(appendSqlBuilder);
        return replaceClause.toString();
    }

    @Override
    public List<Object> getAllParams() {
        ArrayList<Object> list = new ArrayList<Object>(columnAndParam.size());
        for (ReplaceParamObject param : columnAndParam.values()) {
            if (!param.isSqlFunction) {
                list.add(param.param);
            }
        }
        return list;
    }

    @Override
    public SupportDatabaseType getSupportDatabaseType() {
        return SupportDatabaseType.MYSQL;
    }

    /**
     * 参数包装类
     */
    protected class ReplaceParamObject {
        protected boolean isSqlFunction;
        Object param;

        public boolean isSqlFunction() {
            return isSqlFunction;
        }

        public Object getParam() {
            return param;
        }

        public ReplaceParamObject(boolean isSqlFunction, Object param) {
            this.isSqlFunction = isSqlFunction;
            this.param = param;
        }
    }

    @Override
    public int execute() {
        return this.smartDbNode.execute(this);
    }
}

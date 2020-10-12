package net.lab1024.smartdb.sqlbuilder.impl;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.mapping.reflect.OrmClassFieldMeta;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.sqlbuilder.AbstractSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.PatternLocationEnum;
import net.lab1024.smartdb.sqlbuilder.SqlBuilderType;
import net.lab1024.smartdb.sqlbuilder.UpdateSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUpdateSqlBuilder extends AbstractSqlBuilder implements UpdateSqlBuilder {

    protected String table = null;

    protected String asName = null;

    protected List<Object> whereParamList = null;

    protected StringBuilder whereClause = null;

    protected List<String> updateColumns;

    protected List<Object> updateColumnParamList;

    public AbstractUpdateSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
        this.whereClause = new StringBuilder();
    }

    @Override
    public UpdateSqlBuilder appendSql(String sqlClause) {
        appendSqlBuilder.append(BLANK).append(sqlClause);
        return this;
    }

    @Override
    public SqlBuilderType getSqlBuilderType() {
        return SqlBuilderType.UPDATE;
    }

    @Override
    public UpdateSqlBuilder table(String table) {
        if (table != null) {
            this.table = table;
        }
        return this;
    }

    @Override
    public UpdateSqlBuilder table(Class entityClass) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        if (classMeta != null) {
            this.table(classMeta.getTableName(this.tableNameConverter));
        }
        return this;
    }

    @Override
    public UpdateSqlBuilder updateColumn(String updateClause) {
        if (updateColumns == null) {
            updateColumns = new ArrayList<String>();
        }
        updateColumns.add(updateClause);
        return this;
    }

    @Override
    public UpdateSqlBuilder updateColumn(String updateClause, Object param) {
        if (updateColumns == null) {
            updateColumns = new ArrayList<String>();
        }
        updateColumns.add(updateClause);

        if (updateColumnParamList == null) {
            updateColumnParamList = new ArrayList<Object>();
        }
        updateColumnParamList.add(param);
        return this;
    }

    public abstract String generateQuestionMarkPart(String column);

    public abstract String generateSetNullPart(String column);

    @Override
    public UpdateSqlBuilder updateEntitySelective(Object obj) {
        return updateEntitySelective(obj, columnNameConverter);
    }


    @Override
    public UpdateSqlBuilder updateEntitySelective(Object obj, ColumnNameConverter columnNameConverter) {
        Class<?> cls = obj.getClass();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(cls);
        this.table(classMeta.getTableName(this.tableNameConverter));
        OrmClassFieldMeta[] columnsFields = classMeta.getColumnsFields();
        try {
            for (OrmClassFieldMeta f : columnsFields) {
                Object o = f.getField().get(obj);
                if (o == null) {
                    continue;
                }
                String name = f.getOrmColumnName();
                if (columnNameConverter != null) {
                    name = columnNameConverter.fieldConvertToColumn(name);
                }
                updateColumn(generateQuestionMarkPart(name), o);
            }
            appendPrimaryField(obj, columnNameConverter, classMeta);
        } catch (IllegalAccessException e) {
            throw new SmartDbException(e);
        }

        return this;
    }

    @Override
    public UpdateSqlBuilder updateEntity(Object obj) {
        return updateEntity(obj, columnNameConverter);
    }

    @Override
    public UpdateSqlBuilder updateEntity(Object obj, ColumnNameConverter nameConverter) {
        Class<?> cls = obj.getClass();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(cls);
        this.table(classMeta.getTableName(this.tableNameConverter));
        OrmClassFieldMeta[] columnsFields = classMeta.getColumnsFields();
        try {
            for (OrmClassFieldMeta f : columnsFields) {
                String columnName = f.getOrmColumnName();
                if (nameConverter != null) {
                    columnName = nameConverter.fieldConvertToColumn(columnName);
                }
                Object o = f.getField().get(obj);
                if (o == null) {
                    updateColumn(generateSetNullPart(columnName));
                } else {
                    updateColumn(generateQuestionMarkPart(columnName), o);
                }
            }
            appendPrimaryField(obj, nameConverter, classMeta);
        } catch (IllegalAccessException e) {
            throw new SmartDbException(e);
        }

        return this;
    }

    @Override
    public UpdateSqlBuilder clearWhere() {
        this.whereClause = new StringBuilder();
        this.whereParamList = new ArrayList<Object>();
        return this;
    }

    private void appendPrimaryField(Object obj, ColumnNameConverter nameConverter, OrmClassMeta classMeta) throws
            IllegalAccessException {
        OrmClassFieldMeta[] primaryFields = classMeta.getPrimaryKeyFields();
        for (OrmClassFieldMeta f : primaryFields) {
            Object o = f.getField().get(obj);
            if (o == null) {
                continue;
            }
            String name = f.getOrmColumnName();
            if (nameConverter != null) {
                name = nameConverter.fieldConvertToColumn(name);
            }
            whereAnd(generateQuestionMarkPart(name), o);
        }
    }

    @Override
    public UpdateSqlBuilder whereAnd(String where) {
        ensureAppendWhereAnd(where);
        return this;
    }

    private void ensureAppendWhereAnd(String where) {
        if (whereClause.length() == 0) {
            whereClause.append("WHERE ").append(where);
        } else {
            whereClause.append(" AND ").append(where);
        }
    }

    private void ensureAppendWhereOr(String where) {
        if (whereClause.length() == 0) {
            whereClause.append("WHERE ").append(where);
        } else {
            whereClause.append(" OR ").append(where);
        }
    }

    private void ensureAddWhereParams(Object... params) {
        if (params == null || params.length == 0) {
            return;
        }

        if (this.whereParamList == null) {
            this.whereParamList = new ArrayList<Object>();
        }

        for (Object param : params) {
            this.whereParamList.add(param);
        }
    }

    @Override
    public UpdateSqlBuilder whereAnd(String where, Object... param) {
        ensureAppendWhereAnd(where);
        ensureAddWhereParams(param);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereAndLikeForMultiColumn(String like, String... column) {
        if (column == null || column.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(like, true, column);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereAndLikeForMultiPattern(String column, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        appendLikeClause4MultiLikes(column, true, likes);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereAndLikeForMultiColumn(String like, PatternLocationEnum location, String... column) {
        if (column == null || column.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(percentSignFormat(location, like), true, column);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereAndLikeForMultiPattern(String column, PatternLocationEnum location, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        String[] finalLikeArr = new String[likes.length];
        for (int i = 0; i < likes.length; i++) {
            finalLikeArr[i] = percentSignFormat(location, likes[i]);
        }
        appendLikeClause4MultiLikes(column, true, finalLikeArr);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereOr(String where) {
        ensureAppendWhereOr(where);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereOr(String where, Object... param) {
        ensureAppendWhereOr(where);
        ensureAddWhereParams(param);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereOrLikeForMultiColumn(String like, String... column) {
        if (column == null || column.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(like, false, column);
        return this;
    }

    private void appendLikeClause4MultiColumns(String finalLike, boolean isAndThenOr, String... column) {
        if (whereClause.length() == 0) {
            whereClause.append(" WHERE ");
        } else {
            whereClause.append(isAndThenOr ? " AND " : " OR ");
        }
        int len = column.length - 1;
        if (column.length > 0) {
            this.whereClause.append("( ");
            for (int i = 0; i < column.length; i++) {
                if (len == i) {
                    this.whereClause.append(column[i]).append(" LIKE '").append(finalLike).append("' ");
                } else {
                    this.whereClause.append(column[i]).append(" LIKE '").append(finalLike).append("' OR ");
                }
            }
            this.whereClause.append(")");
        } else {
            this.whereClause.append(column[0]).append(" LIKE '").append(finalLike).append("' ");
        }
    }

    private void appendLikeClause4MultiLikes(String column, boolean isAndThenOr, String... finalLikes) {
        if (whereClause.length() == 0) {
            whereClause.append(" WHERE ");
        } else {
            whereClause.append(isAndThenOr ? " AND " : " OR ");
        }
        int len = finalLikes.length - 1;
        if (finalLikes.length > 0) {
            this.whereClause.append("( ");
            for (int i = 0; i < finalLikes.length; i++) {
                if (len == i) {
                    this.whereClause.append(column).append(" LIKE '").append(finalLikes[i]).append("' ");
                } else {
                    this.whereClause.append(column).append(" LIKE '").append(finalLikes[i]).append("' OR ");
                }
            }
            this.whereClause.append(")");
        } else {
            this.whereClause.append(column).append(" LIKE '").append(finalLikes[0]).append("' ");
        }
    }

    @Override
    public UpdateSqlBuilder whereOrLikeForMultiPattern(String column, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        appendLikeClause4MultiLikes(column, false, likes);
        return this;
    }

    @Override
    public UpdateSqlBuilder whereOrLikeForMultiColumn(String like, PatternLocationEnum location, String... column) {
        if (column == null || column.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(percentSignFormat(location, like), false, column);
        return this;
    }

    private String percentSignFormat(PatternLocationEnum location, String str) {
        if (location == PatternLocationEnum.AROUND) {
            return String.format("%%%s%%", str);
        } else if (location == PatternLocationEnum.SUFFIX) {
            return String.format("%s%%", str);
        } else if (location == PatternLocationEnum.PREFIX) {
            return String.format("%%%s", str);
        } else {
            throw new RuntimeException("cannot found PatternLocationEnum " + location);
        }
    }

    @Override
    public UpdateSqlBuilder whereOrLikeForMultiPattern(String column, PatternLocationEnum location, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        String[] finalLikeArr = new String[likes.length];
        for (int i = 0; i < likes.length; i++) {
            finalLikeArr[i] = percentSignFormat(location, likes[i]);
        }
        appendLikeClause4MultiLikes(column, false, finalLikeArr);
        return this;
    }

    @Override
    public String generateSql() {
        return generateSql(false);
    }

    @Override
    public String generateSql(boolean isPretty) {
        StringBuilder updateStringBuilder = new StringBuilder();
        updateStringBuilder.append("UPDATE ").append(table).append(isPretty ? LINE_SEPARATOR : BLANK);

        if (updateColumns != null && updateColumns.size() > 0) {
            updateStringBuilder.append("SET ");
            int len = updateColumns.size() - 1;
            for (int i = 0; i < updateColumns.size(); i++) {
                if (i == len) {
                    updateStringBuilder.append(BLANK).append(updateColumns.get(i));
                } else {
                    updateStringBuilder.append(BLANK).append(updateColumns.get(i)).append(",");
                }
            }
            updateStringBuilder.append(isPretty ? LINE_SEPARATOR : BLANK);
        }

        updateStringBuilder.append(whereClause);
        updateStringBuilder.append(appendSqlBuilder);
        return updateStringBuilder.toString();
    }

    @Override
    public String toString() {
        return generateSql(false);
    }

    @Override
    public List<Object> getWhereParamList() {
        return whereParamList;
    }

    @Override
    public List<Object> getUpdateParams() {
        return updateColumnParamList;
    }

    @Override
    public List<Object> getAllParams() {
        int count = updateColumnParamList == null ? 0 : updateColumnParamList.size() + (whereParamList == null ? 0 :
                whereParamList.size());
        ArrayList<Object> list = new ArrayList<Object>(count);
        if (updateColumnParamList != null) {
            list.addAll(updateColumnParamList);
        }
        if (whereParamList != null) {
            list.addAll(whereParamList);
        }
        return list;
    }

    @Override
    public int execute() {
        return this.smartDbNode.execute(this);
    }
}

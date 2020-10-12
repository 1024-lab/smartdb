package net.lab1024.smartdb.sqlbuilder.impl;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.mapping.reflect.OrmClassFieldMeta;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.sqlbuilder.AbstractSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.DeleteSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.PatternLocationEnum;
import net.lab1024.smartdb.sqlbuilder.SqlBuilderType;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDeleteSqlBuilder extends AbstractSqlBuilder implements DeleteSqlBuilder {

    protected String table;
    protected List<Object> whereParamList = null;
    protected StringBuilder whereClause = new StringBuilder();

    public AbstractDeleteSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
    }

    @Override
    public SqlBuilderType getSqlBuilderType() {
        return SqlBuilderType.DELETE;
    }

    @Override
    public String toString() {
        return generateSql(false);
    }

    @Override
    public String generateSql() {
        return generateSql(false);
    }

    @Override
    public String generateSql(boolean isPretty) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(table).append(isPretty ? LINE_SEPARATOR : BLANK);
        sb.append(whereClause);
        sb.append(appendSqlBuilder);
        return sb.toString();
    }

    @Override
    public DeleteSqlBuilder appendSql(String sqlClause) {
        appendSqlBuilder.append(BLANK).append(sqlClause);
        return this;
    }

    @Override
    public DeleteSqlBuilder table(String table) {
        if (table != null) {
            this.table = table;
        }
        return this;
    }

    @Override
    public DeleteSqlBuilder table(Class entityClass) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        if (classMeta != null) {
            this.table(classMeta.getTableName(this.tableNameConverter));
        }
        return this;
    }

    @Override
    public DeleteSqlBuilder deleteEntity(Object obj) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(obj.getClass());
        this.table(classMeta.getTableName(this.tableNameConverter));
        OrmClassFieldMeta[] primaryKeyFields = classMeta.getPrimaryKeyFields();
        boolean havePrimaryValue = false;
        for (OrmClassFieldMeta primaryKeyField : primaryKeyFields) {
            Object primaryValue = null;
            try {
                primaryValue = primaryKeyField.getField().get(obj);
            } catch (IllegalAccessException e) {
                throw new SmartDbException(e);
            }
            if (primaryValue != null) {
                String name = primaryKeyField.getOrmColumnName();
                name = columnNameConverter.fieldConvertToColumn(name);
                whereAnd(String.format("`%s` = ?", name), primaryValue);
                havePrimaryValue = true;
            }
        }

        if (!havePrimaryValue) {
            throw new SmartDbException("Entity's primary keys are null, it will delete all data in database");
        }
        return this;
    }


    @Override
    public DeleteSqlBuilder whereAnd(String where) {
        ensureAppendWhereAnd(where);
        return this;
    }

    @Override
    public <T> DeleteSqlBuilder whereAndIn(String column, List<T> inList) {
        if (inList == null || inList.isEmpty()) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(" in (");

        sb.append("'").append(inList.get(0).toString()).append("'");
        int len = inList.size();
        for (int i = 1; i < len; i++) {
            sb.append(",'").append(inList.get(i).toString()).append("'");
        }
        sb.append(") ");
        ensureAppendWhereAnd(sb.toString());
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
    public DeleteSqlBuilder whereAnd(String where, Object... param) {
        ensureAppendWhereAnd(where);
        ensureAddWhereParams(param);
        return this;
    }

    @Override
    public DeleteSqlBuilder whereAndLikeColumns(String like, String... columns) {
        if (columns == null || columns.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(like, true, columns);
        return this;
    }

    @Override
    public DeleteSqlBuilder whereAndLikePatterns(String column, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        appendLikeClause4MultiLikes(column, true, likes);
        return this;
    }

    @Override
    public DeleteSqlBuilder whereOr(String where) {
        ensureAppendWhereOr(where);
        return this;
    }

    @Override
    public DeleteSqlBuilder whereOr(String where, Object... param) {
        ensureAppendWhereOr(where);
        ensureAddWhereParams(param);
        return this;
    }

    @Override
    public DeleteSqlBuilder whereOrLikeColumns(String like, String... column) {
        if (column == null || column.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(like, false, column);
        return this;
    }

    @Override
    public DeleteSqlBuilder whereOrLikePatterns(String column, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        appendLikeClause4MultiLikes(column, false, likes);
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
    public List<Object> getAllParams() {
        return whereParamList == null ? new ArrayList<Object>(0) : whereParamList;
    }

    @Override
    public int execute() {
        return this.smartDbNode.execute(this);
    }

}

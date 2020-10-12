package net.lab1024.smartdb.sqlbuilder.impl;

import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.mapping.handler.AbstractListHandler;
import net.lab1024.smartdb.mapping.handler.ResultSetHandler;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.pagination.PaginateParam;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.sqlbuilder.AbstractSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.PatternLocationEnum;
import net.lab1024.smartdb.sqlbuilder.SelectSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.SqlBuilderType;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSelectSqlBuilder extends AbstractSqlBuilder implements SelectSqlBuilder {
    protected StringBuilder selectClause = null;
    protected StringBuilder fromClause = null;
    protected StringBuilder joinClause = null;
    protected StringBuilder whereClause = null;
    protected StringBuilder groupbyClause = null;
    protected StringBuilder havingClause = null;
    protected StringBuilder orderbyClause = null;
    protected StringBuilder limitClause = null;

    protected List<Object> selectParams = null;
    protected List<Object> whereParams = null;
    protected List<Object> havingParam = null;

    public AbstractSelectSqlBuilder(SmartDbNode smartDbNode) {
        super(smartDbNode);
        this.selectClause = new StringBuilder();
        this.fromClause = new StringBuilder();
        this.joinClause = new StringBuilder();
        this.whereClause = new StringBuilder();
        this.groupbyClause = new StringBuilder();
        this.havingClause = new StringBuilder();
        this.orderbyClause = new StringBuilder();
    }

    private void ensureAddSelectParams(Object... params) {
        if (params == null || params.length == 0) {
            return;
        }

        if (this.selectParams == null) {
            this.selectParams = new ArrayList<Object>();
        }

        for (Object param : params) {
            this.selectParams.add(param);
        }
    }

    private void ensureAddWhereParams(Object... params) {
        if (params == null || params.length == 0) {
            return;
        }

        if (this.whereParams == null) {
            this.whereParams = new ArrayList<Object>();
        }

        for (Object param : params) {
            this.whereParams.add(param);
        }
    }

    private void ensureAddHavingParams(Object... params) {
        if (params == null || params.length == 0) {
            return;
        }

        if (this.havingParam == null) {
            this.havingParam = new ArrayList<Object>();
        }

        for (Object param : params) {
            this.havingParam.add(param);
        }
    }

    @Override
    public SqlBuilderType getSqlBuilderType() {
        return SqlBuilderType.SELECT;
    }

    @Override
    public SelectSqlBuilder appendSql(String sqlClause) {
        appendSqlBuilder.append(BLANK).append(sqlClause);
        return this;
    }

    @Override
    public SelectSqlBuilder select(String columns) {
        if (selectClause.length() == 0) {
            selectClause.append("SELECT ").append(columns);
        } else {
            selectClause.append(" , ").append(columns);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder select(String columns, Object... params) {
        this.select(columns);
        ensureAddSelectParams(params);
        return this;
    }

    @Override
    public SelectSqlBuilder from(String table) {
        if (this.fromClause.length() == 0) {
            this.fromClause.append(" FROM ").append(table);
        } else {
            this.fromClause.append(" , ").append(table);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder from(String... tables) {
        if (tables != null && tables.length > 0) {
            from(tables[0]);
            for (int i = 1; i < tables.length; i++) {
                this.fromClause.append(BLANK).append(",").append(tables[i]);
            }
        }
        return this;
    }

    @Override
    public SelectSqlBuilder from(Class entityClass) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        if (classMeta != null) {
            this.from(classMeta.getTableName(this.tableNameConverter));
        }
        return this;
    }

    @Override
    public SelectSqlBuilder from(Class entityClass, String aliasTableName) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        if (classMeta != null) {
            this.from(classMeta.getTableName(this.tableNameConverter));
            if (aliasTableName != null) {
                this.fromClause.append(" ").append(aliasTableName);
            }
        }
        return this;
    }

    @Override
    public SelectSqlBuilder joinInner(String joinClause) {
        if (this.joinClause.length() == 0) {
            this.joinClause.append(" INNER JOIN ").append(joinClause);
        } else {
            this.joinClause.append(BLANK).append(" INNER JOIN ").append(joinClause);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder joinRight(String joinClause) {
        if (this.joinClause.length() == 0) {
            this.joinClause.append(" RIGHT JOIN ").append(joinClause);
        } else {
            this.joinClause.append(BLANK).append(" RIGHT JOIN ").append(joinClause);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder joinLeft(String joinClause) {
        if (this.joinClause.length() == 0) {
            this.joinClause.append(" LEFT JOIN ").append(joinClause);
        } else {
            this.joinClause.append(BLANK).append(" LEFT JOIN ").append(joinClause);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder joinFull(String joinClause) {
        if (this.joinClause.length() == 0) {
            this.joinClause.append(" FULL JOIN ").append(joinClause);
        } else {
            this.joinClause.append(BLANK).append(" FULL JOIN ").append(joinClause);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder whereAnd(String where) {
        ensureAppendWhereAnd(where);
        return this;
    }

    @Override
    public <T> SelectSqlBuilder whereAndIn(String column, List<T> inList) {
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

    @Override
    public <T> SelectSqlBuilder whereAndNotIn(String column, List<T> inList) {
        if (inList == null || inList.isEmpty()) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(" not in (");

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

    @Override
    public SelectSqlBuilder whereAnd(String where, Object... param) {
        ensureAppendWhereAnd(where);
        ensureAddWhereParams(param);
        return this;
    }

    @Override
    public SelectSqlBuilder whereOr(String where) {
        ensureAppendWhereOr(where);
        return this;
    }

    @Override
    public SelectSqlBuilder whereOr(String where, Object... param) {
        ensureAppendWhereOr(where);
        ensureAddWhereParams(param);
        return this;
    }

    @Override
    public SelectSqlBuilder whereOrLikeColumns(String like, String... column) {
        if (column == null || column.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(like, false, column);
        return this;
    }

    @Override
    public SelectSqlBuilder whereOrLikePatterns(String column, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        appendLikeClause4MultiLikes(column, false, likes);
        return this;
    }

    @Override
    public SelectSqlBuilder groupby(String groupby) {
        if (groupbyClause.length() == 0) {
            groupbyClause.append("GROUP BY ").append(groupby);
        } else {
            groupbyClause.append(",").append(groupby);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder havingAnd(String condition) {
        if (havingClause.length() == 0) {
            havingClause.append("HAVING ").append(condition);
        } else {
            havingClause.append(" AND ").append(condition);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder havingAnd(String condition, Object... param) {
        havingAnd(condition);
        ensureAddHavingParams(param);
        return this;
    }

    @Override
    public SelectSqlBuilder havingOr(String condition) {
        if (havingClause.length() == 0) {
            havingClause.append("HAVING ").append(condition);
        } else {
            havingClause.append(" OR ").append(condition);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder havingOr(String condition, Object... param) {
        havingOr(condition);
        ensureAddHavingParams(param);
        return this;
    }

    @Override
    public SelectSqlBuilder orderby(String column, boolean isAsc) {
        if (orderbyClause.length() == 0) {
            orderbyClause.append("ORDER BY ").append(column).append(isAsc ? " ASC" : " DESC");
        } else {
            orderbyClause.append(" ,").append(column).append(isAsc ? " ASC" : " DESC");
        }
        return this;
    }

    @Override
    public SelectSqlBuilder orderby(String orderbyClauseString) {
        if (orderbyClause.length() == 0) {
            orderbyClause.append("ORDER BY ").append(orderbyClauseString);
        } else {
            orderbyClause.append(" ,").append(orderbyClauseString);
        }
        return this;
    }

    @Override
    public SelectSqlBuilder limit(long offset, long rowCount) {
        this.limitClause = new StringBuilder();
        this.limitClause.append("LIMIT ").append(offset).append(",").append(rowCount);
        return this;
    }

    @Override
    public String generateSql() {
        return generateSql(false);
    }

    @Override
    public String generateSql(boolean isPretty) {
        StringBuilder selectSqlBuilder = new StringBuilder();
        //select
        if (selectClause != null && selectClause.length() > 0) {
            selectSqlBuilder.append(selectClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }
        //from
        if (fromClause != null && fromClause.length() > 0) {
            selectSqlBuilder.append(fromClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }

        //join
        if (joinClause != null && joinClause.length() > 0) {
            selectSqlBuilder.append(joinClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }

        //where
        if (whereClause != null && whereClause.length() > 0) {
            selectSqlBuilder.append(whereClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }

        //group by
        if (groupbyClause != null && groupbyClause.length() > 0) {
            selectSqlBuilder.append(groupbyClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }


        //having
        if (havingClause != null && havingClause.length() > 0) {
            selectSqlBuilder.append(havingClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }

        //order by
        if (orderbyClause != null && orderbyClause.length() > 0) {
            selectSqlBuilder.append(orderbyClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }

        //limit
        if (limitClause != null && limitClause.length() > 0) {
            selectSqlBuilder.append(limitClause).append(isPretty ? LINE_SEPARATOR : BLANK);
        }

        //append
        selectSqlBuilder.append(appendSqlBuilder);

        return selectSqlBuilder.toString();
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
    public SelectSqlBuilder whereAndLikeColumns(String like, String... column) {
        if (column == null || column.length < 1) {
            return this;
        }
        appendLikeClause4MultiColumns(like, true, column);
        return this;
    }

    @Override
    public SelectSqlBuilder whereAndLikePatterns(String column, String... likes) {
        if (likes == null || likes.length < 1) {
            return this;
        }
        appendLikeClause4MultiLikes(column, true, likes);
        return this;
    }

    @Override
    public <T> SelectSqlBuilder whereOrIn(String column, List<T> inList) {
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
        ensureAppendWhereOr(sb.toString());
        return this;
    }

    @Override
    public List<Object> getSelectParams() {
        return selectParams;
    }

    @Override
    public List<Object> getWhereParams() {
        return whereParams;
    }

    @Override
    public List<Object> getHavingParams() {
        return havingParam;
    }

    @Override
    public List<Object> getAllParams() {
        int count = selectParams == null ? 0 : selectParams.size() + (whereParams == null ? 0 : whereParams.size()) + (havingParam == null ? 0 : havingParam.size());
        ArrayList<Object> list = new ArrayList<Object>(count);
        if (selectParams != null) {
            list.addAll(selectParams);
        }
        if (whereParams != null) {
            list.addAll(whereParams);
        }
        if (havingParam != null) {
            list.addAll(havingParam);
        }
        return list;
    }

    @Override
    public String toString() {
        return generateSql();
    }

    @Override
    public <T> T queryFirst(Class<T> cls) {
        return this.smartDbNode.queryFirst(cls, this);
    }


    @Override
    public <T> T queryFirst(ScalarHandler<T> resultSetHandler) {
        return this.smartDbNode.query(resultSetHandler, this);
    }

    @Override
    public <T> List<T> queryList(AbstractListHandler<T> resultSetHandler) {
        return this.smartDbNode.query(resultSetHandler, this);
    }

    @Override
    public <T> List<T> queryList(Class<T> cls) {
        return this.smartDbNode.queryList(cls, this);
    }

    @Override
    public <T> PaginateResult<T> paginate(Class<T> cls, PaginateParam paginateParam) {
        return this.smartDbNode.paginate(cls, paginateParam, this);
    }

    @Override
    public <T> PaginateResult<T> paginate(AbstractListHandler<T> handler, PaginateParam paginateParam) {
        return this.smartDbNode.paginate(handler, paginateParam, this);
    }

    @Override
    public <T> T query(ResultSetHandler<T> handler) {
        return this.smartDbNode.query(handler, this);
    }
}

package net.lab1024.smartdb.ext.dao;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.mapping.reflect.OrmClassFieldMeta;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.pagination.PaginateParam;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.sqlbuilder.DeleteSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.SelectSqlBuilder;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 通用的smartdb 父类
 *
 * @author zhuoluodada@qq.com
 */
public abstract class SmartDbDao<T> {

    protected Class<T> entityClass;

    public SmartDbDao() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public long count() {
        SmartDb smartDb = getSmartDb();
        SelectSqlBuilder selectSqlBuilder = smartDb.selectSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        selectSqlBuilder.select("count(1)");
        selectSqlBuilder.from(classMeta.getTableName(smartDb.getTableNameConverter()));
        return smartDb.query(ScalarHandler.Long, selectSqlBuilder);
    }


    public List<T> selectAll() {
        SmartDb smartDb = getSmartDb();
        SelectSqlBuilder selectSqlBuilder = smartDb.selectSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        selectSqlBuilder.select("*");
        selectSqlBuilder.from(classMeta.getTableName(smartDb.getTableNameConverter()));
        return smartDb.queryList(entityClass, selectSqlBuilder);
    }

    public T selectByPrimaryKey(Serializable... keys) {
        SmartDb smartDb = getSmartDb();
        SelectSqlBuilder selectSqlBuilder = smartDb.selectSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        selectSqlBuilder.select("*");
        selectSqlBuilder.from(classMeta.getTableName(smartDb.getTableNameConverter()));
        SupportDatabaseType supportDatabaseType = smartDb.getSupportDatabaseType();
        OrmClassFieldMeta[] primaryFields = classMeta.getPrimaryKeyFields();
        for (int i = 0; i < primaryFields.length; i++) {
            OrmClassFieldMeta f = primaryFields[i];
            String name = f.getOrmColumnName();
            selectSqlBuilder.whereAnd(String.format("%s = ?", supportDatabaseType.getSqlBuilderFactory().wrapSpecialCharacterField(name)), keys[i]);
        }
        return smartDb.queryFirst(entityClass, selectSqlBuilder);
    }

    public PaginateResult<T> paginate(PaginateParam paginateParam) {
        SmartDb smartDb = getSmartDb();
        SelectSqlBuilder selectSqlBuilder = smartDb.selectSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        selectSqlBuilder.select("*");
        selectSqlBuilder.from(classMeta.getTableName(smartDb.getTableNameConverter()));
        return smartDb.paginate(entityClass, paginateParam, selectSqlBuilder);
    }

    public int delete(T t) {
        return getSmartDb().delete(t);
    }

    public int deleteByPrimaryKey(Serializable... keys) {
        SmartDb smartDb = getSmartDb();
        DeleteSqlBuilder deleteSqlBuilder = smartDb.deleteSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        deleteSqlBuilder.table(classMeta.getTableName(smartDb.getTableNameConverter()));
        SupportDatabaseType supportDatabaseType = smartDb.getSupportDatabaseType();
        OrmClassFieldMeta[] primaryFields = classMeta.getPrimaryKeyFields();
        for (int i = 0; i < primaryFields.length; i++) {
            OrmClassFieldMeta f = primaryFields[i];
            String name = f.getOrmColumnName();
            deleteSqlBuilder.whereAnd(String.format("%s = ?", supportDatabaseType.getSqlBuilderFactory().wrapSpecialCharacterField(name)), keys[i]);
        }
        return smartDb.execute(deleteSqlBuilder);
    }

    public int updateSelective(T t) {
        return getSmartDb().updateSelective(t);
    }

    public int update(T t) {
        return getSmartDb().update(t);
    }

    public T insert(T t) {
        return getSmartDb().insert(t);
    }

    public T insertSelective(T t) {
        return getSmartDb().insertSelective(t);
    }

    public abstract SmartDb getSmartDb();

}

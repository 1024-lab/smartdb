package net.lab1024.smartdb.ext.spring;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.mapping.reflect.OrmClassFieldMeta;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;
import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.pagination.PaginateParam;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.sqlbuilder.DeleteSqlBuilder;
import net.lab1024.smartdb.sqlbuilder.SelectSqlBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 通用的smartdb 基类，为spring而生
 *
 * @author zhuoluodada@qq.com
 */
public abstract class SmartDbDaoSupport<T> {

    @Autowired
    protected SmartDb smartDb;

    protected Class<T> entityClass;

//    public abstract void setSmartDb(SmartDb smartDb);

    public SmartDbDaoSupport() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            this.entityClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            throw new SmartDbException("pls use Generic Type for class : " + getClass().getName());
        }
    }

    public long count() {
        SelectSqlBuilder selectSqlBuilder = smartDb.selectSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        selectSqlBuilder.select("count(1)");
        selectSqlBuilder.from(classMeta.getTableName(smartDb.getTableNameConverter()));
        return smartDb.query(ScalarHandler.Long, selectSqlBuilder);
    }

    public List<T> batchInsert(List<T> list) {
        return smartDb.batchInsert(list);
    }

    public List<T> selectAll() {
        SelectSqlBuilder selectSqlBuilder = smartDb.selectSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        selectSqlBuilder.select("*");
        selectSqlBuilder.from(classMeta.getTableName(smartDb.getTableNameConverter()));
        return smartDb.queryList(entityClass, selectSqlBuilder);
    }

    public T selectByPrimaryKey(Serializable... keys) {
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
        SelectSqlBuilder selectSqlBuilder = smartDb.selectSqlBuilder();
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(entityClass);
        selectSqlBuilder.select("*");
        selectSqlBuilder.from(classMeta.getTableName(smartDb.getTableNameConverter()));
        return smartDb.paginate(entityClass, paginateParam, selectSqlBuilder);
    }

    public int delete(T t) {
        return smartDb.delete(t);
    }

    public int deleteAll() {
        return smartDb.deleteSqlBuilder().table(entityClass).execute();
    }

    public int deleteByPrimaryKey(Serializable... keys) {
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
        return smartDb.updateSelective(t);
    }

    public int update(T t) {
        return smartDb.update(t);
    }

    public T insert(T t) {
        return smartDb.insert(t);
    }

    public T insertSelective(T t) {
        return smartDb.insertSelective(t);
    }

}

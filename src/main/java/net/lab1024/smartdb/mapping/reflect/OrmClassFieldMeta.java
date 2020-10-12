package net.lab1024.smartdb.mapping.reflect;

import java.lang.reflect.Field;

/**
 * @author zhuoluodada@qq.com
 */
public class OrmClassFieldMeta {

    private String columnAliasName;
    private Field field;

    public OrmClassFieldMeta(String columnAliasName, Field field) {
        this.columnAliasName = columnAliasName;
        this.field = field;
    }

    public String getOrmColumnName() {
        return columnAliasName == null ? this.field.getName() : columnAliasName;
    }

    public Field getField() {
        return field;
    }

    void setField(Field field) {
        this.field = field;
    }

}

package net.lab1024.smartdb;

import net.lab1024.smartdb.database.SupportDatabaseType;
import net.lab1024.smartdb.ext.SmartDbExtEnum;
import net.lab1024.smartdb.filter.SmartDbFilter;
import net.lab1024.smartdb.mapping.rowconvertor.RowConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuoda
 */
public class SmartDbConfig {

    protected SupportDatabaseType supportDatabaseType;
    protected SmartDbExtEnum smartDbExtEnum;
    protected TableNameConverter tableNameConverter;
    protected ColumnNameConverter columnNameConverter;
    protected RowConverter rowConverter;
    protected boolean showSql = false;
    protected List<SmartDbFilter> filters = new ArrayList<SmartDbFilter>();

    public SupportDatabaseType getSupportDatabaseType() {
        return supportDatabaseType;
    }

    public SmartDbExtEnum getSmartDbExtEnum() {
        return smartDbExtEnum;
    }

    public TableNameConverter getTableNameConverter() {
        return tableNameConverter;
    }

    public ColumnNameConverter getColumnNameConverter() {
        return columnNameConverter;
    }

    public RowConverter getRowConverter() {
        return rowConverter;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public List<SmartDbFilter> getFilters() {
        return filters;
    }


}

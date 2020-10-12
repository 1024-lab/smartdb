package net.lab1024.smartdb.sqlbuilder;


import net.lab1024.smartdb.SmartDbNode;
import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;
import net.lab1024.smartdb.sqlbuilder.convertor.TableNameConverter;

public abstract class AbstractSqlBuilder {

    protected static final String BLANK = " ";
    protected static final String PARAM_PLACEHOLDER = "?";
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");

    protected TableNameConverter tableNameConverter;
    protected ColumnNameConverter columnNameConverter;

    protected SmartDbNode smartDbNode;

    protected StringBuilder appendSqlBuilder = new StringBuilder();

    public AbstractSqlBuilder(SmartDbNode smartDbNode) {
        this.smartDbNode = smartDbNode;
        this.tableNameConverter = smartDbNode.getTableNameConverter();
        this.columnNameConverter = smartDbNode.getColumnNameConverter();
    }

}

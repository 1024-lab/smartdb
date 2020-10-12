package net.lab1024.smartdb.sqlbuilder.convertor;

/**
 * 数据库表的列名和 java类的字段名的转换器
 */
public interface ColumnNameConverter {

    /**
     * 数据库的列名转为 java类的字段名
     * @param column
     * @return
     */
    String columnConvertToField(String column);

    /**
     * java类的字段名转为数据库的列名
     * @param filed
     * @return
     */
    String fieldConvertToColumn(String filed);
}

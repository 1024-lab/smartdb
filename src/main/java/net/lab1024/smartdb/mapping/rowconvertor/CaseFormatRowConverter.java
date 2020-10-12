package net.lab1024.smartdb.mapping.rowconvertor;


import net.lab1024.smartdb.sqlbuilder.convertor.ColumnNameConverter;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author zhuoluodada@qq.com
 */
public class CaseFormatRowConverter extends BaseRowConverter {

    private ColumnNameConverter columnNameConverter;

    public CaseFormatRowConverter(Map<String, String> columnToPropertyOverrides, ColumnNameConverter columnNameConverter) {
        super(columnToPropertyOverrides);
        this.columnNameConverter = columnNameConverter;
    }

    public CaseFormatRowConverter(ColumnNameConverter columnNameConverter) {
        this.columnNameConverter = columnNameConverter;
    }

    @Override
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        ConverterClassInfo classInfo = ConverterClassCache.getConvertClassInfo(type);
        PropertyDescriptor[] props = classInfo.getPropertyDescriptors();
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, classInfo);
        return this.createBean(rs, type, props, columnToProperty,rsmd);
    }

    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
        if (!rs.next()) {
            return Collections.emptyList();
        }
        ConverterClassInfo classInfo = ConverterClassCache.getConvertClassInfo(type);
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, classInfo);
        List<T> results = new ArrayList<T>();
        do {
            results.add(this.createBean(rs, type, classInfo.getPropertyDescriptors(), columnToProperty,rsmd));
        } while (rs.next());

        return results;
    }

    protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, ConverterClassInfo converterClassInfo) throws SQLException {
        int cols = rsmd.getColumnCount();
        int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);
            String propertyName = null;
            if (columnToPropertyOverrides.isEmpty()) {
                propertyName = columnName;
            } else {
                propertyName = columnToPropertyOverrides.get(columnName);
                if (propertyName == null) {
                    propertyName = columnName;
                }
            }

            String convertPropertyName = columnNameConverter.columnConvertToField(propertyName);
            Integer index = converterClassInfo.getFieldIndex().get(convertPropertyName);
            if (index == null) {
                index = converterClassInfo.getFieldIndex().get(propertyName);
            }
            columnToProperty[col] = index == null ? PROPERTY_NOT_FOUND : index.intValue();
        }

        return columnToProperty;
    }

    @Override
    public Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnLabel(i);
            result.put(columnName, rs.getObject(i));
        }

        return result;
    }


}

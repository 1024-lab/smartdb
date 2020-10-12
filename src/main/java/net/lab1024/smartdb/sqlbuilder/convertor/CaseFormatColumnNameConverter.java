package net.lab1024.smartdb.sqlbuilder.convertor;

import com.google.common.base.CaseFormat;

public class CaseFormatColumnNameConverter implements ColumnNameConverter {

    private CaseFormat fieldCaseFormat;
    private CaseFormat columnCaseFormat;

    public CaseFormatColumnNameConverter(CaseFormat field, CaseFormat column) {
        this.fieldCaseFormat = field;
        this.columnCaseFormat = column;
    }


    @Override
    public String columnConvertToField(String column) {
        return columnCaseFormat.to(fieldCaseFormat, column);
    }

    @Override
    public String fieldConvertToColumn(String filed) {
        return fieldCaseFormat.to(columnCaseFormat, filed);
    }
}

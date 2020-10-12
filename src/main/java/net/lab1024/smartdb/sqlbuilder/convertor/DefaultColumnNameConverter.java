package net.lab1024.smartdb.sqlbuilder.convertor;


// 不做任何转换的类名字

public class DefaultColumnNameConverter implements ColumnNameConverter {

    @Override
    public String columnConvertToField(String column) {
        return column;
    }

    @Override
    public String fieldConvertToColumn(String filed) {
        return filed;
    }
}

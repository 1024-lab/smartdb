package net.lab1024.smartdb.mapping.rowconvertor;

import java.beans.PropertyDescriptor;
import java.util.HashMap;

/**
 * @author zhuoluodada@qq.com
 */
class ConverterClassInfo {
    PropertyDescriptor[] propertyDescriptors = null;
    HashMap<String, Integer> fieldIndex = null;

    public ConverterClassInfo(PropertyDescriptor[] propertyDescriptors, HashMap<String, Integer> fieldIndex) {
        this.propertyDescriptors = propertyDescriptors;
        this.fieldIndex = fieldIndex;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }

    public HashMap<String, Integer> getFieldIndex() {
        return fieldIndex;
    }
}

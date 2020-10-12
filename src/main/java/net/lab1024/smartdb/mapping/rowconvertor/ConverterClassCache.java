package net.lab1024.smartdb.mapping.rowconvertor;


import net.lab1024.smartdb.mapping.reflect.SmartDbOrmClassMetaCache;
import net.lab1024.smartdb.exception.SmartDbException;
import net.lab1024.smartdb.mapping.reflect.OrmClassMeta;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuoluodada@qq.com
 */
class ConverterClassCache {

    private static final ConcurrentHashMap<Class<?>, ConverterClassInfo> CLASS_FIELD_CACHE = new ConcurrentHashMap<Class<?>, ConverterClassInfo>();

    public static ConverterClassInfo getConvertClassInfo(Class<?> cls) {
        ConverterClassInfo clsInfo = CLASS_FIELD_CACHE.get(cls);
        if (clsInfo == null) {
            clsInfo = createConverterClassInfo(cls);
            ConverterClassInfo prev = CLASS_FIELD_CACHE.putIfAbsent(cls, clsInfo);
            if (prev != null) {
                clsInfo = prev;
            }
        }
        return clsInfo;
    }

    private static ConverterClassInfo createConverterClassInfo(Class<?> c) {
        OrmClassMeta classMeta = SmartDbOrmClassMetaCache.getClassMeta(c);
        PropertyDescriptor[] propertyDescriptors = propertyDescriptors(c);
        HashMap<String, Integer> fieldIndex = new HashMap<String, Integer>(propertyDescriptors.length);
        for (int i = 0; i < propertyDescriptors.length; i++) {
            fieldIndex.put(propertyDescriptors[i].getName(), i);
        }
        ConverterClassInfo clsInfo = new ConverterClassInfo(propertyDescriptors, fieldIndex);
        return clsInfo;
    }

    private static PropertyDescriptor[] propertyDescriptors(Class<?> c) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);
        } catch (IntrospectionException e) {
            throw new SmartDbException("Bean introspection failed: " + e.getMessage());
        }
        return beanInfo.getPropertyDescriptors();
    }


}

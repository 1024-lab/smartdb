package net.lab1024.smartdb.mapping.reflect;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuoluodada@qq.com
 */
public class SmartDbOrmClassMetaCache {

    private static final ConcurrentHashMap<Class<?>, OrmClassMeta> CLASS_FIELD_CACHE = new ConcurrentHashMap<Class<?>, OrmClassMeta>();

    public static OrmClassMeta getClassMeta(Class<?> cls) {
        OrmClassMeta meta = CLASS_FIELD_CACHE.get(cls);
        if (meta == null) {
            meta = new OrmClassMeta(cls);
            OrmClassMeta prev = CLASS_FIELD_CACHE.putIfAbsent(cls, meta);
            if(prev != null){
                meta = prev;
            }
        }
        return meta;
    }


}

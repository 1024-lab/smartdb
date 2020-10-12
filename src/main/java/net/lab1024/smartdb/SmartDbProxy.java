package net.lab1024.smartdb;


import net.lab1024.smartdb.filter.SmartDbFilter;
import net.lab1024.smartdb.filter.SmartDbFilterChain;
import net.lab1024.smartdb.filter.SmartDbFilterChainImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 代理类，使用jdk自带代理
 *
 * @author zhuoluodada@qq.com
 */
class SmartDbProxy implements InvocationHandler {

    private SmartDb db;

    private SmartDbFilterChainImpl smartDbFilterChain;

    SmartDbProxy(SmartDb db, List<SmartDbFilter> filters) {
        this.db = db;
        this.smartDbFilterChain = new SmartDbFilterChainImpl();
        if (filters != null) {
            for (SmartDbFilter filter : filters) {
                this.smartDbFilterChain.addFilter(filter);
            }
        }
    }

    SmartDb getInstance() {
        Class<?>[] interfaces = db.getClass().getInterfaces();

        Class interfaceClass = null;
        for (Class<?> anInterface : interfaces) {
            if (SmartDb.class.isAssignableFrom(anInterface)) {
                interfaceClass = anInterface;
                break;
            }
        }

        return (SmartDb) Proxy.newProxyInstance(db.getClass().getClassLoader(),
                new Class[]{interfaceClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SmartDbFilterChain chain = new SmartDbFilterChainImpl();
        return chain.doFilter(db, method, args);
    }
}

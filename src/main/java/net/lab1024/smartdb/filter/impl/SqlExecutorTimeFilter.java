package net.lab1024.smartdb.filter.impl;

import net.lab1024.smartdb.filter.SmartDbFilter;
import net.lab1024.smartdb.filter.SmartDbFilterChainImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 监控sql的执行时间
 *
 * @author zhuoluodada@qq.com
 */
public class SqlExecutorTimeFilter implements SmartDbFilter {

    private static final Logger LOG = LoggerFactory.getLogger(SqlExecutorTimeFilter.class);

    @Override
    public Object doFilter(Object proxy, Method method, Object[] args, SmartDbFilterChainImpl chain) throws InvocationTargetException, IllegalAccessException {
        long begin = System.currentTimeMillis();
        Object result = chain.doFilter(proxy, method, args);
        long useTime = System.currentTimeMillis() - begin;
        LOG.debug("method use time mills : {} , method : {} ", useTime, method.getName());
        return result;

    }

}

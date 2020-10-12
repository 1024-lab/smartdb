package net.lab1024.smartdb.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * sql执行过滤器
 *
 * @author zhuoluodada@qq.com
 */
public interface SmartDbFilter {

    Object doFilter(Object proxy, Method method, Object[] args, SmartDbFilterChainImpl chain) throws InvocationTargetException, IllegalAccessException;

}

package net.lab1024.smartdb.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhuoluodada@qq.com
 */
public interface SmartDbFilterChain {

    Object doFilter(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}

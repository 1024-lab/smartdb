package net.lab1024.smartdb.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuoluodada@qq.com
 */
public class SmartDbFilterChainImpl implements SmartDbFilterChain {

    private List<SmartDbFilter> filters = new ArrayList<SmartDbFilter>();

    private int index = 0;

    public void addFilter(SmartDbFilter f) {
        filters.add(f);
    }

    @Override
    public Object doFilter(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (index == filters.size()) {
            return method.invoke(proxy, args);
        }
        SmartDbFilter filter = filters.get(index++);
        return filter.doFilter(proxy, method, args, this);
    }
}

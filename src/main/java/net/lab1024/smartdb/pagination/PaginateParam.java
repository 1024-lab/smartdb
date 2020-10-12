package net.lab1024.smartdb.pagination;

/**
 * @author zhuoluodada@qq.com
 */
public interface PaginateParam {

    int getPageNumber();

    int getPageSize();

    boolean isSearchCount();
}

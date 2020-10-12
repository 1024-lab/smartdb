package net.lab1024.smartdb.pagination;

/**
 * @author zhuoluodada@qq.com
 */
public class SmartDbPaginateParam implements PaginateParam {
    protected int pageNumber;
    protected int pageSize;
    protected boolean searchCount;

    public SmartDbPaginateParam() {
    }

    public SmartDbPaginateParam(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public SmartDbPaginateParam(int pageNumber, int pageSize, boolean searchCount) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.searchCount = searchCount;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    public SmartDbPaginateParam setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public SmartDbPaginateParam setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public boolean isSearchCount() {
        return searchCount;
    }

    public SmartDbPaginateParam setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
        return this;
    }
}

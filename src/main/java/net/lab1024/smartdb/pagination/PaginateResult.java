package net.lab1024.smartdb.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhuoluodada@qq.com
 */
public class PaginateResult<T> implements Serializable{
    //当前页
    private int pageNumber;
    //每页的数量
    private int pageSize;
    //结果集
    private List<T> records;
    //当前页面第一个元素在数据库中的行号
    private int startRow;
    //当前页面最后一个元素在数据库中的行号
    private int endRow;
    //总记录数
    private long totalRecordCount;
    //总页数
    private int totalPageCount;
    //第一页
    private int firstPageNumber;
    //前一页
    private int prePageNumber;
    //下一页
    private int nextPageNumber;
    //最后一页
    private int lastPageNumber;
    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;
    //是否有前一页
    private boolean hasPreviousPage = false;
    //是否有下一页
    private boolean hasNextPage = false;
    //导航页码数
    private int navigatePageCount;
    //所有导航页号
    private int[] navigatePageNumbers;

    public PaginateResult(int pageNumber, int pageSize) {
        this(new ArrayList<T>(0),0, pageNumber, pageSize);
    }

    public PaginateResult(List<T> list, long totalRecordCount, int pageNumber, int pageSize) {
        this(list, totalRecordCount, pageNumber, pageSize, 8);
    }

    public PaginateResult(List<T> list, long totalRecordCount, int pageNumber, int pageSize, int navigatePageCount) {
        if (list == null) {
            this.records = new ArrayList<T>(0);
        } else {
            this.records = list;
        }
        this.totalRecordCount = totalRecordCount;
        if (this.totalRecordCount < 1) {
            this.totalPageCount = 0;
        } else if (totalRecordCount % pageSize == 0) {
            this.totalPageCount = (int) (totalRecordCount / pageSize);
        } else {
            this.totalPageCount = (int) (totalRecordCount / pageSize) + 1;
        }

        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.navigatePageCount = navigatePageCount;

        //计算导航页
        calcNavigatepageNums();
        //计算前后页，第一页，最后一页
        calcPage();
        //判断页面边界
        judgePageBoudary();
    }

    /**
     * 计算导航页
     */
    private void calcNavigatepageNums() {
        //当总页数小于或等于导航页码数时
        if (totalPageCount <= navigatePageCount) {
            navigatePageNumbers = new int[totalPageCount];
            for (int i = 0; i < totalPageCount; i++) {
                navigatePageNumbers[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatePageNumbers = new int[navigatePageCount];
            int startNum = pageNumber - navigatePageCount / 2;
            int endNum = pageNumber + navigatePageCount / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePageCount; i++) {
                    navigatePageNumbers[i] = startNum++;
                }
            } else if (endNum > totalPageCount) {
                endNum = totalPageCount;
                //最后navigatePages页
                for (int i = navigatePageCount - 1; i >= 0; i--) {
                    navigatePageNumbers[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePageCount; i++) {
                    navigatePageNumbers[i] = startNum++;
                }
            }
        }
    }

    /**
     * 计算前后页，第一页，最后一页
     */
    private void calcPage() {
        if (navigatePageNumbers != null && navigatePageNumbers.length > 0) {
            firstPageNumber = navigatePageNumbers[0];
            lastPageNumber = navigatePageNumbers[navigatePageNumbers.length - 1];
            if (pageNumber > 1) {
                prePageNumber = pageNumber - 1;
            }
            if (pageNumber < totalPageCount) {
                nextPageNumber = pageNumber + 1;
            }
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = pageNumber == 1;
        isLastPage = pageNumber == totalPageCount;
        hasPreviousPage = pageNumber > 1;
        hasNextPage = pageNumber < totalPageCount;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getFirstPageNumber() {
        return firstPageNumber;
    }

    public void setFirstPageNumber(int firstPageNumber) {
        this.firstPageNumber = firstPageNumber;
    }

    public int getPrePageNumber() {
        return prePageNumber;
    }

    
    public void setPrePageNumber(int prePageNumber) {
        this.prePageNumber = prePageNumber;
    }

    public int getNextPageNumber() {
        return nextPageNumber;
    }

    public void setNextPageNumber(int nextPageNumber) {
        this.nextPageNumber = nextPageNumber;
    }

    public int getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePageCount() {
        return navigatePageCount;
    }

    public void setNavigatePageCount(int navigatePageCount) {
        this.navigatePageCount = navigatePageCount;
    }

    public int[] getNavigatePageNumbers() {
        return navigatePageNumbers;
    }

    public void setNavigatePageNumbers(int[] navigatePageNumbers) {
        this.navigatePageNumbers = navigatePageNumbers;
    }

    public List<T> getRecords() {
        return records;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    @Override
    public String toString() {
        return "PaginateResult{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", totalRecordCount=" + totalRecordCount +
                ", totalPageCount=" + totalPageCount +
                ", records=" + records +
                ", firstPageNumber=" + firstPageNumber +
                ", prePageNumber=" + prePageNumber +
                ", nextPageNumber=" + nextPageNumber +
                ", lastPageNumber=" + lastPageNumber +
                ", isFirstPage=" + isFirstPage +
                ", isLastPage=" + isLastPage +
                ", hasPreviousPage=" + hasPreviousPage +
                ", hasNextPage=" + hasNextPage +
                ", navigatePageCount=" + navigatePageCount +
                ", navigatepageNumbers=" + Arrays.toString(navigatePageNumbers) +
                '}';
    }
}

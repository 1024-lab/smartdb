package net.lab1024.smartdb.pagination;

/**
 * @author zhuoluodada@qq.com
 */
public interface PaginateSqlGenerator {

    String generatePaginateSql(int pageNumber, int pageSize, String sql);

    String generateCountSql(int pageNumber, int pageSize, String sql);

}

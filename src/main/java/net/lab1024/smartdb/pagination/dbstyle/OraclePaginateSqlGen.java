package net.lab1024.smartdb.pagination.dbstyle;

import net.lab1024.smartdb.pagination.PaginateSqlGenerator;

/**
 * Oracle 分页实现类
 */
public class OraclePaginateSqlGen implements PaginateSqlGenerator {

    public static final OraclePaginateSqlGen INSTANCE = new OraclePaginateSqlGen();

    @Override
    public String generatePaginateSql(int pageNumber, int pageSize, String sql) {
        int offset = pageSize * (pageNumber - 1);
        StringBuilder ret = new StringBuilder();
        ret.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( ");
        ret.append(sql).append(" ) TMP WHERE ROWNUM <=").append(offset >= 1 ? offset + pageSize : pageSize);
        ret.append(") WHERE ROW_ID > ").append(offset);
        return ret.toString();
    }

    @Override
    public String generateCountSql(int pageNumber, int pageSize, String sql) {
        StringBuilder ret = new StringBuilder();
        ret.append(" select count(1) from (  ").append(sql).append(" )");
        return ret.toString();
    }

}

package net.lab1024.smartdb.pagination.dbstyle;


import net.lab1024.smartdb.pagination.PaginateSqlGenerator;

public class MysqlPaginateSqlGen implements PaginateSqlGenerator {

    public static final MysqlPaginateSqlGen INSTANCE = new MysqlPaginateSqlGen();

    @Override
    public String generatePaginateSql(int pageNumber, int pageSize, String sql) {
        int offset = pageSize * (pageNumber - 1);
        StringBuilder ret = new StringBuilder();
        ret.append(sql).append(" limit ").append(offset).append(", ").append(pageSize);
        return ret.toString();
    }

    @Override
    public String generateCountSql(int pageNumber, int pageSize, String sql) {
        StringBuilder ret = new StringBuilder();
        ret.append(" select count(1) from (  ").append(sql).append(" ) as t");
        return ret.toString();
    }

}

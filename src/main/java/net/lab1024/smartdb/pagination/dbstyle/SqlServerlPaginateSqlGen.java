package net.lab1024.smartdb.pagination.dbstyle;


import net.lab1024.smartdb.pagination.PaginateSqlGenerator;

public class SqlServerlPaginateSqlGen implements PaginateSqlGenerator {

    public static final SqlServerlPaginateSqlGen INSTANCE = new SqlServerlPaginateSqlGen();

    @Override
    public String generatePaginateSql(int pageNumber, int pageSize, String sql) {
        int end = pageNumber * pageSize;
        if (end <= 0) {
            end = pageSize;
        }
        int begin = (pageNumber - 1) * pageSize;
        if (begin < 0) {
            begin = 0;
        }
        StringBuilder ret = new StringBuilder();
        ret.append("SELECT * FROM ( SELECT row_number() over (order by tempcolumn) temprownumber, * FROM ");
        ret.append(" ( SELECT TOP ").append(end).append(" tempcolumn=0,");
        ret.append(sql.toString().replaceFirst("(?i)select", ""));
        ret.append(")vip)mvp where temprownumber>").append(begin);
        return ret.toString();
    }

    @Override
    public String generateCountSql(int pageNumber, int pageSize, String sql) {
        StringBuilder ret = new StringBuilder();
        ret.append(" select count(1) from (  ").append(sql).append(" ) as t");
        return ret.toString();
    }

}

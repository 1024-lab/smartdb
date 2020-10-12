package net.lab1024.smartdb.sqlbuilder.delete;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.mapping.handler.ScalarHandler;
import net.lab1024.smartdb.sqlbuilder.select.BaseSelectBuilderTest;
import net.lab1024.smartdb.sqlbuilder.select.SelectSqlBuilderEntity;
import org.junit.Assert;

import java.text.ParseException;
import java.util.Arrays;

public abstract class BaseDeleteBuilderTest extends BaseSelectBuilderTest {


    public void test() throws ParseException {

        testDeleteAll();

        testSelect();

        testWhereOr();

        testWhereAnd();

    }

    public void testDeleteAll() {
        int rows = getDb().deleteSqlBuilder()
                .table(SELECT_BUILD_TABLE_NAME)
                .execute();
        Assert.assertEquals(EntityList.size(), rows);
        Long count = getDb().selectSqlBuilder()
                .select("count(1)")
                .from(SELECT_BUILD_TABLE_NAME)
                .queryFirst(ScalarHandler.Long);
        Assert.assertEquals(0L, count.longValue());
    }


    public void testSelect() {
        SmartDb smartDb = getDb();
        String sql = smartDb.deleteSqlBuilder()
                .table(SELECT_BUILD_TABLE_NAME)
                .generateSql();

        Assert.assertEquals("DELETE FROM " + SELECT_BUILD_TABLE_NAME + " ", sql);

        sql = smartDb.deleteSqlBuilder()
                .table(SelectSqlBuilderEntity.class)
                .generateSql();

        Assert.assertEquals("DELETE FROM " + SELECT_BUILD_TABLE_NAME + " ", sql);
    }

    public void testWhereOr() {
        SmartDb smartDb = getDb();
        String whereOrSql = smartDb
                .deleteSqlBuilder()
                .table(SelectSqlBuilderEntity.class)
                .whereOr("id > 5")
                .whereOr("id < ?", 13)
                .whereOrLikePatterns("nick_name", "%name%", "%login")
                .whereOrLikeColumns("%city%", "city", "name")
                .generateSql();

        String sql = "DELETE FROM t_select_sql_builder WHERE" +
                " id > 5 OR id < ? " +
                "OR ( nick_name LIKE '%name%' OR nick_name LIKE '%login' ) " +
                "OR ( city LIKE '%city%' OR name LIKE '%city%' )";

        Assert.assertEquals(sql, whereOrSql);
    }

    public void testWhereAnd() {
        SmartDb smartDb = getDb();
        String whereAndSql = smartDb
                .deleteSqlBuilder()
                .table(SelectSqlBuilderEntity.class)
                .whereAnd("id > 5")
                .whereAnd("id < ?", 13)
                .whereAndLikePatterns("nick_name", "%name%", "%login")
                .whereAndLikeColumns("%city%", "city", "name")
                .whereAndIn("id", Arrays.asList(1, 2, 3))
                .generateSql();

        String sql = "DELETE FROM t_select_sql_builder WHERE" +
                " id > 5 AND id < ? " +
                "AND ( nick_name LIKE '%name%' OR nick_name LIKE '%login' ) " +
                "AND ( city LIKE '%city%' OR name LIKE '%city%' ) " +
                "AND id in ('1','2','3') " ;

        Assert.assertEquals(sql, whereAndSql);
    }

}

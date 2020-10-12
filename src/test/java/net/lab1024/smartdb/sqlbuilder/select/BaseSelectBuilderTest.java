package net.lab1024.smartdb.sqlbuilder.select;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.common.BaseTest;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.pagination.SmartDbPaginateParam;
import org.junit.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BaseSelectBuilderTest extends BaseTest {

    protected static final String SELECT_BUILD_TABLE_NAME = "t_select_sql_builder";
    protected static final List<SelectSqlBuilderEntity> EntityList = new ArrayList<SelectSqlBuilderEntity>();

    public abstract SmartDb getDb();

    public void test() throws ParseException {

        insertSelectData();

        //from
        testFrom();
        testFromAs();

        //join
        testLeftJoin();
        testRightJoin();
        testInnerJoin();
        testFullJoin();

        //where and
        testWhereAnd();

        //where or
        testWhereOr();

        //select
        testSelect();

        //select orm
        testSelectORM();

        //select paginate
        testSelectPaginate();

        //select group by
        testSelectGroupBy();

        //select group by having
        testSelectGroupByHaving();

        //test limit
        testSelectLimit();
    }

    protected void testSelectLimit() {
        int offset = 2;
        int count = 3;

        SmartDb smartDb = getDb();
        List<SelectSqlBuilderEntity> selectList = smartDb.selectSqlBuilder()
                .select("*")
                .from(SelectSqlBuilderEntity.class)
                .orderby("id ", true)
                .limit(offset, count)
                .queryList(SelectSqlBuilderEntity.class);

        for (int i = 0; i < count; i++) {
            Assert.assertEquals(EntityList.get(i + offset).getId(), selectList.get(i).getId());
        }
    }


    public void testSelectGroupByHaving() {
        final HashMap<Integer, Integer> age2count = new HashMap<Integer, Integer>();

        for (SelectSqlBuilderEntity selectSqlBuilderEntity : EntityList) {
            Integer count = age2count.get(selectSqlBuilderEntity.getAge());
            count = count == null ? 0 : count;
            count++;
            age2count.put(selectSqlBuilderEntity.getAge(), count);
        }

        SmartDb smartDb = getDb();
        List<Map> mapList = smartDb.selectSqlBuilder()
                .select("age")
                .select("count(1) as count")
                .from(SelectSqlBuilderEntity.class)
                .groupby("age")
                .havingAnd("age > 1")
                .havingAnd("age < ?", 100)
                .queryList(Map.class);

        for (Map<String, Object> map : mapList) {
            Integer age = (Integer) map.get("age");
            Long actualCount = (Long) map.get("count");
            Integer count = age2count.get(age);
            LOG.info("group by : age : {}, count: {} , actualCount：　{}", age, count, actualCount);
            Assert.assertEquals(count.intValue(), actualCount.intValue());
        }

        mapList = smartDb.selectSqlBuilder()
                .select("age")
                .select("count(1) as count")
                .from(SelectSqlBuilderEntity.class)
                .groupby("age")
                .havingOr("age > 0")
                .havingOr("age < ?", 100)
                .queryList(Map.class);

        Assert.assertEquals(age2count.size(), mapList.size());
        for (Map<String, Object> map : mapList) {
            Integer age = (Integer) map.get("age");
            Long actualCount = (Long) map.get("count");
            Integer count = age2count.get(age);
            LOG.info("group by : age : {}, count: {} , actualCount：　{}", age, count, actualCount);
            Assert.assertEquals(count.intValue(), actualCount.intValue());
        }

    }

    public void testSelectGroupBy() {
        SmartDb smartDb = getDb();
        List<Map> mapList = smartDb.selectSqlBuilder()
                .select("age")
                .select("count(1) as count")
                .from(SelectSqlBuilderEntity.class)
                .groupby("age")
                .queryList(Map.class);

        final HashMap<Integer, Integer> age2count = new HashMap<Integer, Integer>();

        for (SelectSqlBuilderEntity selectSqlBuilderEntity : EntityList) {
            Integer count = age2count.get(selectSqlBuilderEntity.getAge());
            count = count == null ? 0 : count;
            count++;
            age2count.put(selectSqlBuilderEntity.getAge(), count);
        }

        Assert.assertEquals(age2count.size(), mapList.size());
        for (Map<String, Object> map : mapList) {
            Integer age = (Integer) map.get("age");
            Long actualCount = (Long) map.get("count");
            Integer count = age2count.get(age);
            LOG.info("group by : age : {}, count: {} , actualCount：　{}", age, count, actualCount);
            Assert.assertEquals(count.intValue(), actualCount.intValue());
        }
    }

    public void testSelectPaginate() {
        SmartDb smartDb = getDb();
        int pageSize = 10;
        PaginateResult<SelectSqlBuilderEntity> paginate = smartDb.selectSqlBuilder()
                .select("*")
                .from(SelectSqlBuilderEntity.class)
                .paginate(SelectSqlBuilderEntity.class, new SmartDbPaginateParam(1, pageSize, true));

        for (int i = 0; i < pageSize; i++) {
            Assert.assertEquals(EntityList.get(i), paginate.getRecords().get(i));
        }

        Assert.assertEquals(EntityList.size(), paginate.getTotalRecordCount());
    }

    public void testSelectORM() {
        SmartDb smartDb = getDb();
        List<SelectSqlBuilderEntity> resultList = smartDb.selectSqlBuilder()
                .select("*")
                .from(SelectSqlBuilderEntity.class)
                .queryList(SelectSqlBuilderEntity.class);

        for (int i = 0; i < resultList.size(); i++) {
            Assert.assertEquals(EntityList.get(i), resultList.get(i));
        }
    }

    public void testSelect() {
        SmartDb smartDb = getDb();
        String sql = smartDb.selectSqlBuilder()
                .select("*")
                .from(SelectSqlBuilderEntity.class)
                .generateSql();

        Assert.assertEquals("SELECT *  FROM " + SELECT_BUILD_TABLE_NAME + " ", sql);

        sql = smartDb.selectSqlBuilder()
                .select("id,age")
                .select("city")
                .from(SelectSqlBuilderEntity.class)
                .generateSql();

        Assert.assertEquals("SELECT id,age , city  FROM " + SELECT_BUILD_TABLE_NAME + " ", sql);
    }

    public void testWhereOr() {
        SmartDb smartDb = getDb();
        String whereOrSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class)
                .whereOr("id > 5")
                .whereOr("id < ?", 13)
                .whereOrLikePatterns("nick_name", "%name%", "%login")
                .whereOrLikeColumns("%city%", "city", "name")
                .whereOrIn("id", Arrays.asList(1, 2, 3))
                .generateSql();

        String sql = " FROM t_select_sql_builder WHERE" +
                " id > 5 OR id < ? " +
                "OR ( nick_name LIKE '%name%' OR nick_name LIKE '%login' ) " +
                "OR ( city LIKE '%city%' OR name LIKE '%city%' ) " +
                "OR id in ('1','2','3')  ";

        Assert.assertEquals(sql, whereOrSql);
    }

    public void testWhereAnd() {
        SmartDb smartDb = getDb();
        String whereAndSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class)
                .whereAnd("id > 5")
                .whereAnd("id < ?", 13)
                .whereAndLikePatterns("nick_name", "%name%", "%login")
                .whereAndLikeColumns("%city%", "city", "name")
                .whereAndIn("id", Arrays.asList(1, 2, 3))
                .whereAndNotIn("id", Arrays.asList(11, 22, 33))
                .generateSql();

        String sql = " FROM t_select_sql_builder WHERE" +
                " id > 5 AND id < ? " +
                "AND ( nick_name LIKE '%name%' OR nick_name LIKE '%login' ) " +
                "AND ( city LIKE '%city%' OR name LIKE '%city%' ) " +
                "AND id in ('1','2','3')  " +
                "AND id not in ('11','22','33')  ";

        Assert.assertEquals(sql, whereAndSql);
    }

    protected void testFullJoin() {
        String joinClause = "t_user u on t.id = u.id";
        SmartDb smartDb = getDb();
        String leftJoinSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class)
                .joinFull(joinClause)
                .generateSql();

        LOG.info("<<testJoin>> full join : {}", leftJoinSql);
        Assert.assertEquals(" FROM " + SELECT_BUILD_TABLE_NAME + "  FULL JOIN " + joinClause + " ", leftJoinSql);
    }

    protected void testInnerJoin() {
        String joinClause = "t_user u on t.id = u.id";
        SmartDb smartDb = getDb();
        String leftJoinSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class)
                .joinInner(joinClause)
                .generateSql();

        LOG.info("<<testJoin>> inner join : {}", leftJoinSql);
        Assert.assertEquals(" FROM " + SELECT_BUILD_TABLE_NAME + "  INNER JOIN " + joinClause + " ", leftJoinSql);
    }

    protected void testRightJoin() {
        String joinClause = "t_user u on t.id = u.id";
        SmartDb smartDb = getDb();
        String leftJoinSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class)
                .joinRight(joinClause)
                .generateSql();

        LOG.info("<<testJoin>> right join : {}", leftJoinSql);
        Assert.assertEquals(" FROM " + SELECT_BUILD_TABLE_NAME + "  RIGHT JOIN " + joinClause + " ", leftJoinSql);
    }

    protected void testLeftJoin() {
        String joinClause = "t_user u on t.id = u.id";
        SmartDb smartDb = getDb();
        String leftJoinSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class)
                .joinLeft(joinClause)
                .generateSql();

        LOG.info("<<testJoin>> left join : {}", leftJoinSql);
        Assert.assertEquals(" FROM " + SELECT_BUILD_TABLE_NAME + "  LEFT JOIN " + joinClause + " ", leftJoinSql);
    }

    protected void testFromAs() {
        SmartDb smartDb = getDb();
        String fromTableSql = smartDb
                .selectSqlBuilder()
                .from(SELECT_BUILD_TABLE_NAME + " t")
                .generateSql();

        String fromClassSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class, "t")
                .generateSql();

        LOG.info("<<testFromAs>> from table : {}", fromTableSql);
        LOG.info("<<testFromAs>> from class : {}", fromClassSql);

        Assert.assertEquals(fromClassSql, fromTableSql);
    }

    protected void testFrom() {
        SmartDb smartDb = getDb();
        String fromTableSql = smartDb
                .selectSqlBuilder()
                .from(SELECT_BUILD_TABLE_NAME)
                .generateSql();

        String fromClassSql = smartDb
                .selectSqlBuilder()
                .from(SelectSqlBuilderEntity.class)
                .generateSql();

        LOG.info("<<testFrom>> from table : {}", fromTableSql);
        LOG.info("<<testFrom>> from class : {}", fromClassSql);

        Assert.assertEquals(fromClassSql, fromTableSql);
    }

    protected void insertSelectData() throws ParseException {
        getDb().execute("delete from " + SELECT_BUILD_TABLE_NAME);

        Random random = new Random();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        for (int i = 1; i <= 15; i++) {
            SelectSqlBuilderEntity selectSqlBuilderEntity = new SelectSqlBuilderEntity();
            selectSqlBuilderEntity.setId(i);
            selectSqlBuilderEntity.setNickName("login-name " + i);
            selectSqlBuilderEntity.setAge(random.nextInt(3) + 1);
            selectSqlBuilderEntity.setCity("city" + i);
            selectSqlBuilderEntity.setUpdateTime(sdf.parse("2018-08-" + (10 + i) + " 00:00:00"));
            selectSqlBuilderEntity.setCreateTime(sdf.parse("2018-08-" + (10 + i) + " 00:00:00"));
            getDb().insertSelective(selectSqlBuilderEntity);

            EntityList.add(selectSqlBuilderEntity);
        }
    }

}


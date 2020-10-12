package net.lab1024.smartdb.sqlbuilder.update;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.common.BaseTest;
import org.junit.Assert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseUpdateBuilderTest extends BaseTest {

    protected static final String UPDATE_BUILD_TABLE_NAME = "t_update_sql_builder";
    protected static final List<UpdateSqlBuilderEntity> UpdateEntityList = new ArrayList<UpdateSqlBuilderEntity>();

    abstract SmartDb getDb();


    public void test() throws ParseException {

        //column
        initData();
        testUpdateColumn();

        //column
        initData();
        testUpdateColumn();

        //clear where
        initData();
        testUpdateColumnClearWhere();

        //entity
        initData();
        testUpdateEntity();

        //selective
        initData();
        testUpdateEntitySelective();

    }

    private void testUpdateEntitySelective() {
        SmartDb smartDb = getDb();
        final int index = 2;
        UpdateSqlBuilderEntity updateSqlBuilderEntity = UpdateEntityList.get(index);
        final String tag = "update-tag";
        final String name = "hi";

        UpdateSqlBuilderEntity updateEntity = new UpdateSqlBuilderEntity();
        updateEntity.setId(updateSqlBuilderEntity.getId());
        updateEntity.setName(name);
        updateEntity.setTag(tag);

        int rows = smartDb.updateSelective(updateEntity);
        Assert.assertEquals(1, rows);

        UpdateSqlBuilderEntity selectEntity = getDb().selectSqlBuilder()
                .select("*")
                .from(UPDATE_BUILD_TABLE_NAME)
                .whereAnd("id = ?", updateEntity.getId())
                .queryFirst(UpdateSqlBuilderEntity.class);

        Assert.assertEquals(tag, selectEntity.getTag());
        Assert.assertEquals(name, selectEntity.getName());
        Assert.assertNotNull(selectEntity.getCreateTime());
    }

    private void testUpdateEntity() {
        SmartDb smartDb = getDb();
        final int index = 2;
        UpdateSqlBuilderEntity updateSqlBuilderEntity = UpdateEntityList.get(index);
        final String tag = "update-tag";
        final String name = "hi";

        UpdateSqlBuilderEntity updateEntity = new UpdateSqlBuilderEntity();
        updateEntity.setId(updateSqlBuilderEntity.getId());
        updateEntity.setName(name);
        updateEntity.setTag(tag);

        int rows = smartDb.update(updateEntity);
        Assert.assertEquals(1, rows);

        UpdateSqlBuilderEntity selectEntity = getDb().selectSqlBuilder()
                .select("*")
                .from(UPDATE_BUILD_TABLE_NAME)
                .whereAnd("id = ?", updateEntity.getId())
                .queryFirst(UpdateSqlBuilderEntity.class);

        Assert.assertEquals(tag, selectEntity.getTag());
        Assert.assertEquals(name, selectEntity.getName());
        Assert.assertEquals(null, selectEntity.getCreateTime());
    }

    public void testUpdateColumnClearWhere() {
        SmartDb smartDb = getDb();
        final int index = 2;
        final String tag = "update-tag";
        final String name = "hi";
        UpdateSqlBuilderEntity updateSqlBuilderEntity = UpdateEntityList.get(index);

        int rows = smartDb.updateSqlBuilder()
                .table(updateSqlBuilderEntity.getClass())
                .updateColumn("tag = ?", tag)
                .updateColumn("name = '" + name + "'")
                .whereAnd("id = ?", updateSqlBuilderEntity.getId())
                .clearWhere()
                .execute();


        Assert.assertEquals(rows, UpdateEntityList.size());
    }

    public void testUpdateColumn() {
        SmartDb smartDb = getDb();
        final int index = 2;
        final String tag = "update-tag";
        final String name = "hi";
        UpdateSqlBuilderEntity updateSqlBuilderEntity = UpdateEntityList.get(index);

        int rows = smartDb.updateSqlBuilder()
                .table(UPDATE_BUILD_TABLE_NAME)
                .updateColumn("tag = ?", tag)
                .updateColumn("name = '" + name + "'")
                .whereAnd("id = ?", updateSqlBuilderEntity.getId())
                .execute();

        Assert.assertEquals(1, rows);
        UpdateSqlBuilderEntity selectEntity = getDb().selectSqlBuilder()
                .select("*")
                .from(UPDATE_BUILD_TABLE_NAME)
                .whereAnd("id = ?", updateSqlBuilderEntity.getId())
                .queryFirst(UpdateSqlBuilderEntity.class);

        Assert.assertEquals(tag, selectEntity.getTag());
        Assert.assertEquals(name, selectEntity.getName());

    }

    public void initData() {
        getDb().execute("delete from " + UPDATE_BUILD_TABLE_NAME);
        UpdateEntityList.clear();

        final int count = 10;
        for (int i = 0; i < count; i++) {
            UpdateSqlBuilderEntity updateBuilder = new UpdateSqlBuilderEntity();
            updateBuilder.setTag("tag" + i);
            updateBuilder.setName("name " + i);
            updateBuilder.setCreateTime(new Date());
            UpdateEntityList.add(updateBuilder);
        }

        getDb().batchInsert(UpdateEntityList);
    }

}

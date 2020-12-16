package net.lab1024.smartdb.demos;

import net.lab1024.smartdb.SmartDb;
import net.lab1024.smartdb.TransactionSmartDbNode;
import net.lab1024.smartdb.domain.UserEntity;
import net.lab1024.smartdb.pagination.PaginateResult;
import net.lab1024.smartdb.pagination.SmartDbPaginateParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zhuoda
 * @Date 2020/12/8
 */
public class SmartDbDemos {

    private SmartDb smartDb;

    public void insert() {

// 增加
        smartDb.execute("insert into t_user values ('卓大','洛阳')");
        smartDb.execute("insert into t_user values (?,?)", "卓大", "洛阳");
        smartDb.execute("insert into t_user values (?,?)", Arrays.asList("卓大", "洛阳"));

// 修改
        smartDb.execute("update t_user set name='卓大2', area='洛阳'  where id = 1");
        smartDb.execute("update t_user set name=?, area=?  where id = ?", "卓大", "洛阳", 1);
        smartDb.execute("insert into t_user values (?,?)", Arrays.asList("卓大", "洛阳", 1));

// 删除
        smartDb.execute("delete from t_user  where id = 1");
        smartDb.execute("delete from t_user  where id = ?", 1);
        smartDb.execute("delete from t_user  where id = ?", Arrays.asList(1));

// 批量
        List<Object[]> batchParamsList = new ArrayList<>();
        batchParamsList.add(new Object[]{"卓大", "洛阳"});
        batchParamsList.add(new Object[]{"zhuoda", "luoyang"});
        smartDb.batch("insert into t_user values (?,?) ", batchParamsList);

// 查询单条记录
        UserEntity userEntity = smartDb.queryFirst(UserEntity.class, "select * from t_user where id = 1");
        UserEntity userEntity1 = smartDb.queryFirst(UserEntity.class, "select * from t_user where id = ?", 1);
// 查询总计
        Long count = smartDb.queryFirst(Long.class, "select count(*) from t_user ");


// 查询多条记录
        List<UserEntity> userEntityList = smartDb.queryList(UserEntity.class, "select * from t_user where id > 1");
        List<UserEntity> userEntityList2 = smartDb.queryList(UserEntity.class, "select * from t_user where id > ?", 1);


        List<Map> userMapList = smartDb.queryList(Map.class, "select * from t_user where id > 1");
        for (Map map : userMapList) {
            System.out.println(map.get("name") + " , " + map.get("area"));
        }

    }

    public void insertBuilder() {

        SmartDb smartDb = null;

//        int rows = smartDb.insertSqlBuilder()
//                // 表 名
//                .table("t_user")
//                // 列名 和 对应的值
//                .insertColumn("name", "卓")
//                // 列名 和 对应的值
//                .insertColumn("city", "洛阳")
//                // 列名 ， 此列为函数
//                .insertFunctionColumn("uuid", "uuid()")
//                // 列名 ， 此列为函数
//                .insertFunctionColumn("create_time", "now()")
//                // 执行
//                .execute();

        UserEntity userEntity = new UserEntity();

//// ORM插入对象(包含属性为null的数据)
//int rows = smartDb.insertSqlBuilder()
//        .insertEntity(userEntity)
//        .execute();
////  ORM插入对象，排除属性为null的数据
//int rows = smartDb.insertSqlBuilder()
//        .insertEntitySelective(userEntity)
//        .execute();


//List<UserEntity> entityList = smartDb.selectSqlBuilder()
//        .select("username, city") // 查询 username和city两个自字段，需要用 , 分割
//        .select(" update_time") // 查询 update time字段
//        .select("create")
//        .from("t_user") // 表名
//        .whereAnd("id = 1") // and 过滤
//        .whereAnd("id = ?", 1)// 切记带上 ? 问号
//        .whereAndIn("id", Arrays.asList(1, 2, 3)) // 非常简单的 in 语句
//        .whereAndLikePatterns("name", "%卓%") // name 列 like 查询带有 “卓” 的名字
//        .whereAndLikeColumns("%卓%", "nickname", "username") // nickname和username 两列 like 查询带有 “卓” 的名字
//        // 以下 为 or 语句
//        .whereOr("id = 1")
//        .whereOrIn("id", Arrays.asList(1, 2, 3)) // 非常简单的 in 语句
//        .queryList(UserEntity.class); // 查询列表


//smartDb.selectSqlBuilder()
//        .select("t1.name as username")
//        .select("t1.city")
//        .select("t1.role_name")
//        .from("t_user as t1")// 表名 重命名
//        .joinLeft("t_user_role t2 on t1.id = t2.user_id") // 关联
//        .joinLeft("t_user_position t3 on t1.id = t3.user_id") // 关联
//        .whereAnd("t3.id = ?",1)
//        .queryList(UserEntity.class);

//PaginateResult<UserEntity> paginateResult = smartDb.selectSqlBuilder()
//        .select("username, city") // 查询 username和city两个自字段，需要用 , 分割
//        .select(" update_time") // 查询 update time字段
//        .select("create")
//        .from("t_user") // 表名
//        .whereAnd("id > 1") // and 过滤
//        .paginate(UserEntity.class, new SmartDbPaginateParam(1, 10));


//int rows = smartDb.updateSqlBuilder()
//        .table("t_user") // 表名
//        .updateColumn("name = '卓大'") //字段
//        .updateColumn("city = ?","洛阳") // 带参数字段
//        .whereAnd("id = ?", 1) // where 同 SelectSqlBuilder
//        .execute();


//int rows = smartDb.updateSqlBuilder()
//        .updateEntity(userEntity) // ORM更新对象(包含属性为null的数据)
//        .execute();
//
//int rows = smartDb.updateSqlBuilder()
//        .updateEntitySelective(userEntity) //  ORM更新对象，排除属性为null的数据
//        .execute();


//int rows = smartDb.deleteSqlBuilder()
//        .table("t_user")
//        .whereAnd("id = ?", id) // 支持and 和 or
//        .execute();

//
//        smartDb.replaceSqlBuilder()
//                .table("t_user")
//                .replaceColumn("")
//                .

//        smartDb.runTransaction(transactionSmartDb -> {
//            // 进行sql操作
//            transactionSmartDb.execute("delete from t_user where id = 1");
//            int rows = transactionSmartDb.execute("update t_user set name = ? where id = ?", "卓大", 2);
//            if (rows > 0) {
//                transactionSmartDb.batch("......", null);
//                // 返回 true 表示提交事务
//                return true;
//            } else {
//                // 返回 false 表示 回滚
//                return false;
//            }
//        });


    }
}

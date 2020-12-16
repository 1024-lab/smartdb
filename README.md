### SmartDb  卓大观点
【卓大观点】互联网已经发展了二十年，每个企业均有一定的数字化发展，但随着“产业互联网”，“新基建”，“国产数据库”，“国产开源框架”等时代的到来，越来越多的企业在原有数字化（原有各类数据库）的基础之上，需要再向上构建一层新的数据聚合，以此来适应产业互联网的发展需要。所以当下的系统都会在原有数据库基础之上连接其他两个、甚至多个数据库，故一个支持多数据库、多数据源且轻量级的java数据库中间件是及其需要的! 

 **所以必须有一个能5分钟上手，就可以完美解决多数据库、多数据源问题的框架 。** 

### SmartDb特点
mybatis现在在国内已经是一家独大，而且也非常好用，笔者也推荐在大型项目中使用mybatis这种框架，但是总有一些场景，mybatis显得有些力不从心，这个时候选择SmartDb是一个不错的选择。

- 在原有mybatis或hibernate连接其他数据库进行一些数据处理
- 读写分离场景，有些数据需要操作主库，有些操作从库
- 多数据源场景，要从mysql读出数据，然后进行转换存储到sqlserver中，等等
- 在一个方法中进行多个数据源的数据库操作
- 一个简单并追求开发效率的小型项目，例如 [SmartBlog（卓大的官方博客）](https://zhuoluodada.cn "SmartBlog（一个精心雕琢的博客）")
- 不想使用sharding-sphere,sharding-jdbc, mycat，cobar等重型读写分离框架
- 等等其他



基于以上种种，设计了一个支持多数据源、读写分离的轻量级ORM框架:SmartDb, 让使用者有个极致的开发体验。

### SmartDb简介
**SmartDb** 是以SQL为中心，支持多数据源、读写分离，同时又不与原有项目mybatis、hibernate冲突的非常轻量级的Java ORM框架。

特性如下：
- 支持ORM操作、常见增删改查、批量、分页等功能
- 支持MySQL、Oracle、Postgresql、SqlServer
- 支持在mybatis、hibernate等项目上引入SmartDb
- 支持多数据源
- 支持分写分离
- 支持枚举Enum映射
- 支持Spring和Spring事务
- 支持SQL链式API
- 支持代码生成
- 支持 filter 过滤器
- 所有类均可扩展
- 支持Java 6 ( **收费，收费是为了更好的伺候 “客官 ” ** )

### SmartDb 文档
github:  [https://github.com/1024-lab/smartdb](https://github.com/1024-lab/smartdb "https://github.com/1024-lab/smartdb")

gitee:  [https://gitee.com/lab1024/smartdb](https://gitee.com/lab1024/smartdb "https://gitee.com/lab1024/smartdb")

官方文档:  [https://zhuoluodada.cn/smartdb](https://zhuoluodada.cn/smartdb "https://zhuoluodada.cn/smartdb")

例子:  [https://gitee.com/lab1024/smartdb-demos](https://gitee.com/lab1024/smartdb-demos)

### SmartDb演示
#### SmartDb POM
```xml
<dependency>
  <groupId>net.1024lab</groupId>
  <artifactId>smartdb</artifactId>
  <version>1.0.0</version>
</dependency>
```

#### SmartDb 操作多数据源
在一个方法中操作多个数据库，不用再使用注解切换来切换去，还容易出错， oh yeah~
```java
public void multiDatasource(UserEntity userEntity) {
    // mysqlSmartDb 对象为 连接SqlServer的SmartDb
    SmartDb mysqlSmartDb = build1(); 
    // sqlServerSmartDb 对象为 连接SqlServer的SmartDb
    SmartDb sqlServerSmartDb = build2();
    // 将数据插入mysql数据库
    mysqlSmartDb.insert(userEntity);
    // 将数据插入sql server数据库
    sqlServerSmartDb.insert(userEntity);
}
```
#### SmartDb ORM操作
支持简单的orm操作
```java
UserEntity userEntity = new UserEntity(1, "zhuoda");
// 插入
smartDb.insert(userEntity);
smartDb.insertSelective(userEntity); // 非空字段插入
smartDb.batchInsert(userList);//批量插入
// 更新
smartDb.update(userEntity);  
smartDb.updateSelective(userEntity); // 非空字段更新
// 删除
smartDb.delete(userEntity);//根据主键删除
```
#### SmartDb 读写分离操作
```java
//默认操作 都是操作从库
List<UserEntity> userList = smartDb.selectSqlBuilder()
        .select("*")
        .from(UserEntity.class)
        .queryList(UserEntity.class);

//获取写库
SmartDb writeSmartDb = smartDb.getMaster();
//将数据写入主库
writeSmartDb.insert(userEntity);
//进行主库其他操作
writeSmartDb.delete(userEntity);
writeSmartDb.updateSelective(userEntity);
```

#### SmartDb 链式Builder操作
smartdb为了支持快速开发，支持增删查改四种链式Builder操作
##### SmartDb 链式SelectBuilder操作
支持and、or、in、like、group、having、limit等等
```java
UserEntity userEntity = smartDb.selectSqlBuilder()
        .select("user.*")
        .select("score.*")
        .from(UserEntity.class)
        .joinLeft("t_score on t_user.id = t_score.user_id")
        .whereAnd("user.id = ?", 1)
        .whereAnd("type = 3")
        .whereAndIn(" status ", Arrays.asList(1, 2, 3))
        .groupby("user.type")
        .havingAnd("user.type = 1")
        .queryFirst(UserEntity.class);
```
##### SmartDb 链式InsertBuilder操作
```java
smartDb.insertSqlBuilder()
        .table("t_user")
        .insertColumn("id",1)
        .insertColumn("name","smartdb")
        .insertFunctionColumn("login_time","now()") //sql方法
        .execute();
```
##### SmartDb 链式UpdateBuilder操作
更新操作支持and、or、in、like、等等
```java
smartDb.updateSqlBuilder()
        .table("t_user")
        .updateColumn("name = ?","smart")
        .updateColumn(" login_time = now()")
        .whereAnd("id = 1")
        .execute();
```
##### SmartDb 链式DeleteBuilder操作
更新操作支持and、or、in、like、等等
```java
smartDb.deleteSqlBuilder()
        .table("t_user")
        .whereAnd("id = 1")
        .execute();
```
##### SmartDb 链式ReplaceBuilder操作
```java
((MysqlSmartDb)smartDb).replaceSqlBuilder()
        .table("t_user")
        .replaceColumn("id",1)
        .replaceColumn("name","smartdb")
        .replaceFunctionColumn("login_time","now()")
        .execute();
```
#### 创建 SmartDb 
```java
SmartDb smartDb = 
	SmartDbBuilder.create()
	//设置 写库 数据源
	.setMasterDataSource(writeDataSource)
	//设置 两个读库 数据源
	.setSlaveDataSource(readDataSource1,readDataSource2)
	// 打印 info 级别sql
	.setShowSql(true)
	//设置数据库类型
	.setSupportDatabaseType(SupportDatabaseType.MYSQL)
	//设置支持spring
	.setSmartDbExtEnum(SmartDbExtEnum.SPRING5)
	//表名与类名转换
	.setTableNameConverter(cls -> "t_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName()))
	//列名字 转换
	.setColumnNameConverter(new CaseFormatColumnNameConverter(CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_UNDERSCORE))
	.build();
```


#### SmartDb 文档

官方文档:  [https://zhuoluodada.cn/smartdb](https://zhuoluodada.cn/smartdb "https://zhuoluodada.cn/smartdb")

例子:  [https://gitee.com/lab1024/smartdb-demos](https://gitee.com/lab1024/smartdb-demos)

---

#### 联系我们 :
[1024创新实验室](https://www.1024lab.net/)

#### SmartDb微信交流群（**加我微信拉你入群，和小伙伴们一起探讨！**）
![](https://zhuoluodada.cn/cdn/images/zhuoda/zhuoda-wechat.jpg)

#### 捐赠
开源不易，感谢捐赠
>*佛祖保佑捐赠这些人写程序永无bug，工资翻倍，迎娶白富美，走上人生巅峰！*

![](https://zhuoluodada.cn/cdn/images/zhuoda/zhuoda-wechat-money-v1.jpg)

---
作者简介:
[卓大](https://zhuoluodada.cn)， 1024创新实验室主任，混迹于各个技术圈，研究过计算机，熟悉点java，略懂点前端。



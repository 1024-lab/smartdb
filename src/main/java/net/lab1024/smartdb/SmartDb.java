package net.lab1024.smartdb;


import net.lab1024.smartdb.datasource.OptEnum;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 精致、极简、高质量的 ORM框架 ： SmartDb
 *
 * @author zhuoluodada@qq.com
 */
public interface SmartDb extends SmartDbNode {


    /**
     * 获取真实数据库连接
     *
     * @param opt 读库 or 写库
     * @return 返回真实的数据连接
     * @throws SQLException
     */
    Connection getConnection(OptEnum opt) throws SQLException;


    /**
     * 获取 写 数据库  节点
     *
     * @return
     */
    SmartDbNode getWriteSmartDb();

    /**
     * 获取 写 数据库  节点
     *
     * @return
     */
    SmartDbNode getMaster();


    /**
     * 获取所有 slave节点（即 读数据库 节点）
     *
     * @return
     */
    List<SmartDbNode> getSlaves();

    /**
     * 获取某个特点的slave ( read ) 数据库节点
     *
     * @return
     */
    SmartDbNode getSlave(int index);
}

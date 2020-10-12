package net.lab1024.smartdb.domain;

import net.lab1024.smartdb.annotation.PrimaryKey;
import net.lab1024.smartdb.annotation.TableAlias;
import net.lab1024.smartdb.annotation.UseGeneratedKey;

import java.util.Date;

/**
 * t_user  实体类
 * 表备注：t_user  实体类
 */

@TableAlias("t_user")
public class UserEntity {
    /**
     * (最大长度:11,类型:INT)
     */
    @PrimaryKey
    @UseGeneratedKey
    private Integer id;
    /**
     * (最大长度:50,类型:VARCHAR)
     */
    private String userName;
    /**
     * (最大长度:19,类型:DATETIME)
     */
    private Date createTime;

}


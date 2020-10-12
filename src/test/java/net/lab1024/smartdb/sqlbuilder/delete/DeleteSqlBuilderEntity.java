package net.lab1024.smartdb.sqlbuilder.delete;

import net.lab1024.smartdb.annotation.PrimaryKey;
import net.lab1024.smartdb.annotation.TableAlias;
import net.lab1024.smartdb.annotation.UseGeneratedKey;

import java.util.Date;

@TableAlias("t_delete_sql_builder")
class DeleteSqlBuilderEntity {
    @PrimaryKey
    @UseGeneratedKey
    private Integer id;
    private String uuid;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}

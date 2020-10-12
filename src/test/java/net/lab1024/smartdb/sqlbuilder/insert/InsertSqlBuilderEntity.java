package net.lab1024.smartdb.sqlbuilder.insert;

import net.lab1024.smartdb.annotation.PrimaryKey;
import net.lab1024.smartdb.annotation.TableAlias;
import net.lab1024.smartdb.annotation.UseGeneratedKey;

import java.math.BigDecimal;
import java.util.Date;

@TableAlias("t_insert_sql_builder")
public class InsertSqlBuilderEntity {
    @PrimaryKey
    @UseGeneratedKey
    private Integer id;
    private String key;
    private Date createTime;
    private BigDecimal balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "InsertSqlBuilderEntity{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", createTime=" + createTime +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InsertSqlBuilderEntity that = (InsertSqlBuilderEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return balance != null ? balance.equals(that.balance) : that.balance == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}

package net.lab1024.smartdb.xenum;

import net.lab1024.smartdb.annotation.ColumnAlias;
import net.lab1024.smartdb.annotation.PrimaryKey;
import net.lab1024.smartdb.annotation.TableAlias;
import net.lab1024.smartdb.annotation.UseGeneratedKey;

@TableAlias("t_enum")
public class EnumEntity {
    @PrimaryKey
    @UseGeneratedKey
    // 自增主键id
    private Integer id;
    // 用户名
    private String userName;

    // 列名 自定义
    @ColumnAlias("city")
    private String area;

    // 枚举： girl 和 boy; 数据库必须为 char、varchar等字符类型
    private Sex sex;

    // 枚举： Level.class 实现了 SmartEnum 接口; 数据库则为 int 等整数类型
    private Level level;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnumEntity that = (EnumEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (sex != that.sex) return false;
        return level == that.level;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}

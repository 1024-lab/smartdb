package net.lab1024.smartdb.xenum;

import net.lab1024.smartdb.annotation.PrimaryKey;
import net.lab1024.smartdb.annotation.TableAlias;
import net.lab1024.smartdb.annotation.UseGeneratedKey;

@TableAlias("t_enum")
public class EnumEntity {

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

    private Sex sex;

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

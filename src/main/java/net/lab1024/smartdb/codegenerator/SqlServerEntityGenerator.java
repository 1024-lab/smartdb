package net.lab1024.smartdb.codegenerator;

/**
 * isNullable(int column)：指示指定列中的值是否可以为 null。
 */
public class SqlServerEntityGenerator extends MysqlEntityGenerator {


    public SqlServerEntityGenerator(SmartDbEntityGeneratorBuilder smartDbEntityGeneratorBuilder) {
        super(smartDbEntityGeneratorBuilder);
    }
}
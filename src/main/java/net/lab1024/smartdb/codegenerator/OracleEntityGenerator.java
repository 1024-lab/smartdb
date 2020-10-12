package net.lab1024.smartdb.codegenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * isNullable(int column)：指示指定列中的值是否可以为 null。
 */
public class OracleEntityGenerator extends MysqlEntityGenerator {

    protected static final Logger LOG = LoggerFactory.getLogger(OracleEntityGenerator.class);

    public OracleEntityGenerator(SmartDbEntityGeneratorBuilder smartDbEntityGeneratorBuilder) {
        super(smartDbEntityGeneratorBuilder);
    }
}

package net.lab1024.smartdb.generator;

import net.lab1024.smartdb.codegenerator.OracleEntityGenerator;
import net.lab1024.smartdb.codegenerator.SmartDbEntityGeneratorBuilder;
import org.junit.Test;
//TODO zhuoluodada 还没测试
public class OracleGeneratorTest extends BaseGeneratorTest {

    @Test
    public void testGeneratorUser() {

        SmartDbEntityGeneratorBuilder smartDbEntityGeneratorBuilder = getSmartDbEntityGeneratorBuilder();

        //创建一个mysql的代码生成器，然后进行生成代码
        new OracleEntityGenerator(smartDbEntityGeneratorBuilder).generate();
    }

}

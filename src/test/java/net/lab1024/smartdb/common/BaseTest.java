package net.lab1024.smartdb.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BaseTest {
    public static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);

    public static Properties mysqlProp = null;

    static {
        try {
            mysqlProp = new Properties();
            mysqlProp.load(TestSmartDbContainer.class.getClassLoader().getResourceAsStream("mysql.properties"));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

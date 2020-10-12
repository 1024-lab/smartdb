package net.lab1024.smartdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 表别名的注解
 * @author  zhuoluodada
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface TableAlias {
    String value() default "";

}

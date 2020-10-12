package net.lab1024.smartdb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识主键的注解
 * @author  zhuoluodada
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface PrimaryKey {
}

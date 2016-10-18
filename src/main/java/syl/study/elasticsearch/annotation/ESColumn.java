package syl.study.elasticsearch.annotation;

import syl.study.elasticsearch.enums.Analyzed;
import syl.study.elasticsearch.enums.Store;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Mtime on 2016/10/17.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ESColumn {

    boolean subClass();

    Class<?> clazz();

    Store store() default Store.STORE;

    Analyzed analyzed() default Analyzed.NOT_ANALYZED;

    String name() default "";















}

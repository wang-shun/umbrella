package com.harmony.umbrella.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuxii@foxmail.com
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestQueryBundle {

    boolean required() default true;

    Option option() default Option.EMPTY_CONJUNCTION;

    int page() default -1;

    int size() default -1;

    String[] gouping() default {};

    String[] asc() default {};

    String[] desc() default {};

    public static enum Option {
        EMPTY_CONJUNCTION, EMPTY_DISJUNCTION
    }

}

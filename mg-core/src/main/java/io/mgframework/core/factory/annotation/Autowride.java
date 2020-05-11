package io.mgframework.core.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:14
 * @Desc: 按照类型加载Bean
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowride {

    /**
     * 是否必须依赖,默认true
     * @return
     */
    boolean required() default true;
}

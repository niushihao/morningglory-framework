package io.mgframework.core.factory.annotation;

import java.lang.annotation.*;

/**
 * @Author: qianniu
 * @Date: 2020-04-06 10:54
 * @Desc:
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    String value();
}

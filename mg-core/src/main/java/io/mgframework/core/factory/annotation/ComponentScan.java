package io.mgframework.core.factory.annotation;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:42
 * @Desc: 扫描器注解
 */
public @interface ComponentScan {

    /**
     * 需要扫描的包
     * @return
     */
    String[] basePackages() default {};

}

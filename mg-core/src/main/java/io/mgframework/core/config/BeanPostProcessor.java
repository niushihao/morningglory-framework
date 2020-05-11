package io.mgframework.core.config;

import io.mgframework.core.BeansException;

/**
 * @Author: qianniu
 * @Date: 2020-04-06 12:13
 * @Desc:
 */
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}

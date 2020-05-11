package io.mgframework.core.config;

import io.mgframework.core.BeansException;
import io.mgframework.core.core.PropertyValues;

/**
 * @Author: qianniu
 * @Date: 2020-04-06 12:14
 * @Desc:
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {

        return null;
    }
}

package io.mgframework.core.factory;

import io.mgframework.core.BeansException;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 15:14
 * @Desc:
 */
@FunctionalInterface
public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}

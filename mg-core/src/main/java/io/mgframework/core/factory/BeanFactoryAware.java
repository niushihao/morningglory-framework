package io.mgframework.core.factory;

import io.mgframework.core.BeansException;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:30
 * @Desc: 实现此接口用于获取BeanFactory
 */
public interface BeanFactoryAware extends Aware{

    /**
     * 回调方法,用于向实现类设置BeanFactory对象
     * @param beanFactory
     * @throws BeansException
     */
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}

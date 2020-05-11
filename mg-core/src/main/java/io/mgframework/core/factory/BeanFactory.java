package io.mgframework.core.factory;

import io.mgframework.core.BeansException;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:18
 * @Desc: 创建Bean的工厂类
 */
public interface BeanFactory {

    String FACTORY_BEAN_PREFIX = "&";

    /**
     * 根据指定名称加载bean
     * @param name bean名称
     * @return bean的实例
     * @throws BeansException bean名称不存在时抛出
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据指定类型加载bean
     * @param requiredType bean类型
     * @return bean实例
     */
    <T> T getBean(Class<T> requiredType);

    /**
     * 根据指定名称和类型加载bean
     * @param name bean名称
     * @param requiredType bean类型
     * @return bean的实例
     */
    <T> T getBean(String name,Class<T> requiredType);

    /**
     * 判断工厂容器中是否包含指定名称的bean
     * @param name
     * @return
     */
    boolean containsBean(String name);


}

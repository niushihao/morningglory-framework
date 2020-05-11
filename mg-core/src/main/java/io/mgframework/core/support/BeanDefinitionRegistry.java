package io.mgframework.core.support;

import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.config.BeanPostProcessor;

import java.util.List;
import java.util.Set;

/**
 * @Author: qianniu
 * @Date: 2020-03-31 22:31
 * @Desc: BeanDefinition注册器
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册一个新的 beanDefinition
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 根据名称删除对应的 beanDefinition
     * @param beanName
     */
    void removeBeanDefinition(String beanName);

    /**
     * 根据指定名称获取 beanDefinition
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 判断指定名称是否有对应的 beanDefinition
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取所有注册的 beanDefinition 的名称
     * @return
     */
    Set<String> getBeanDefinitionNames();

    /**
     * 统计注册的个数
     * @return
     */
    int getBeanDefinitionCount();

    /**
     * 添加bean处理器
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}

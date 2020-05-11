package io.mgframework.core.config;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 15:09
 * @Desc:
 */
public interface SingletonBeanRegistry {

    /**
     * 注册bean
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 获取注册的bean
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

    /**
     * bean是否存在
     * @param beanName
     * @return
     */
    boolean containsSingleton(String beanName);

    /**
     * 获取所有的beanName
     * @return
     */
    String[] getSingletonNames();

    /**
     * 获取bean的数量
     * @return
     */
    int getSingletonCount();
}

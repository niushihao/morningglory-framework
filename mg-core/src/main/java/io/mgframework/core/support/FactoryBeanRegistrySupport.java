package io.mgframework.core.support;

import io.mgframework.core.BeansException;
import io.mgframework.core.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 16:37
 * @Desc:
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    /**
     * 通过FactoryBean 创建的对象会在这里缓存 beanName to bean instance
     */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);

    /**
     * 根据beanName从缓存获取映射的 FactoryBean 实例
     * @param beanName
     * @return
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        return this.factoryBeanObjectCache.get(beanName);
    }

    /**
     * 通过FactoryBean 创建对象
     * @param factory FactoryBean对象
     * @param beanName 对象名字
     * @param shouldPostProcess 是否需要加入bean后置处理器
     * @return
     */
    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess){
        //
        if(factory.isSingleton() && containsSingleton(beanName)){
            Object object = factoryBeanObjectCache.get(beanName);
            if(object == null){
                // 通过FactoryBean 获取bean对象
                object = doGetObjectFromFactoryBean(factory,beanName);
                if(shouldPostProcess){
                    // 如果bean正在被创建则直接返回
                    if(isSingletonCurrentlyInCreation(beanName)){
                        return object;
                    }

                    beforeSingletonCreation(beanName);
                    try {
                        object = postProcessObjectFromFactoryBean(object, beanName);
                    }catch (Exception e){
                        throw new BeansException("");
                    }finally {
                        afterSingletonCreation(beanName);
                    }

                    if(containsSingleton(beanName)){
                        this.factoryBeanObjectCache.put(beanName,object);
                    }
                }
            }
            return object;
        }else {
            Object object = doGetObjectFromFactoryBean(factory,beanName);
            if(shouldPostProcess){
                try {
                    object = postProcessObjectFromFactoryBean(object, beanName);
                }catch (Exception e){
                    throw new BeansException("");
                }
            }
            return object;
        }
    }

    /**
     * 具体后置处理器的调用由子类实现
     * @param object
     * @param beanName
     * @return
     */
    protected  Object postProcessObjectFromFactoryBean(Object object, String beanName){
        return object;
    }

    /**
     * 通过FactoryBean 获取bean对象
     * @param factory
     * @param beanName
     * @return
     */
    protected  Object doGetObjectFromFactoryBean(FactoryBean<?> factory, String beanName){

        return factory.getObject();
    }
}

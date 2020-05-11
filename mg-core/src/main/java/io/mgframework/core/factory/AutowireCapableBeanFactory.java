package io.mgframework.core.factory;

import io.mgframework.core.BeansException;
import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.config.BeanPostProcessor;
import io.mgframework.core.config.InstantiationAwareBeanPostProcessor;
import io.mgframework.core.core.PropertyValues;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 14:58
 * @Desc:
 */
@Slf4j
public  class AutowireCapableBeanFactory extends AbstractBeanFactory{

    private boolean allowCircularReferences = true;


    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {

        //BeanPostProcessorsBeforeInstantiation、BeanPostProcessorsAfterInitialization

        /**
         * 1. create instants,
         * 在创建实例完成后为了解决循环依赖需要将bean实例包装秤ObjectFactory,放入 singletonFactories缓存中
         * 2. populateBean
         * 3. initializeBean
         */
        Object instance = null;
        try {
            instance = Class.forName(beanDefinition.getBeanClassName()).newInstance();

        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException(e.getMessage());
        }

        // 为了解决单利属性注入时的循环依赖,这里需要将刚实例化的bean包装成ObjectFactory放入换粗
        Object instanceReference = instance;
        if(beanDefinition.isSingleton() && allowCircularReferences){
            addSingletonFactory(beanName,() -> instanceReference);
        }


        Object exposedObject = instance;

        // 填充属性
        populateBean(beanName,beanDefinition,instance);

        // 初始化
        initializeBean(beanName,instance,beanDefinition);

        // 放入缓存
        nameInstanceCache.put(beanName,exposedObject);

        return exposedObject;
    }

    private void initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // invoke aware methods
        if(bean instanceof Aware){

            if(bean instanceof BeanFactoryAware){
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
        }
        Object wrappedBean = bean;

        // applyBeanPostProcessorsBeforeInitialization
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

        // invokeInitMethods
        try {
            wrappedBean = invokeInitMethods(beanName,beanDefinition,wrappedBean);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        // applyBeanPostProcessorsAfterInitialization
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    private Object applyBeanPostProcessorsAfterInitialization(Object wrappedBean, String beanName) {
        Object result = wrappedBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    private Object invokeInitMethods(String beanName, BeanDefinition beanDefinition, Object wrappedBean) throws Exception {
        if(wrappedBean instanceof InitializingBean){
            ((InitializingBean) wrappedBean).afterPropertiesSet();
        }
        return wrappedBean;
    }

    private Object applyBeanPostProcessorsBeforeInitialization(Object wrappedBean, String beanName) {
        Object result = wrappedBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 新创建的实例填充属性
     * @param beanName
     * @param beanDefinition
     * @param instance
     */
    private void populateBean(String beanName, BeanDefinition beanDefinition, Object instance) {
        if(instance == null){
            return;
        }

        boolean continueWithPropertyPopulation = true;
        if(hasInstantiationAwareBeanPostProcessors()) {

            for(BeanPostProcessor bp : getBeanPostProcessors()){
                if(bp instanceof InstantiationAwareBeanPostProcessor){
                    InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;

                    if(!ibp.postProcessAfterInstantiation(instance,beanName)){
                        continueWithPropertyPopulation = false;
                    }
                }
            }
        }

        if(!continueWithPropertyPopulation){
            return;
        }

        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        for(BeanPostProcessor bp : getBeanPostProcessors()){
            if(bp instanceof InstantiationAwareBeanPostProcessor){
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                ibp.postProcessProperties(propertyValues,instance,beanName);
            }
        }

    }

}

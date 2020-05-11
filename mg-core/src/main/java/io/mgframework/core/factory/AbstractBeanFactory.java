package io.mgframework.core.factory;

import io.mgframework.core.BeansException;
import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.config.BeanPostProcessor;
import io.mgframework.core.config.InstantiationAwareBeanPostProcessor;
import io.mgframework.core.support.BeanDefinitionRegistry;
import io.mgframework.core.support.BeanFactoryUtils;
import io.mgframework.core.support.FactoryBeanRegistrySupport;
import io.mgframework.core.util.CollectionUtils;
import io.mgframework.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 15:00
 * @Desc:
 */
@Slf4j
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements BeanDefinitionRegistry,BeanFactory{

    /**
     * 保存当前正在创建的beanName
     */
    private final ThreadLocal<Set<String>> prototypesCurrentlyInCreation = ThreadLocal.withInitial(() -> new HashSet<>());

    private final Map<String,BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();

    protected final ConcurrentMap<String,Object> nameInstanceCache = new ConcurrentHashMap();


    private volatile boolean hasInstantiationAwareBeanPostProcessors;

    public AbstractBeanFactory() {
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null, null);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {

        // 遍历beanDefinitions 获取所有类型匹配的bean名称
        List<String> names = getBeanNamesForType(requiredType);
        if(CollectionUtils.isEmpty(names)){
            throw new BeansException("no beanDefinitions for type "+requiredType);
        }

        // 这里需要根据@Primiry注解处理
        if(names.size() > 1){

        }

        // 这里先写死去第一个
        return getBean(names.get(0),requiredType);
    }

    /**
     * 根据类型获取所有与类型匹配的beanName
     * Class.isAssignableFrom
     * @param requiredType
     * @param <T>
     * @return
     */
    protected  <T> List<String> getBeanNamesForType(Class<T> requiredType){
        List<String> beanNames = new ArrayList<>();
        for(Map.Entry<String,BeanDefinition> entry : beanDefinitions.entrySet()){
            BeanDefinition value = entry.getValue();

            if(requiredType.isAssignableFrom(value.getBeanClass())){
                beanNames.add(entry.getKey());
            }
        }
        return beanNames;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        Object bean = nameInstanceCache.get(name);

        if(bean == null){
            return doGetBean(name, requiredType, null);
        }
        return (T) bean;

    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }


    protected <T> T doGetBean(String name,Class<T> requiredType,Object[] args){
        String beanName = name;
        Object bean = null;
        Object sharedInstance = getSingleton(beanName);
        if(sharedInstance != null && args == null){

            bean = getObjectForBeanInstance(sharedInstance,name,beanName,null);
        }else{

            // 判断是否正在创建
            if(isPrototypeCurrentlyInCreation(beanName)){
                throw new BeansException(beanName +"正在被创建");
            }
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            if(beanDefinition == null){
                throw new BeansException("没有定义 "+ beanName + "对应的 beanDefinition");
            }
            if(beanDefinition.isSingleton()){
                sharedInstance = getSingleton(beanName,() -> createBean(beanName,beanDefinition,args));
                bean = getObjectForBeanInstance(sharedInstance,name,beanName,beanDefinition);
            }else if(beanDefinition.isPrototype()){
                Object prototypeInstance = null;
                prototypeInstance = createBean(beanName, beanDefinition, args);
                bean = getObjectForBeanInstance(sharedInstance,name,beanName,beanDefinition);
            }
            
        }
        return (T) bean;
    }

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args);


    /**
     * 判断beanName是否正在创建
     * @param beanName
     * @return
     */
    public boolean isPrototypeCurrentlyInCreation(String beanName){
        return prototypesCurrentlyInCreation.get().contains(beanName);
    }

    /**
     * 根据bean的实例创建bean
     * @param instance 共享的bean实例
     * @param name     bean的名称,如果是特殊的bean可能有特殊的前缀
     * @param beanName 原始bean的名称
     * @param mbd
     * @return
     */
    protected Object getObjectForBeanInstance(Object instance, String name, String beanName, BeanDefinition mbd){


        // 名称符合FactoryBean命名
        if(BeanFactoryUtils.isFactoryDereference(name)){
            // 如果不是FactoryBean类型,则返回当前实例
            if(!(instance instanceof FactoryBean)){
                return instance;
            }

            Object object = null;
            if (mbd == null) {
                object = getCachedObjectForFactoryBean(beanName);
            }
            if(object == null){
                FactoryBean<?> factoryBean = (FactoryBean) instance;
                object = getObjectFromFactoryBean(factoryBean,beanName,false);
            }

            return object;
        }

        // 名称不符合FactoryBean命名但确是BeanFactory类型
        if(instance instanceof FactoryBean){
            Object object = null;
            if (mbd == null) {
                object = getCachedObjectForFactoryBean(beanName);
            }
            if(object == null){
                FactoryBean<?> factoryBean = (FactoryBean) instance;
                object = getObjectFromFactoryBean(factoryBean,beanName,false);
            }

            return object;
        }

        return instance;
    }

    protected void addBeanDefinition(String beanName,BeanDefinition definition){
        this.beanDefinitions.put(beanName,definition);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanName,beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanDefinitions.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitions.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitions.containsKey(beanName);
    }

    @Override
    public Set<String> getBeanDefinitionNames() {
        return this.beanDefinitions.keySet();
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitions.size();
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
            this.hasInstantiationAwareBeanPostProcessors = true;
        }

        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    protected boolean hasInstantiationAwareBeanPostProcessors() {
        return this.hasInstantiationAwareBeanPostProcessors;
    }
}

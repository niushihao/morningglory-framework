package io.mgframework.core.support;

import io.mgframework.core.BeansException;
import io.mgframework.core.config.SingletonBeanRegistry;
import io.mgframework.core.factory.ObjectFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 15:11
 * @Desc:
 */
@Slf4j
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * bean的缓存: beanName to bean instance
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     *
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>();

    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    private final Set<String> registeredSingletons = new LinkedHashSet<>();

    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {

        synchronized (this.singletonObjects){
            Object obj = singletonObjects.get(beanName);
            if(obj != null){
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + obj + "] bound");
            }
            addSingleton(beanName,singletonObject);
        }

    }

    protected void addSingleton(String beanName, Object singletonObject) {

        synchronized (this.singletonObjects){
            this.singletonObjects.put(beanName,singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    public void addSingletonFactory(String beanName,ObjectFactory<?> objectFactory){
        synchronized (this.singletonObjects){
            if(!singletonObjects.containsKey(beanName)){
                this.singletonFactories.put(beanName,objectFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return getSingleton(beanName,true);
    }

    /**
     * 获取bean
     * @param beanName
     * @param allowEarlyReference
     * @return
     */
    protected Object getSingleton(String beanName,boolean allowEarlyReference){
        Object singletonObject = this.singletonObjects.get(beanName);
        if(singletonObject != null){
            return singletonObject;
        }

        synchronized (this.singletonObjects){
            singletonObject = this.earlySingletonObjects.get(beanName);
            if(singletonObject == null && allowEarlyReference){
                ObjectFactory<?> objectFactory = this.singletonFactories.get(beanName);
                if(objectFactory != null){
                    singletonObject = objectFactory.getObject();
                    this.earlySingletonObjects.put(beanName,singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    protected Object getSingleton(String beanName,ObjectFactory<?> singletonFactory){
        synchronized (this.singletonObjects){
            Object singletonObject = singletonObjects.get(beanName);
            if(singletonObject != null){
                return singletonObject;
            }

            log.debug("Creating shared instance of singleton bean '" + beanName + "'");
            beforeSingletonCreation(beanName);
            boolean newSingleton = false;
            try {
                singletonObject = singletonFactory.getObject();
                newSingleton = true;
            }catch (Exception e){
                throw e;
            }finally {
                afterSingletonCreation(beanName);
            }

            if(newSingleton){
                addSingleton(beanName,singletonObject);
            }
            return singletonObject;
        }

    }

    @Override
    public boolean containsSingleton(String beanName) {
        synchronized (this.singletonObjects){
            return this.registeredSingletons.contains(beanName);
        }
    }

    @Override
    public String[] getSingletonNames() {
        return new String[0];
    }

    @Override
    public int getSingletonCount() {
        synchronized (this.singletonObjects){
            return this.registeredSingletons.size();
        }
    }

    public void beforeSingletonCreation(String beanName){
        if(!singletonsCurrentlyInCreation.add(beanName)){
            throw new BeansException(beanName +"正在被创建");
        }
    }

    public void afterSingletonCreation(String beanName){
        singletonsCurrentlyInCreation.remove(beanName);
    }



    protected boolean isActuallyInCreation(String beanName) {
        return isSingletonCurrentlyInCreation(beanName);
    }

    /**
     * Return whether the specified singleton bean is currently in creation
     * (within the entire factory).
     * @param beanName the name of the bean
     */
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

}

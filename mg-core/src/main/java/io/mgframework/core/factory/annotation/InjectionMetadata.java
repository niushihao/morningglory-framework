package io.mgframework.core.factory.annotation;

import io.mgframework.core.core.PropertyValues;
import io.mgframework.core.factory.BeanFactory;
import io.mgframework.core.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @Author: qianniu
 * @Date: 2020-04-20 19:42
 * @Desc: 需要注入的原信息
 */
public class InjectionMetadata {

    private final Class<?> targetClass;

    private final Collection<InjectedElement> injectedElements;


    public InjectionMetadata(Class<?> targetClass, Collection<InjectedElement> injectedElements) {
        this.targetClass = targetClass;
        this.injectedElements = injectedElements;
    }

    public void inject(Object target, String beanName, PropertyValues propertyValues) throws Throwable {
        for(InjectedElement element : injectedElements){
            element.inject(target,beanName,propertyValues);
        }
    }

    /**
     * 是否需要刷新缓存
     * @param metadata 缓存中的元信息
     * @param clazz    当前的bean
     * @return
     */
    public static boolean needsRefresh(InjectionMetadata metadata, Class<?> clazz) {
        return (metadata == null || metadata.targetClass != clazz);
    }

    public  static class InjectedElement{

        protected final Member member;

        protected final boolean isField;

        protected final String injectedBeanName;

        protected final Class<?> injectedBeanType;

        private final BeanFactory beanFactory;


        protected InjectedElement(Member member) {
            this.member = member;
            this.isField = (member instanceof Field);
            this.injectedBeanName = null;
            this.injectedBeanType = null;
            this.beanFactory = null;
        }

        protected InjectedElement(Member member,String injectedBeanName,BeanFactory beanFactory) {
            this.member = member;
            this.isField = (member instanceof Field);
            this.injectedBeanName = injectedBeanName;
            this.injectedBeanType = null;
            this.beanFactory = beanFactory;
        }

        protected InjectedElement(Member member,Class<?> injectedBeanType,BeanFactory beanFactory) {
            this.member = member;
            this.isField = (member instanceof Field);
            this.injectedBeanName = null;
            this.injectedBeanType = injectedBeanType;
            this.beanFactory = beanFactory;
        }

        protected InjectedElement(Member member,String injectedBeanName, Class<?> injectedBeanType,BeanFactory beanFactory) {
            this.member = member;
            this.isField = (member instanceof Field);
            this.injectedBeanName = injectedBeanName;
            this.injectedBeanType = injectedBeanType;
            this.beanFactory = beanFactory;
        }

        /**
         * 注入的模板方法
         * @param target
         * @param beanName
         * @param propertyValues
         * @throws Throwable
         */
        protected void inject(Object target, String beanName, PropertyValues propertyValues) throws Throwable {

            // 如果是属性,反射调用属性的set方法
            if(this.isField){
                Field field = (Field) this.member;
                field.setAccessible(true);
                field.set(target, StringUtils.isEmpty(this.injectedBeanName) ?
                        beanFactory.getBean(injectedBeanType) : beanFactory.getBean(injectedBeanName));
            // 如果是方法,反射调用方法的invoke方法
            }else{
                Method method = (Method) this.member;
                method.setAccessible(true);
                method.invoke(target,StringUtils.isEmpty(this.injectedBeanName) ?
                        beanFactory.getBean(injectedBeanType) : beanFactory.getBean(injectedBeanName));
            }
        }

        // 由子类实现
        protected Object getResourceToInject(Object target, String requestingBeanName) {
            return null;
        }
    }
}

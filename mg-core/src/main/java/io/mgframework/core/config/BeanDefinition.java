package io.mgframework.core.config;

import io.mgframework.core.BeanMetadataElement;
import io.mgframework.core.core.AttributeAccessor;
import io.mgframework.core.core.PropertyValue;
import io.mgframework.core.core.PropertyValues;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:51
 * @Desc: 用于描述一个bean的完整信息（参数,父类,依赖）等，会在扫描完成后进行填充
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void setBeanName(String beanName);

    String getBeanName();
    /**
     * 设置父类
     * @param parentName
     */
    void setParentName(String parentName);

    /**
     * 获取父类的名字
     * @return
     */
    String getParentName();

    /**
     * 设置bean的class
     * @param beanClass
     */
    void setBeanClass(Class<?> beanClass);

    /**
     * 获取bean的className
     * @return
     */
    Class getBeanClass();

    /**
     * 设置bean的className
     * @param beanClassName
     */
    void setBeanClassName(String beanClassName);

    /**
     * 获取bean的className
     * @return
     */
    String getBeanClassName();

    /**
     * 设置bean类型(单利/多利)
     * @param scope
     */
    void setScope(String scope);

    /**
     * 返回当前bean的类型(单利/多利)
     * @return
     */
    String getScope();

    /**
     * 设置bean初始化时依赖的bean
     * @param dependsOn
     */
    void setDependsOn(String... dependsOn);

    /**
     * 获取bean依赖的其他bean
     * @return
     */
    String[] getDependsOn();

    void setFactoryBeanName(String factoryBeanName);

    String getFactoryBeanName();

    void setFactoryMethodName(String factoryMethodName);

    String getFactoryMethodName();

    boolean isSingleton();

    boolean isPrototype();

    boolean isAbstract();

    PropertyValues getPropertyValues();
}

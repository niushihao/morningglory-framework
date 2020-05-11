package io.mgframework.core.support;

import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.config.Resource;
import io.mgframework.core.core.PropertyValues;
import io.mgframework.core.core.classreading.MetadataReader;
import io.mgframework.core.util.ClassNodeUtils;
import io.mgframework.core.util.StringUtils;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: qianniu
 * @Date: 2020-03-30 09:33
 * @Desc:
 */
public  class GenericBeanDefinition implements BeanDefinition {

    private volatile Class<?> beanClass;

    private String beanName;

    private String parentName;

    private String beanClassName;

    private String scope;

    private String[] dependsOn;

    private String factoryBeanName;

    private String factoryMethodName;

    private boolean abstractFlag = false;

    private Resource resource;

    private ClassNode classNode;

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    private final Map<String,Object> annotationAttributes = new HashMap<>();

    private PropertyValues propertyValues;

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(MetadataReader reader) {
        this.beanClassName = StringUtils.isEmpty(reader.getClassNode().name) ? null : reader.getClassNode().name.replace("/",".");
        this.parentName = StringUtils.isEmpty(reader.getClassNode().superName) ? null : reader.getClassNode().superName.replace("/",".");
        this.scope = SCOPE_SINGLETON;
        this.classNode = reader.getClassNode();
        this.annotationAttributes.putAll(ClassNodeUtils.getAnnotationAttribute(classNode));
    }

    public GenericBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }


    @Override
    public void setBeanClass(Class<?> beanClass){
        this.beanClass = beanClass;
    }

    @Override
    public Class<?> getBeanClass(){
        return (Class<?>) beanClass;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String getParentName() {
        return parentName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setDependsOn(String... dependsOn) {
        this.dependsOn = dependsOn;
    }

    @Override
    public String[] getDependsOn() {
        return new String[0];
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override
    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public boolean isAbstract() {
        return abstractFlag;
    }

    @Override
    public PropertyValues getPropertyValues() {
        return this.propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
    public Object getSource() {
        return resource;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name,value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Object removeAttribute(String name) {
        return attributes.remove(name);
    }

    @Override
    public String[] attributeNames() {
        return StringUtils.toStringArray(attributes.keySet());
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public void setClassNode(ClassNode classNode) {
        this.classNode = classNode;
    }

    public Map<String, Object> getAnnotationAttributes() {
        return annotationAttributes;
    }
}

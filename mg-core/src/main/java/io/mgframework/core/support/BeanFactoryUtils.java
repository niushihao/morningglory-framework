package io.mgframework.core.support;

import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.factory.BeanFactory;
import io.mgframework.core.util.ClassNodeUtils;
import io.mgframework.core.util.CollectionUtils;
import io.mgframework.core.util.StringUtils;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 16:22
 * @Desc:
 */
public class BeanFactoryUtils {

    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    private static final char PACKAGE_SEPARATOR = '.';


    private static final Map<String, String> transformedBeanNameCache = new ConcurrentHashMap<>();


    /**
     * 是否是FactoryBean
     * @param name
     * @return
     */
    public static boolean isFactoryDereference(String name) {
        return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
    }

    /**
     * 去掉FACTORY_BEAN_PREFIX前缀
     * @param name
     * @return
     */
    public static String transformedBeanName(String name) {
        if (!name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            return name;
        }
        return transformedBeanNameCache.computeIfAbsent(name, beanName -> {
            do {
                beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
            }
            while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
            return beanName;
        });
    }

    /**
     * 获取originalBeanName
     * @param name
     * @return
     */
    public static String originalBeanName(String name) {
        int separatorIndex = name.indexOf(GENERATED_BEAN_NAME_SEPARATOR);
        return (separatorIndex != -1 ? name.substring(0, separatorIndex) : name);
    }

    /**
     * 构建beanName
     * @param definition
     * @return beanName
     * 1. GenericBeanDefinition创建时会解析ClassNode中的注解信息,放入AnnotationAttributes中
     * 2.从注解信息的value中获取
     * 3.如果没有就获取类的短名称
     */
    public static String generateBeanName(BeanDefinition definition){
        String generatedBeanName = definition.getBeanClassName();
        if(definition instanceof GenericBeanDefinition){
            Map<String, Object> annotationAttributes = ((GenericBeanDefinition) definition).getAnnotationAttributes();
            generatedBeanName = String.valueOf(annotationAttributes.get("value"));
        }

        if(StringUtils.isEmpty(generatedBeanName)){
            String className = definition.getBeanClassName();
            int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
            int nameEndIndex = className.length();
            generatedBeanName = className.substring(lastDotIndex + 1, nameEndIndex);
        }

        if(StringUtils.isEmpty(generatedBeanName)){
           return generatedBeanName;
        }

        if(Character.isUpperCase(generatedBeanName.charAt(0))){
            char[] chars = generatedBeanName.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }

        return generatedBeanName;
    }

    public static String generateBeanName(String shortName){
        if(StringUtils.isEmpty(shortName)){
            return shortName;
        }

        if(Character.isUpperCase(shortName.charAt(0))){
            char[] chars = shortName.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
        return shortName;
    }

}

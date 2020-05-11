package io.mgframework.core.config;

import io.mgframework.core.factory.AutowireCapableBeanFactory;
import io.mgframework.core.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import io.mgframework.core.support.BeanDefinitionRegistry;
import io.mgframework.core.support.GenericBeanDefinition;

/**
 * @Author: qianniu
 * @Date: 2020-04-06 10:36
 * @Desc:
 */
public class AnnotationConfigUtils {

    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "io.mgframework.core.annotation.internalAutowiredAnnotationProcessor";

    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry){
        registerAnnotationConfigProcessors(registry, null);
    }

    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry,Object source){

        AutowireCapableBeanFactory beanFactory = getBeanFactory(registry);
        // @Order @Lazy
        if(beanFactory != null){

        }


        if(!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)){
            GenericBeanDefinition definition = new GenericBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
            registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,definition);
        }
    }

    private static AutowireCapableBeanFactory getBeanFactory(BeanDefinitionRegistry registry) {
        if(registry instanceof AutowireCapableBeanFactory){
            return (AutowireCapableBeanFactory) registry;
        }
        return null;
    }
}

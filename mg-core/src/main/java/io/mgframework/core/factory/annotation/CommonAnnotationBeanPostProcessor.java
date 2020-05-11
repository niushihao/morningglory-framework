package io.mgframework.core.factory.annotation;

import io.mgframework.core.BeansException;
import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.config.InstantiationAwareBeanPostProcessor;
import io.mgframework.core.core.PropertyValues;
import io.mgframework.core.factory.AutowireCapableBeanFactory;
import io.mgframework.core.factory.BeanFactory;
import io.mgframework.core.factory.BeanFactoryAware;
import io.mgframework.core.support.BeanFactoryUtils;
import io.mgframework.core.util.ReflectionUtils;
import io.mgframework.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: qianniu
 * @Date: 2020-04-28 17:06
 * @Desc: 处理 @Resource注解 的注入
 */
@Slf4j
public class CommonAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * 注入元信息的缓存
     */
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);


    /**
     * 设置对象处理注入的注解
     */
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);

    public CommonAnnotationBeanPostProcessor() {
        this.autowiredAnnotationTypes.add(Resource.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {

        InjectionMetadata metadata = findResourceMetadata(beanName,bean.getClass(),pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return pvs;
    }

    private InjectionMetadata findResourceMetadata(String beanName, Class<?> beanClass, PropertyValues pvs) {
        String cacheKey = StringUtils.isEmpty(beanName) ? beanClass.getName() : beanName;
        InjectionMetadata metadata = injectionMetadataCache.get(cacheKey);
        // 双重校验加锁
        if(InjectionMetadata.needsRefresh(metadata,beanClass)){
            synchronized (injectionMetadataCache){
                metadata = injectionMetadataCache.get(cacheKey);
                if(InjectionMetadata.needsRefresh(metadata,beanClass)){
                    metadata = buildResourceMetadata(beanClass,beanName);
                    injectionMetadataCache.put(cacheKey,metadata);
                }
            }
        }
        return metadata;
    }

    private InjectionMetadata buildResourceMetadata(Class<?> beanClass,String beanName) {
        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        do{
            ReflectionUtils.doWithLocalFields(beanClass, field -> {

                for(Class clazz : autowiredAnnotationTypes){
                    Annotation annotation = field.getAnnotation(clazz);
                    if(annotation == null){
                        continue;
                    }
                    if(annotation instanceof Resource){
                        if(beanFactory instanceof AutowireCapableBeanFactory){
                            AutowireCapableBeanFactory factory = (AutowireCapableBeanFactory) beanFactory;
                            BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
                            String name = BeanFactoryUtils.generateBeanName(beanDefinition);
                            elements.add(new InjectionMetadata.InjectedElement(field,name,field.getType(),beanFactory));
                        }else {
                            elements.add(new InjectionMetadata.InjectedElement(field,field.getType(),beanFactory));
                        }

                    }
                    /*field.
                    clazz.getMethod("")*/
                }

            });

            beanClass = beanClass.getSuperclass();
        }while (beanClass != null && beanClass != Object.class);

        return new InjectionMetadata(beanClass,elements);
    }
}

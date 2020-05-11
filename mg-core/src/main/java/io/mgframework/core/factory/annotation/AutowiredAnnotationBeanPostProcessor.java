package io.mgframework.core.factory.annotation;

import io.mgframework.core.BeansException;
import io.mgframework.core.config.InstantiationAwareBeanPostProcessor;
import io.mgframework.core.core.PropertyValues;
import io.mgframework.core.factory.BeanFactory;
import io.mgframework.core.factory.BeanFactoryAware;
import io.mgframework.core.util.ReflectionUtils;
import io.mgframework.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: qianniu
 * @Date: 2020-04-06 10:51
 * @Desc: 处理 @Autowired,@Value注解 的注入
 */
@Slf4j
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    /**
     * 设置对象处理注入的注解
     */
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);

    /**
     * 注入元信息的缓存
     */
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);

    private BeanFactory beanFactory;

    public AutowiredAnnotationBeanPostProcessor() {
        this.autowiredAnnotationTypes.add(Autowride.class);
        this.autowiredAnnotationTypes.add(Value.class);
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {

        InjectionMetadata metadata = findAutowiringMetadata(beanName,bean.getClass(),pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return pvs;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 获取要注入的信息
     * @param beanName
     * @param beanClass
     * @param pvs
     * @return
     */
    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> beanClass, PropertyValues pvs) {

        String cacheKey = StringUtils.isEmpty(beanName) ? beanClass.getName() : beanName;
        InjectionMetadata metadata = injectionMetadataCache.get(cacheKey);
        // 双重校验加锁
        if(InjectionMetadata.needsRefresh(metadata,beanClass)){
            synchronized (injectionMetadataCache){
                metadata = injectionMetadataCache.get(cacheKey);
                if(InjectionMetadata.needsRefresh(metadata,beanClass)){
                    metadata = buildAutowiringMetadata(beanClass);
                    injectionMetadataCache.put(cacheKey,metadata);
                }
            }
        }
        return metadata;
    }

    /**
     * 构建要注入的元信息
     * 会遍历beanClass本身和所有父类 获取相关的注解信息
     * @param beanClass
     * @return
     */
    private InjectionMetadata buildAutowiringMetadata(Class<?> beanClass) {
        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        do{
            ReflectionUtils.doWithLocalFields(beanClass,field -> {

                for(Class clazz : autowiredAnnotationTypes){
                    Annotation annotation = field.getAnnotation(clazz);
                    if(annotation == null){
                        continue;
                    }
                    if(annotation instanceof Autowride){
                        elements.add(new InjectionMetadata.InjectedElement(field,field.getType(),beanFactory));
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

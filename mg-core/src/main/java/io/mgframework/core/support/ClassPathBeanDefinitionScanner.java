package io.mgframework.core.support;

import io.mgframework.core.config.AnnotationConfigUtils;
import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.config.Resource;
import io.mgframework.core.core.classreading.MetadataReader;
import io.mgframework.core.core.classreading.SimpleMeatadataReader;
import io.mgframework.core.core.filter.AnnotationTypeFilter;
import io.mgframework.core.core.filter.TypeFilter;
import io.mgframework.core.factory.annotation.Component;
import io.mgframework.core.io.PathMatchingResourcePatternResolver;
import io.mgframework.core.io.ResourceLoader;
import io.mgframework.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author: qianniu
 * @Date: 2020-03-31 22:30
 * @Desc: 根据ClassPath扫描资源并注册Definition
 * 1.通过 resourceLoader 加载指定位置的资源
 * 2.通过 metadataReader 将加载到的资源解析成类的元信息(此处是通过ams)
 * 3.拿到元信息后通过TypeFilter过滤,找到哪些bean是需要解析的
 * 4.被TypeFilter匹配成功后会创建 BeanDefinition 对象,并将原信息放入
 * 5.通过 BeanDefinitionRegistry 将 BeanDefinition 注册到容器中
 */
@Slf4j
public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    private boolean includeAnnotationConfig = true;

    private final List<TypeFilter> includeFilters = new LinkedList<>();

    private final List<TypeFilter> excludeFilters = new LinkedList<>();

    private ResourceLoader resourceLoader;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this(beanDefinitionRegistry,true);
    }

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry,boolean useDefaultFilters) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        if(useDefaultFilters){
            registerDefaultFilters();
        }
        this.resourceLoader = new PathMatchingResourcePatternResolver();
    }

    /**
     * 增加 IncludeFilter
     * @param includeFilter
     */
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * 增加 ExcludeFilter
     * @param excludeFilter
     */
    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }
    /**
     * 注册默认的TypeFilter
     */
    protected void registerDefaultFilters() {
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }

    /**
     * 扫描指定包下的资源并解析成 BeanDefinition
     * @param backPackages 指定的包路径
     * @return 新增的数量
     */
    public int scan(String... backPackages){
        int count = this.beanDefinitionRegistry.getBeanDefinitionCount();

        doScan(backPackages);
        log.info("ClassPathBeanDefinitionScanner end");
        // TODO 注册注解解析器
        if(this.includeAnnotationConfig){
            AnnotationConfigUtils.registerAnnotationConfigProcessors(this.beanDefinitionRegistry);
        }

        count = this.beanDefinitionRegistry.getBeanDefinitionCount() - count;
        log.info("registered {}  BeanDefinitions",count);
        return count;
    }

    /**
     * @param backPackages
     * @return
     */
    private Set<BeanDefinition> doScan(String[] backPackages) {
        Set<BeanDefinition> beanDefinitionHolders = new LinkedHashSet<>();

        for(String basePackage : backPackages){

            Set<BeanDefinition> beanDefinitions = scanCandidateComponents(basePackage);

            // 有些信息需要从注解获取，比如@Lazy，@Primary，@DependsOn这些可以在下边处理

            // 需要校验 BeanDefinition 是否已经被注册,并根据是否允许


            beanDefinitionHolders.addAll(beanDefinitionHolders);

            registerBeanDefinition(beanDefinitions);
        }

        return beanDefinitionHolders;
    }

    private void registerBeanDefinition(Set<BeanDefinition> beanDefinitions) {
        for(BeanDefinition definition : beanDefinitions){
            this.beanDefinitionRegistry.registerBeanDefinition(definition.getBeanName(),definition);
            //log.debug("registered customize beanDefinition ,beanName = {}",definition.getBeanName());
        }
    }

    private Set<BeanDefinition>  scanCandidateComponents(String basePackage) {
        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        try {
            List<Resource> resources = this.resourceLoader.getResource(basePackage);

            if(CollectionUtils.isEmpty(resources)){
                return beanDefinitions;
            }

            for(Resource resource : resources){
                MetadataReader metadataReader = new SimpleMeatadataReader(resource);
                // 执行TypeFilter的匹配方法
                if(isCandidateComponent(metadataReader)){

                    GenericBeanDefinition beanDefinition = new GenericBeanDefinition(metadataReader);
                    beanDefinition.setResource(resource);
                    beanDefinition.setBeanName(BeanFactoryUtils.generateBeanName(beanDefinition));
                    beanDefinition.setBeanClass(Class.forName(beanDefinition.getBeanClassName()));
                    beanDefinitions.add(beanDefinition);
                    log.debug("Identified candidate component class: " + resource.getFilename());
                }else {
                    log.debug("Ignored because not matching any filter: " + resource.getFilename());
                }
            }

        }catch (Exception e){

        }
        return beanDefinitions;
    }

    /**
     * excludeFilters 中任意一个匹配成功则不解析
     * includeFilters 中任意一个匹配成功则解析
     * @param metadataReader
     * @return
     */
    private boolean isCandidateComponent(MetadataReader metadataReader) {

        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader)){
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader)) {
                return true;
            }
        }

        return false;
    }

}

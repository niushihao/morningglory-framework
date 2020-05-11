package io.mgframework.core.factory;

import io.mgframework.core.config.BeanDefinition;
import io.mgframework.core.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import io.mgframework.core.support.ClassPathBeanDefinitionScanner;
import io.mgframework.core.support.GenericBeanDefinition;
import io.mgframework.core.support.bean.ComponentService;
import io.mgframework.core.support.bean.IService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Author: qianniu
 * @Date: 2020-03-30 13:20
 * @Desc:
 */
@Slf4j
public class AutowireCapableBeanFactoryTester {

    String bacePackage = "io.mgframework.core.support.bean";

    @Test
    public void getBean(){

        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setAttribute("123","123");
        definition.setBeanClass(IService.class);
        definition.setScope(BeanDefinition.SCOPE_SINGLETON);
        definition.setBeanClassName("io.mgframework.core.support.bean.ServiceImpl");


        AutowireCapableBeanFactory factory = new AutowireCapableBeanFactory();

        factory.addBeanDefinition("iService",definition);

        IService iService = (IService) factory.getBean("iService");
        log.info("iService = {}",iService);
        iService.sayHi("nsh");

    }

    @Test
    public void getBeanBySacn(){
        AutowireCapableBeanFactory factory = new AutowireCapableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);
        scanner.scan(bacePackage);

        IService iService = factory.getBean("service",IService.class);
        log.info("iService = {}",iService);
        iService.sayHi("nsh");

    }

    @Test
    public void getInjectionBean(){
        AutowireCapableBeanFactory factory = new AutowireCapableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);
        scanner.scan(bacePackage);

        AutowiredAnnotationBeanPostProcessor beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(factory);
        factory.addBeanPostProcessor(beanPostProcessor);
        ComponentService componentService = factory.getBean("componentService",ComponentService.class);
        log.info("componentService = {}",componentService);
        componentService.sayHi();

    }
}

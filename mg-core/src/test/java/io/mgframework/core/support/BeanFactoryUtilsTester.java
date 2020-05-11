package io.mgframework.core.support;

import io.mgframework.core.core.classreading.SimpleMeatadataReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: qianniu
 * @Date: 2020-04-03 13:04
 * @Desc:
 */
@Slf4j
public class BeanFactoryUtilsTester {

    BeanFactoryUtils beanFactoryUtils = new BeanFactoryUtils();

    @Test
    public void generateBeanName() throws IOException {

        SimpleMeatadataReader simpleMeatadataReader = new SimpleMeatadataReader("io.mgframework.core.support.bean.ServiceImpl");

        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition(simpleMeatadataReader);
        String beanName = BeanFactoryUtils.generateBeanName(genericBeanDefinition);

        log.info("beanName = {}",beanName);
    }
}

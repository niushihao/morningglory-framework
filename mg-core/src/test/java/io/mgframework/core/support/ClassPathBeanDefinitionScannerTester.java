package io.mgframework.core.support;

import io.mgframework.core.factory.AutowireCapableBeanFactory;
import org.junit.Test;

/**
 * @Author: qianniu
 * @Date: 2020-04-03 13:25
 * @Desc:
 */
public class ClassPathBeanDefinitionScannerTester {

    String bacePackage = "io.mgframework.core.support.bean";

    @Test
    public void scan(){

        AutowireCapableBeanFactory factory = new AutowireCapableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);
        scanner.scan(bacePackage);
    }
}

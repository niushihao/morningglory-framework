package io.mgframework.core.support;

import io.mgframework.core.support.bean.IService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 12:00
 * @Desc: SPI加载器测试类
 */
@Slf4j
public class MgFactoriesLoaderTester {

    @Test
    public void loadFactoryNames(){
        List<String> factoryNames = MgFactoriesLoader.loadFactoryNames(IService.class, MgFactoriesLoaderTester.class.getClassLoader());

        log.info("factoryNames = {}",factoryNames);
    }

    @Test
    public void loadFactories(){
        List<Object> objects = MgFactoriesLoader
                .loadFactories(IService.class, MgFactoriesLoaderTester.class.getClassLoader());
        log.info("factoryNames = {}",objects);
    }
}

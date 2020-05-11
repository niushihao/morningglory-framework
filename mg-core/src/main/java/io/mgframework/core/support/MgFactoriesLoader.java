package io.mgframework.core.support;

import io.mgframework.core.config.UrlResource;
import io.mgframework.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 09:20
 * @Desc: SPI加载器
 */
@Slf4j
public class MgFactoriesLoader {

    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/mg.factories";

    private static final Map<ClassLoader, Map<String, List<String>>> cache = new ConcurrentHashMap<>();

    private MgFactoriesLoader() {
    }

    /**
     * 加载并创建配置类的对象
     * @param factoryClass
     * @param classLoader
     * @param <T>
     * @return
     */
    public static <T> List<T> loadFactories(Class<?> factoryClass,ClassLoader classLoader){
        List<String> factoryNames = loadFactoryNames(factoryClass, classLoader);
        log.debug("load factoryNames = [{}]",factoryNames);

        List<T> result = new ArrayList<>(factoryNames.size());
        for(String factoryName : factoryNames){
            result.add(instantiateFactory(factoryName,factoryClass,classLoader));
        }
        return result;
    }

    /**
     * 创建fatory实例
     * @param factoryName
     * @param factoryClass
     * @param classLoader
     * @param <T>
     * @return
     */
    private static <T> T instantiateFactory(String factoryName, Class<?> factoryClass, ClassLoader classLoader) {
        try {
            return (T)Class.forName(factoryName).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to instantiate factory class: " + factoryClass.getName(), e);
        }
    }

    /**
     * 加载指定类型下的所有配置类的名称(配置文件的名称)
     * @param factoryClass
     * @param classLoader
     * @return
     */
    public static List<String> loadFactoryNames(Class<?> factoryClass,ClassLoader classLoader){
        String factoryClassName = factoryClass.getName();
        return loadMgFactories(classLoader).getOrDefault(factoryClassName,Collections.EMPTY_LIST);
    }

    /**
     * 加载指定配置文件下所有的配置
     * @param classLoader
     * @return
     */
    private static Map<String,List<String>> loadMgFactories(ClassLoader classLoader){
        Map<String,List<String>> result = cache.get(classLoader);
        if(result != null){
            return result;
        }

        try {
            Enumeration<URL> urls = (classLoader == null) ?
                    ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION) :
                    classLoader.getResources(FACTORIES_RESOURCE_LOCATION);
            result = new LinkedHashMap<>();

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                UrlResource urlResource = new UrlResource(url);
                Properties properties = PropertiesLoaderUtils.loadProperties(urlResource);
                for(Map.Entry<?,?> entry : properties.entrySet()){
                    String factoryClassName = ((String)entry.getKey()).trim();
                    List<String> factoryNames = StringUtils.commaDelimitedListToStringList((String) entry.getValue());
                    List<String> value = result.computeIfAbsent(factoryClassName, k -> new LinkedList<>());
                    value.addAll(factoryNames);
                }
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load factories from location [" +
                    FACTORIES_RESOURCE_LOCATION + "]", e);
        }

        return result;
    }
}

package io.mgframework.core.factory;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 16:31
 * @Desc:
 */
public interface FactoryBean<T> {

    /**
     * 通过 FactoryBean创建对象
     * @return
     */
    T getObject();

    /**
     * 是否为单利
     * @return
     */
    default boolean isSingleton() {
        return true;
    }
}

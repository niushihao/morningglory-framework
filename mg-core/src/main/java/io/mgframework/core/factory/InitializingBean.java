package io.mgframework.core.factory;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:35
 * @Desc: bean被实例化后回调的方法
 */
public interface InitializingBean {

    /**
     * 当bean被实例化后,触发回调此方法
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}

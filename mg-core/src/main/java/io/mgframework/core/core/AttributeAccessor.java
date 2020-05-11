package io.mgframework.core.core;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:49
 * @Desc: 定义metadata属性处理接口
 */
public interface AttributeAccessor {

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    Object removeAttribute(String name);

    String[] attributeNames();

    boolean hasAttribute(String name);
}

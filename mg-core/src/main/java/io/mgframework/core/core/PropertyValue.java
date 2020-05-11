package io.mgframework.core.core;

/**
 * @Author: qianniu
 * @Date: 2020-04-20 14:49
 * @Desc:
 */
public class PropertyValue extends AttributeAccessorSupport{

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}

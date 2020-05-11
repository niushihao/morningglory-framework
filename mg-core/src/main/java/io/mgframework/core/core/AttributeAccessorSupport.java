package io.mgframework.core.core;

import io.mgframework.core.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: qianniu
 * @Date: 2020-04-20 14:43
 * @Desc:
 */
public class AttributeAccessorSupport implements AttributeAccessor{

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    @Override
    public void setAttribute(String name, Object value) {

        if(value != null){
            this.attributes.put(name,value);
        }else {
            this.attributes.remove(name);
        }
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public Object removeAttribute(String name) {
        return this.attributes.remove(name);
    }

    @Override
    public String[] attributeNames() {
        return StringUtils.toStringArray(this.attributes.keySet());
    }

    @Override
    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }
}

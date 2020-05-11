package io.mgframework.core.core;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: qianniu
 * @Date: 2020-04-20 14:54
 * @Desc:
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList;

    public PropertyValues(Map<String, Object> original) {
        if(original == null){
            this.propertyValueList = new ArrayList<>(0);
        }else {
            this.propertyValueList = new ArrayList<>(original.size());
            original.forEach((k,v) -> {
                PropertyValue propertyValue = new PropertyValue(k,v);
                propertyValueList.add(propertyValue);
            });
        }
    }

    public PropertyValues addPropertyValues(Map<String, ?> other) {
        if (other != null) {
            other.forEach((attrName, attrValue) -> addPropertyValue(
                    new PropertyValue(attrName.toString(), attrValue)));
        }
        return this;
    }

    public PropertyValues addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
        return this;
    }

    public void addPropertyValue(String propertyName, Object propertyValue) {
        addPropertyValue(new PropertyValue(propertyName, propertyValue));
    }
}

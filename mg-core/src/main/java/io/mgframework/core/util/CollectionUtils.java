package io.mgframework.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * @Author: qianniu
 * @Date: 2020-04-02 13:20
 * @Desc:
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection){
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }
}

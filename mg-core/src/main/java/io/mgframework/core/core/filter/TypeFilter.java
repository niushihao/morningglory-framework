package io.mgframework.core.core.filter;

import io.mgframework.core.core.classreading.MetadataReader;

/**
 * @Author: qianniu
 * @Date: 2020-04-01 22:04
 * @Desc:
 */
public interface TypeFilter {

    /**
     * 读取资源，并校验是否匹配
     * @param metadataReader
     * @return
     */
    boolean match(MetadataReader metadataReader);
}

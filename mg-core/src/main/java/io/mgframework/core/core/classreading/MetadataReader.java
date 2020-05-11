package io.mgframework.core.core.classreading;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

/**
 * @Author: qianniu
 * @Date: 2020-04-01 22:05
 * @Desc:
 */
public interface MetadataReader {

    /**
     * 这里直接使用第三方类库，增加了接口的局限性，可以考虑转化成自己的对象
     * @return
     */
    ClassNode getClassNode();

}

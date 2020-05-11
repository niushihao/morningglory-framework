package io.mgframework.core.io;

import io.mgframework.core.config.Resource;

import java.io.IOException;
import java.util.List;

/**
 * @Author: qianniu
 * @Date: 2020-03-31 19:59
 * @Desc: 资源加载
 */
public interface ResourceLoader {

    /**
     * 根据路径加载资源
     * @param path
     * @return
     */
    List<Resource> getResource(String path) throws IOException;
}

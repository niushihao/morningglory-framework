package io.mgframework.core.io;

import io.mgframework.core.config.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @Author: qianniu
 * @Date: 2020-03-31 22:18
 * @Desc:
 */
@Slf4j
public class PathMatchingResourcePatternResolverTester {

    private final static String bace_package = "io.mgframework.core";

    @Test
    public void getResource() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resource = resolver.getResource(bace_package);
        log.info(String.valueOf(resource.size()));
    }
}

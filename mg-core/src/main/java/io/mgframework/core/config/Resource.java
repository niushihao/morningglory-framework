package io.mgframework.core.config;

import java.io.File;

/**
 * @Author: qianniu
 * @Date: 2020-03-26 09:19
 * @Desc:
 */
public interface Resource extends InputStreamSource{

    String getFilename();

    File getFile();
}

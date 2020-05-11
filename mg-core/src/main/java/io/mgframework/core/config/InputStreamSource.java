package io.mgframework.core.config;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: qianniu
 * @Date: 2020-03-26 09:18
 * @Desc:
 */
public interface InputStreamSource {

    InputStream getInputStream() throws IOException;
}

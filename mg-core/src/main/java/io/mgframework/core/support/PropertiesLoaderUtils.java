package io.mgframework.core.support;

import io.mgframework.core.config.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 09:51
 * @Desc:
 */
public class PropertiesLoaderUtils {

    private static final String XML_FILE_EXTENSION = ".xml";

    public static Properties loadProperties(Resource resource) throws IOException {
        Properties properties = new Properties();
        fillProperties(properties,resource);
        return properties;
    }

    private static void fillProperties(Properties properties, Resource resource) throws IOException {
        InputStream is = resource.getInputStream();
        try {
            String filename = resource.getFilename();
            if(filename != null && filename.endsWith(XML_FILE_EXTENSION)){
                properties.loadFromXML(is);
            }else {
                properties.load(is);
            }

        }finally {
            is.close();
        }

    }

}

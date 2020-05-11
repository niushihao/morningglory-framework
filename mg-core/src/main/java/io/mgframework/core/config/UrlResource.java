package io.mgframework.core.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 09:55
 * @Desc:
 */
public class UrlResource implements Resource{

    private  URI uri = null;

    private  URL url = null;

    private  URL cleanenUrl = null;

    public UrlResource(URL url) {
        this.uri = null;
        this.url = url;
        this.cleanenUrl = url;
    }

    public UrlResource(File file){
        this.uri = null;
        try {
            this.url = file.toURL();
            this.cleanenUrl = url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {

        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }

    @Override
    public String getFilename() {
        return url.getFile();
    }

    @Override
    public File getFile() {
        return new File(url.getFile());
    }
}

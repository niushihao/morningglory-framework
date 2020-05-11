package io.mgframework.core.io;

import io.mgframework.core.config.Resource;
import io.mgframework.core.config.UrlResource;
import io.mgframework.core.util.ResourceUtils;
import io.mgframework.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @Author: qianniu
 * @Date: 2020-03-31 20:08
 * @Desc: 根据类路径加载资源
 */
@Slf4j
public class PathMatchingResourcePatternResolver implements ResourceLoader {
    @Override
    public List<Resource> getResource(String path) throws IOException {
        if(StringUtils.isEmpty(path)){
            throw new IllegalArgumentException("path must not null");
        }
        path = path.replace(".","/");

        return findPathMatchingResources(path);
    }

    /**
     * 通过 ClassLoader.getSystemResources 加载指定路径下的资源
     * @param path
     * @return
     * @throws IOException
     */
    private List<Resource> findPathMatchingResources(String path) throws IOException {


        Set<Resource> result = new LinkedHashSet<>(16);
        Enumeration<URL> rootResources = ClassLoader.getSystemResources(path);
        while (rootResources.hasMoreElements()){
            URL url = rootResources.nextElement();
            Resource rootResource = new UrlResource(url);
            if(url.getProtocol().startsWith(ResourceUtils.URL_PROTOCOL_VFS)){

                
            }else if(ResourceUtils.isJarFileURL(url)){

            }else if(ResourceUtils.isFileURL(url)){
               result.addAll(doFindPathMatchingFileResources(rootResource));
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * 获取 根资源下的所有资源
     * @param rootResource
     * @return
     */
    private Set<Resource> doFindPathMatchingFileResources(Resource rootResource) {
        File rootFile = rootResource.getFile().getAbsoluteFile();
        if(!rootFile.exists()){
            return Collections.EMPTY_SET;
        }

        if(!rootFile.isDirectory()){
            return Collections.EMPTY_SET;
        }

        if(!rootFile.canRead()){
            return Collections.EMPTY_SET;
        }

        String fullPath = rootFile.getAbsolutePath().replace(File.separator,"/");

        Set<File> files = new HashSet<>(16);
        doRetrieveMatchingFiles(fullPath,rootFile,files);
        log.debug("find {} classFiles from {}",files.size(),fullPath);
        Set<Resource> resources = new HashSet<>();
        for(File file : files){
            Resource resource = new UrlResource(file);
            resources.add(resource);
        }
        return resources;
    }

    private void doRetrieveMatchingFiles(String fullPath, File rootFile,Set<File> files) {

        for(File file : rootFile.listFiles()){
            String currPath = file.getAbsolutePath().replace(File.separator,"/");
            if(file.isDirectory()){
                doRetrieveMatchingFiles(currPath,file,files);
            }else {
                files.add(file);
            }
        }
    }
}

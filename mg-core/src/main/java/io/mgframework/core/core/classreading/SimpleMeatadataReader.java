package io.mgframework.core.core.classreading;

import io.mgframework.core.config.Resource;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: qianniu
 * @Date: 2020-04-01 22:06
 * @Desc:
 */
public class SimpleMeatadataReader implements MetadataReader{


    private final ClassNode classNode;

    public SimpleMeatadataReader(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();
        ClassReader classReader = new ClassReader(inputStream);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode,0);
        this.classNode = classNode;

    }

    public SimpleMeatadataReader(String className) throws IOException {
        ClassReader classReader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode,0);
        this.classNode = classNode;
    }


    @Override
    public ClassNode getClassNode() {
        return this.classNode;
    }
}

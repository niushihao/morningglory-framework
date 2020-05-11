package io.mgframework.core.core.filter;

import io.mgframework.core.core.classreading.MetadataReader;
import io.mgframework.core.util.CollectionUtils;
import io.mgframework.core.util.StringUtils;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qianniu
 * @Date: 2020-04-01 22:16
 * @Desc: 这里的设计是每个 AnnotationTypeFilter 只能过滤一种Annotation,如果需要过滤多个,可以使用衍生注解或者多个TypeFilter
 */
@Slf4j
public class AnnotationTypeFilter implements TypeFilter{

    private final Class<? extends Annotation> annotationType;

    public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }


    /**
     * 1.通过asm读取类信息,并获取注解信息
     * 2.判断类上的注解是否和 this.annotationType 匹配;如果匹配则结束
     * 3.遍历注解上的注解 并判断 和 this.annotationType 匹配
     * @param metadataReader
     * @return
     */
    @Override
    public boolean match(MetadataReader metadataReader) {
        ClassNode classNode = metadataReader.getClassNode();
        List<AnnotationNode> visibleAnnotations = classNode.visibleAnnotations;
        if(CollectionUtils.isEmpty(visibleAnnotations)){
            return false;
        }

        // 获取注解的名称
        List<String> annotationNames = getAnnotationNames(visibleAnnotations);
        if(CollectionUtils.isEmpty(annotationNames)){
            return false;
        }

        // 如果有设置的注解,则匹配成功
        if(annotationNames.contains(annotationType.getName())){
            return true;
        }

        // 获取衍生的注解名称
        List<String> derivativeNames = getDerivatNames(annotationNames);
        if(derivativeNames.contains(annotationType.getName())){
            return true;
        }
        return false;
    }

    /**
     * 根据主键全路径名获取 注解上的注解
     * e.g. this.annotationType 为 @Component 时，我们希望当我们的类使用 @Service 时也可以匹配
     * 所以这里会找 @Service 上是否有 @Component注解
     * @param annotationNames 我们类或方法上使用的注解
     * @return
     */
    public List<String> getDerivatNames(List<String> annotationNames) {
        List<String> derivativeNames = new ArrayList<>();
        for(String name : annotationNames){
            doGetDerivatNames(name,derivativeNames);
        }
        return derivativeNames;
    }

    /**
     * 递归获取所有的衍生注解名称
     * e.g. @Service可以看做是@Component衍生出的注解
     * @param annotationName    我们类或方法上使用的注解
     * @param derivativeNames   注解中使用到的注解
     */
    private void doGetDerivatNames(String annotationName,List<String> derivativeNames){
        try {
            Class<? extends Annotation> annotationClass =
                    (Class<? extends Annotation>) ClassLoader.getSystemClassLoader().loadClass(annotationName);

            Annotation[] annotations = annotationClass.getAnnotations();
            if(annotations == null || annotations.length == 0){
                return;
            }

            for(Annotation annotation : annotations){
                String name = annotation.annotationType().getName();
                // 忽略jdk自带注解
                if(StringUtils.isInJavaLangAnnotationPackage(name)){
                    continue;
                }
                doGetDerivatNames(name,derivativeNames);
                derivativeNames.add(name);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据 AnnotationNode 获取注解名称
     * @param visibleAnnotations
     */
    private List<String> getAnnotationNames(List<AnnotationNode> visibleAnnotations) {
        List<String> list = new ArrayList<>();
        for(AnnotationNode node : visibleAnnotations){
            String desc = node.desc.replace("/",".");
            String annoName = desc.substring(1,desc.length()-1);
            list.add(annoName);
        }
        return list;
    }
}

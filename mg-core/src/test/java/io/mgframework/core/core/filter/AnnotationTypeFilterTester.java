package io.mgframework.core.core.filter;

import io.mgframework.core.core.classreading.MetadataReader;
import io.mgframework.core.core.classreading.SimpleMeatadataReader;
import io.mgframework.core.factory.annotation.Component;
import io.mgframework.core.factory.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qianniu
 * @Date: 2020-04-02 13:51
 * @Desc:
 */
@Slf4j
public class AnnotationTypeFilterTester {


    @Test
    public void getDerivatNames(){
        List<String> annotationNames = new ArrayList<>();
        annotationNames.add(Service.class.getName());

        AnnotationTypeFilter filter = new AnnotationTypeFilter(Component.class);

        List<String> derivatNames = filter.getDerivatNames(annotationNames);

        log.info("derivatNames = {}",derivatNames);
    }

    @Test
    public void match() throws IOException {
        AnnotationTypeFilter filter = new AnnotationTypeFilter(Component.class);

        MetadataReader reader = new SimpleMeatadataReader(Service.class.getName());
        boolean result = filter.match(reader);
        log.info("Service match Component result ={}",result);
    }
}

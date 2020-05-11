package io.mgframework.core.support.bean;

import io.mgframework.core.factory.annotation.Autowride;
import io.mgframework.core.factory.annotation.Component;

/**
 * @Author: qianniu
 * @Date: 2020-04-26 14:11
 * @Desc:
 */
@Component
public class ComponentService {

    @Autowride
    private IService iService;

    public void sayHi(){
        iService.sayHi("nsh");
    }
}

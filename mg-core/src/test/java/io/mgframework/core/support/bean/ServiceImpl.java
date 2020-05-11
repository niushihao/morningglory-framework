package io.mgframework.core.support.bean;

import io.mgframework.core.factory.annotation.Service;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 12:07
 * @Desc:
 */
@Service(value = "service")
public class ServiceImpl implements IService{


    @Override
    public void sayHi(String msg) {
        System.out.println("Hi,"+msg);
    }
}

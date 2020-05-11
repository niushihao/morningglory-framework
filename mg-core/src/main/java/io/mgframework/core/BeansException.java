package io.mgframework.core;

/**
 * @Author: qianniu
 * @Date: 2020-03-25 22:19
 * @Desc:
 */
public class BeansException extends RuntimeException{

    /**
     * 根据指定信息创建异常
     * @param msg
     */
    public BeansException(String msg) {
        super(msg);
    }

    /**
     * 根据指定信息和异常堆栈创建异常
     * @param msg
     * @param cause
     */
    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

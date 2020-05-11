package io.mgframework.core.util;

import java.lang.reflect.Field;

/**
 * @Author: qianniu
 * @Date: 2020-04-21 10:43
 * @Desc: 反射工具类
 */
public class ReflectionUtils {


    public static void doWithLocalFields(Class<?> clazz,FieldCallback fieldCallback){
        for(Field field : clazz.getDeclaredFields()){
            try {
                fieldCallback.doWith(field);
            }catch (IllegalAccessException ex) {
                throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
            }
        }
    }

    @FunctionalInterface
    public interface FieldCallback {

        /**
         * Perform an operation using the given field.
         * @param field the field to operate on
         */
        void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
    }
}

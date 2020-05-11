package io.mgframework.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: qianniu
 * @Date: 2020-03-27 10:08
 * @Desc: String工具类
 */
public class StringUtils {

    private static final String FOLDER_SEPARATOR = "/";
    private static final String COMMA = ",";

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.length() == 0 || str.trim().length() == 0 || "null".equals(str);
    }
    /**
     * 根据路径 获取文件名
     * eg: /usr/demo.txt -> demo.txt
     * @param path
     * @return
     */
    public static String getFileName(String path){

        if(path == null){
            return null;
        }

        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * 按逗号分隔字符传
     * @param str
     * @return
     */
    public static String[] commaDelimitedListToStringArray(String str){
        if(str.contains(COMMA)){
            return str.split(COMMA);
        }
        return new String[]{str};
    }

    /**
     * 按照逗号分隔字符串
     * @param str
     * @return
     */
    public static List<String> commaDelimitedListToStringList(String str){
        if(str.contains(COMMA)){
           return Arrays.stream(str.split(COMMA)).collect(Collectors.toList());
        }
        List<String> list = new ArrayList<>(1);
        list.add(str);
        return list;
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection.toArray(new String[0]);
    }

    /**
     * 判断注解是否是jdk注解
     * @param packageName
     * @return
     */
    public static boolean isInJavaLangAnnotationPackage(String packageName){
        return (packageName != null && packageName.startsWith("java.lang.annotation"));
    }

}

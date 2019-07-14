package com.watson.saralink;

public class ExceptionUtils {

    /**
     * 判断是否为runtime，
     * 是直接抛出
     * 不是转成runtime抛出
     * @param e
     */
    public static void throwException(Exception e) {
        if(e instanceof RuntimeException) {
            throw (RuntimeException)e;
        }
        throw new RuntimeException(e);
    }
}

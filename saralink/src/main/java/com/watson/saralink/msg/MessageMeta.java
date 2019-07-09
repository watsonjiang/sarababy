package com.watson.saralink.msg;


import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型注册器
 */
public class MessageMeta {

    static Map<Integer, Class<? extends Message>>  type2Clz = new HashMap<>();
    static Map<Class<? extends Message>, Integer>  clz2Type = new HashMap<>();

    public static void registerMessage(int type, Class<? extends Message> clz) {
        type2Clz.put(type, clz);
        clz2Type.put(clz, type);
    }

    public static int getMessageType(Class<? extends Message> clz) {
        return clz2Type.get(clz);
    }

    public static Class<? extends Message> getMessageClz(Integer type) {
        return type2Clz.get(type);
    }
}

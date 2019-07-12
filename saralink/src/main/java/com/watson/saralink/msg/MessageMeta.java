package com.watson.saralink.msg;


import java.util.HashMap;
import java.util.Map;

import static com.watson.saralink.msg.Constant.TYPE_CMD_EXEC_REQ;
import static com.watson.saralink.msg.Constant.TYPE_CMD_EXEC_RSP;
import static com.watson.saralink.msg.Constant.TYPE_HEARTBEAT_REQ;
import static com.watson.saralink.msg.Constant.TYPE_HEARTBEAT_RSP;
import static com.watson.saralink.msg.Constant.TYPE_LOGIN_REQ;
import static com.watson.saralink.msg.Constant.TYPE_LOGIN_RSP;

/**
 * 消息类型注册器
 */
public class MessageMeta {
    static Map<Integer, Class<? extends Message>>  type2Clz = new HashMap<>();
    static Map<Class<? extends Message>, Integer>  clz2Type = new HashMap<>();

    static {
        registerMessage(TYPE_CMD_EXEC_REQ, CmdExecReq.class);
        registerMessage(TYPE_CMD_EXEC_RSP, CmdExecRsp.class);
        registerMessage(TYPE_HEARTBEAT_REQ, HeartBeatReq.class);
        registerMessage(TYPE_HEARTBEAT_RSP, HeartBeatRsp.class);
        registerMessage(TYPE_LOGIN_REQ, LoginReq.class);
        registerMessage(TYPE_LOGIN_RSP, LoginRsp.class);
    }

    public static void registerMessage(int type, Class<? extends Message> clz) {
        type2Clz.put(type, clz);
        clz2Type.put(clz, type);
    }

    /**
     * 消息class换消息类型id
     * @param clz
     * @return -1 未知消息class
     */
    public static int getMessageType(Class<? extends Message> clz) {

        return clz2Type.get(clz);
    }

    /**
     * 消息类型id换消息class
     * @param type
     * @return null 未知消息类型id
     */
    public static Class<? extends Message> getMessageClz(Integer type) {
        return type2Clz.get(type);
    }
}

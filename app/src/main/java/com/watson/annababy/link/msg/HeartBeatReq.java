package com.watson.annababy.link.msg;

import static com.watson.annababy.link.msg.Constant.TYPE_HEARTBEAT_REQ;

public class HeartBeatReq extends Message {

    static {
        MessageMeta.registerMessage(TYPE_HEARTBEAT_REQ, HeartBeatReq.class);
    }

    public HeartBeatReq() {
        id = genId();
    }

}

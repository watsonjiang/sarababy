package com.watson.saralink.msg;

import static com.watson.saralink.msg.Constant.TYPE_HEARTBEAT_RSP;

public class HeartBeatRsp extends Message {

    static {
        MessageMeta.registerMessage(TYPE_HEARTBEAT_RSP, HeartBeatRsp.class);
    }

    public String message;

    public HeartBeatRsp(HeartBeatReq req) {
        id = req.id;
    }

}

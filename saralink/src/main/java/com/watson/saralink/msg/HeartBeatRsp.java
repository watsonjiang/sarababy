package com.watson.saralink.msg;

import static com.watson.saralink.msg.Constant.TYPE_HEARTBEAT_RSP;

public class HeartBeatRsp extends Message {

    public String message;

    public HeartBeatRsp(HeartBeatReq req) {
        id = req.id;
    }

}

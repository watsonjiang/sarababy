package com.watson.saralink.msg;

import static com.watson.saralink.msg.Constant.TYPE_HEARTBEAT_REQ;

public class HeartBeatReq extends Message {

    public HeartBeatReq() {
        id = genId();
    }

}

package com.watson.annababy.link.msg;

import static com.watson.annababy.link.msg.Constant.TYPE_LOGIN_REQ;

public class LoginReq extends Message{

    static {
        MessageMeta.registerMessage(TYPE_LOGIN_REQ, LoginReq.class);
    }

    public String utdid;

    public LoginReq() {
        id = genId();
    }

}

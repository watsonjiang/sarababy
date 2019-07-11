package com.watson.saralink.msg;

import static com.watson.saralink.msg.Constant.TYPE_LOGIN_RSP;

public class LoginRsp extends Message {

    static {
        MessageMeta.registerMessage(TYPE_LOGIN_RSP, LoginRsp.class);
    }

    public String peerId;

    public LoginRsp(LoginReq req) {
        id = req.id;
    }

}

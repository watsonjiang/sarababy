package com.watson.annababy.link.msg;

import static com.watson.annababy.link.msg.Constant.TYPE_LOGIN_RSP;

public class LoginRsp extends Message {

    static {
        MessageMeta.registerMessage(TYPE_LOGIN_RSP, LoginRsp.class);
    }

    public LoginRsp(LoginReq req) {
        id = req.id;
    }

}

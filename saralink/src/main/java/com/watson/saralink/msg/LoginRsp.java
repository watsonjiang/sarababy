package com.watson.saralink.msg;

import static com.watson.saralink.msg.Constant.TYPE_LOGIN_RSP;

public class LoginRsp extends Message {

    public String peerId;

    public LoginRsp(LoginReq req) {
        id = req.id;
    }

}

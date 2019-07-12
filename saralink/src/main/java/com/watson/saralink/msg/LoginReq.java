package com.watson.saralink.msg;

import static com.watson.saralink.msg.Constant.TYPE_LOGIN_REQ;

public class LoginReq extends Message{

    public String peerId;

    public LoginReq() {
        id = genId();
    }

}

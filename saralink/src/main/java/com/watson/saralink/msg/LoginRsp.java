package com.watson.saralink.msg;

public class LoginRsp extends Message {

    public String peerId;

    public LoginRsp(LoginReq req) {
        id = req.id;
    }

}

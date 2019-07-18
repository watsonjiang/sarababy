package com.watson.saralink.msg;

public class CmdExecReq extends Message{

    public String cmdLine;

    public CmdExecReq() {
        id = genId();
    }
}

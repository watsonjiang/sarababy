package com.watson.saralink.msg;

import static com.watson.saralink.msg.Constant.TYPE_CMD_EXEC_REQ;

public class CmdExecReq extends Message{

    public String cmdLine;

    public CmdExecReq() {
        id = genId();
    }

}

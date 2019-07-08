package com.watson.annababy.link.msg;

import static com.watson.annababy.link.msg.Constant.TYPE_CMD_EXEC_REQ;

public class CmdExecReq extends Message{

    static {
       MessageMeta.registerMessage(TYPE_CMD_EXEC_REQ, CmdExecReq.class);
    }

    public CmdExecReq() {
        id = genId();
    }

}

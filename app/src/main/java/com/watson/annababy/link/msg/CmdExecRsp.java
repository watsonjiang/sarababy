package com.watson.annababy.link.msg;

import static com.watson.annababy.link.msg.Constant.TYPE_CMD_EXEC_RSP;

public class CmdExecRsp extends Message{

    static {
        MessageMeta.registerMessage(TYPE_CMD_EXEC_RSP, CmdExecRsp.class);
    }

    public CmdExecRsp(CmdExecReq req) {
        id = req.id;
    }

}

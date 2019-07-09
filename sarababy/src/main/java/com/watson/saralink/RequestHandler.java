package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;

/**
 * 职责，处理命令
 */
public class RequestHandler {

    public CmdExecRsp onCmdExec(CmdExecReq req) {
        CmdExecRsp rsp = new CmdExecRsp(req);

        return rsp;
    }
}

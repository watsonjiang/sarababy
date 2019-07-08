package com.watson.annababy;

import com.watson.annababy.link.msg.CmdExecReq;
import com.watson.annababy.link.msg.CmdExecRsp;

/**
 * 职责，处理命令
 */
public class RequestHandler {

    public CmdExecRsp onCmdExec(CmdExecReq req) {
        CmdExecRsp rsp = new CmdExecRsp(req);

        return rsp;
    }
}

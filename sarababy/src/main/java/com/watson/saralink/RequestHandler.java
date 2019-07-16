package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 职责，处理命令
 */
public class RequestHandler implements IRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    public CmdExecRsp onCmdExec(CmdExecReq req) {
        LOGGER.info("----onCmdExec. cmd:{}", req.cmdLine);
        CmdExecRsp rsp = new CmdExecRsp(req);
        rsp.output = "hello anna";
        return rsp;
    }
}

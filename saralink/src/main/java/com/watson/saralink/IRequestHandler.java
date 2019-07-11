package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;

/**
 * 业务请求接口
 */
public interface IRequestHandler {
    CmdExecRsp onCmdExec(CmdExecReq req);
}

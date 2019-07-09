package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;

public interface IRequestHandler {
    CmdExecRsp onCmdExec(CmdExecReq req);
}

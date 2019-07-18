package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.ScreenCapReq;
import com.watson.saralink.msg.ScreenCapRsp;

/**
 * 业务请求接口
 */
public interface IRequestHandler {
    CmdExecRsp onCmdExec(CmdExecReq req);

    ScreenCapRsp onScreenCap(ScreenCapReq req);
}

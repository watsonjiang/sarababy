package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.LoginRsp;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理链路消息
 */
public class LinkMessageHandler extends IoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkMessageHandler.class);

    BaseFSM fsm;

    public LinkMessageHandler(BaseFSM fsm) {
        this.fsm = fsm;
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        LOGGER.info("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        LOGGER.info("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        LOGGER.info("sessionClosed");
        fsm.postEv(LinkEvent.EV_EXCEPTION);
    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        if(o instanceof LoginRsp) {
            linkConnector.onLoginRsp((LoginRsp)o);
        }else if(o instanceof CmdExecReq) {
            linkConnector.onCmdExecReq((CmdExecReq)o);
        }
    }

}

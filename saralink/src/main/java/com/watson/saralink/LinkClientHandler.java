package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.LoginReq;
import com.watson.saralink.msg.LoginRsp;
import com.watson.saralink.msg.ScreenCapReq;
import com.watson.saralink.msg.ScreenCapRsp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.statemachine.annotation.IoHandlerTransition;
import org.apache.mina.statemachine.annotation.State;
import org.apache.mina.statemachine.event.Event;
import org.apache.mina.statemachine.event.IoHandlerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkClientHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkAcceptor.class);

    @State
    public static final String ST_ROOT = "Root";

    @State(ST_ROOT)
    public static final String ST_EMPTY = "Empty";

    @State(ST_ROOT)
    public static final String ST_AUTHENTICATING = "Authenticating";

    @State(ST_ROOT)
    public static final String ST_AUTHENTICATED = "Authenticated";

    private String peerId;

    public LinkClientHandler(String peerId) {
        this.peerId = peerId;
    }

    @IoHandlerTransition(on = IoHandlerEvents.SESSION_OPENED, in = ST_EMPTY, next = ST_AUTHENTICATING)
    public void onSessionOpen(IoSession session) {
        LOGGER.info("session open. local:{} remote:{}", session.getLocalAddress(), session.getRemoteAddress());
        LoginReq req = new LoginReq();
        req.peerId = peerId;
        session.write(req);
    }

    @IoHandlerTransition(on = IoHandlerEvents.MESSAGE_RECEIVED, in = ST_AUTHENTICATING, next = ST_AUTHENTICATED)
    public void onLoginRsp(LinkStateContext context, IoSession session, LoginRsp rsp) {
        context.peerId = rsp.peerId;
        context.session = session;
        LinkManager.getInstance().register(context.peerId, context);
    }

    @IoHandlerTransition(on = IoHandlerEvents.MESSAGE_RECEIVED, in = ST_AUTHENTICATED)
    public void onCmdExecRep(LinkStateContext context, IoSession session, CmdExecReq req) {
        CmdExecRsp rsp = context.requestHandler.onCmdExec(req);
        session.write(rsp);
    }

    @IoHandlerTransition(on = IoHandlerEvents.MESSAGE_RECEIVED, in = ST_AUTHENTICATED)
    public void onScreenCapReq(LinkStateContext context, IoSession session, ScreenCapReq req) {
        ScreenCapRsp rsp = context.requestHandler.onScreenCap(req);
        session.write(rsp);
    }

    @IoHandlerTransition(on = IoHandlerEvents.INPUT_CLOSED, in = ST_ROOT)
    public void onInputClosed(IoSession session) {
        session.closeNow();
    }

    @IoHandlerTransition(on = IoHandlerEvents.SESSION_CLOSED, in = ST_ROOT)
    public void onSessionClosed(LinkStateContext context, IoSession session) {
        LinkManager.getInstance().unregister(context.peerId);
    }

    @IoHandlerTransition(in = ST_ROOT, weight = 100)
    public void unhandledEvent(Event ev) {
        LOGGER.info("unhandled event. ev:{}", ev);
    }

}

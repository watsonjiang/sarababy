package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.LoginReq;
import com.watson.saralink.msg.LoginRsp;
import com.watson.saralink.msg.ScreenCapRsp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.statemachine.annotation.IoHandlerTransition;
import org.apache.mina.statemachine.annotation.State;
import org.apache.mina.statemachine.event.Event;
import org.apache.mina.statemachine.event.IoHandlerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class LinkServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkServerHandler.class);

    @State
    public static final String ST_ROOT = "Root";

    @State(ST_ROOT)
    public static final String ST_EMPTY = "Empty";

    @State(ST_ROOT)
    public static final String ST_CONNECTED = "Connected";

    @State(ST_ROOT)
    public static final String ST_AUTHENTICATED = "Authenticated";

    @State(ST_ROOT)
    public static final String ST_DISCONNECTED = "Disconnected";

    @IoHandlerTransition(on = IoHandlerEvents.SESSION_OPENED, in = ST_EMPTY, next = ST_CONNECTED)
    public void onSessionOpened(IoSession session) {
        LOGGER.info("incoming session. remote addr:{}", session.getRemoteAddress());
    }

    @IoHandlerTransition(on = IoHandlerEvents.MESSAGE_RECEIVED, in = ST_CONNECTED, next = ST_AUTHENTICATED)
    public void onAuthenticate(LinkStateContext context, IoSession session, LoginReq req) {
        context.peerId = req.peerId;
        LoginRsp rsp = new LoginRsp(req);
        rsp.peerId = "annababy";
        context.session = session;
        session.write(rsp);
        LinkManager.getInstance().register(req.peerId, context);
    }

    @IoHandlerTransition(on = IoHandlerEvents.MESSAGE_RECEIVED, in = ST_AUTHENTICATED)
    public void onCmdExecRsp(LinkStateContext context, IoSession session, CmdExecRsp rsp) {
        while(false == context.resultQueue.offer(rsp));
    }

    @IoHandlerTransition(on = IoHandlerEvents.MESSAGE_RECEIVED, in = ST_AUTHENTICATED)
    public void onScreenCapRsp(LinkStateContext context, IoSession session, ScreenCapRsp rsp) {
        while(false == context.resultQueue.offer(rsp));
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

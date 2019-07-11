package com.watson.saralink;

import com.watson.saralink.codec.MessageCodecFactory;
import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.HeartBeatReq;
import com.watson.saralink.msg.HeartBeatRsp;
import com.watson.saralink.msg.LoginReq;
import com.watson.saralink.msg.LoginRsp;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.statemachine.StateMachine;
import org.apache.mina.statemachine.StateMachineFactory;
import org.apache.mina.statemachine.StateMachineProxyBuilder;
import org.apache.mina.statemachine.annotation.IoFilterTransition;
import org.apache.mina.statemachine.annotation.IoHandlerTransition;
import org.apache.mina.statemachine.context.IoSessionStateContextLookup;
import org.apache.mina.statemachine.context.StateContext;
import org.apache.mina.statemachine.context.StateContextFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LinkAcceptor {
    //触发加载消息类，不用赋值
    static CmdExecReq cmdExecReq;
    static CmdExecRsp cmdExecRsp;
    static HeartBeatReq heartBeatReq;
    static HeartBeatRsp heartBeatRsp;
    static LoginReq loginReq;
    static LoginRsp loginRsp;

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkAcceptor.class);

    InetSocketAddress socketAddress;

    IoHandler createIoHandler() {
        StateMachine sm = StateMachineFactory.getInstance(
                IoHandlerTransition.class).create(LinkServerHandler.ST_EMPTY,
                new LinkServerHandler());

        return new StateMachineProxyBuilder().setStateContextLookup(
                new IoSessionStateContextLookup(new StateContextFactory() {
                    public StateContext create() {
                        return new LinkStateContext();
                    }
                })).create(IoHandler.class, sm);
    }

    public LinkAcceptor(String addr, int port) {
        socketAddress = new InetSocketAddress(addr, port);
    }

    void bind() {
        SocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setReuseAddress(true);
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
        acceptor.getFilterChain().addLast("keepalive", new KeepAliveFilter(new KeepAliveMessageFactoryImpl(true)));
        acceptor.setHandler(createIoHandler());
        try {
            acceptor.bind(socketAddress);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

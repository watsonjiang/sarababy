package com.watson.saralink;

import com.watson.saralink.codec.MessageCodecFactory;
import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.HeartBeatReq;
import com.watson.saralink.msg.HeartBeatRsp;
import com.watson.saralink.msg.LoginReq;
import com.watson.saralink.msg.LoginRsp;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.statemachine.StateMachine;
import org.apache.mina.statemachine.StateMachineFactory;
import org.apache.mina.statemachine.StateMachineProxyBuilder;
import org.apache.mina.statemachine.annotation.IoHandlerTransition;
import org.apache.mina.statemachine.context.IoSessionStateContextLookup;
import org.apache.mina.statemachine.context.StateContext;
import org.apache.mina.statemachine.context.StateContextFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 职责：
 *    创建并保持与anna的连接
 */
public class LinkConnector {

    //触发加载消息类，不用赋值
    static CmdExecReq cmdExecReq;
    static CmdExecRsp cmdExecRsp;
    static HeartBeatReq heartBeatReq;
    static HeartBeatRsp heartBeatRsp;
    static LoginReq loginReq;
    static LoginRsp loginRsp;

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkConnector.class);

    NioSocketConnector connector = new NioSocketConnector();

    IRequestHandler requestHandler;

    InetSocketAddress socketAddress;


    LinkConnector(String peerId, IRequestHandler handler, String addr, int port) {
        initConnector(peerId, handler);
        socketAddress = new InetSocketAddress(addr, port);
    }

    IoHandler createIoHandler(String peerId, IRequestHandler handler) {
        StateMachine sm = StateMachineFactory.getInstance(
                IoHandlerTransition.class).create(LinkClientHandler.ST_EMPTY,
                new LinkClientHandler(peerId));

        return new StateMachineProxyBuilder().setStateContextLookup(
                new IoSessionStateContextLookup(new StateContextFactory() {
                    public StateContext create() {
                        return new LinkStateContext() {{requestHandler = handler;}};
                    }
                })).create(IoHandler.class, sm);
    }

    void initConnector(String peerId, IRequestHandler handler) {
        connector.setConnectTimeoutMillis(1000);
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageFactoryImpl(false));
        keepAliveFilter.setRequestInterval(30);
        connector.getFilterChain().addLast("keepalive", keepAliveFilter);
        connector.setHandler(createIoHandler(peerId, handler));
    }

    public void setRequestHandler(IRequestHandler h) {
        requestHandler = h;
    }

    void connect() {
        ConnectFuture f = connector.connect(socketAddress);
        f.awaitUninterruptibly();
        if (f.isConnected()) {
            LOGGER.info("connect success.");
        } else {
            LOGGER.info("connect failed.");
        }
    }

}

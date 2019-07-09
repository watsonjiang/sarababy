package com.watson.saralink;

import com.watson.saralink.codec.MessageCodecFactory;
import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.HeartBeatReq;
import com.watson.saralink.msg.HeartBeatRsp;
import com.watson.saralink.msg.LoginReq;
import com.watson.saralink.msg.LoginRsp;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 职责：
 *    创建并保持与annaServer的连接
 */
public class LinkManager {

    //触发加载消息类，不用赋值
    static CmdExecReq cmdExecReq;
    static CmdExecRsp cmdExecRsp;
    static HeartBeatReq heartBeatReq;
    static HeartBeatRsp heartBeatRsp;
    static LoginReq loginReq;
    static LoginRsp loginRsp;

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkManager.class);

    final int SERVER_PORT = 9999;

    IoSession session;

    LinkFsm linkFsm;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    NioSocketConnector connector = new NioSocketConnector();

    static LinkManager _instance = null;

    IRequestHandler requestHandler;

    LinkManager() {
        Runnable acConnect = new Runnable() {
            @Override
            public void run() {
                connect();
            }
        };
        Runnable acLogin = new Runnable() {
            @Override
            public void run() {
                login();
            }
        };

        linkFsm = new LinkFsm(acConnect, acLogin);
        initConnector();
        executorService.submit(linkFsm);
    }

    void initConnector() {
        connector.setConnectTimeoutMillis(1000);
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageFactoryImpl(false));
        keepAliveFilter.setRequestInterval(30);
        connector.getFilterChain().addLast("keepalive", keepAliveFilter);
        connector.setHandler(new LinkMessageHandler(this));
    }

    public static LinkManager instance() {
        if(null == _instance) {
            _instance = new LinkManager();
        }
        return _instance;
    }

    void sleep() {
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            //ignore
        }
    }

    void login() {
        LoginReq req = new LoginReq();
        req.utdid = "123456";  //TODO: use idManager
        session.write(req);
        linkFsm.postEv(LinkEvent.EV_LOGIN_START);
    }

    void onLoginRsp(LoginRsp rsp) {
        linkFsm.postEv(LinkEvent.EV_LOGIN_SUCC);
    }

    void onCmdExecReq(CmdExecReq req) {
        CmdExecRsp rsp = requestHandler.onCmdExec(req);
        session.write(rsp);
    }

    void onLinkException() {
        linkFsm.postEv(LinkEvent.EV_EXCEPTION);
    }

    public void setRequestHandler(IRequestHandler h) {
        requestHandler = h;
    }

    void connect() {
        sleep();
        try {
            session = null;
            LOGGER.info("connect....");
            ConnectFuture f = connector.connect(new InetSocketAddress("192.168.1.107", SERVER_PORT));
            f.awaitUninterruptibly();
            if(f.isConnected()) {
                session = f.getSession();
                linkFsm.postEv(LinkEvent.EV_CONN_SUCC);
            }else{
                LOGGER.info("connect failed.");
                linkFsm.postEv(LinkEvent.EV_EXCEPTION);
            }
        }catch (Exception e) {
            LOGGER.info("connect failed.", e);
            linkFsm.postEv(LinkEvent.EV_EXCEPTION);
        }
    }

}

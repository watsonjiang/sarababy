package com.watson.annababy.link;

import android.util.Log;

import com.watson.annababy.RequestHandler;
import com.watson.annababy.link.codec.MessageCodecFactory;
import com.watson.annababy.link.msg.CmdExecReq;
import com.watson.annababy.link.msg.CmdExecRsp;
import com.watson.annababy.link.msg.LoginReq;
import com.watson.annababy.link.msg.LoginRsp;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 职责：
 *    创建并保持与annaServer的连接
 */
public class LinkManager {

    private String TAG = "LINK_MANAGER";

    final int SERVER_PORT = 9999;

    IoSession session;

    LinkFsm linkFsm;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    NioSocketConnector connector = new NioSocketConnector();

    static LinkManager _instance = null;

    RequestHandler requestHandler;

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

    public void setRequestHandler(RequestHandler h) {
        requestHandler = h;
    }

    void connect() {
        sleep();
        try {
            session = null;
            Log.i(TAG, "connect....");
            ConnectFuture f = connector.connect(new InetSocketAddress("192.168.1.107", SERVER_PORT));
            f.awaitUninterruptibly();
            if(f.isConnected()) {
                session = f.getSession();
                linkFsm.postEv(LinkEvent.EV_CONN_SUCC);
            }else{
                Log.i(TAG, "connect failed.");
                linkFsm.postEv(LinkEvent.EV_EXCEPTION);
            }
        }catch (Exception e) {
            Log.i(TAG, "connect failed.", e);
            linkFsm.postEv(LinkEvent.EV_EXCEPTION);
        }
    }

}

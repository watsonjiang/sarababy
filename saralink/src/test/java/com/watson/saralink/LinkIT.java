package com.watson.saralink;

import com.watson.saralink.codec.MessageCodecFactory;
import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.LoginReq;
import com.watson.saralink.msg.LoginRsp;

import org.apache.log4j.BasicConfigurator;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LinkIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkIT.class);

    @Before
    public void setup() {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
    }


    @Test
    public void testMockServer() throws IOException {

        LinkAcceptor acceptor = new LinkAcceptor("localhost", 9999);

        acceptor.bind();

        while(true) {
            try{
                Thread.sleep(10000);
                LOGGER.info("----------sleep");
            }catch(InterruptedException e) {
                //ignore
            }
        }
    }

    @Test
    public void testMockClient() throws InterruptedException {
        LinkConnector connector = new LinkConnector(new IRequestHandler() {
            @Override
            public CmdExecRsp onCmdExec(CmdExecReq req) {
                LOGGER.info("----onCmdExec. req:{}", req);
                CmdExecRsp rsp = new CmdExecRsp(req);
                return rsp;
            }
        }, "127.0.0.1", 9999);

        connector.connect();
        while(true) {
            try{
                Thread.sleep(10000);
                LOGGER.info("----------sleep");
            }catch(InterruptedException e) {
                //ignore
            }
        }

    }


}

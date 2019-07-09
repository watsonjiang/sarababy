package com.watson.saralink;

import com.watson.saralink.codec.MessageCodecFactory;
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

import java.io.IOException;
import java.net.InetSocketAddress;

public class LinkIT {

    public static class TestMessageHandler extends IoHandlerAdapter {

        String id;

        public TestMessageHandler(String id) {
            this.id = id;
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            System.out.println(id+"-------session created");
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            System.out.println(id+"--------session opened");
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            System.out.println(id+"-------message received:" + message);
            if(message instanceof LoginReq) {
                session.write(new LoginRsp((LoginReq)message));
            }
        }

    }

    @Before
    public void setup() {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
    }


    @Test
    public void testMockServer() throws IOException {

        LinkManager acceptor = new LinkAcceptor();

        while(null != linkMgr.getPeer("123456")) {

            rsp = linkMgr.getPeer("123456").execCmd(Req);

        }


        while(true) {
            try{
                Thread.sleep(10000);
            }catch(InterruptedException e) {
                //ignore
            }
        }
    }

    @Test
    public void testMockClient() throws InterruptedException {
        LinkConnector linkConnector = new LinkConnector(requestHandler);
        linkConnector.start();

    }

    @Test
    public void testMockClient1() throws InterruptedException {
        LinkConnector.instance();
        while(true) {
            Thread.sleep(1000);
        }
    }
}

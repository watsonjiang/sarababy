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

public class MockServerIT {

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
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
        acceptor.getFilterChain().addLast("keepalive", new KeepAliveFilter(new KeepAliveMessageFactoryImpl(true)));

        acceptor.setDefaultLocalAddress(new InetSocketAddress("0.0.0.0", 9999));

        acceptor.setHandler(new TestMessageHandler("mockserver"));

        acceptor.bind();

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
        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(1000);
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageFactoryImpl(false));
        keepAliveFilter.setRequestInterval(10);
        connector.getFilterChain().addLast("keepalive", keepAliveFilter);

        connector.setHandler(new TestMessageHandler("mockclient"));

        ConnectFuture f = connector.connect(new InetSocketAddress("localhost", 9999));
        f.awaitUninterruptibly();

        while(true) {
            Thread.sleep(1000);
        }
    }

    @Test
    public void testMockClient1() throws InterruptedException {
        LinkManager.instance();
        while(true) {
            Thread.sleep(1000);
        }
    }
}

package com.watson.saralink;

import com.watson.saralink.codec.MessageCodecFactory;
import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.LoginReq;
import com.watson.saralink.msg.LoginRsp;
import com.watson.saralink.msg.ScreenCapReq;
import com.watson.saralink.msg.ScreenCapRsp;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import sun.misc.HexDumpEncoder;

public class LinkIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkIT.class);

    @Before
    public void setup() {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
    }


    @Test
    public void testMockServer() throws IOException {

        LinkAcceptor acceptor = new LinkAcceptor("0.0.0.0", 9999);

        acceptor.bind();

        boolean isCalled = false;
        while(true) {
            try{
                Thread.sleep(10000);

                ISarababy sarababy = LinkManager.getInstance().getSarababy("sarababy");
                if(null != sarababy) {
                    if(!isCalled) {
                        isCalled = true;
                        try {
                            //String rsp = sarababy.exec("hello sara");
                            byte[] data = sarababy.screenCap();
                            LOGGER.info("------exec rsp:{}", data);
                            File f = new File("1.png");
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(data);
                            fos.close();
                        } catch (Exception e) {
                            LOGGER.error("unexpected exception.", e);
                        }
                    }
                }else{
                    LOGGER.info("------waiting sara.");
                }
            }catch(InterruptedException e) {
                //ignore
            }
        }
    }

    @Test
    public void testMockClient() throws InterruptedException {
        SaraReconnector connector = new SaraReconnector("sarababy", new IRequestHandler() {
            @Override
            public CmdExecRsp onCmdExec(CmdExecReq req) {
                LOGGER.info("----onCmdExec. cmd:{}", req.cmdLine);
                CmdExecRsp rsp = new CmdExecRsp(req);
                rsp.output = "hello anna";
                return rsp;
            }

            @Override
            public ScreenCapRsp onScreenCap(ScreenCapReq req) {
                LOGGER.info("----onScreenCap.");
                ScreenCapRsp rsp = new ScreenCapRsp();
                rsp.setData("a image".getBytes());
                return rsp;
            }


        }, "127.0.0.1", 9999);

        connector.start();

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

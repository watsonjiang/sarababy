package com.watson.saralink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 封装重连逻辑， 供sarababy使用
 */
public class SaraReconnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaraReconnector.class);

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    IRequestHandler requestHandler;

    String addr;

    int port;

    String peerId;

    public SaraReconnector(String peerId, IRequestHandler requestHandler, String addr, int port) {
        this.requestHandler = requestHandler;
        this.addr = addr;
        this.port = port;
        this.peerId = peerId;
    }

    public void start() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        sleep();
                        checkAndReconnect();
                    }catch(Throwable t) {
                        LOGGER.error("unexpected exception.", t);
                    }
                }
            }
        });
    }

    public void sleep() {
        try{
           Thread.sleep(1000);
        }catch(InterruptedException e){
            //ignore
        }
    }

    public void checkAndReconnect() {
        if(!LinkManager.getInstance().isAnnababyExist()) {
            LOGGER.info("reconnect....");
            LinkConnector connector = new LinkConnector(peerId, requestHandler, addr, port);
            connector.connect();
        }
    }
}

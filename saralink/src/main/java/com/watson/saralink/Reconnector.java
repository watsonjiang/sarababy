package com.watson.saralink;

import org.apache.mina.core.future.ConnectFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 重连逻辑
 */
public class Reconnector {

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    LinkConnector connector;

    public Reconnector(LinkConnector connector) {
        this.connector = connector;
    }

    public void start() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
               doConnect();
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

    public void doConnect() {
        //ConnectFuture f = connector.connect();
        //f.awaitUninterruptibly();
        //if(f.isConnected()) {
        //}

    }
}

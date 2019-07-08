package com.watson.annababy.link;

import com.watson.annababy.link.msg.HeartBeatReq;
import com.watson.annababy.link.msg.HeartBeatRsp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

    boolean isServer;

    public KeepAliveMessageFactoryImpl(boolean isServer) {
        this.isServer = isServer;
    }

    @Override
    public boolean isRequest(IoSession ioSession, Object o) {
        if(o instanceof HeartBeatReq) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isResponse(IoSession ioSession, Object o) {
        if(o instanceof HeartBeatRsp) {
            return true;
        }
        return false;
    }

    @Override
    public Object getRequest(IoSession ioSession) {
        if(isServer) {
            return null;  //passive
        }else {
            return new HeartBeatReq();  //active
        }
    }

    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        HeartBeatRsp rsp = new HeartBeatRsp((HeartBeatReq)o);
        rsp.message = "OK";
        return rsp;
    }
}

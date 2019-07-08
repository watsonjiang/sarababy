package com.watson.annababy.link;

import android.util.Log;

import com.watson.annababy.link.msg.CmdExecReq;
import com.watson.annababy.link.msg.LoginRsp;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

/**
 * 处理链路消息
 */
public class LinkMessageHandler extends IoHandlerAdapter {
    final String TAG = "MESSAGE_HANDLER";

    LinkManager linkManager;

    public LinkMessageHandler(LinkManager m) {
        linkManager = m;
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        Log.i(TAG, "sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        Log.i(TAG, "sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        Log.i(TAG, "sessionClosed");
        linkManager.onLinkException();
    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        if(o instanceof LoginRsp) {
            linkManager.onLoginRsp((LoginRsp)o);
        }else if(o instanceof CmdExecReq) {
            linkManager.onCmdExecReq((CmdExecReq)o);
        }
    }

}

package com.watson.saralink;

import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.Message;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.statemachine.context.AbstractStateContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 状态上下文。
 * 状态机核心
 */
public class LinkStateContext extends AbstractStateContext {

    public IRequestHandler requestHandler;

    public String peerId;

    public BlockingQueue<Message> resultQueue = new LinkedBlockingQueue<>();

    public IoSession session;
}

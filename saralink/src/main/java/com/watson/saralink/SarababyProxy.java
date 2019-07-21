package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.Message;
import com.watson.saralink.msg.ScreenCapReq;
import com.watson.saralink.msg.ScreenCapRsp;

import org.apache.mina.core.session.IoSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 同步转异步代理
 */
public class SarababyProxy implements InvocationHandler {

    private LinkStateContext context;

    public SarababyProxy(LinkStateContext context) {
        this.context = context;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        IoSession session = context.session;
        if("exec".equals(method.getName())) {

            CmdExecReq req = new CmdExecReq();

            req.cmdLine = (String) objects[0];

            session.write(req);

            Message msg = context.resultQueue.poll(30, TimeUnit.SECONDS);
            if(null == msg) {
                throw new RuntimeException("request timeout.");
            }

            CmdExecRsp rsp = (CmdExecRsp)msg;
            return rsp.output;
        }else if("screenCap".equals(method.getName())) {
            ScreenCapReq req = new ScreenCapReq();
            session.write(req);
            Message msg = context.resultQueue.poll(30, TimeUnit.SECONDS);
            if(null == msg) {
                throw new RuntimeException("request timeout");
            }
            ScreenCapRsp rsp = (ScreenCapRsp)msg;
            return rsp.getData();
        }
        return null;
    }
}

package com.watson.saralink.codec;

import com.google.gson.Gson;
import com.watson.saralink.msg.Message;
import com.watson.saralink.msg.MessageMeta;
import com.watson.saralink.msg.ScreenCapReq;
import com.watson.saralink.msg.ScreenCapRsp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MessageEncoder implements ProtocolEncoder {

    private Gson gson = new Gson();

    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {

        IoBuffer buf;
        if(o instanceof ScreenCapRsp) {
            buf = encodeScreenCap((ScreenCapRsp)o);
        }else{
            buf = encodeMessage((Message)o);
        }
        protocolEncoderOutput.write(buf);
    }

    IoBuffer encodeMessage(Message m) {
        int msgType = MessageMeta.getMessageType(m.getClass());
        if(-1 == msgType) {
            throw new RuntimeException("未知消息类型. class:" + m.getClass());
        }
        byte[] type = Utils.intToByteArray(msgType);
        byte[] data = gson.toJson(m).getBytes();
        byte[] len = Utils.intToByteArray(data.length);
        IoBuffer buf = IoBuffer.allocate(8 + data.length);
        buf.put(type);
        buf.put(len);
        buf.put(data);
        buf.flip();
        return buf;
    }

    //抓屏响应带了图片，单独编解码
    IoBuffer encodeScreenCap(ScreenCapRsp m) {
        int msgType = MessageMeta.getMessageType(m.getClass());
        byte[] type = Utils.intToByteArray(msgType);
        byte[] data = m.getData();
        byte[] len = Utils.intToByteArray(data.length);

        IoBuffer buf = IoBuffer.allocate(8 + data.length);
        buf.put(type);
        buf.put(len);
        buf.put(data);
        buf.flip();

        return buf;
    }


    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }

}
